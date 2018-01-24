/**
 *
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package cn.telmochan.antopen.demo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.telmochan.antopen.constants.MessageProtocolParamEnum;
import cn.telmochan.antopen.message.handler.MessageHandler;
import cn.telmochan.antopen.message.model.AMSProcessContext;

/**
 * @author telmochan
 * @version $Id: SimpleMessageHandler.java, v 0.1 2018-01-17 下午10:45 telmochan Exp $
 */
public class SimpleMessageHandler implements MessageHandler {

    /**``
     * LOG
     */
    Log log = LogFactory.getLog("AOP-MESSAGE-DEMO");

    /**
     * @param processContext
     * @return
     */
    public boolean process(AMSProcessContext processContext) {
        log.info(processContext.getProcessingParams()
            .get(MessageProtocolParamEnum.BIZ_CONTENT.getName()));
        return true;
    }
}
