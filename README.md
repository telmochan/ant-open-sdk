# message-client

> 蚂蚁金服开放平台消息服务sdk，用于处理蚂蚁金服发送给开发者的消息（比如说交易成功消息）。鉴于蚂蚁金服开放平台官方服务端sdk没有提供开箱即用的工具，所以才有了这个小项目。由于是个人项目，若您在生产环境使用此工具请注意做好回归测试和性能测试。任何意见和建议请邮件至telmochan@hotmail.com

## 功能说明

- 本工程可用于对接 [蚂蚁金服开放平台](https://www.ant-open.com/platform/home.htm) 的异步通知（消息服务）
- 可以自定义加密服务来源

## 简单示例（仅供测试使用）-低安全等级
将项目clone到本地后，执行`cn.telmochan.antopen.MessageProcessDemoTest#testApp`可以看下效果，其中AopMessageClient的初始化工作如下：
```java
//消息处理配置(这里使用了兜底的消息处理器SimpleMessageHandler，你可以根据需要配置不同消息的处理器)
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

//使用消息客户端处理蚂蚁开放平台的消息
messageClient.processHttpMessage(XXX)
```
其中安全配置文件的格式如下：
```
#安全配置示例
config_alipay-你的应用ID-RSA=支付宝的RSA公钥
config_alipay-你的应用ID-RSA2=支付宝的RSA2公钥
config_alipay-你的应用ID-AES=支付宝分配的AES密钥
```
## 生产配置
`cn.telmochan.antopen.message.AopMessageClient#processHttpMessage`是处理蚂蚁开放平台的入口，Client的启动依赖两个配置：`cn.telmochan.antopen.message.config.AMSConfig`（接口配置）和`cn.telmochan.antopen.message.manager.AMSSecurityManager`（安全处理服务）。

### AMSConfig配置
主要配置`messageHandlers`属性即可，其它属性可以采用默认配置即可
* `messageHandlers`
key为蚂蚁金服开放平台的消息接口名，对应报文中的notify_Type或者msg_method（比如：[支付宝当面付异步通知](https://docs.open.alipay.com/194/103296/)中交易通知接口为notify_type=trade_status_sync），value需要你自己实现`cn.telmochan.antopen.message.handler.MessageHandler`接口，实现类中仅需要处理业务逻辑即可，如果处理成功则返回true，如果处理失败则返回false。
* `defaultMessageHandler`
该处理器为兜底处理器，如果在messageHandlers找不到某消息接口的处理器则使用该兜底处理器
* `timeInterval`
报文有效性校验的时间窗口，默认为60秒。
* `checkLevel`
时间戳校验等级，分三类：
    * TIME_CHECK_FORCE：强制校验（默认），要求报文中必须“报文投递时间”并且“当前时间”-报文投递时间 <= timeInterval, 否则会被认为是不合法报文；
    * TIME_CHECK_COMPATIBLE：兼容校验，若报文中有“报文投递时间”则校验时间戳，否则不校验
    * TIME_CHECK_OFF：关闭校验，生产环境应避免使用此配置
* `signTypeIncludedMsgs`
sign_type参与签名的接口集合，蚂蚁金服开放平台的异步消息有少部分接口在做验签时需要将sign_type参与签名验签，这部分接口可以配置到集合中
* `messageDecryptKeys`
加密字段key，该配置为预留配置。
* `defaultDecryptKeys`
默认加密字段key，默认为biz_content字段为可选的解密字段

### AMSSecurityManager配置
提供了两种`AMSSecurityManager`实现类：
* `MemorySecurityManagerImpl`，基于内存的安全服务，这种情况下需要你实现`cn.telmochan.antopen.message.model.ConfigHelper`接口用于向框架提供验签和解密所需要的密钥。【message-client-demo提供了一个测试用实现类`cn.telmochan.antopen.demo.FileBasedEvnConstantHelper`，由于该实现类在类路径下查找密钥配置文件，基于安全原因考虑，在生产环境应避免使用该实现类。】

基于当前机器内存做验签和解密运算的实现类，
* `EncryptorSecurityManagerImpl`
基于外部安全服务的实现类（安全等级要求高的公司一般会提供统一的安全服务器用于提供加解密和加验签服务，如果你工作于这样的公司，大概率可能会不需要这个github项目），这种情况你需要注入`cn.telmochan.antopen.message.model.AMSSecurityAdapterApi`接口的实现类用于对接你公司的安全服务。

## 其它说明
### web层代码示例
根据本人了解，蚂蚁金服的异步消息均需要在开发者在处理成功后返回“success”（不包括引号的七个小写字符），在处理失败的时候返回“fail”（不包括引号的四个小写字符）。简单的web层处理示例代码如下
```java
@ResponseBody
@RequestMapping(value = "standardGateway", method = RequestMethod.POST)
public String doPost(HttpServletRequest request) {
    Map<String, String[]> allParam = request.getParameterMap();

    if (aopMessageClient.processHttpMessage(allParam)) {
    return "success";
    } else {
    return "fail";
    }
}
```

### 高并发情况处理最佳实践
> 这个最佳实践需要多丰富的系统设计能力，如果有任何好的设计欢迎在评论区共享给需要的开发者
由于蚂蚁金服开放平台为是走http(s)将异步消息推送给开发者网关，这种Push模式的消息推送在高并发的情况可能会对你的网关造成较大的压力。
* 可以考虑在`cn.telmochan.antopen.message.handler.MessageHandler`实现类中作异步化处理（比如将业务处理请求投递到自己的消息队列或者存到DB中）。这样不仅可以实现消息服务的平滑消费处理，同时在你的网关地址发生故障的时候可以自行恢复而不需要依赖蚂蚁金服的重试投递，否则大量的重试消息和新增的消息可能还会把你的服务器压垮。
* 若这种处理消息处理的实时性有影响的话，可以通过差异化的异步策略尽量去处理，比如区分消息的等级走不同的异步策略等。



