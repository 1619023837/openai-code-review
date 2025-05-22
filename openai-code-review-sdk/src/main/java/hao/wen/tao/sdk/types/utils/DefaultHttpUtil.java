package hao.wen.tao.sdk.types.utils;

import com.alibaba.fastjson2.JSON;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;


public class DefaultHttpUtil
{

    public static String execute(String uri, Map<String,String> headers)
        throws Exception
    {

        URL url = new URL(uri);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        headers.forEach((key,value)->
        {
            connection.setRequestProperty(key,value);
        });
        connection.setDoOutput(true);
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        connection.connect();
        return response.toString();
    }

    public static String executePostRequest(String uri, Map<String,String> headers,
                                            Object request)
        throws Exception
    {

        URL url = new URL(uri);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("POST");
        headers.forEach((key,value)->
        {
            connection.setRequestProperty(key,value);
        });
        connection.setDoOutput(true);
        //添加参数
        try (OutputStream outputStream = connection.getOutputStream())
        {
            byte[] bytes = JSON.toJSONString(request).getBytes(StandardCharsets.UTF_8);
            outputStream.write(bytes, 0, bytes.length);
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        connection.connect();

        return response.toString();
    }


    //加一个获取某个HTTP地址的字节输入流的方法，如
    public static InputStream getHttpInputStream(String uri) throws Exception {
        URL url = new URL(uri);
        HttpURLConnection httpURLConnection =(HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setDoOutput(true);
        return httpURLConnection.getInputStream();
    }
}
