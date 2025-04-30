package hao.wen.tao.sdk.test;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.*;
import hao.wen.tao.sdk.domain.ChatCompletionRequest;
import hao.wen.tao.sdk.domain.ChatCompletionSyncResponse;
import hao.wen.tao.sdk.domain.Message;
import hao.wen.tao.sdk.types.utils.BearerTokenUtils;
import hao.wen.tao.sdk.types.utils.WXAccessTokenUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;


@RunWith(JUnit4.class)
public class ApiTest
{

    @Test
    public void test01()
    {
        System.out.println(Integer.parseInt("abc12134"));
    }

    @Test
    public void test()
        throws IOException
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
        //设置请求信息
        String code = "int k = 1/0";
        ChatCompletionRequest chatCompletionSyncResponse = new ChatCompletionRequest();
        chatCompletionSyncResponse.setModel("glm-4-flash");
        chatCompletionSyncResponse.setMessages(new ArrayList<ChatCompletionRequest.Prompt>(){{
            add(new ChatCompletionRequest.Prompt("user","你是一个高级编程架构师，精通各类场景方案、架构设计和编程语言请，请您根据git diff记录，对代码做出评审。代码为:"));
            add(new ChatCompletionRequest.Prompt("user",code));
        }});
        try (OutputStream outputStream = httpURLConnection.getOutputStream())
        {
            byte[] bytes = JSON.toJSONString(chatCompletionSyncResponse).getBytes(StandardCharsets.UTF_8);
            outputStream.write(bytes);
        }

        String requestMethod = httpURLConnection.getRequestMethod();
        System.out.println(requestMethod);

        //获取到输出
        try (InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader inputStreamReader = new BufferedReader(
                new InputStreamReader(inputStream));)
        {
            String s;
            List<String> list = new ArrayList();
            while ((s = inputStreamReader.readLine()) != null)
            {
                //跳过空行
                if (s == null && s.length() == 0) {
                    continue;
                }
                list.add(s);
            }
            List<ChatCompletionSyncResponse> dataList = list.stream().map(x -> {
                ChatCompletionSyncResponse objectMap = JSONObject.parseObject(x,
                    new TypeReference<ChatCompletionSyncResponse>()
                    {
                    });
                return objectMap;
            }).collect(Collectors.toList());
            if (dataList.size() == 0) {
                return;
            }
            String collect = dataList.stream().flatMap(
                d -> d.getChoices().stream().map(x -> x.getMessage().getContent())).collect(
                Collectors.joining());
            System.out.println(collect);
        }
        catch (IOException e)
        {
        }
    }

    @Test
    public void testvx()
        throws Exception
    {
        //获取access_token
        String accessToken = WXAccessTokenUtils.getAccessToken();
        String urlStr = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s";
        urlStr = String.format(urlStr, accessToken);
        Message message = new Message();
        message.setTouser("oqu5O7Dmpwyq-6rlOVvTXGZGjAcM");
        message.setTemplate_id("Gu4ulHjLuEpWXFm9IB_j87l2HU8ncOoHo-w2mY4D7L8");
        message.setUrl("https://github.com/1619023837/openai-code-review/actions");
        message.put("project","big-market");
        message.put("review","feat: 新加功能");
        sendPostRequest(urlStr, JSON.toJSONString(message));
    }

    private static void sendPostRequest(String urlStr, String message)
        throws IOException
    {
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        try(OutputStream outputStream = connection.getOutputStream();)
        {
            byte[] input = message.getBytes(StandardCharsets.UTF_8);
            outputStream.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        System.out.println(responseCode);

//        if (responseCode == HttpURLConnection.HTTP_OK){
//            try(BufferedReader bufferedReader = new BufferedReader(
//                new InputStreamReader(connection.getInputStream())))
//            {
//                String line = "";
//                StringBuilder response = new StringBuilder();
//                while ((line = bufferedReader.readLine()) != null)
//                {
//                    response.append(line);
//                }
//                System.out.println(response);
//            }
//        }
        try (Scanner scanner = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8.name())) {
            String response = scanner.useDelimiter("\\A").next();
            System.out.println(response);
        }
    }

//    public static class Message{
//        private String touser;
//
//        private String template_id;
//
//        private String url;
//        private Map<String, Map<String, String>> data = new HashMap<>();
//
//        public void put(String key, String value)
//        {
//            data.put(key,new HashMap(){
//                {
//                    put("value",value);
//                }
//            });
//        }
//        public String getTouser() {
//            return touser;
//        }
//
//        public void setTouser(String touser) {
//            this.touser = touser;
//        }
//
//        public String getTemplate_id() {
//            return template_id;
//        }
//
//        public void setTemplate_id(String template_id) {
//            this.template_id = template_id;
//        }
//
//        public String getUrl() {
//            return url;
//        }
//
//        public void setUrl(String url) {
//            this.url = url;
//        }
//
//        public Map<String, Map<String, String>> getData() {
//            return data;
//        }
//
//        public void setData(Map<String, Map<String, String>> data) {
//            this.data = data;
//        }
//
//    }

    public void test02()
    {
        System.out.println("test02");
    }
}
