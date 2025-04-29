package hao.wen.tao.sdk;

import hao.wen.tao.sdk.domain.service.impl.OpenAiCodeReviewService;
import hao.wen.tao.sdk.infrastructure.git.GitCommand;
import hao.wen.tao.sdk.infrastructure.git.GitRestAPIOperation;
import hao.wen.tao.sdk.infrastructure.openai.IOpenAI;
import hao.wen.tao.sdk.infrastructure.openai.impl.ChatGLM;
import hao.wen.tao.sdk.infrastructure.weixin.WeiXin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class OpenAiCodeReview
{
    private static final Logger logger = LoggerFactory.getLogger(OpenAiCodeReview.class);


    //微信配置
    private static String weixin_appid = "wx68eb13f81b27a798";
    private static String weixin_secret = "f86566ce071357365c4a2649f9701287";
    private static String weixin_touser = "oqu5O7Dmpwyq-6rlOVvTXGZGjAcM";
    private static String weixin_template_id = "D7-YVlgn5hW06o-4Lzb9ppMxMLibysb9Cpxn_p7CqAs";


    //chatglm 配置
    private static String chatglm_apiHost = "https://open.bigmodel.cn/api/paas/v4/chat/completions";
    private static String chatglm_apiKeySecret = "3763aa13ab2847528d6ffdc2fa6c53c7.6pb98r42BWeB5KWJ";


    //gitlab GITHUB_REVIEW_LOG_URI
    private static String review_log_uri;
    private static String github_token;


    // 工程配置 - 自动获取  这个是github action 提供的
    private static String commit_project;
    private static String commit_branch;
    private static String commit_author;




    /**
     * 程序入口 从脚本中获取
     * @param args
     * @throws Exception
     */
    public static void main(String[] args)
        throws Exception
    {
        System.out.println("start");
        GitCommand gitCommand = new GitCommand(
            getEnv("GITHUB_REVIEW_LOG_URI"),
            getEnv("GITHUB_TOKEN"),
            getEnv("COMMIT_PROJECT"),
            getEnv("COMMIT_BRANCH"),
            getEnv("COMMIT_AUTHOR"),
            getEnv("COMMIT_MESSAGE")
        );

        /**
         * 项目：{{repo_name.DATA}} 分支：{{branch_name.DATA}} 作者：{{commit_author.DATA}} 说明：{{commit_message.DATA}}
         */
        WeiXin weiXin = new WeiXin(
            getEnv("WEIXIN_APPID"),
            getEnv("WEIXIN_SECRET"),
            getEnv("WEIXIN_TOUSER"),
            getEnv("WEIXIN_TEMPLATE_ID")
        );

        //chatglm 地址  生成token地址
        IOpenAI iOpenAI = new ChatGLM(getEnv("CHATGLM_APIHOST"), getEnv("CHATGLM_APIKEYSECRET"));
        GitRestAPIOperation gitRestAPIOperation = new GitRestAPIOperation(
            getEnv("GIT_CHECK_COMMIT_URL"), getEnv("CHATGLM_APIHOST"));
        OpenAiCodeReviewService openAiCodeReviewService = new OpenAiCodeReviewService(gitRestAPIOperation,gitCommand, iOpenAI, weiXin);
        openAiCodeReviewService.exec();
        logger.info("openai-code-review done!");
    }

    private static String getEnv(String key)
    {
        String token = System.getenv(key);
        if (token == null || token.length() == 0) {
            throw new RuntimeException(key + "token is null");
        }
        return token;
    }

}