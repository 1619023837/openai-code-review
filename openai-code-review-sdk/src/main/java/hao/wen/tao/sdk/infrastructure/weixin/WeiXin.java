package hao.wen.tao.sdk.infrastructure.weixin;

import com.alibaba.fastjson2.JSON;
import hao.wen.tao.sdk.infrastructure.weixin.dto.TemplateMessageDTO;
import hao.wen.tao.sdk.types.utils.WXAccessTokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Scanner;


public class WeiXin
{

    private final static Logger logger = LoggerFactory.getLogger(WeiXin.class);
    private final String appid;

    private final String secret;

    private final String touser;

    private final String template_id;

    public WeiXin(String appid, String secret, String touser, String template_id)
    {
        this.appid = appid;
        this.secret = secret;
        this.touser = touser;
        this.template_id = template_id;
    }

    /**
     *
     * @param logUrl 评审地址
     * @param data 数据参数
     */
    public void sendTemplateMessage(String logUrl,Map<String, Map<String, String>> data)
        throws Exception
    {
        String accessToken = WXAccessTokenUtils.getAccessToken(appid, secret);

        TemplateMessageDTO templateMessageDTO = new TemplateMessageDTO(template_id,touser);
        templateMessageDTO.setUrl(logUrl);
        templateMessageDTO.setData(data);
        String urlStr = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s";
        URL url = new URL(String.format(urlStr, accessToken));
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        //url 谢书记
        try (OutputStream outputStream = connection.getOutputStream();)
        {
            byte[] input = JSON.toJSONString(templateMessageDTO).getBytes(StandardCharsets.UTF_8);
            outputStream.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        System.out.println(responseCode);
        try (Scanner scanner = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8.name())) {
            String response = scanner.useDelimiter("\\A").next();
            logger.info("message{}", response);
        }
    }



}
