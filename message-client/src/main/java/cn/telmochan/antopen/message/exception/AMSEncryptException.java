/**
 *
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package cn.telmochan.antopen.message.exception;

/**
 * @author telmochan
 * @version $Id: AMSEncryptException.java, v 0.1 2018-01-17 下午7:08 telmochan Exp $
 */
public class AMSEncryptException extends AMSException {

    public AMSEncryptException() {
    }

    public AMSEncryptException(String message) {
        super(message);
    }

    public AMSEncryptException(String message, Throwable cause) {
        super(message, cause);
    }

    public AMSEncryptException(Throwable cause) {
        super(cause);
    }

    public AMSEncryptException(String message, Throwable cause, boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
