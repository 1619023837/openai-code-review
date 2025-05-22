package hao.wen.tao.sdk.domain.service;

import hao.wen.tao.sdk.domain.BaseGitOperation;
import hao.wen.tao.sdk.infrastructure.context.model.ExecuteCodeReviewRequestContext;
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
            //封装执行请求上下文 进行业务处理
            ExecuteCodeReviewRequestContext executeCodeReviewRequestContext = new ExecuteCodeReviewRequestContext();

             getDiffCode(executeCodeReviewRequestContext);
            //2、ai 评审
            String recommend = codeReview(executeCodeReviewRequestContext);
            //3、记录评审结果 返回日志信息
            String logUrl = recordCodeReview(recommend);
            // 4. 发送消息通知；日志地址、通知的内容
            pushMessage(logUrl);

        } catch (Exception e) {
            logger.error("openai-code-review error", e);
        }


    }

    protected abstract void pushMessage(String logUrl)
        throws Exception;

    protected abstract void getDiffCode(ExecuteCodeReviewRequestContext executeCodeReviewRequestContext)
        throws Exception;


    protected abstract String codeReview(ExecuteCodeReviewRequestContext executeCodeReviewRequestContext)
        throws Exception;


    protected abstract String recordCodeReview(String recommend)
        throws Exception;









}
