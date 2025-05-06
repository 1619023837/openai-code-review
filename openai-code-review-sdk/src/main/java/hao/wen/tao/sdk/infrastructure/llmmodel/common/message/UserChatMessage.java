package hao.wen.tao.sdk.infrastructure.llmmodel.common.message;

import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class UserChatMessage implements ChatMessage
{
    @Builder.Default
    private String role = Role.USER.toString().toLowerCase();

    private String content;

    public static UserChatMessage from(String content){
        return UserChatMessage.builder()
            .content(content)
            .build();
    }
}
