package hao.wen.tao.sdk.infrastructure.llmmodel.common.message;

import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class AssistantChatMessage implements ChatMessage
{

    @Builder.Default
    private String role = Role.ASSISTANT.toString().toLowerCase();

    private String content;

    public static AssistantChatMessage from(String content){
        return AssistantChatMessage.builder()
            .content(content)
            .build();
    }
}
