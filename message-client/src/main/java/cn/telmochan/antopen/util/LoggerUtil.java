/**
 *
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package cn.telmochan.antopen.util;

import java.text.MessageFormat;

import org.apache.commons.logging.Log;

/**
 *
 * @author telmochan
 * @version $Id: LoggerUtil.java, v 0.1 2016-07-08 上午2:36 telmochan Exp $
 */
public final class LoggerUtil {
    /**
     * <BR>
     *
     * @param template
     * @param parameters
     * @return
     */
    public static String getMessage(String template, Object... parameters) {
        return MessageFormat.format(template, parameters);
    }

    /**
     * <p>生成调试级别日志</p>
     * <p>
     * 根据带参数的日志模板和参数集合，生成debug级别日志
     * 带参数的日志模板中以{数字}表示待替换为变量的日志点，如a={0}，表示用参数集合中index为0的参数替换{0}处
     * </p>
     * @param logger        logger引用
     * @param template      带参数的日志模板
     * @param parameters    参数集合
     */
    public static void debug(Log logger, String template, Object... parameters) {
        if (logger.isDebugEnabled()) {
            logger.debug(getMessage(template, parameters));
        }
    }

    /**
     * <p>生成通知级别日志</p>
     * <p>
     * 根据带参数的日志模板和参数集合，生成info级别日志
     * 带参数的日志模板中以{数字}表示待替换为变量的日志点，如a={0}，表示用参数集合中index为0的参数替换{0}处
     * </p>
     * @param logger        logger引用
     * @param template      带参数的日志模板
     * @param parameters    参数集合
     */
    public static void info(Log logger, String template, Object... parameters) {
        if (logger.isInfoEnabled()) {
            logger.info(getMessage(template, parameters));
        }
    }

    /**
     * <p>生成警告级别日志</p>
     * <p>
     * 根据带参数的日志模板和参数集合，生成warn级别日志
     * 带参数的日志模板中以{数字}表示待替换为变量的日志点，如a={0}，表示用参数集合中index为0的参数替换{0}处
     * </p>
     * @param e             错误异常堆栈
     * @param logger        logger引用
     * @param template      带参数的日志模板
     * @param parameters    参数集合
     */
    public static void warn(Log logger, String template, Object... parameters) {
        logger.warn(getMessage(template, parameters));
    }

    /**
     * <p>生成警告级别日志</p>
     * <p>带异常堆栈</p>
     * @param e
     * @param logger
     * @param template
     * @param parameters
     */
    public static void warn(Throwable e, Log logger, String template, Object... parameters) {
        logger.warn(getMessage(template, parameters), e);
    }

    /**
     * <p>生成错误级别日志</p>
     * <p>
     * 根据带参数的日志模板和参数集合，生成error级别日志
     * 带参数的日志模板中以{数字}表示待替换为变量的日志点，如a={0}，表示用参数集合中index为0的参数替换{0}处
     * </p>
     * @param e             错误异常堆栈
     * @param logger        logger引用
     * @param template      带参数的日志模板
     * @param parameters    参数集合
     */
    public static void error(Throwable e, Log logger, String template, Object... parameters) {
        logger.error(getMessage(template, parameters), e);
    }

    /**
     * <p>生成错误级别日志</p>
     * <p>
     * 根据带参数的日志模板和参数集合，生成error级别日志
     * 带参数的日志模板中以{数字}表示待替换为变量的日志点，如a={0}，表示用参数集合中index为0的参数替换{0}处
     * </p>
     * @param logger        logger引用
     * @param template      带参数的日志模板
     * @param parameters    参数集合
     */
    public static void error(Log logger, String template, Object... parameters) {
        logger.error(getMessage(template, parameters));
    }
}
