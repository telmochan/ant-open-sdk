/**
 *
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package cn.telmochan.antopen.constants;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 消息服务的协议字段
 *
 * @author telmochan
 * @version $Id: MessageProtocolParamEnum.java, v 0.1 2017-12-20 下午7:54 telmochan Exp $
 */
public enum MessageProtocolParamEnum {

                                      //===== 消息核心协议字段-start

                                      /**
                                       * [必输]消息ID,消息接收方用于做幂等(点对点消息为现有的notify_id,广播消息需要保证和)
                                       */
                                      NOTIFY_ID("notify_id", "通知ID"),

                                      /**
                                       * [必输]消息发送时间,用于做消息的时效性控制(消息投递时的服务端时间,1970年1月1日零点开始算起的毫秒数)
                                       */
                                      MESSAGE_TIMESTAMPS("utc_timestamp", "消息发送时的服务端时间"),

                                      /**
                                       * [必输]消息API的名称
                                       */
                                      MESSAGE_API_NAME("msg_method", "消息接口名称"),

                                      /**
                                       * [必输]消息接受者的appId(接收方需要根据此参数选择密钥进行验签或者解密)
                                       */
                                      APP_ID("app_id", "消息接受方的应用id"),

                                      /**
                                       * [必输]消息类型. 分为: 系统消息, 用户消息(纯用户消息, 应用消息, 门店消息)
                                       */
                                      MESSAGE_TYPE("msg_type", "消息类型"),

                                      /**
                                       * [用户消息必输]消息所有者的userId(简单理解是商户2088开头的uid)
                                       */
                                      MESSAGE_USER_ID("msg_uid", "消息归属的商户支付宝uid"),

                                      /**
                                       * [应用消息必输]消息细分类型(应用消息)
                                       */
                                      MESSAGE_APP_ID("msg_app_id", "消息归属方的应用id"),

                                      /**
                                       * [门店消息必输]消息细分类型(门店消息)
                                       */
                                      MESSAGE_MSHOP_ID("msg_mshop_id", "门店消息对应的门店id"),

                                      /**
                                       * 消息接口版本
                                       */
                                      VERSION("version", "版本号"),

                                      /**
                                       * 标准消息的消息内容
                                       */
                                      BIZ_CONTENT("biz_content", "消息报文"),

                                      //===== 消息核心协议字段-end

                                      //===== 安全相关-start
                                      /**
                                       * 签名
                                       */
                                      SIGN("sign", "签名"),

                                      /**
                                       * 签名类型
                                       */
                                      SIGN_TYPE("sign_type", "签名类型"),

                                      /**
                                       * 加密算法
                                       */
                                      ENCRYPT_TYPE("encrypt_type", "加密算法"),

                                      //===== 安全相关-end,

                                      /**
                                       * 签名验签字符集
                                       */
                                      CHARSET("charset", "编码集"),

                                      //===== 老配置-start
                                      /**
                                       * [升级兼容参数]通知类型
                                       */
                                      OLD_NOTIFY_TYPE("notify_type", "[老协议参数]通知类型"),

                                      /**
                                       * [升级兼容参数]通知时间(通知发送时的服务端当前时区的时间,目前都是北京时间)
                                       */
                                      OLD_NOTIFY_TIME("notify_time", "[老协议参数]通知时间"),

                                      /**
                                       * [升级兼容参数]授权方的应用
                                       */
                                      OLD_AUTH_APP_ID("auth_app_id", "[老协议参数]授权方的应用id"),

    //===== 老配置-end

    ;

    private static final Map<String, MessageProtocolParamEnum> protocals;

    static {
        protocals = new HashMap<String, MessageProtocolParamEnum>();
        for (MessageProtocolParamEnum oneProtocol : MessageProtocolParamEnum.values()) {
            protocals.put(oneProtocol.getName(), oneProtocol);
        }
    }

    /**
     * 枚举名称或代码
     */
    private final String name;
    /**
     * 枚举实际值或详细信息
     */
    private final String value;

    /**
     * 根据枚举名称和枚举值来创建一个新的枚举
     *
     * @param name
     * @param value
     */
    private MessageProtocolParamEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * 根据名称获取枚举对象
     *
     * @param name
     * @return
     */
    public static MessageProtocolParamEnum getEnumByName(String name) {
        String str = name;
        if (str == null) {
            return null;
        }
        str = str.trim();
        if (str.length() == 0) {
            return null;
        }
        for (MessageProtocolParamEnum oneProtocol : values()) {
            if (oneProtocol.getName().equals(str)) {
                return oneProtocol;
            }
        }
        return null;
    }

    /**
     * 判断请求的集合是否包含协议参数
     * @param comparesValues
     * @return
     */
    public static boolean containsAny(Set<String> comparesValues) {
        if (null == comparesValues) {
            return false;
        }
        for (String key : comparesValues) {
            if (protocals.keySet().contains(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Getter method for property <tt>name</tt>
     */
    public String getName() {
        return name;
    }

    /**
     * Getter method for property <tt>value</tt>
     */
    public String getValue() {
        return value;
    }
}
