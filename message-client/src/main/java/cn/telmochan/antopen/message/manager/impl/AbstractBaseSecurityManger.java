/**
 *
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package cn.telmochan.antopen.message.manager.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.telmochan.antopen.constants.TimeCheckLevel;
import cn.telmochan.antopen.message.config.AMSConfig;
import cn.telmochan.antopen.message.exception.AMSEncryptException;
import cn.telmochan.antopen.message.exception.AMSSignatureException;
import cn.telmochan.antopen.message.manager.AMSSecurityManager;
import cn.telmochan.antopen.message.model.AMSProcessContext;
import cn.telmochan.antopen.message.model.AopDecryptRequest;
import cn.telmochan.antopen.message.model.AopVerifySignRequest;
import cn.telmochan.antopen.util.AlipaySignature;
import cn.telmochan.antopen.util.LoggerUtil;

/**
 * @author telmochan
 * @version $Id: AbstractBaseSecurityManger.java, v 0.1 2018-01-17 下午8:39 telmochan Exp $
 */
public abstract class AbstractBaseSecurityManger implements AMSSecurityManager {
    /**
     * logger
     */
    protected Log logger = LogFactory.getLog("AOP-SDK-MESSAGE-SECURITY");

    /**
     * 验签
     *
     * @param processContext
     * @param amsConfig
     */
    public void checkSign(AMSProcessContext processContext,
                          AMSConfig amsConfig) throws AMSSignatureException {

        //时间戳校验
        long messageSenderTime = processContext.getMessageSenderTime();
        TimeCheckLevel checkLevel = amsConfig.getCheckLevel();
        boolean needCheckTimeValidaty = TimeCheckLevel.TIME_CHECK_FORCE.equals(checkLevel)
                                        || (TimeCheckLevel.TIME_CHECK_COMPATIBLE.equals(checkLevel)
                                            && messageSenderTime > 0);
        if (needCheckTimeValidaty) {
            if (messageSenderTime <= 0) {
                throw new AMSSignatureException("时间戳校验失败,发送端时间缺失,level=" + checkLevel
                                                + ",messageSenderTime" + messageSenderTime);
            }
            if (System.currentTimeMillis() - messageSenderTime > amsConfig.getTimeInterval()) {
                throw new AMSSignatureException(
                    "时间戳校验失败,报文投递超时,level=" + checkLevel + ",messageSenderTime" + messageSenderTime
                                                + ",interval=" + amsConfig.getTimeInterval());
            }
        }

        Map<String, String> params = new HashMap<String, String>(
            processContext.getProcessingParams());
        String content = "";
        if (processContext.isSignTypeIncluded()) {
            content = AlipaySignature.getSignCheckContentV2(params);
        } else {
            content = AlipaySignature.getSignCheckContentV1(params);
        }
        if (null == content) {
            throw new AMSSignatureException("验签原文为null");
        }

        AopVerifySignRequest verifySignRequest = new AopVerifySignRequest(processContext.getAppId(),
            processContext.getSign(), processContext.getSignType(), content,
            processContext.getCharset());

        //签名校验
        try {
            checkSignInner(verifySignRequest);
        } catch (AMSSignatureException e) {
            LoggerUtil.error(e, logger, "验签失败,verifySignRequest={0}", verifySignRequest);
            throw e;
        }
    }

    /**
     * 解密
     *
     * @param processContext
     * @throws AMSEncryptException
     */
    public void decryptContent(AMSProcessContext processContext) throws AMSEncryptException {
        if (!processContext.isNeedDecrypt()) {
            //不需要解密
            return;
        }

        //解密所有字段
        Set<String> decryptKeys = processContext.getDecryptKeys();
        if (null == decryptKeys) {
            LoggerUtil.debug(logger, "未找到解密key, context={0}", processContext);
            throw new AMSEncryptException("未找到解密key");
        }
        LoggerUtil.debug(logger, "循环解析报文中的以下key={0}", decryptKeys);
        for (String key : decryptKeys) {
            Map<String, String> processingParams = processContext.getProcessingParams();
            String ciphertext = processingParams.get(key);
            if (StringUtils.isBlank(ciphertext)) {
                LoggerUtil.info(logger, "{0}解密忽略, 该值为空或者不存在", key);
                continue;
            }
            AopDecryptRequest decryptRequest = new AopDecryptRequest(processContext.getAppId(),
                ciphertext, processContext.getEncryptType(), processContext.getCharset());
            try {
                String decryptContent = decryptContentInner(decryptRequest);
                LoggerUtil.debug(logger, "解密成功. ciphertext = {0}, decyptedContent{1}", ciphertext,
                    decryptContent);
                processContext.modifyValue(key, decryptContent);
            } catch (AMSEncryptException e) {
                LoggerUtil.warn(logger, "解密失败,decryptRequest={0}", decryptRequest);
                throw e;
            }
        }

    }

    /**
     *
     * @param verifySignRequest
     * @throws AMSSignatureException
     */
    protected abstract void checkSignInner(AopVerifySignRequest verifySignRequest) throws AMSSignatureException;

    /**
     *
     * @param decryptRequest
     */
    protected abstract String decryptContentInner(AopDecryptRequest decryptRequest) throws AMSEncryptException;

}
