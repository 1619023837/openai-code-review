package hao.wen.tao.sdk.infrastructure.feishu;

import java.util.Map;


public interface IMessageStrategy
{

    String name();

    /**
     * 发送消息的接口
     * @param logUrl
     * @param data    参数
     */
    void sendMessage(String logUrl, Map<String, Map<String, String>> data);

}
