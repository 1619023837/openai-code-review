package hao.wen.tao.sdk.infrastructure.feishu.impl;

import hao.wen.tao.sdk.infrastructure.feishu.Feishu;
import hao.wen.tao.sdk.infrastructure.feishu.IMessageStrategy;
import hao.wen.tao.sdk.infrastructure.feishu.untils.EnvUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;


@Slf4j
public class FeishuMessageStrategy implements IMessageStrategy
{
    @Override
    public String name()
    {
        return "feishu";
    }

    @Override
    public void sendMessage(String logUrl, Map<String, Map<String, String>> data)
    {
        String botWebhook = EnvUtils.getEnv("FEISHU_URL");
        Feishu feishu = new Feishu(botWebhook);
        try
        {
            feishu.sendMessage(logUrl);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }

    }
}
