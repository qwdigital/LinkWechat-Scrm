package com.linkwechat.common.handler;

import com.dtflys.forest.exceptions.ForestRuntimeException;
import com.linkwechat.common.constant.HttpConstants;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.exception.CustomException;
import com.linkwechat.common.exception.DemoModeException;
import com.linkwechat.common.exception.InnerAuthException;
import com.linkwechat.common.exception.ServiceException;
import com.linkwechat.common.exception.auth.NotPermissionException;
import com.linkwechat.common.exception.auth.NotRoleException;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理器
 *
 * @author ruoyi
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 权限码异常
     */
    @ExceptionHandler(NotPermissionException.class)
    public AjaxResult handleNotPermissionException(NotPermissionException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',权限码校验失败'{}'", requestURI, e.getMessage());
        return AjaxResult.error(HttpConstants.Status.FORBIDDEN.getCode(), "没有访问权限，请联系管理员授权");
    }

    /**
     * 角色权限异常
     */
    @ExceptionHandler(NotRoleException.class)
    public AjaxResult handleNotRoleException(NotRoleException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',角色权限校验失败'{}'", requestURI, e.getMessage());
        return AjaxResult.error(HttpConstants.Status.FORBIDDEN.getCode(), "没有访问权限，请联系管理员授权");
    }

    /**
     * 请求方式不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public AjaxResult handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',不支持'{}'请求", requestURI, e.getMethod());
        return AjaxResult.error(e.getMessage());
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(ServiceException.class)
    public AjaxResult handleServiceException(ServiceException e, HttpServletRequest request) {
        log.error(e.getMessage(), e);
        Integer code = e.getCode();
        return StringUtils.isNotNull(code) ? AjaxResult.error(code, e.getMessage()) : AjaxResult.error(e.getMessage());
    }

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public AjaxResult handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生未知异常.", requestURI, e);
        String msg = e.getMessage();
        if (StringUtils.isEmpty(msg)) {
            msg = "未知异常";
        }
        return AjaxResult.error(msg);
    }


    /**
     * 自定义验证异常
     */
    @ExceptionHandler(BindException.class)
    public AjaxResult handleBindException(BindException e) {
        log.error(e.getMessage(), e);
        String message = e.getAllErrors().get(0).getDefaultMessage();
        return AjaxResult.error(message);
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return AjaxResult.error(message);
    }

    /**
     * 内部认证异常
     */
    @ExceptionHandler(InnerAuthException.class)
    public AjaxResult handleInnerAuthException(InnerAuthException e) {
        return AjaxResult.error(e.getMessage());
    }

    /**
     * 演示模式异常
     */
    @ExceptionHandler(DemoModeException.class)
    public AjaxResult handleDemoModeException(DemoModeException e) {
        return AjaxResult.error("演示模式，不允许操作");
    }

    /**
     * 企微接口异常
     */
    @ExceptionHandler(WeComException.class)
    public AjaxResult handleWeComException(WeComException e) {
        return AjaxResult.error(e.getCode(), e.getMessage());
    }

    /**
     * 企微接口异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(ForestRuntimeException.class)
    public AjaxResult handleForestRuntimeException(ForestRuntimeException e) {
        if (e.getCause() instanceof WeComException) {
            WeComException weComException = (WeComException) e.getCause();
            return AjaxResult.error(weComException.getCode(), e.getMessage());
        } else {
            return AjaxResult.error(e.getMessage());
        }

    }

    /**
     * 数据库操作异常
     *
     * @param e 异常
     * @return {@link AjaxResult}
     * @author WangYX
     * @date 2023/06/09 14:29
     */
    @ExceptionHandler(DataAccessException.class)
    public AjaxResult handleDataAccessException(DataAccessException e) {
        e.printStackTrace();
        log.error("数据库操作异常：{}", e.getMessage());
        return AjaxResult.error("数据库操作异常，请联系管理员！");
    }


    /**
     * 自定义异常
     */
    @ExceptionHandler(CustomException.class)
    public AjaxResult handleCustomException(CustomException e) {
        return AjaxResult.error(e.getCode(), e.getMessage());
    }


    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public AjaxResult handleException(Exception e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生系统异常.", requestURI, e);
        return AjaxResult.error(e.getMessage());
    }

}
