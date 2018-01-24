/**
 *
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package cn.telmochan.antopen.message.model;

import java.util.Map;

/**
 * @author telmochan
 * @version $Id: SecurityConfig.java, v 0.1 2016-07-10 下午10:13 telmochan Exp $
 */
public class SecurityConfig {
    private String              appId;

    //    //RSA=>公钥
    private Map<String, String> appPublicKeys;

    //RSA=>私钥
    private Map<String, String> appPrivateKeys;

    //RSA=>支付宝公钥
    private Map<String, String> alipayPublicKeys;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Map<String, String> getAppPublicKeys() {
        return appPublicKeys;
    }

    public void setAppPublicKeys(Map<String, String> appPublicKeys) {
        this.appPublicKeys = appPublicKeys;
    }

    public Map<String, String> getAppPrivateKeys() {
        return appPrivateKeys;
    }

    public void setAppPrivateKeys(Map<String, String> appPrivateKeys) {
        this.appPrivateKeys = appPrivateKeys;
    }

    public Map<String, String> getAlipayPublicKeys() {
        return alipayPublicKeys;
    }

    public void setAlipayPublicKeys(Map<String, String> alipayPublicKeys) {
        this.alipayPublicKeys = alipayPublicKeys;
    }
}
