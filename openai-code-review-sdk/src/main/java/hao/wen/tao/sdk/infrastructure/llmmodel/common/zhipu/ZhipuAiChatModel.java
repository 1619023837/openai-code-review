package hao.wen.tao.sdk.infrastructure.llmmodel.common.zhipu;

import hao.wen.tao.sdk.infrastructure.llmmodel.common.chat.ChatLanguageModel;
import hao.wen.tao.sdk.infrastructure.llmmodel.common.output.Response;
import hao.wen.tao.sdk.infrastructure.llmmodel.common.request.ChatCompletionRequest;
import hao.wen.tao.sdk.infrastructure.llmmodel.common.response.ChatCompletionResponse;
import hao.wen.tao.sdk.infrastructure.llmmodel.common.text.AIMessageText;
import hao.wen.tao.sdk.infrastructure.llmmodel.common.text.ChatMessageText;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


@Slf4j
public class ZhipuAiChatModel implements ChatLanguageModel
{

    private final  String model;

    private final  ZhipuAiHttpClinet zhipuAiHttpClinet;

    @Builder
    public ZhipuAiChatModel(String model,String baseUrl,String apiSecret)
    {
       this.model = model==""?"glm-4-flash":model;
       this.zhipuAiHttpClinet = ZhipuAiHttpClinet.builder()
           .baseUrl(baseUrl)
           .apiSecret(apiSecret)
           .build();
    }

    @Override
    public Response<AIMessageText> generate(List<ChatMessageText> chatMessageText)
    {
        ChatCompletionRequest.ChatCompletionRequestBuilder builder = ChatCompletionRequest.builder().model(
            model).messages(ZhipuAIHelper.toMessage(chatMessageText));
        ChatCompletionRequest request = builder.build();
        ChatCompletionResponse chatCompletionResponse = zhipuAiHttpClinet.chatCompletion(request);
        Response<AIMessageText> from = Response.from(
            ZhipuAIHelper.aiMessageFrom(chatCompletionResponse));
        return from;
    }
}
