package hao.wen.tao.sdk.infrastructure.openai.impl;

import hao.wen.tao.sdk.infrastructure.openai.IOpenAI;
import hao.wen.tao.sdk.infrastructure.openai.dto.ChatCompletionSyncResponseDTO;

public class ZhiHui implements IOpenAI {

    private final String apiSecret;

    private final String zhiUrl;

    public ZhiHui(String apiSecret, String zhiUrl) {
        this.apiSecret = apiSecret;
        this.zhiUrl = zhiUrl;
    }

    @Override
    public ChatCompletionSyncResponseDTO complateion(Object chatCompletionRequestDTO) throws Exception {
        return null;
    }

    @Override
    public String getUrl() {
        return zhiUrl;
    }

    @Override
    public String apiSecret() {
        return apiSecret;
    }
}
