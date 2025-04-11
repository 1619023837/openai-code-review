package hao.wen.tao.sdk.types.utils;

import com.alibaba.fastjson2.JSON;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class WXAccessTokenUtils
{

    private static final String APP_ID = "wx68eb13f81b27a798";
    private static final String SECRET = "f86566ce071357365c4a2649f9701287";
    private static final String GRANT_TYPE = "client_credential";
    private static final String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=%s&appid=%s&secret=%s";

    public static String getAccessToken()
    {

        try
        {
            String urlStr = String.format(url, GRANT_TYPE, APP_ID, SECRET);
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            System.out.println("responseCode:" + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK){
                try(BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream())))
                {
                    String line = "";
                    StringBuilder response = new StringBuilder();
                    while ((line = bufferedReader.readLine()) != null){
                        response.append(line);
                    }
                    // Print the response
                    System.out.println("Response: " + response.toString());
                    Token token = JSON.parseObject(response.toString(), Token.class);
                    return token.getAccess_token();
                }

            }else
            {
                System.out.println("GET request failed");
                return null;
            }
        }
        catch (Exception  e)
        {
            e.printStackTrace();
            return null;

        }
    }
    public static class Token {
        private String access_token;
        private Integer expires_in;

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public Integer getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(Integer expires_in) {
            this.expires_in = expires_in;
        }
    }



}
