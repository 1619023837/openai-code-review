package hao.wen.tao.sdk.infrastructure.feishu.impl;

import hao.wen.tao.sdk.infrastructure.feishu.IMessageStrategy;
import hao.wen.tao.sdk.infrastructure.feishu.untils.EnvUtils;
import hao.wen.tao.sdk.infrastructure.weixin.WeiXin;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;


@Slf4j
public class WeixinMessageStrategy implements IMessageStrategy
{
    @Override
    public String name()
    {
        return "weixin";
    }

    @Override
    public void sendMessage(String logUrl, Map<String, Map<String, String>> data)
    {
        try
        {
            WeiXin weiXin = new WeiXin(
                EnvUtils.getEnv("WEIXIN_APPID"),
                EnvUtils.getEnv("WEIXIN_SECRET"),
                EnvUtils.getEnv("WEIXIN_TOUSER"),
                EnvUtils.getEnv("WEIXIN_TEMPLATE_ID")
            );
            weiXin.sendTemplateMessage(logUrl, data);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }
    }
}
