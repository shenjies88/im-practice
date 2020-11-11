package com.shenjies88.practice.im.netty.service;

import com.alibaba.fastjson.JSON;
import com.shenjies88.practice.im.common.bean.client.NettyClient;
import com.shenjies88.practice.im.common.bean.manager.MyCacheManager;
import com.shenjies88.practice.im.common.dto.GroupChatTxtDTO;
import com.shenjies88.practice.im.common.dto.SingleChatTxtDTO;
import com.shenjies88.practice.im.common.dto.base.LoginTypeDTO;
import com.shenjies88.practice.im.common.dto.base.MessageDTO;
import com.shenjies88.practice.im.common.vo.ServiceMetadataVO;
import com.shenjies88.practice.im.netty.cache.MemberChannelCache;
import com.shenjies88.practice.im.netty.manager.MyMessageManager;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * @author shenjies88
 * @since 2020/11/04/16:27
 */
@Slf4j
@Service
public class MessageHandService {

    private final String serverPort;
    private final String host;

    private final MyMessageManager messageManager;
    private final MyCacheManager cacheManager;
    private final NettyClient nettyClient;

    @Autowired
    public MessageHandService(@Value("${server.port}") String serverPort, @Value("${spring.cloud.zookeeper.discovery.metadata.host}") String host, MyMessageManager messageManager, MyCacheManager cacheManager, NettyClient nettyClient) {
        this.serverPort = serverPort;
        this.host = host;
        this.messageManager = messageManager;
        this.cacheManager = cacheManager;
        this.nettyClient = nettyClient;
    }

    /**
     * 群聊-文本 预检查
     */
    private Integer groupChatTxtPreCheck(MessageDTO messageDTO, Integer memberId) {
        //序列化
        GroupChatTxtDTO groupChatTxtDTO = null;
        try {
            groupChatTxtDTO = JSON.parseObject(messageDTO.getContentJson(), GroupChatTxtDTO.class);
        } catch (Exception e) {
            Assert.isTrue(false, "无效的内容类型");
        }
        //校验
        Assert.hasText(groupChatTxtDTO.getMsg(), "消息内容不能为空");
        Integer groupId = groupChatTxtDTO.getGroupId();
        Assert.notNull(groupId, "目标群id不能为空");
        Set<Object> groupOnline = cacheManager.getGroupOnline(groupId);
        Assert.isTrue(groupOnline.contains(memberId), "当前用户不在该群内");
        return groupId;
    }

    /**
     * 私聊-文本 预检查
     */
    private Integer singleChatTxtPreCheck(ChannelHandlerContext ctx, MessageDTO messageDTO) {
        //序列化
        SingleChatTxtDTO singleChatTxtDTO = null;
        try {
            singleChatTxtDTO = JSON.parseObject(messageDTO.getContentJson(), SingleChatTxtDTO.class);
        } catch (Exception e) {
            Assert.isTrue(false, "无效的内容类型");
        }
        //校验
        Assert.hasText(singleChatTxtDTO.getMsg(), "消息内容不能为空");
        Integer toMemberId = singleChatTxtDTO.getToMemberId();
        Assert.notNull(toMemberId, "目标会员id不能为空");
        Integer myMemberId = MemberChannelCache.get(ctx);
        Assert.isTrue(!toMemberId.equals(myMemberId), "目标会员不能是自己");
        return toMemberId;
    }


    /**
     * 发送私聊消息
     *
     * @param ctx        管道上下文
     * @param messageDTO 消息体
     * @param body       消息体json
     * @param toMemberId 接受用户id
     */
    private void sendSingleChat(ChannelHandlerContext ctx, MessageDTO messageDTO, String body, Integer toMemberId) {
        //缓存中获取目标会员管道
        ChannelHandlerContext toCtx = MemberChannelCache.get(toMemberId);
        if (toCtx == null) {
            //从redis获取用户信息
            ServiceMetadataVO userNettyLogin = cacheManager.getUserNettyLogin(toMemberId);
            if (userNettyLogin == null) {
                log.warn("用户 id:{} 不在线", toMemberId);
                messageManager.writeError(ctx, "目标用户不在线");
                return;
            }
            //异步调用 用户所在的netty服务发送消息
            nettyClient.sendSingleChat(NettyClient.createBaseUrl(userNettyLogin.getHost(), userNettyLogin.getServerPort()), messageDTO, toMemberId);
            return;
        }
        //发送给目标用户
        messageManager.writeBody(toCtx, body);
        messageManager.writeSuccessful(ctx);
    }

