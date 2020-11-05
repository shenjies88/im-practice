package com.shenjies88.practice.im.backend.vo.authentication;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

/**
 * @author shenjies88
 * @since 2020/11/5-9:55 PM
 */
@Builder
@Data
@ApiModel("登陆返回")
public class LoginRespVO {

    private Integer id;
    private String account;
    private String nickname;
    private String token;
}
