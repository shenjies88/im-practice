package com.shenjies88.im.netty.service;

import com.alibaba.fastjson.JSON;
import com.shenjies88.im.netty.cache.MemberChannelCache;
import com.shenjies88.im.netty.dto.base.LoginTypeDTO;
import com.shenjies88.im.netty.dto.base.MessageDTO;
import com.shenjies88.im.netty.manager.MyMessageManager;
import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author shenjies88
 * @since 2020/11/04/16:27
 */
@Slf4j
@AllArgsConstructor(onConstructor_ = {@Autowired})
@Service
public class MessageService {

    private final MyMessageManager messageManager;

    /**
     * 登录处理
     *
     * @param ctx        管道上下文
     * @param messageDTO 消息体
     */
    public void handLogin(ChannelHandlerContext ctx, MessageDTO messageDTO) {
        LoginTypeDTO loginTypeDTO;
        try {
            loginTypeDTO = JSON.parseObject(messageDTO.getContentJson(), LoginTypeDTO.class);
        } catch (Exception e) {
            messageManager.writeErrorClose(ctx, "无效的内容类型");
            return;
        }
        //校验
        if (loginTypeDTO.getMemberId() == null) {
            messageManager.writeErrorClose(ctx, "会员id不能为空");
            return;
        }
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
        MemberChannelCache.remove(ctx.channel());
        ctx.channel().close();
    }
}
