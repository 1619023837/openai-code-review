package hao.wen.tao.sdk;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import hao.wen.tao.sdk.domain.ChatCompletionRequest;
import hao.wen.tao.sdk.domain.ChatCompletionSyncResponse;
import hao.wen.tao.sdk.types.utils.BearerTokenUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;


public class OpenAiCodeReview
{
    public static void main(String[] args)
        throws Exception
    {

        //第一种 直接args 获取

        //第二种
        String token = System.getenv("GITHUB_TOKEN");
        if (token == null || token.isEmpty()){
            throw new RuntimeException("GITHUB_TOKEN is empty");
        }
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
            String codeReview = codeReview(diffCode.toString());
            if (codeReview == null || codeReview.trim().length() <= 0)
            {
            }else {

                //执行将日志输入到日志仓库中
                String codePath = writeLog(token, codeReview);
            }

        }

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

        ChatCompletionRequest chatCompletionSyncResponse = new ChatCompletionRequest();
        chatCompletionSyncResponse.setModel("glm-4-flash");
        chatCompletionSyncResponse.setMessages(new ArrayList<ChatCompletionRequest.Prompt>(){{
            add(new ChatCompletionRequest.Prompt("user","你是一个高级编程架构师，精通各类场景方案、架构设计和编程语言请，请您根据git diff记录，对代码做出评审。代码为:"));
            add(new ChatCompletionRequest.Prompt("user",diffCode));
        }});
        try (OutputStream outputStream = httpURLConnection.getOutputStream())
        {
            byte[] bytes = JSON.toJSONString(chatCompletionSyncResponse).getBytes(StandardCharsets.UTF_8);
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
                //trim 之后再判断长度
                if (s == null && s.trim().length() == 0) {
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

    private static String writeLog(String token,String log)
        throws GitAPIException
    {
        //https://github.com/1619023837/openai-code-review-log.git

        //拉取仓库 setDirectory 文件夹
        Git git = Git.cloneRepository().setURI("https://github.com/1619023837/openai-code-review-log").setDirectory(
            new File("repo")).setCredentialsProvider(
            new UsernamePasswordCredentialsProvider(token, "")).call();
        //一天的 写到一个文件夹
        String dateFoldName = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        File dateFolder = new File("repo/" + dateFoldName);
        if (!dateFolder.exists()) {
            dateFolder.mkdirs();
        }
        String fileName = getRandomString(12) + ".md";
        File newFile = new File(dateFolder, fileName);
        try(FileWriter writer = new FileWriter(newFile);)
        {
            writer.write(log);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        //使用git提交 fileName 文件名  dateFoldName 文件夹名
        git.add().addFilepattern(dateFoldName +"/" + fileName).call();
        git.commit().setMessage("添加一个新的文件");

        git.push().setCredentialsProvider(new UsernamePasswordCredentialsProvider(token, "")).call();

        //写到哪里了
        return "https://github.com/1619023837/openai-code-review-log/blob/master/"+ dateFoldName +"/" + fileName;
    }

    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(str.length());
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}