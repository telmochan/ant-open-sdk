/**
 *
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package cn.telmochan.antopen.message.exception;

/**
 * @author telmochan
 * @version $Id: AMSException.java, v 0.1 2018-01-17 下午3:57 telmochan Exp $
 */
public class AMSException extends Exception {
    public AMSException() {
    }

    public AMSException(String message) {
        super(message);
    }

    public AMSException(String message, Throwable cause) {
        super(message, cause);
    }

    public AMSException(Throwable cause) {
        super(cause);
    }

    public AMSException(String message, Throwable cause, boolean enableSuppression,
                        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
