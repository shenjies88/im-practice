package com.shenjies88.practice.im.backend.vo.authentication;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author shenjies88
 * @since 2020/11/5-5:49 PM
 */
@Data
@ApiModel("注册请求")
public class RegReqVO {

    @ApiModelProperty("账号")
    private String account;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("密码")
    private String pwd;
}
