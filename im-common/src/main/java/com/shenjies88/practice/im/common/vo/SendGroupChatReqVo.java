package com.shenjies88.practice.im.common.vo;

import com.shenjies88.practice.im.common.dto.base.MessageDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author shenjies88
 * @since 2020/11/11-3:11 PM
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SendGroupChatReqVo {

    private List<ServiceMetadataVO> serviceMetadataList;

    private MessageDTO messageDTO;
}
