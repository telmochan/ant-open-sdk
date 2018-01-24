/**
 *
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package cn.telmochan.antopen.message.manager.impl;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import cn.telmochan.antopen.message.exception.AMSEncryptException;
import cn.telmochan.antopen.message.exception.AMSSignatureException;
import cn.telmochan.antopen.message.model.AMSSecurityAdapterApi;
import cn.telmochan.antopen.message.model.AopDecryptRequest;
import cn.telmochan.antopen.message.model.AopVerifySignRequest;
import cn.telmochan.antopen.util.LoggerUtil;

/**
 *
 * 基于独立加密机的安全服务(适用于以下类型的开发者使用, 所在机构的安全等级要求加解密和加验签由专用服务提供)
 *
 * @author telmochan
 * @version $Id: EncryptorSecurityManagerImpl.java, v 0.1 2018-01-17 下午8:37 telmochan Exp $
 */
public class EncryptorSecurityManagerImpl extends AbstractBaseSecurityManger {

    /**
     * 开发者需要自己注入外部安全服务
     */
    private AMSSecurityAdapterApi securityAdapterApi;

    @Override
    protected void checkSignInner(AopVerifySignRequest verifySignRequest) throws AMSSignatureException {
        String appId = verifySignRequest.getAppId();
        String content = verifySignRequest.getSignContent();
        String signString = verifySignRequest.getSign();
        String signType = verifySignRequest.getSignType();
        String charset = verifySignRequest.getCharset();

        if (StringUtils.isBlank(signString)) {
            throw new AMSSignatureException("sign值为空");
        }
        byte[] sign = ((String) signString).getBytes();

        try {
            byte[] byteContent = content.getBytes(charset);
            securityAdapterApi.checkSign(appId, sign, signType, byteContent);
        } catch (Throwable e) {
            LoggerUtil.error(e, logger, "未知外部验签异常");
            throw new AMSSignatureException(e);
        }
    }

    @Override
    protected String decryptContentInner(AopDecryptRequest decryptRequest) throws AMSEncryptException {
        String appId = decryptRequest.getAppId();
        String charset = decryptRequest.getCharset();
        String encryptType = decryptRequest.getEncryptType();
        String ciphertext = decryptRequest.getCiphertext();

        try {
            byte[] decyptResult = securityAdapterApi.decypt(appId, encryptType,
                Base64.decodeBase64(ciphertext.getBytes()));
            return new String(decyptResult, charset);
        } catch (Throwable e) {
            LoggerUtil.error(e, logger, "未知外部解密异常");
            throw new AMSEncryptException(e);
        }
    }

    /**
     * Setter method for property <tt>securityAdapterApi</tt>
     */
    public void setSecurityAdapterApi(AMSSecurityAdapterApi securityAdapterApi) {
        this.securityAdapterApi = securityAdapterApi;
    }
}
