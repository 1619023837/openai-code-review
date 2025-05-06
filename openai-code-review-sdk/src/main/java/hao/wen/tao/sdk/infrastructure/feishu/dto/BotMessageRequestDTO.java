package hao.wen.tao.sdk.infrastructure.feishu.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;


@Data
public class BotMessageRequestDTO
{

    @JSONField(name = "msg_type")
    private String msgType = "text";

    @JSONField(name = "content")
    private BotMessageContent content;


    @Data
    public static class BotMessageContent
    {

        /**
         * 消息内容
         */
        @JSONField(name = "text")
        private String text;

    }
}
