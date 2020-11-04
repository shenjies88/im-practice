package com.shenjies88.im.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shenjies88
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpResult<T> {

    @ApiModelProperty("状态码 0-正常 1-异常")
    private Integer code;

    @ApiModelProperty("true-成功 false-失败")
    private Boolean status;

    @ApiModelProperty("信息")
    private String message;

    @ApiModelProperty("具体数据")
    private T data;

    public static HttpResult<Void> success() {
        return new HttpResult<>(0, true, "success", null);
    }

    public static <U> HttpResult<U> success(U data) {
        return new HttpResult<>(0, true, "success", data);
    }

    public static HttpResult<Void> fail(String message) {
        return new HttpResult<>(1, false, message, null);
    }
}
