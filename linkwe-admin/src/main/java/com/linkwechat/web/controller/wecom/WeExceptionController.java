package com.linkwechat.web.controller.wecom;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.exception.wecom.WeComException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author danmo
 * @description 统一异常处理
 * @date 2021/6/4 16:55
 **/
@Slf4j
@RestControllerAdvice(basePackages = "com.linkwechat.web.controller.wecom")
public class WeExceptionController {

    @ExceptionHandler(WeComException.class)
    public AjaxResult weComException(WeComException ex){
        return AjaxResult.error(ex.getCode(),ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public AjaxResult runtimeException(Exception ex){
        log.error("接口异常拦截 ex:{}",ex);
        return AjaxResult.error();
    }
}
