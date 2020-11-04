package com.shenjies88.im.netty.dto;

import com.shenjies88.im.netty.dto.base.SingleChatTypeDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 私聊-文本
 *
 * @author shenjies88
 * @since 2020/11/04/15:51
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SingleChatTxtDTO extends SingleChatTypeDTO {

    /**
     * 消息
     */
    private String msg;
}
