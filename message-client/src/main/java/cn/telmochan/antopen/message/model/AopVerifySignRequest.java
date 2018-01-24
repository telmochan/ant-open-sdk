/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package cn.telmochan.antopen.message.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author telmo.czl
 * @version $Id: AopVerifySignRequest.java, v 0.1 2018-01-19 下午1:14 telmo.czl Exp $
 */
public class AopVerifySignRequest {
    /**
     * 需要验签的appId
     */
    private String appId;

    /**
     * Base64格式的签名串
     */
    private String sign;

    /**
     * 签名算法
     */
    private String signType;

    /**
     * 待验证签名的原文
     */
    private String signContent;

    /**
     * 签名字符集(通过此参数获取signContent签名原文的字节数组)
     */
    private String charset;

    /**
     * Constructor
     */
    public AopVerifySignRequest() {
    }

    /**
     * Constructor
     * @param appId
     * @param sign
     * @param signContent
     * @param charset
     */
    public AopVerifySignRequest(String appId, String sign, String signType, String signContent,
                                String charset) {
        this.appId = appId;
        this.sign = sign;
        this.signType = signType;
        this.signContent = signContent;
        this.charset = charset;
    }

    /**
     * Getter method for property <tt>appId</tt>
     */
    public String getAppId() {
        return appId;
    }

    /**
     * Setter method for property <tt>appId</tt>
     */
    public void setAppId(String appId) {
        this.appId = appId;
    }

    /**
     * Getter method for property <tt>sign</tt>
     */
    public String getSign() {
        return sign;
    }

    /**
     * Setter method for property <tt>sign</tt>
     */
    public void setSign(String sign) {
        this.sign = sign;
    }

    /**
     * Getter method for property <tt>signType</tt>
     */
    public String getSignType() {
        return signType;
    }

    /**
     * Setter method for property <tt>signType</tt>
     */
    public void setSignType(String signType) {
        this.signType = signType;
    }

    /**
     * Getter method for property <tt>signContent</tt>
     */
    public String getSignContent() {
        return signContent;
    }

    /**
     * Setter method for property <tt>signContent</tt>
     */
    public void setSignContent(String signContent) {
        this.signContent = signContent;
    }

    /**
     * Getter method for property <tt>charset</tt>
     */
    public String getCharset() {
        return charset;
    }

    /**
     * Setter method for property <tt>charset</tt>
     */
    public void setCharset(String charset) {
        this.charset = charset;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
    }
}
