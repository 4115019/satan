package com.satan.trade.common.web.controller;

import com.satan.trade.common.exception.BaseException;
import com.satan.trade.common.web.exception.WebBasicCodeEnum;
import com.satan.trade.common.web.result.JSONResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by huangpin on 17/3/16.
 */
@ControllerAdvice
@RequestMapping("/")
@Slf4j
public class GlobalController {
    @RequestMapping(value = "/health", method = RequestMethod.GET)
    @ResponseBody
    public JSONResult health() {
        return JSONResult.ok();
    }

    /**
     * 自定义异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(BaseException.class)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView baseExceptionHandler(BaseException e) {
        log.error("BaseException异常被截获", e);
        return JSONResult.error(e.getCode(), e.getMessage()).buildModelAndView();
    }

    /**
     * 参数错误异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView baseExceptionHandler(MissingServletRequestParameterException e) {
        log.error("MissingServletRequestParameterException异常被截获", e);
        return JSONResult.error(WebBasicCodeEnum.ILLEGAL_ARGUMENT_ERROR).buildModelAndView();
    }

    /**
     * 参数错误异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView baseExceptionHandler(IllegalArgumentException e) {
        log.error("IllegalArgumentException异常被截获", e);
        return JSONResult.error(WebBasicCodeEnum.ILLEGAL_ARGUMENT_ERROR).buildModelAndView();
    }

    /**
     * 参数错误异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView baseExceptionHandler(HttpMessageNotReadableException e) {
        log.error("IllegalArgumentException异常被截获", e);
        return JSONResult.error(WebBasicCodeEnum.ILLEGAL_ARGUMENT_ERROR).buildModelAndView();
    }

    /**
     * 参数错误异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView baseExceptionHandler(MethodArgumentNotValidException e) {
        log.error("IllegalArgumentException异常被截获", e);
        return JSONResult.error(WebBasicCodeEnum.ILLEGAL_ARGUMENT_ERROR.getCode(), String.format("%s -> %s", e.getBindingResult().getFieldError().getField(), e.getBindingResult().getFieldError().getDefaultMessage())).buildModelAndView();
    }

    /**
     * 未知异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView exceptionHandler(Exception e) {
        log.error("Exception异常被截获", e);
        if (e.getCause() instanceof BaseException) {
            BaseException baseException = (BaseException) e.getCause();
            return JSONResult.error(baseException.getiErrorCode()).buildModelAndView();
        } else if (e instanceof HttpRequestMethodNotSupportedException) {
            return JSONResult.error(WebBasicCodeEnum.NOT_SUPPORTED_METHOD_ERROR).buildModelAndView();
        }
        if (e instanceof BeansException) {
            return JSONResult.error(WebBasicCodeEnum.ILLEGAL_ARGUMENT_ERROR).buildModelAndView();
        }
        return JSONResult.error().buildModelAndView();
    }
}
