package cn.telmochan.antopen;

import java.util.Map;

import com.alibaba.fastjson.JSON;

import cn.telmochan.antopen.constants.TimeCheckLevel;
import cn.telmochan.antopen.demo.FileBasedEvnConstantHelper;
import cn.telmochan.antopen.demo.RequestTypeReference;
import cn.telmochan.antopen.demo.SimpleMessageHandler;
import cn.telmochan.antopen.message.AopMessageClient;
import cn.telmochan.antopen.message.config.AMSConfig;
import cn.telmochan.antopen.message.manager.impl.MemorySecurityManagerImpl;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple MessageProcessDemo.
 */
public class MessageProcessDemoTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public MessageProcessDemoTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(MessageProcessDemoTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
        //消息处理配置
        AMSConfig amsConfig = new AMSConfig();
        amsConfig.setDefaultMessageHandler(new SimpleMessageHandler());
        amsConfig.setCheckLevel(TimeCheckLevel.TIME_CHECK_OFF);//关闭时间戳校验

        //安全配置(FileBasedEvnConstantHelper)默认依赖了类路径下的aopSecConfig.properties文件（FileBasedEvnConstantHelper依赖项目中明文存储的支付宝密钥，不推荐在生产环境使用）
        MemorySecurityManagerImpl securityManager = new MemorySecurityManagerImpl();
        FileBasedEvnConstantHelper simpleEvnHelper = new FileBasedEvnConstantHelper();
        securityManager.setConfigHelper(simpleEvnHelper);

        //消息客户端
        AopMessageClient messageClient = new AopMessageClient();
        messageClient.setAmsConfig(amsConfig);
        messageClient.setAmsSecurityManager(securityManager);

        //test1: 明文+验签失败
        String testRequest1 = "{\"charset\":[\"UTF-8\"],\"biz_content\":[\"{\\\"app_auth_token\\\":\\\"201801BB_Tc344ef084543a5a0a68060bc435X30\\\",\\\"auth_app_id\\\":\\\"2016071000048511\\\",\\\"user_id\\\":\\\"2088202831559302\\\",\\\"app_id\\\":\\\"2017072700094770\\\"}\"],\"utc_timestamp\":[\"1516114883040\"],\"sign\":[\"jYeXF2lnOsRlWOMgR8qm+wgsQAZikP3FbbFuICVkhNwGSNLBUGVovVxS7lJWNgxoetGEo66sbDik7q9K15RjeCLJxhq+51KqQyOjmRcxzcYRxNKm+Kd/rXTJtcJ27iJ5zvURGZPY79lkjr9iDlXvlS6OzUaqqvNEA7XSLmCR1+gBjtGYuBmCTrv01GvfrSnhf9GwM545dFgLUaSoGpgKA1V/YxpJWP2UiEJ0Hmn9Cwa4f0OkgokopWixY5SbMfZIvnfx0jjnX0tTb5uwHixBv0cwV721cF+lIN2c5qfITc/VgaK0NXPq/mKwGBs2qzp4EUnKUnmIBubho7geRzwk+Q==\"],\"app_id\":[\"2017072700094770\"],\"sign_type\":[\"RSA2\"],\"version\":[\"1.1\"],\"notify_id\":[\"157035bf84dc39b8a4ea9fb397fc800lpm\"],\"msg_method\":[\"alipay.open.auth.appauth.cancel\"]}";
        //test2: 密文+验签成功
        String testRequest2 = "{\"charset\":[\"UTF-8\"],\"biz_content\":[\"Qnb5HAiJfG6PpJF3Z43k7mm6yqQPvEK5WCrQarpnB6UUYaM1z/GWpgP3qEnmLYbaf4WYWYdejvj8uyqv1b+pRrmITD4MWUApUv1Vr8tFkxTy4Ujs/5Wv7J9SMCzoSwbGOAi6HGGwpHSodYM0V+HewI9OzHB8fvSnrT0YjHrn1CAuC4qr4TMbZp+UDARmgI+4fmjN9+UiTcdI8rzl4CMCPw==\"],\"encrypt_type\":[\"AES\"],\"utc_timestamp\":[\"1516363124021\"],\"sign\":[\"KxX9rGipJsA3COasEWwwqmuRn6debvy8BjmlarDPnJjbfhgkmgd/adzBZ9cbc/Xg1ybSfSmqnqAbTcFf+Hp95ocdPrFzW+FpEz4Uw26yEZLVctVw031HeleygMJ///ZmETURdOjTgEKwlxQkPAg3mwTi9LFiPgApcT/WKQjrA+H1WuTkIvxHkT66aGWDFwn3tR+hLmn6jsFZ2RR9gSvIQSsbViE62myCMJqTHZuok10JFLHFMebazLXGt2TFYhA28NjJSfWfdVhfwciUNct5TqWHqtbOWyt8AsXn8ky8uFuV5DHU+OrSfr97eSOVzIz6VHFdPFdFcgvTh8yxqppbCA==\"],\"app_id\":[\"2017072700094770\"],\"version\":[\"1.1\"],\"sign_type\":[\"RSA2\"],\"notify_id\":[\"ddea366c81886426648b97a33290bd8hxi\"],\"msg_method\":[\"alipay.open.auth.appauth.cancel\"]}";

        Map<String, String[]> request = JSON.parseObject(testRequest1, new RequestTypeReference());
        assertFalse(messageClient.processHttpMessage(request));

        Map<String, String[]> request2 = JSON.parseObject(testRequest2, new RequestTypeReference());
        assertTrue(messageClient.processHttpMessage(request2));
    }
}
