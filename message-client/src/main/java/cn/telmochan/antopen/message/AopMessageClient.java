/**
 *
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package cn.telmochan.antopen.message;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.telmochan.antopen.message.config.AMSConfig;
import cn.telmochan.antopen.message.exception.AMSException;
import cn.telmochan.antopen.message.handler.MessageHandler;
import cn.telmochan.antopen.message.manager.AMSSecurityManager;
import cn.telmochan.antopen.message.model.AMSProcessContext;
import cn.telmochan.antopen.util.LoggerUtil;

/**
 * @author telmochan
 * @version $Id: AopMessageClient.java, v 0.1 2018-01-15 下午11:22 telmochan Exp $
 */
public class AopMessageClient {

    /**
     * LOGGER
     */
    private Log                logger       = LogFactory.getLog("AOP-SDK-MESSAGE-CLIENT");

    /**
     * digest log
     */
    private Log                detailLogger = LogFactory.getLog("AOP-SDK-MESSAGE-CLIENT-DETAIL");

    /**
     * 配置
     */
    private AMSConfig          amsConfig;

    /**
     * 安全处理器
     */
    private AMSSecurityManager amsSecurityManager;

    /**
     * 处理蚂蚁开放平台投递的Http消息
     *
     * @param params
     * @return
     * @throws AMSException
     */
    public boolean processHttpMessage(Map<String, String[]> params) {
        try {
            //基本参数校验
            AMSProcessContext processContext = new AMSProcessContext();
            processContext.initProtocolParam(params, amsConfig);
            LoggerUtil.debug(detailLogger,
                "AopMessageClient收到处理请求:appId={0},messageName={1},notifyId={2},senderTime={3},processingParams={4}",
                processContext.getAppId(), processContext.getMessageName(),
                processContext.getNotifyId(), processContext.getMessageSenderTime(),
                processContext.getProcessingParams());

            //验签(带可选的时效性校验)
            amsSecurityManager.checkSign(processContext, amsConfig);

            //解密
            amsSecurityManager.decryptContent(processContext);

            //业务处理
            MessageHandler messageHandler = amsConfig
                .getMessageHandler(processContext.getMessageName());
            return messageHandler.process(processContext);
        } catch (AMSException e) {
            LoggerUtil.error(e, logger, "原始请求解析异常");
            return false;
        } catch (Throwable e) {
            LoggerUtil.error(e, logger, "未知异常");
            return false;
        }
    }

    /**
     * 注册消息处理器
     *
     * @param messageName
     */
    public void registerMessageHandler(String messageName, MessageHandler messageHandler) {
        this.amsConfig.registerMessageHandler(messageName, messageHandler);
    }

    /**
     * Setter method for property <tt>amsConfig</tt>
     */
    public void setAmsConfig(AMSConfig amsConfig) {
        this.amsConfig = amsConfig;
    }

    /**
     * Setter method for property <tt>amsSecurityManager</tt>
     */
    public void setAmsSecurityManager(AMSSecurityManager amsSecurityManager) {
        this.amsSecurityManager = amsSecurityManager;
    }
}
