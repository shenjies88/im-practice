package com.shenjies88.practice.im.netty.utils;

import com.alibaba.fastjson.JSON;
import com.shenjies88.practice.im.common.dto.GroupChatTxtDTO;
import com.shenjies88.practice.im.common.dto.SingleChatTxtDTO;
import com.shenjies88.practice.im.common.dto.base.MessageDTO;
import com.shenjies88.practice.im.netty.cache.MemberChannelCache;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.util.Assert;

/**
 * @author shenjies88
 * @since 2020/11/6-1:58 PM
 */
public class MsgPreCheckUtil {

    /**
     * 群聊-文本 预检查
     */
    public static Integer groupChatTxtPreCheck(MessageDTO messageDTO) {
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
        //TODO 当前用户在该群内
        return groupId;
    }

    /**
     * 私聊-文本 预检查
     */
    public static Integer singleChatTxtPreCheck(ChannelHandlerContext ctx, MessageDTO messageDTO) {
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
}
