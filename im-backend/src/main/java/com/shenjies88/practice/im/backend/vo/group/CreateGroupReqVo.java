package com.shenjies88.practice.im.backend.vo.group;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Set;

/**
 * @author shenjies88
 * @since 2020/11/6-2:50 PM
 */
@Data
@ApiModel("建群")
public class CreateGroupReqVo {

    @ApiModelProperty("邀请入群的会员id列表")
    private Set<Integer> memberIdList;
}
