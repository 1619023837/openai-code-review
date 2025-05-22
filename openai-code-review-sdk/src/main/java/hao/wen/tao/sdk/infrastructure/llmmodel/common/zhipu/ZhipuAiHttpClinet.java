package hao.wen.tao.sdk.infrastructure.llmmodel.common.zhipu;

import com.alibaba.fastjson2.JSON;
import hao.wen.tao.sdk.infrastructure.llmmodel.common.request.ChatCompletionRequest;
import hao.wen.tao.sdk.infrastructure.llmmodel.common.response.ChatCompletionResponse;
import hao.wen.tao.sdk.types.utils.BearerTokenUtils;
import hao.wen.tao.sdk.types.utils.DefaultHttpUtil;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


@Data
@Slf4j
@Builder
public class ZhipuAiHttpClinet
{

    private String baseUrl;

    private String apiSecret;

    public ChatCompletionResponse chatCompletion(ChatCompletionRequest request)
    {
        try
        {
            String token = BearerTokenUtils.getToken(apiSecret);
            Map<String, String> header = new HashMap();
            header.put("Authorization", "Bearer " + token);
            header.put("Content-Type", "application/json");
            header.put("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            String execute = DefaultHttpUtil.executePostRequest(baseUrl, header,request);
            ChatCompletionResponse chatCompletionResponse = JSON.parseObject(execute,
                ChatCompletionResponse.class);
            return chatCompletionResponse;

        }
        catch (Exception e)
        {
            log.error(e.getMessage(),e);
        }
        return null;

    }

}
