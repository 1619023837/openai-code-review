package hao.wen.tao.sdk.test;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.*;
import hao.wen.tao.sdk.domain.ChatCompletionSyncResponse;
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
import java.util.Map;
import java.util.stream.Collectors;


@RunWith(JUnit4.class)
public class ApiTest
{

    @Test
    public void test01()
    {
        int k = 1 / 0;
        System.out.println("返回值"+ k);
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
        String jsonInpuString =
            "{" + "\"model\": \"glm-4-flash\"," + "\"stream\": \"true\"," + "\"messages\": [{"
            + "\"role\": \"user\","
            + "\"content\": \"你是一个高级编程架构师，精通各类场景方案、架构设计和编程语言请，请您根据git diff记录，对代码做出评审。代码为: "
            + code  + "并帮忙做出修正,按照java代码规范进行修正,代码输出\"}]" + "}";
        try (OutputStream outputStream = httpURLConnection.getOutputStream())
        {
            byte[] bytes = jsonInpuString.getBytes(StandardCharsets.UTF_8);
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
                if (s.equals("[]")){
                    continue;
                }
                int startIndex = s.indexOf("{");
                //既不是空数据 也不是 {} json 结构的
                if (startIndex == -1) {
                    continue;
                }
                s = s.substring(s.indexOf("{"));
                if (s.trim().endsWith("}")) {
                    list.add(s);
                }else {
                    //说明不是一个 完整的json
                }
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
            String collect = dataList.stream().flatMap(x->x.getChoices().stream().filter(d->d.getDelta().get(0).getContent().length()>0 && d.getDelta().get(0).getContent()
                .indexOf("\n")==-1).map(
                d->d.getDelta().get(0).getContent()
            )).collect(
                Collectors.joining());
            System.out.println(collect);
        }
        catch (IOException e)
        {
        }
    }
}
