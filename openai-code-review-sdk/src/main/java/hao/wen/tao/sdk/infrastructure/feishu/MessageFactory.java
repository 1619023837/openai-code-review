package hao.wen.tao.sdk.infrastructure.feishu;

import hao.wen.tao.sdk.infrastructure.feishu.impl.FeishuMessageStrategy;
import hao.wen.tao.sdk.infrastructure.feishu.impl.WeixinMessageStrategy;

import java.util.HashMap;
import java.util.Map;


public class MessageFactory
{
    private static Map<String,IMessageStrategy> messageStrategies = new HashMap();

    static {
        messageStrategies.put("weixin",new WeixinMessageStrategy());
        messageStrategies.put("feishu",new FeishuMessageStrategy());
    }

    public static IMessageStrategy getMessageStrategy(String name)
    {
        return messageStrategies.get(name);
    }
}
