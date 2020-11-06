package com.shenjies88.practice.im.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shenjies88
 * @since 2020/11/5-9:55 PM
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ContextTokenVO {

    private Integer id;
    private String account;
    private String nickname;
    private String token;
}
