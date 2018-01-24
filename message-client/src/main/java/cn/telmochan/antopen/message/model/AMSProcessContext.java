/**
 *
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package cn.telmochan.antopen.message.model;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.telmochan.antopen.constants.MessageProtocolParamEnum;
import cn.telmochan.antopen.message.config.AMSConfig;
import cn.telmochan.antopen.message.exception.AMSException;
import cn.telmochan.antopen.util.LoggerUtil;

/**
 * @author telmochan
 * @version $Id: AMSProcessContext.java, v 0.1 2018-01-17 下午4:05 telmochan Exp $
 */
public class AMSProcessContext {

    /**
     * 是否需要解密
     */
    boolean                       needDecrypt       = false;
    /**
     * log
     */
    private Log                   logger            = LogFactory.getLog("AOP-SDK-MESSAGE-CLIENT");
    /**
     * 消息接受方的appId
     */
    private String                appId;
    /**
     * 消息接口的名称(兼容老的notify_type)
     */
    private String                messageName;
    /**
     * 消息的版本
     */
    private String                version;
    /**
     * 签名
     */
    private String                sign;
    /**
     * 签名算法
     */
    private String                signType;
    /**
     * 加密算法
     */
    private String                encryptType;

    /**
     * 消息唯一序号
     */
    private String                notifyId;

    /**
     * 本次需要解密的key[若需要解密,则这些key为所有需要解密字段的key]
     */
    private Set<String>           decryptKeys;

    /**
     * 字符集
     */
    private String                charset;
    /**
     * 服务端发送消息的时间(接收方可以根据此参数做s时间戳校验
     */
    private long                  messageSenderTime = -1;

    /**
     * 中间处理参数(值取自originalInputs的第0个元素)
     */
    private Map<String, String>   processingParams;
    /**
     * 原始请求参数(不允许修改)
     */
    private Map<String, String[]> originalInputs;

    /**
     * signType是否参与签名
     */
    private boolean               signTypeIncluded;

    /**
     * 初始化
     *
     * @param originalInputs
     * @param amsConfig
     * @throws AMSException
     */
    public void initProtocolParam(Map<String, String[]> originalInputs,
                                  AMSConfig amsConfig) throws AMSException {
        if (null == originalInputs || originalInputs.size() == 0) {
            throw new AMSException("input为空");
        }

        processingParams = new HashMap<String, String>(originalInputs.size());

        for (Map.Entry<String, String[]> entry : originalInputs.entrySet()) {
            String[] values = entry.getValue();
            if (null == values || 0 == values.length) {
                LoggerUtil.warn(logger, "请求key的值为空,key={0}", entry.getKey());
            }
            processingParams.put(entry.getKey(), values[0]);
        }

        appId = processingParams.get(MessageProtocolParamEnum.APP_ID.getName());
        notifyId = processingParams.get(MessageProtocolParamEnum.NOTIFY_ID.getName());
        messageName = processingParams.get(MessageProtocolParamEnum.MESSAGE_API_NAME.getName());
        if (StringUtils.isBlank(messageName)) {
            messageName = processingParams.get(MessageProtocolParamEnum.OLD_NOTIFY_TYPE.getName());
        }
        version = StringUtils
            .defaultIfBlank(processingParams.get(MessageProtocolParamEnum.VERSION.getName()), "");
        if (StringUtils.isBlank(appId)) {
            throw new AMSException("appId missing");
        }
        if (StringUtils.isBlank(messageName)) {
            throw new AMSException("messageApiName missing");
        }
        try {
            messageSenderTime = Long.parseLong(
                processingParams.get(MessageProtocolParamEnum.MESSAGE_TIMESTAMPS.getName()));
            if (-1 == messageSenderTime) {
                SimpleDateFormat defaultDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                    Locale.CHINA);
                messageSenderTime = defaultDateFormat
                    .parse(processingParams.get(MessageProtocolParamEnum.OLD_NOTIFY_TIME.getName()))
                    .getTime();
            }
        } catch (Exception e) {
            //ignore
        }
        sign = processingParams.get(MessageProtocolParamEnum.SIGN.getName());
        if (StringUtils.isBlank(sign)) {
            throw new AMSException("sign为空");
        }
        signType = processingParams.get(MessageProtocolParamEnum.SIGN_TYPE.getName());
        if (StringUtils.isBlank(signType)) {
            throw new AMSException("sign_type为空");
        }
        charset = processingParams.get(MessageProtocolParamEnum.CHARSET.getName());
        if (StringUtils.isBlank(charset) || !Charset.isSupported(charset)) {
            throw new AMSException("不支持的字符集:charset=" + charset);
        }

        signTypeIncluded = false;
        if (null != amsConfig && null != amsConfig.getSignTypeIncludedMsgs()
            && amsConfig.getSignTypeIncludedMsgs().contains(messageName)) {
            signTypeIncluded = true;
        }

        //目前仅支持biz_content字段的加密(若报文中有encrypt_type且有值的情况下,表示biz_content为密文)
        encryptType = processingParams.get(MessageProtocolParamEnum.ENCRYPT_TYPE.getName());
        if (StringUtils.isNotBlank(encryptType)) {
            needDecrypt = true;
        }
        decryptKeys = amsConfig.getMessageDecrpytKeys(messageName);

        this.originalInputs = originalInputs;
    }

    /**
     * 修改配置参数
     *
     * @param key
     * @param value
     */
    public void modifyValue(String key, String value) {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(value)) {
            return;
        }
        processingParams.put(key, value);
    }

    /**
     * Getter method for property <tt>appId</tt>
     */
    public String getAppId() {
        return appId;
    }

    /**
     * Getter method for property <tt>messageName</tt>
     */
    public String getMessageName() {
        return messageName;
    }

    /**
     * Getter method for property <tt>version</tt>
     */
    public String getVersion() {
        return version;
    }

    /**
     * Getter method for property <tt>sign</tt>
     */
    public String getSign() {
        return sign;
    }

    /**
     * Getter method for property <tt>needDecrypt</tt>
     */
    public boolean isNeedDecrypt() {
        return needDecrypt;
    }

    /**
     * Getter method for property <tt>signType</tt>
     */
    public String getSignType() {
        return signType;
    }

    /**
     * Getter method for property <tt>encryptType</tt>
     */
    public String getEncryptType() {
        return encryptType;
    }

    /**
     * Getter method for property <tt>charset</tt>
     */
    public String getCharset() {
        return charset;
    }

    /**
     * Getter method for property <tt>messageSenderTime</tt>
     */
    public long getMessageSenderTime() {
        return messageSenderTime;
    }

    /**
     * Getter method for property <tt>processingParams</tt>
     */
    public Map<String, String> getProcessingParams() {
        return Collections.unmodifiableMap(processingParams);
    }

    /**
     * Getter method for property <tt>originalInputs</tt>
     */
    public Map<String, String[]> getOriginalInputs() {
        return Collections.unmodifiableMap(originalInputs);
    }

    /**
     * Getter method for property <tt>decryptKeys</tt>
     */
    public Set<String> getDecryptKeys() {
        return Collections.unmodifiableSet(decryptKeys);
    }

    /**
     * Getter method for property <tt>signTypeIncluded</tt>
     */
    public boolean isSignTypeIncluded() {
        return signTypeIncluded;
    }

    /**
     * Getter method for property <tt>notifyId</tt>
     */
    public String getNotifyId() {
        return notifyId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
    }
}