    /**
     * 发送群聊消息
     *
     * @param ctx        管道上下文
     * @param messageDTO 消息体
     * @param body       消息体json
     * @param groupId    群id
     */
    private void sendGroupChat(ChannelHandlerContext ctx, MessageDTO messageDTO, String body, Integer groupId) {
        Set<Object> onLineMembers = cacheManager.getGroupOnline(groupId);
        if (CollectionUtils.isEmpty(onLineMembers)) {
            log.warn("群: {} 无人在线", groupId);
            return;
        }
        List<ServiceMetadataVO> loginInfos = new ArrayList<>();
        onLineMembers.forEach(e ->
                loginInfos.add(cacheManager.getUserNettyLogin((Integer) e))
        );
        //根据所登陆的服务器分组，在本服务器的直接循环发送
        Map<String, List<ServiceMetadataVO>> loginInfoGroup = loginInfos.stream().collect(Collectors.groupingBy(e -> NettyClient.createBaseUrl(e.getHost(), e.getServerPort())));
        //不在本服务器的一次性批量异步请求
        String currServer = NettyClient.createBaseUrl(host, serverPort);
        loginInfoGroup.forEach((url, serviceMetadataList) -> {
            if (currServer.equals(url)) {
                //本地循环
                log.info("本地群用户数量 {}", serviceMetadataList.size());
                serviceMetadataList.forEach(metadata -> {
                    ChannelHandlerContext c = MemberChannelCache.get(metadata.getId());
                    if (c == null) {
                        log.warn("群用户不在线 {}", metadata);
                    } else {
                        messageManager.writeBody(c, body);
                    }
                });
            } else {
                //异步请求
                log.info("服务器 {} 群用户数量 {}", url, serviceMetadataList.size());
                nettyClient.sendGroupChat(url, serviceMetadataList, messageDTO, groupId);
            }
        });
        messageManager.writeSuccessful(ctx);
    }

    /**
     * 登录处理
     *
     * @param ctx        管道上下文
     * @param messageDTO 消息体
     */
    public void handLogin(ChannelHandlerContext ctx, MessageDTO messageDTO) {
        //序列化
        LoginTypeDTO loginTypeDTO;
        try {
            loginTypeDTO = JSON.parseObject(messageDTO.getContentJson(), LoginTypeDTO.class);
        } catch (Exception e) {
            messageManager.writeErrorClose(ctx, "无效的内容类型");
            return;
        }
        //校验
        Assert.notNull(loginTypeDTO.getMemberId(), "会员id不能为空");
        //绑定通道和会员id
        boolean save = MemberChannelCache.save(loginTypeDTO.getMemberId(), ctx);
        if (save) {
            messageManager.writeSuccessful(ctx);
        } else {
            messageManager.writeError(ctx, "您已登录");
        }
    }

    /**
     * 下线处理
     *
     * @param ctx 管道上下文
     */
    public void handLogout(ChannelHandlerContext ctx) {
        //移除通道和会员的缓存
        MemberChannelCache.remove(ctx);
        ctx.channel().close();
    }

    /**
     * 处理私聊消息
     *
     * @param ctx        管道上下文
     * @param messageDTO 消息体
     * @param body       消息体json
     */
    public void handSingleChat(ChannelHandlerContext ctx, MessageDTO messageDTO, String body) {
        //已登录
        Assert.notNull(MemberChannelCache.get(ctx), "您未登录");
        switch (messageDTO.getContentType()) {
            case TXT:
                Integer toMemberId = singleChatTxtPreCheck(ctx, messageDTO);
                sendSingleChat(ctx, messageDTO, body, toMemberId);
                break;
            default:
                messageManager.writeErrorClose(ctx, "无效的内容类型");
        }
    }

    /**
     * 处理群聊消息
     *
     * @param ctx        管道上下文
     * @param messageDTO 消息体
     * @param body       消息体json
     */
    public void handGroupChat(ChannelHandlerContext ctx, MessageDTO messageDTO, String body) {
        //已登录
        Assert.notNull(MemberChannelCache.get(ctx), "您未登录");
        switch (messageDTO.getContentType()) {
            case TXT:
                Integer groupId = groupChatTxtPreCheck(messageDTO, MemberChannelCache.get(ctx));
                sendGroupChat(ctx, messageDTO, body, groupId);
                break;
            default:
                messageManager.writeErrorClose(ctx, "无效的内容类型");
        }
    }
}
