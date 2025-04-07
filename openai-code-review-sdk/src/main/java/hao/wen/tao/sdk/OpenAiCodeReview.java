package hao.wen.tao.sdk;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;


public class OpenAiCodeReview
{
    public static void main(String[] args)
        throws IOException
    {
        System.out.println("你好！！11！！");
        //代码检出

        //拉取代码
        ProcessBuilder processBuilder = new ProcessBuilder("git", "diff", "master", "03");
        processBuilder.directory(new File("."));

        Process start = processBuilder.start();
        try ( //读取内容
            BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(start.getInputStream()));)
        {
            String line;
            StringBuilder diffCode = new StringBuilder();
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

    }
}