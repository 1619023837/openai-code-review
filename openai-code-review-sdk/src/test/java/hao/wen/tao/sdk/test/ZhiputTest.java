package hao.wen.tao.sdk.test;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import hao.wen.tao.sdk.infrastructure.llmmodel.common.output.Response;
import hao.wen.tao.sdk.infrastructure.llmmodel.common.text.AIMessageText;
import hao.wen.tao.sdk.infrastructure.llmmodel.common.text.ChatMessageText;
import hao.wen.tao.sdk.infrastructure.llmmodel.common.text.SystemMessageText;
import hao.wen.tao.sdk.infrastructure.llmmodel.common.text.UserMessageText;
import hao.wen.tao.sdk.infrastructure.llmmodel.common.zhipu.ZhipuAiChatModel;
import hao.wen.tao.sdk.infrastructure.openai.dto.ChatCompletionRequestDTO;
import hao.wen.tao.sdk.infrastructure.openai.dto.ChatCompletionSyncResponseDTO;
import hao.wen.tao.sdk.types.utils.BearerTokenUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RunWith(JUnit4.class)
public class ZhiputTest
{

    @Test
    public void test()
    {
        SystemMessageText messageText = new SystemMessageText(
            "你是一个高级编程架构师，精通各类场景方案、架构设计和编程语言请帮助用户进行代码审查");
        UserMessageText userMessageText = new UserMessageText(
            "这是我的代码内容  System.out.println(Integer.parseInt(\"abc12134\"));");

        ZhipuAiChatModel chatModel = ZhipuAiChatModel.builder()
            .model("")
            .apiSecret("3763aa13ab2847528d6ffdc2fa6c53c7.6pb98r42BWeB5KWJ")
            .baseUrl("https://open.bigmodel.cn/api/paas/v4/chat/completions").build();
        List<ChatMessageText> chatMessageTexts = new ArrayList(){
            {
                add(messageText);
                add(userMessageText);
            }
        };

        Response<AIMessageText> generate = chatModel.generate(chatMessageTexts);
        System.out.println(generate.content().text());

    }

    @Test
    public void test03()
        throws Exception
    {
        codeReview("111221");
    }
    private static  String  codeReview(String diffCode)
            throws Exception
        {
            String apiSecret = "3763aa13ab2847528d6ffdc2fa6c53c7.6pb98r42BWeB5KWJ";
            String token = BearerTokenUtils.getToken(apiSecret);
            URL url = new URL("https://open.bigmodel.cn/api/paas/v4/chat/completions");
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Authorization", "Bearer " + token);
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("User-Agent",
                "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            httpURLConnection.setDoOutput(true);

            ChatCompletionRequestDTO chatCompletionSyncResponse = new ChatCompletionRequestDTO();
            chatCompletionSyncResponse.setModel("glm-4-flash");
            chatCompletionSyncResponse.setMessages(new ArrayList<ChatCompletionRequestDTO.Prompt>(){{
                add(new ChatCompletionRequestDTO.Prompt("user","你是一个高级编程架构师，精通各类场景方案、架构设计和编程语言请，请您根据git diff记录，对代码做出评审。代码为:"));
                add(new ChatCompletionRequestDTO.Prompt("system",diffCode));
            }});
            try (OutputStream outputStream = httpURLConnection.getOutputStream())
            {
                byte[] bytes = JSON.toJSONString(chatCompletionSyncResponse).getBytes(
                    StandardCharsets.UTF_8);
                outputStream.write(bytes, 0, bytes.length);
            }
            //获取到输出
            try (InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader inputStreamReader = new BufferedReader(
                    new InputStreamReader(inputStream));)
            {
                String s;
                List<String> list = new ArrayList();
                while ((s = inputStreamReader.readLine()) != null)
                {
                    //trim 之后再判断长度
                    if (s == null && s.trim().length() == 0) {
                        continue;
                    }
                    list.add(s);
                }
                List<ChatCompletionSyncResponseDTO> dataList = list.stream().map(x -> {
                    ChatCompletionSyncResponseDTO objectMap = JSONObject.parseObject(x,
                        new TypeReference<ChatCompletionSyncResponseDTO>()
                        {
                        });
                    return objectMap;
                }).collect(Collectors.toList());
                if (dataList.size() == 0) {
                    return  null;
                }
                String collect = dataList.stream().flatMap(
                    d -> d.getChoices().stream().map(x -> x.getMessage().getContent())).collect(
                    Collectors.joining());

                return collect;
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            return null;
        }
}
