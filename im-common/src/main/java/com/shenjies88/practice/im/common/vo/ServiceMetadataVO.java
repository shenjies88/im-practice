package com.shenjies88.practice.im.common.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author shenjies88
 * @since 2020/11/6-9:53 AM
 */
@Data
@ApiModel("netty服务元数据")
public class ServiceMetadataVO {

    private String host;

    private String serverPort;

    private String wsPort;

    private Integer id;
}
