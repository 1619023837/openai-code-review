package hao.wen.tao.sdk.domain.service;

import hao.wen.tao.sdk.domain.BaseGitOperation;
import hao.wen.tao.sdk.infrastructure.feishu.IMessageStrategy;
import hao.wen.tao.sdk.infrastructure.git.GitCommand;
import hao.wen.tao.sdk.infrastructure.openai.IOpenAI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




public abstract class AbstractOpenAiCodeReviewService implements IOpenAiCodeReviewService
{

    private final Logger logger = LoggerFactory.getLogger(AbstractOpenAiCodeReviewService.class);


    protected final BaseGitOperation baseGitOperation;
    protected final IOpenAI openAI;
    protected final IMessageStrategy iMessageStrategy;

    public AbstractOpenAiCodeReviewService(BaseGitOperation baseGitOperation, IOpenAI openAI, IMessageStrategy iMessageStrategy)
    {
        this.baseGitOperation = baseGitOperation;
        this.openAI = openAI;
        this.iMessageStrategy = iMessageStrategy;
    }

    @Override
    public void exec()
    {

        try {
            //1、拉取代码 git
            String diffCode = getDiffCode();
            //2、ai 评审
            String recommend = codeReview(diffCode);
            //3、推送仓库地址
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
        throws Exception;


    protected abstract String codeReview(String diffCode)
        throws Exception;


    protected abstract String recordCodeReview(String recommend)
        throws Exception;









}
