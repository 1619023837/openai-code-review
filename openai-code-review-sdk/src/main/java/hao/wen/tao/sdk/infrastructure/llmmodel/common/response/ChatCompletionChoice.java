package hao.wen.tao.sdk.infrastructure.llmmodel.common.response;

import hao.wen.tao.sdk.infrastructure.llmmodel.common.message.AssistantChatMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatCompletionChoice
{
    private Integer index;

    private AssistantChatMessage message;


    private String finishReason;
}
