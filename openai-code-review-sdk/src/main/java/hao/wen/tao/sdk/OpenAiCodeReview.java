package hao.wen.tao.sdk;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import hao.wen.tao.sdk.domain.ChatCompletionSyncResponse;
import hao.wen.tao.sdk.types.utils.BearerTokenUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class OpenAiCodeReview
{
    public static void main(String[] args)
        throws Exception
    {
        System.out.println("你好！！！！");
        //代码检出

        //拉取代码
        ProcessBuilder processBuilder = new ProcessBuilder("git", "diff", "HEAD~1", "HEAD");
        processBuilder.directory(new File("."));

        Process start = processBuilder.start();
        StringBuilder diffCode = new StringBuilder();
        try ( //读取内容
            BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(start.getInputStream()));)
        {
            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                diffCode.append(line);
            }
            //等待退出
            int exit = start.waitFor();
            System.out.println("processed 退出"+exit);
            System.out.println("评审代码" + diffCode);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }


        //chatglm 代码评审
        if (diffCode.length() > 0){
            String log = codeReview(diffCode.toString());
            System.out.println("code Review : " + log);
        }

    }

    private static String codeReview(String diffCode)
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

        //设置请求信息
        String jsonInpuString =
            "{" + "\"model\": \"glm-4-flash\"," + "\"stream\": \"true\"," + "\"messages\": [{"
            + "\"role\": \"user\","
            + "\"content\": \"你是一个高级编程架构师，精通各类场景方案、架构设计和编程语言请，请您根据git diff记录，对代码做出评审。代码为: "
            + diffCode  + "并帮忙做出修正,按照java代码规范进行修正,代码输出\"}]" + "}";
        try (OutputStream outputStream = httpURLConnection.getOutputStream())
        {
            byte[] bytes = jsonInpuString.getBytes(StandardCharsets.UTF_8);
            outputStream.write(bytes);
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
                return null;
            }
            String collect = dataList.stream().flatMap(x->x.getChoices().stream().filter(d->d.getDelta().get(0).getContent().length()>0).map(
                d->d.getDelta().get(0).getContent()
            )).collect(
                Collectors.joining());
            return collect;
        }
        catch (IOException e)
        {
            return null;
        }
    }
}