package com.shenjies88.practice.im.backend.vo.authentication;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author shenjies88
 * @since 2020/11/6-9:24 AM
 */
@Data
@ApiModel("登陆请求")
public class LoginReqVO {

    @ApiModelProperty("账号")
    private String account;

    @ApiModelProperty("密码")
    private String pwd;
}
