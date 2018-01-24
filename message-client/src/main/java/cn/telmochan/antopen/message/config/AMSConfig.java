/**
 *
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package cn.telmochan.antopen.message.config;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import cn.telmochan.antopen.constants.MessageProtocolParamEnum;
import cn.telmochan.antopen.constants.TimeCheckLevel;
import cn.telmochan.antopen.message.exception.AMSException;
import cn.telmochan.antopen.message.handler.MessageHandler;

/**
 * 蚂蚁消息服务配置(接口维度的配置)
 *
 * @author telmochan
 * @version $Id: AMSConfig.java, v 0.1 2018-01-17 下午3:41 telmochan Exp $
 */
public class AMSConfig {

    /**
     * 报文的时间戳校验强度
     */
    private TimeCheckLevel                            checkLevel         = TimeCheckLevel.TIME_CHECK_FORCE;

    /**
     * 有效报文时间间隔(默认为1分钟, 毫秒, 只有checkLevel需要校验报文时间戳时该参数有效, 大于该时间间隔的报文会被丢弃)
     */
    private long                                      timeInterval       = 60 * 1000;

    /**
     * sign_type参与签名的接口列表(请接入方自主注入)
     */
    private Set<String>                               signTypeIncludedMsgs;
    /**
     * 消息接口处理器(接入方自行注入)
     */
    private ConcurrentHashMap<String, MessageHandler> messageHandlers;

    /**
     * 兜底的消息处理器,若messageHandlers没有对应的接口处理器的时候若该处理器配置了则使用此默认处理器
     */
    private MessageHandler                            defaultMessageHandler;

    /**
     * 接口对应需要解密的字段集合
     */
    private ConcurrentHashMap<String, Set<String>>    messageDecryptKeys;

    /**
     * 默认解密的字段名(若messageDecryptKeys没有配置对应的接口的配置则使用此默认配置,默认为biz_content)
     */
    private Set<String>                               defaultDecryptKeys = new HashSet<String>() {
                                                                             {
                                                                                 add(MessageProtocolParamEnum.BIZ_CONTENT
                                                                                     .getName());
                                                                             }
                                                                         };

    /**
     * 对应接口的报文签名时,sign_type参数是否参与签名验签
     *
     * @param messageName
     * @return
     */
    public boolean signTypeIncluded(String messageName) {
        return (null != signTypeIncludedMsgs && signTypeIncludedMsgs.contains(messageName));
    }

    /**
     * 获取消息接口业务处理器
     *
     * @param messageName
     * @return
     * @throws AMSException
     */
    public MessageHandler getMessageHandler(String messageName) throws AMSException {
        if (null != messageHandlers && null != messageHandlers.get(messageName)) {
            return messageHandlers.get(messageName);
        }
        if (null != defaultMessageHandler) {
            return defaultMessageHandler;
        }
        throw new AMSException(messageName + "无有效接口处理器");
    }

    /**
     * 获取消息接口需要解密的字段
     * @param messageName
     * @return
     * @throws AMSException
     */
    public Set<String> getMessageDecrpytKeys(String messageName) throws AMSException {
        if (null != messageDecryptKeys && null != messageDecryptKeys.get(messageName)) {
            return Collections.unmodifiableSet(messageDecryptKeys.get(messageName));
        }
        if (null != defaultDecryptKeys) {
            return Collections.unmodifiableSet(defaultDecryptKeys);
        }
        throw new AMSException(messageName + "找不到有效的解密key");
    }

    /**
     * 注册消息处理器
     *
     * @param messageName
     */
    public void registerMessageHandler(String messageName, MessageHandler messageHandler) {
        messageHandlers.put(messageName, messageHandler);
    }

    /**
     * Getter method for property <tt>checkLevel</tt>
     */
    public TimeCheckLevel getCheckLevel() {
        return checkLevel;
    }

    /**
     * Setter method for property <tt>checkLevel</tt>
     */
    public void setCheckLevel(TimeCheckLevel checkLevel) {
        this.checkLevel = checkLevel;
    }

    /**
     * Getter method for property <tt>signTypeIncludedMsgs</tt>
     */
    public Set<String> getSignTypeIncludedMsgs() {
        if (null == signTypeIncludedMsgs) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(signTypeIncludedMsgs);
    }

    /**
     * Setter method for property <tt>signTypeIncludedMsgs</tt>
     */
    public void setSignTypeIncludedMsgs(Set<String> signTypeIncludedMsgs) {
        this.signTypeIncludedMsgs = signTypeIncludedMsgs;
    }

    /**
     * Getter method for property <tt>messageHandlers</tt>
     */
    public Map<String, MessageHandler> getMessageHandlers() {
        return Collections.unmodifiableMap(messageHandlers);
    }

    /**
     * Setter method for property <tt>messageHandlers</tt>
     */
    public void setMessageHandlers(ConcurrentHashMap<String, MessageHandler> messageHandlers) {
        this.messageHandlers = messageHandlers;
    }

    /**
     * Getter method for property <tt>timeInterval</tt>
     */
    public long getTimeInterval() {
        return timeInterval;
    }

    /**
     * Setter method for property <tt>timeInterval</tt>
     */
    public void setTimeInterval(long timeInterval) {
        if (timeInterval > 0) {
            this.timeInterval = timeInterval;
        }
    }

    /**
     * Setter method for property <tt>defaultMessageHandler</tt>
     */
    public void setDefaultMessageHandler(MessageHandler defaultMessageHandler) {
        this.defaultMessageHandler = defaultMessageHandler;
    }

    /**
     * Setter method for property <tt>messageDecryptKeys</tt>
     */
    public void setMessageDecryptKeys(ConcurrentHashMap<String, Set<String>> messageDecryptKeys) {
        this.messageDecryptKeys = messageDecryptKeys;
    }

    /**
     * Setter method for property <tt>defaultDecryptKeys</tt>
     */
    public void setDefaultDecryptKeys(Set<String> defaultDecryptKeys) {
        this.defaultDecryptKeys = defaultDecryptKeys;
    }
}
