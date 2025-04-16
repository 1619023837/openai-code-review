package hao.wen.tao.sdk.infrastructure.openai;

import hao.wen.tao.sdk.infrastructure.openai.dto.ChatCompletionRequestDTO;
import hao.wen.tao.sdk.infrastructure.openai.dto.ChatCompletionSyncResponseDTO;




public interface IOpenAI
{

    ChatCompletionSyncResponseDTO complateion(ChatCompletionRequestDTO chatCompletionRequestDTO)
        throws Exception;
}
