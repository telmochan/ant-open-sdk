/**
 *
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package cn.telmochan.antopen.demo;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.telmochan.antopen.message.model.ConfigHelper;
import cn.telmochan.antopen.message.model.SecurityConfig;
import cn.telmochan.antopen.util.LoggerUtil;

/**
 * 基于文件的密钥查询器
 * @author telmochan
 * @version $Id: FileBasedEvnConstantHelper.java, v 0.1 2016-07-10 下午10:12 telmochan Exp $
 */
public class FileBasedEvnConstantHelper implements ConfigHelper {

    private final String                CONF_KEY_APP_PREFIX    = "config_app-";
    private final String                CONF_KEY_ALIPAY_PREFIX = "config_alipay-";
    private final String                CONF_SPLITTER          = "-";

    private final String                CLASSPATH_URL_PREFIX   = "classpath:";
    /**
     * log
     */
    private Log                         log                    = LogFactory
        .getLog("AOP-SDK-MESSAGE-CLIENT");
    private String                      configFile             = "aopSecConfig.properties";

    //app=>安全配置
    private Map<String, SecurityConfig> securityConfigMap      = new HashMap<String, SecurityConfig>();

    /**
     *
     * @param appId
     * @return
     */
    public SecurityConfig getSecurityConfig(String appId) {
        if (null == securityConfigMap || securityConfigMap.size() == 0) {
            try {
                reloadConfig();
            } catch (IOException e) {
                return null;
            }
        }
        if (securityConfigMap.containsKey(appId)) {
            return securityConfigMap.get(appId);
        }
        return null;
    }

    /**
     *
     * @return
     */
    public Map<String, SecurityConfig> getSecurityConfigMap() {
        return securityConfigMap;
    }

    /**
     * 接在配置
     * @throws IOException
     */
    public void reloadConfig() throws IOException {
        HashMap<String, SecurityConfig> newConfig = new HashMap<String, SecurityConfig>();

        InputStream configStream = loadClassPathConfig(configFile);
        if (null != configStream) {
            Properties properties = new Properties();
            properties.load(configStream);
            Set<Object> keys = properties.keySet();
            for (Object key : keys) {
                String oneKey = (String) key;
                parseOneKey(newConfig, oneKey, (String) properties.get(oneKey));
            }
        }
        if (newConfig.size() > 0) {
            securityConfigMap = newConfig;
        }
    }

    /**
     * 读取配置文件
     * @param filePath
     * @return
     */
    private InputStream loadClassPathConfig(String filePath) {
        try {
            if (StringUtils.startsWith(filePath, CLASSPATH_URL_PREFIX)) {
                filePath = filePath.substring(CLASSPATH_URL_PREFIX.length());
            }
            if (StringUtils.startsWith(filePath, "/")) {
                filePath = filePath.substring(1);
            }
            ClassLoader cl = null;
            try {
                cl = Thread.currentThread().getContextClassLoader();
            } catch (Throwable ex) {
                // Cannot access thread context ClassLoader - falling back to system class loader...
            }
            if (cl == null) {
                // No thread context class loader -> use class loader of this class.
                cl = this.getClass().getClassLoader();
            }
            return cl.getResourceAsStream(filePath);
        } catch (Exception e) {
            LoggerUtil.error(log, "加载本地安全配置失败.configFile=" + configFile);
            return null;
        }
    }

    private void parseOneKey(Map<String, SecurityConfig> securityConfigMap, String oneKey,
                             String value) {
        if (StringUtils.isEmpty(oneKey)) {
            return;
        }
        if (oneKey.startsWith(CONF_KEY_APP_PREFIX)
            && oneKey.length() > CONF_KEY_APP_PREFIX.length() + 1) {
            int newStart = oneKey.indexOf(CONF_KEY_APP_PREFIX) + CONF_KEY_APP_PREFIX.length();
            parseAppSecConfig(securityConfigMap, oneKey.substring(newStart), value);
        } else if (oneKey.startsWith(CONF_KEY_ALIPAY_PREFIX)
                   && oneKey.length() > CONF_KEY_ALIPAY_PREFIX.length() + 1) {
            int newStart = oneKey.indexOf(CONF_KEY_ALIPAY_PREFIX) + CONF_KEY_ALIPAY_PREFIX.length();
            parseAlipaySecConfig(securityConfigMap, oneKey.substring(newStart), value);
        }
    }

    private void parseAlipaySecConfig(Map<String, SecurityConfig> securityConfigMap,
                                      String substring, String value) {
        //RSA-pub
        String[] params = substring.split(CONF_SPLITTER);
        if (null == params || params.length != 2) {
            return;
        }
        String appId = params[0];
        String alg = params[1];

        if (!securityConfigMap.containsKey(appId)) {
            securityConfigMap.put(appId, new SecurityConfig());
        }
        SecurityConfig securityConfig = securityConfigMap.get(appId);
        if (null == securityConfig.getAlipayPublicKeys()) {
            securityConfig.setAlipayPublicKeys(new HashMap<String, String>());
        }
        Map<String, String> alipayPublicKeys = securityConfig.getAlipayPublicKeys();
        alipayPublicKeys.put(alg, value);
    }

    private void parseAppSecConfig(Map<String, SecurityConfig> securityConfigMap, String substring,
                                   String value) {
        //APPID-RSA-pri
        String[] params = substring.split(CONF_SPLITTER);
        if (null == params || params.length != 3) {
            return;
        }
        String appId = params[0];
        String alg = params[1];
        String type = params[2];
        if (!StringUtils.equals(type, "pri") && !StringUtils.equals(type, "pub")) {
            return;
        }

        if (!securityConfigMap.containsKey(appId)) {
            securityConfigMap.put(appId, new SecurityConfig());
        }
        SecurityConfig securityConfig = securityConfigMap.get(appId);
        securityConfig.setAppId(appId);
        Map<String, String> appPriMap = securityConfig.getAppPrivateKeys();
        Map<String, String> appPubMap = securityConfig.getAppPublicKeys();
        if (null == appPriMap) {
            appPriMap = new HashMap<String, String>();
            securityConfig.setAppPrivateKeys(appPriMap);
        }
        if (null == appPubMap) {
            appPubMap = new HashMap<String, String>();
            securityConfig.setAppPublicKeys(appPubMap);
        }

        if (type.equals("pri")) {
            appPriMap.put(alg, value);
        } else if (type.equals("pub")) {
            appPubMap.put(alg, value);
        }
    }

    public String getConfigFile() {
        return configFile;
    }

    public void setConfigFile(String configFile) {
        this.configFile = configFile;
    }

}
