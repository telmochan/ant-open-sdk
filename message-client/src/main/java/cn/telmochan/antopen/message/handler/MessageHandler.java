/**
 *
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package cn.telmochan.antopen.message.handler;

import cn.telmochan.antopen.message.model.AMSProcessContext;

/**
 * @author telmochan
 * @version $Id: MessageHandler.java, v 0.1 2018-01-17 下午3:49 telmochan Exp $
 */
public interface MessageHandler {

    /**
     * 是否处理成功
     * @param processContext
     * @return
     */
    boolean process(AMSProcessContext processContext);
}
