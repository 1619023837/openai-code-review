package hao.wen.tao.sdk.infrastructure.llmmodel.common.chat;

import hao.wen.tao.sdk.infrastructure.llmmodel.common.output.Response;
import hao.wen.tao.sdk.infrastructure.llmmodel.common.text.AIMessageText;
import hao.wen.tao.sdk.infrastructure.llmmodel.common.text.ChatMessageText;

import java.util.Arrays;
import java.util.List;


public interface ChatLanguageModel
{

    default Response<AIMessageText> generate(ChatMessageText... chatMessageText){
        return generate(Arrays.asList(chatMessageText));
    }

    Response<AIMessageText> generate(List<ChatMessageText> chatMessageText);
}
