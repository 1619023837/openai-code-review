package hao.wen.tao.sdk.infrastructure.feishu.untils;

public class EnvUtils
{


    public static String getEnv(String key)
    {
        String token = System.getenv(key);
        if (token == null || token.length() == 0) {
            throw new RuntimeException(key + "token is null");
        }
        return token;
    }
}
