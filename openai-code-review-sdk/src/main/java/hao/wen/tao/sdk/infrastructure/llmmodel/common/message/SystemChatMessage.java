package hao.wen.tao.sdk.infrastructure.llmmodel.common.message;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class SystemChatMessage implements ChatMessage
{

    @Builder.Default
    private String role = Role.SYSTEM.toString().toLowerCase();

    private String content;

    public static SystemChatMessage from(String content){
        return SystemChatMessage.builder()
            .content(content)
            .build();
    }

}
