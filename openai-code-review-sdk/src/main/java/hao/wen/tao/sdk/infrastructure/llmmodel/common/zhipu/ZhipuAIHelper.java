package hao.wen.tao.sdk.infrastructure.llmmodel.common.zhipu;

import hao.wen.tao.sdk.infrastructure.llmmodel.common.message.AssistantChatMessage;
import hao.wen.tao.sdk.infrastructure.llmmodel.common.message.ChatMessage;
import hao.wen.tao.sdk.infrastructure.llmmodel.common.message.SystemChatMessage;
import hao.wen.tao.sdk.infrastructure.llmmodel.common.message.UserChatMessage;
import hao.wen.tao.sdk.infrastructure.llmmodel.common.response.ChatCompletionResponse;
import hao.wen.tao.sdk.infrastructure.llmmodel.common.text.AIMessageText;
import hao.wen.tao.sdk.infrastructure.llmmodel.common.text.ChatMessageText;
import hao.wen.tao.sdk.infrastructure.llmmodel.common.text.SystemMessageText;
import hao.wen.tao.sdk.infrastructure.llmmodel.common.text.UserMessageText;

import java.util.List;
import java.util.stream.Collectors;


/**
 * 将提示词转换为消息
 */
public class ZhipuAIHelper
{

    public static List<ChatMessage> toMessage(List<ChatMessageText> chatMessageTexts){
        return chatMessageTexts.stream().map(ZhipuAIHelper::toChatMessage).collect(Collectors.toList());
    }

    public static ChatMessage toChatMessage(ChatMessageText messageText)
    {
        if (messageText instanceof SystemMessageText) {
            return SystemChatMessage.from(messageText.text());
        }
        if (messageText instanceof UserMessageText) {
            return UserChatMessage.from(messageText.text());
        }

        if (messageText instanceof AIMessageText) {
            return AssistantChatMessage.from(messageText.text());
        }
        throw new IllegalArgumentException(" unknown message type" + messageText.text());
    }

    public static AIMessageText aiMessageFrom(ChatCompletionResponse chatCompletionResponse)
    {

        String content = chatCompletionResponse.getChoices().get(0).getMessage().getContent();
        return AIMessageText.from(content);
    }

}
