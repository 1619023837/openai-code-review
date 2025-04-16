package hao.wen.tao.sdk.domain.service;

import hao.wen.tao.sdk.infrastructure.git.GitCommand;
import hao.wen.tao.sdk.infrastructure.openai.IOpenAI;
import hao.wen.tao.sdk.infrastructure.weixin.WeiXin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public abstract class AbstractOpenAiCodeReviewService implements IOpenAiCodeReviewService
{

    private final Logger logger = LoggerFactory.getLogger(AbstractOpenAiCodeReviewService.class);


    protected final GitCommand gitCommand;
    protected final IOpenAI openAI;
    protected final WeiXin weiXin;

    public AbstractOpenAiCodeReviewService(GitCommand gitCommand, IOpenAI openAI, WeiXin weiXin)
    {
        this.gitCommand = gitCommand;
        this.openAI = openAI;
        this.weiXin = weiXin;
    }

    @Override
    public void exec()
    {

        try {
            System.out.println("开始");
            //1、拉取代码 git
            String diffCode = getDiffCode();
            System.out.println("开始 diffCode");
            //2、ai 评审
            String recommend = codeReview(diffCode);
            System.out.println("开始 recommend");
            //3、推送仓库
            String logUrl = recordCodeReview(recommend);
            // 4. 发送消息通知；日志地址、通知的内容
            pushMessage(logUrl);

        } catch (Exception e) {
            logger.error("openai-code-review error", e);
        }


    }

    protected abstract void pushMessage(String logUrl)
        throws Exception;

    protected abstract String getDiffCode()
        throws IOException, InterruptedException;


    protected abstract String codeReview(String diffCode)
        throws Exception;


    protected abstract String recordCodeReview(String recommend)
        throws Exception;







}
