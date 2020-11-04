package com.shenjies88.im.common.bean.exeption;

import com.shenjies88.im.common.vo.HttpResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author shenjies88
 */
@Slf4j
@ResponseStatus(HttpStatus.OK)
@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(IllegalArgumentException.class)
    public HttpResult<Void> handIllegalArgumentException(IllegalArgumentException e) {
        String errorMessage = "服务器繁忙";
        if (!CollectionUtils.isEmpty(Arrays.stream(e.getStackTrace()).filter(o -> o.getClassName().startsWith("com.shenjies88")).collect(Collectors.toList()))) {
            log.error("业务异常 {}", e.getMessage());
            errorMessage = e.getMessage();
        } else {
            log.error("服务器异常", e);
        }
        return HttpResult.fail(errorMessage);
    }

    @ExceptionHandler(Exception.class)
    public HttpResult<Void> exceptionHandler(Exception e) {
        log.error("通用异常", e);
        return HttpResult.fail("服务器繁忙");
    }
}
