package hao.wen.tao.sdk.domain.service;

import hao.wen.tao.sdk.domain.BaseGitOperation;
import hao.wen.tao.sdk.infrastructure.context.model.CodeReviewFile;
import hao.wen.tao.sdk.infrastructure.context.model.ExecuteCodeReviewRequestContext;
import hao.wen.tao.sdk.infrastructure.feishu.IMessageStrategy;
import hao.wen.tao.sdk.infrastructure.git.GitCommand;
import hao.wen.tao.sdk.infrastructure.openai.IOpenAI;
import hao.wen.tao.sdk.infrastructure.quatify.listener.impl.CodeReviewResultListenerFactory;
import hao.wen.tao.sdk.infrastructure.quatify.model.CodeReviewResultContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;


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
            //5、触发评审结果的监听器对象
            triggerOnComplete(recommend, executeCodeReviewRequestContext);

        } catch (Exception e) {
            logger.error("openai-code-review error", e);
        }


    }

    private void triggerOnComplete(String recommend, ExecuteCodeReviewRequestContext executeCodeReviewRequestContext) {
        CodeReviewResultContext codeReviewResultContext = new CodeReviewResultContext();
        codeReviewResultContext.setResult(recommend);
        codeReviewResultContext.setAuthor("wuchuzai");
        codeReviewResultContext.setBizName("需求");
        StringBuilder sb = new StringBuilder();
        for (CodeReviewFile codeReviewFile : executeCodeReviewRequestContext.getCodeReviewFiles()) {
            sb.append(codeReviewFile.getFileName()).append("-------------------");
        }
        codeReviewResultContext.setFileList(sb.toString());
        codeReviewResultContext.setBrachName("feature/2010");
        CodeReviewResultListenerFactory.triggerListenerOnComplete(codeReviewResultContext);
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
