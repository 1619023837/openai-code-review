package hao.wen.tao.sdk.infrastructure.openai.impl;

import com.alibaba.fastjson2.JSON;
import hao.wen.tao.sdk.infrastructure.openai.IOpenAI;
import hao.wen.tao.sdk.infrastructure.openai.dto.ChatCompletionRequestDTO;
import hao.wen.tao.sdk.infrastructure.openai.dto.ChatCompletionSyncResponseDTO;
import hao.wen.tao.sdk.types.utils.BearerTokenUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;


public class ChatGLM implements IOpenAI
{

    private final String apiHost;

    private final String apiSecret;

    public ChatGLM(String apiHost, String apiSecret)
    {
        this.apiHost = apiHost;
        this.apiSecret = apiSecret;
    }

    @Override
    public ChatCompletionSyncResponseDTO complateion(Object chatCompletionRequestDTO) throws Exception
    {
        String token = BearerTokenUtils.getToken(apiSecret);
        URL url = new URL(apiHost);
        HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Authorization", "Bearer " + token);
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        httpURLConnection.setRequestProperty("User-Agent",
            "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        httpURLConnection.setDoOutput(true);
        try (OutputStream outputStream = httpURLConnection.getOutputStream())
        {
            byte[] bytes = JSON.toJSONString(chatCompletionRequestDTO).getBytes(StandardCharsets.UTF_8);
            outputStream.write(bytes, 0, bytes.length);
        }


        try (InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader inputStreamReader = new BufferedReader(
                new InputStreamReader(inputStream)))
        {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = inputStreamReader.readLine()) != null)
            {
               stringBuilder.append(line);
            }
            return JSON.parseObject(stringBuilder.toString(), ChatCompletionSyncResponseDTO.class);
        }
    }

    @Override
    public String getUrl() {
        return apiHost;
    }

    @Override
    public String apiSecret() {
        return apiSecret;
    }
}
