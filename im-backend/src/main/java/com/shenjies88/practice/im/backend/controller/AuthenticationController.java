package com.shenjies88.practice.im.backend.controller;

import com.shenjies88.practice.im.backend.service.AuthenticationService;
import com.shenjies88.practice.im.backend.utils.RegularUtil;
import com.shenjies88.practice.im.backend.vo.authentication.LoginReqVO;
import com.shenjies88.practice.im.backend.vo.authentication.RegReqVO;
import com.shenjies88.practice.im.common.vo.HttpResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shenjies88
 * @since 2020/11/5-5:39 PM
 */
@Api(tags = "鉴权接口")
@AllArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("/im/authentication")
@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @ApiOperation("注册并登陆")
    @PostMapping("/reg-login")
    public HttpResult<String> regLogin(@RequestBody RegReqVO params) {
        Assert.isTrue(params.getNickname() != null, "昵称不能为空");
        Assert.isTrue(RegularUtil.legalPassword(params.getPwd()), "密码强度过弱");
        Assert.isTrue(RegularUtil.isPhone(params.getAccount()), "手机号格式不正确");
        return HttpResult.success(authenticationService.regLogin(params));
    }

    @ApiOperation("登陆")
    @PostMapping("/login")
    public HttpResult<String> login(@RequestBody LoginReqVO params) {
        Assert.isTrue(RegularUtil.isPhone(params.getAccount()), "手机号格式不正确");
        Assert.hasText(params.getPwd(), "密码不能为空");
        return HttpResult.success(authenticationService.login(params));
    }

    @ApiOperation("退出登陆")
    @PostMapping("/logout")
    public HttpResult<Void> logout() {
        authenticationService.logout();
        return HttpResult.success();
    }
}
