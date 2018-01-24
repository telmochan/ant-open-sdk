/**
 *
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package cn.telmochan.antopen.message.model;

import java.util.Map;

/**
 * @author telmochan
 * @version $Id: ConfigHelper.java, v 0.1 2016-07-10 下午11:59 telmochan Exp $
 */
public interface ConfigHelper {
    /**
     * @return
     */
    public Map<String, SecurityConfig> getSecurityConfigMap();

    /**
     * @param appId
     * @return
     */
    public SecurityConfig getSecurityConfig(String appId);
}
