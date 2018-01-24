/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package cn.telmochan.antopen.message.model;

/**
 * @author telmo.czl
 * @version $Id: AopDecryptRequest.java, v 0.1 2018-01-19 下午1:36 telmo.czl Exp $
 */
public class AopDecryptRequest {
    /**
     * 需要解密的appId
     */
    private String appId;

    /**
     * Base64格式的密文内容
     */
    private String ciphertext;

    /**
     * 加密算法
     */
    private String encryptType;

    /**
     * 字符集(解密后的字节数组,通过该字符集转化为对应的原文字符串)
     */
    private String charset;

    /**
     * Constructor
     */
    public AopDecryptRequest() {
    }

    /**
     * Constructor
     * @param appId
     * @param ciphertext
     * @param encryptType
     * @param charset
     */
    public AopDecryptRequest(String appId, String ciphertext, String encryptType, String charset) {
        this.appId = appId;
        this.ciphertext = ciphertext;
        this.encryptType = encryptType;
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
     * Getter method for property <tt>ciphertext</tt>
     */
    public String getCiphertext() {
        return ciphertext;
    }

    /**
     * Setter method for property <tt>ciphertext</tt>
     */
    public void setCiphertext(String ciphertext) {
        this.ciphertext = ciphertext;
    }

    /**
     * Getter method for property <tt>encryptType</tt>
     */
    public String getEncryptType() {
        return encryptType;
    }

    /**
     * Setter method for property <tt>encryptType</tt>
     */
    public void setEncryptType(String encryptType) {
        this.encryptType = encryptType;
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
}
