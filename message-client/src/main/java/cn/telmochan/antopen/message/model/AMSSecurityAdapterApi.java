/**
 *
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package cn.telmochan.antopen.message.model;

/**
 * @author telmochan
 * @version $Id: AMSSecurityAdapterApi.java, v 0.1 2018-01-17 下午8:52 telmochan Exp $
 */
public interface AMSSecurityAdapterApi {
    /**
     * 加密
     * @param appId
     * @param sign
     * @param signType
     * @param verifyContent
     */
    public void checkSign(String appId, byte[] sign, String signType, byte[] verifyContent);

    /**
     * 解密
     * @param appId 开放平台应用id
     * @param encryptType 加密算法,目前支持AES(请使用AES/CBC/PKCS5Padding),初始化向量为128字节的0
     * @param encryptedContent 密文
     * @return
     */
    public byte[] decypt(String appId, String encryptType, byte[] encryptedContent);
}
