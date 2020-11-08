package com.shenjies88.practice.im.backend.controller;

import com.shenjies88.practice.im.backend.service.GroupService;
import com.shenjies88.practice.im.backend.utils.TokenUtil;
import com.shenjies88.practice.im.backend.vo.group.CreateGroupReqVo;
import com.shenjies88.practice.im.common.vo.HttpResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author shenjies88
 * @since 2020/11/6-2:42 PM
 */
@Api(tags = "群组接口")
@AllArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("/im/group")
@RestController
public class GroupController {

    private final GroupService groupService;

    @ApiOperation("建群")
    @PostMapping("/create-group")
    public HttpResultVO<Integer> createGroup(@RequestBody CreateGroupReqVo params) {
        Assert.notEmpty(params.getMemberIdList(), "未邀请会员进入");
        Assert.isTrue(!params.getMemberIdList().contains(TokenUtil.getContextToken().getId()), "不能邀请自己");
        params.getMemberIdList().add(TokenUtil.getContextToken().getId());
        return HttpResultVO.success(groupService.createGroup(params.getMemberIdList()));
    }

    @ApiOperation("我的群列表")
    @PostMapping("/my-list")
    public HttpResultVO<List<Integer>> myList() {
        return HttpResultVO.success(groupService.myList());
    }
}
