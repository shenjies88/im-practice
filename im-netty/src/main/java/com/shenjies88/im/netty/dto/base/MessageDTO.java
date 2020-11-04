package com.shenjies88.im.netty.dto.base;

import com.shenjies88.im.netty.constant.MessageContentTypeEnum;
import com.shenjies88.im.netty.constant.MessageTypeEnum;
import lombok.Data;

/**
 * @author shenjies88
 * @since 2020/11/04/15:39
 */
@Data
public class MessageDTO {

    /**
     * 消息类型
     */
    private MessageTypeEnum type;

    /**
     * 内容类型
     */
    private MessageContentTypeEnum contentType;

    /**
     * 自定义内容json
     */
    private String contentJson;

    /**
     * 时间戳
     */
    private Long timestamp;
}
