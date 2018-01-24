/**
 *
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package cn.telmochan.antopen.constants;

/**
 * @author telmochan
 * @version $Id: TimeCheckLevel.java, v 0.1 2018-01-17 下午7:17 telmochan Exp $
 */
public enum TimeCheckLevel {
                            /**
                             * 关闭时效性校验
                             */
                            TIME_CHECK_OFF,
                            /**
                             * 可降级校验(若有时间戳参数则校验报文时效性,没有则不校验)
                             */
                            TIME_CHECK_COMPATIBLE,
                            /**
                             * 强制校验(报文必须有时间戳参数,同时校验报文时效性)
                             */
                            TIME_CHECK_FORCE,;
}
