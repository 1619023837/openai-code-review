package hao.wen.tao.sdk;

import hao.wen.tao.sdk.domain.service.impl.OpenAiCodeReviewService;
import hao.wen.tao.sdk.infrastructure.feishu.IMessageStrategy;
import hao.wen.tao.sdk.infrastructure.feishu.MessageFactory;
import hao.wen.tao.sdk.infrastructure.feishu.untils.EnvUtils;
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
        String url = EnvUtils.getEnv("GIT_CHECK_COMMIT_URL") + "/" + EnvUtils.getEnv("GITHUB_VERSION");
        GitCommand gitCommand = new GitCommand(
            EnvUtils.getEnv("GITHUB_REVIEW_LOG_URI"),
            EnvUtils.getEnv("GITHUB_TOKEN"),
            EnvUtils.getEnv("COMMIT_PROJECT"),
            EnvUtils.getEnv("COMMIT_BRANCH"),
            EnvUtils.getEnv("COMMIT_AUTHOR"),
            EnvUtils.getEnv("COMMIT_MESSAGE")
        );

//        /**
//         * 项目：{{repo_name.DATA}} 分支：{{branch_name.DATA}} 作者：{{commit_author.DATA}} 说明：{{commit_message.DATA}}
//         */
//        WeiXin weiXin = new WeiXin(
//            EnvUtils.getEnv("WEIXIN_APPID"),
//            EnvUtils.getEnv("WEIXIN_SECRET"),
//            EnvUtils.getEnv("WEIXIN_TOUSER"),
//            EnvUtils.getEnv("WEIXIN_TEMPLATE_ID")
//        );

        //获取信息类型
        String notify= EnvUtils.getEnv("NOTIFY");

        IMessageStrategy messageStrategy = MessageFactory.getMessageStrategy(notify);
        //chatglm 地址  生成token地址
        IOpenAI iOpenAI = new ChatGLM( EnvUtils.getEnv("CHATGLM_APIHOST"),  EnvUtils.getEnv("CHATGLM_APIKEYSECRET"));
        GitRestAPIOperation gitRestAPIOperation = new GitRestAPIOperation(
            url,  EnvUtils.getEnv("GITHUB_TOKEN"));
        OpenAiCodeReviewService openAiCodeReviewService = new OpenAiCodeReviewService(gitRestAPIOperation,gitCommand, iOpenAI, messageStrategy);
        openAiCodeReviewService.exec();
        logger.info("openai-code-review done!");
    }


}