/**
 *
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package cn.telmochan.antopen.message.manager.impl;

import org.apache.commons.lang3.StringUtils;

import cn.telmochan.antopen.constants.AlipayConstants;
import cn.telmochan.antopen.message.exception.AMSEncryptException;
import cn.telmochan.antopen.message.exception.AMSSignatureException;
import cn.telmochan.antopen.message.model.AopDecryptRequest;
import cn.telmochan.antopen.message.model.AopVerifySignRequest;
import cn.telmochan.antopen.message.model.ConfigHelper;
import cn.telmochan.antopen.message.model.SecurityConfig;
import cn.telmochan.antopen.util.AlipayEncrypt;
import cn.telmochan.antopen.util.AlipaySignature;

/**
 * 基于业务系统内存的加密器(适用于以下类型的开发者使用, 所在机构的安全等级允许在业务服务端内存进行加验签和加解密)
 *
 * @author telmochan
 * @version $Id: MemorySecurityManagerImpl.java, v 0.1 2018-01-17 下午7:02 telmochan Exp $
 */
public class MemorySecurityManagerImpl extends AbstractBaseSecurityManger {

    /**
     * 密钥加载器
     */
    private ConfigHelper configHelper;

    @Override
    protected void checkSignInner(AopVerifySignRequest verifySignRequest) throws AMSSignatureException {
        String appId = verifySignRequest.getAppId();
        String content = verifySignRequest.getSignContent();
        String sign = verifySignRequest.getSign();
        String signType = verifySignRequest.getSignType();
        String charset = verifySignRequest.getCharset();

        SecurityConfig securityConfig = configHelper.getSecurityConfig(appId);
        if (null == securityConfig) {
            throw new AMSSignatureException("安全配置未找到");
        }
        String publicKey = securityConfig.getAlipayPublicKeys().get(signType);
        if (StringUtils.isBlank(publicKey)) {
            throw new AMSSignatureException("应用未配置支付宝验签公钥");
        }

        try {
            boolean signChecked = false;
            if (AlipayConstants.SIGN_TYPE_RSA.equals(signType)) {
                signChecked = AlipaySignature.rsaCheckContent(content, sign, publicKey, charset);
            } else if (AlipayConstants.SIGN_TYPE_RSA2.equals(signType)) {
                signChecked = AlipaySignature.rsa256CheckContent(content, sign, publicKey, charset);
            } else {
                throw new AMSSignatureException("Sign Type is Not Support");
            }
            if (!signChecked) {
                throw new AMSSignatureException("验签失败");
            }

        } catch (AMSSignatureException e) {
            throw e;
        } catch (Exception e) {
            throw new AMSSignatureException(e);
        }
    }

    /**
     * @param decryptRequest
     */
    protected String decryptContentInner(AopDecryptRequest decryptRequest) throws AMSEncryptException {
        String appId = decryptRequest.getAppId();
        String encryptType = decryptRequest.getEncryptType();
        String ciphertext = decryptRequest.getCiphertext();
        String charset = decryptRequest.getCharset();

        SecurityConfig securityConfig = configHelper.getSecurityConfig(appId);
        if (null == securityConfig) {
            throw new RuntimeException("安全配置未找到");
        }

        String decryptKey = securityConfig.getAlipayPublicKeys().get(encryptType);
        if (StringUtils.isBlank(decryptKey)) {
            throw new RuntimeException("应用未配置解密密钥");
        }

        try {
            return AlipayEncrypt.decryptContent(ciphertext, encryptType, decryptKey, charset);
        } catch (AMSEncryptException e) {
            throw e;
        }
    }

    /**
     * Setter method for property <tt>configHelper</tt>
     */
    public void setConfigHelper(ConfigHelper configHelper) {
        this.configHelper = configHelper;
    }
}
