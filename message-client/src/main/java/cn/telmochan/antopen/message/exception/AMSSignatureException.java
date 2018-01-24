/**
 *
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package cn.telmochan.antopen.message.exception;

/**
 * @author telmochan
 * @version $Id: AMSSignatureException.java, v 0.1 2018-01-17 下午7:07 telmochan Exp $
 */
public class AMSSignatureException extends AMSException {

    public AMSSignatureException() {
    }

    public AMSSignatureException(String message) {
        super(message);
    }

    public AMSSignatureException(String message, Throwable cause) {
        super(message, cause);
    }

    public AMSSignatureException(Throwable cause) {
        super(cause);
    }

    public AMSSignatureException(String message, Throwable cause, boolean enableSuppression,
                                 boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
