package com.linkwechat.web.controller.wecom;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.exception.wecom.WeComException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

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

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public AjaxResult customException(MethodArgumentNotValidException e) {
        // 获取参数校验失败信息
        List<ObjectError> list = e.getBindingResult().getAllErrors();
        StringBuilder errorMsg = new StringBuilder();
        for (ObjectError error : list){
            errorMsg.append(error.getDefaultMessage()).append("\n");
        }
        return AjaxResult.error(errorMsg.toString());
    }

    @ExceptionHandler(Exception.class)
    public AjaxResult runtimeException(Exception ex){
        log.error("接口异常拦截 ex:{}",ex);
        return AjaxResult.error();
    }
}
