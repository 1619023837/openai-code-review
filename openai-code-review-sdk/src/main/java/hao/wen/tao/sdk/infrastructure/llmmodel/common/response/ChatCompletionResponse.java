package hao.wen.tao.sdk.infrastructure.llmmodel.common.response;

import hao.wen.tao.sdk.infrastructure.openai.dto.ChatCompletionSyncResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public final class ChatCompletionResponse
{

    private String id;

    private Integer created;

    private String model;

    private List<ChatCompletionChoice> choices;
}
