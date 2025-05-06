package hao.wen.tao.sdk.infrastructure.feishu;

import hao.wen.tao.sdk.infrastructure.feishu.dto.BotMessageRequestDTO;
import hao.wen.tao.sdk.types.utils.DefaultHttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * 维护消息发送的方法
 */
public class Feishu
{
    private final Logger log = LoggerFactory.getLogger(Feishu.class);

    private final  String botWebHook;

    public Feishu(String botWebHook)
    {
        this.botWebHook = botWebHook;
    }

    /**
     * 推送的仓库地址
     * @param redirectUrl
     */
    public void sendMessage(String redirectUrl)
        throws Exception
    {
        Map<String, String> headers = new HashMap();
        headers.put("content-type", "pplication/json");
        BotMessageRequestDTO botMessageRequestDTO = new BotMessageRequestDTO();
        BotMessageRequestDTO.BotMessageContent content = new BotMessageRequestDTO.BotMessageContent();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("你好,你的github Action的AI 代码评审已经处理完成,请查看处理结果");
        stringBuilder.append(redirectUrl);
        content.setText(stringBuilder.toString());
        botMessageRequestDTO.setContent(content);
        String result =    DefaultHttpUtil.executePostRequest(botWebHook, headers, botMessageRequestDTO);
        log.error("飞书消息发送结果{}", result);
    }
}
