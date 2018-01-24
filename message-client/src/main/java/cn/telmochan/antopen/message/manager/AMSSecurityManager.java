/**
 *
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package cn.telmochan.antopen.message.manager;

import cn.telmochan.antopen.message.config.AMSConfig;
import cn.telmochan.antopen.message.exception.AMSEncryptException;
import cn.telmochan.antopen.message.exception.AMSSignatureException;
import cn.telmochan.antopen.message.model.AMSProcessContext;

/**
 * 蚂蚁消息服务安全源
 * @author telmochan
 * @version $Id: AMSSecurityManager.java, v 0.1 2018-01-17 下午3:40 telmochan Exp $
 */
public interface AMSSecurityManager {

    /**
     * 验签
     * @param processContext
     * @param amsConfig
     */
    void checkSign(AMSProcessContext processContext,
                   AMSConfig amsConfig) throws AMSSignatureException;

    /**
     * 解密
     * @param processContext
     * @throws AMSEncryptException
     */
    void decryptContent(AMSProcessContext processContext) throws AMSEncryptException;
}
