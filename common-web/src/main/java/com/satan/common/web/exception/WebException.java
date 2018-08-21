package com.satan.common.web.exception;

import com.satan.common.exception.BaseException;
import com.satan.common.exception.IErrorCode;

/**
 * Created by huangpin on 17/3/14.
 */
public class WebException extends BaseException {
    public WebException(IErrorCode iErrorCode, Throwable e) {
        super(iErrorCode, e);
    }

    public WebException(IErrorCode iErrorCode) {
        super(iErrorCode);
    }

    public WebException(String code , String message) {
        super(code,message);
    }
}
