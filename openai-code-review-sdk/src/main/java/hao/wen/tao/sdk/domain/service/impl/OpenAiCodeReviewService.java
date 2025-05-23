package hao.wen.tao.sdk.domain.service.impl;

import hao.wen.tao.sdk.domain.BaseGitOperation;
import hao.wen.tao.sdk.domain.service.AbstractOpenAiCodeReviewService;
import hao.wen.tao.sdk.infrastructure.context.enums.CodeContextStrategyProviderEnum;
import hao.wen.tao.sdk.infrastructure.context.model.CodeReviewFile;
import hao.wen.tao.sdk.infrastructure.context.model.ExecuteCodeReviewRequestContext;
import hao.wen.tao.sdk.infrastructure.context.model.ExecuteProviderParamContext;
import hao.wen.tao.sdk.infrastructure.context.model.ProviderSwitchConfig;
import hao.wen.tao.sdk.infrastructure.context.provider.CodeContextStrategyProvider;
import hao.wen.tao.sdk.infrastructure.context.provider.factory.CodeContextStrategyProviderFactory;
import hao.wen.tao.sdk.infrastructure.feishu.IMessageStrategy;
import hao.wen.tao.sdk.infrastructure.git.write.IWriteHandlerStrategy;
import hao.wen.tao.sdk.infrastructure.git.write.WriteHandlerStrategeFactory;
import hao.wen.tao.sdk.infrastructure.llmmodel.common.input.Prompt;
import hao.wen.tao.sdk.infrastructure.llmmodel.common.input.PromptTemplate;
import hao.wen.tao.sdk.infrastructure.llmmodel.common.output.Response;
import hao.wen.tao.sdk.infrastructure.llmmodel.common.text.AIMessageText;
import hao.wen.tao.sdk.infrastructure.llmmodel.common.text.ChatMessageText;
import hao.wen.tao.sdk.infrastructure.llmmodel.common.text.SystemMessageText;
import hao.wen.tao.sdk.infrastructure.llmmodel.common.text.UserMessageText;
import hao.wen.tao.sdk.infrastructure.llmmodel.common.zhipu.ZhipuAiChatModel;
import hao.wen.tao.sdk.infrastructure.openai.IOpenAI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class OpenAiCodeReviewService extends AbstractOpenAiCodeReviewService
{

    public OpenAiCodeReviewService(BaseGitOperation baseGitOperation, IOpenAI openAI, IMessageStrategy iMessageStrategy)
    {
        super(baseGitOperation, openAI, iMessageStrategy);
    }


    @Override
    protected void getDiffCode(ExecuteCodeReviewRequestContext executeCodeReviewRequestContext)
        throws Exception
    {
        List<CodeReviewFile> codeReviewFiles = super.baseGitOperation.diffFileList();
        executeCodeReviewRequestContext.setCodeReviewFiles(codeReviewFiles);
        executeCodeReviewRequestContext.setDiffCode(this.baseGitOperation.diff());
    }

    @Override
    protected String codeReview(ExecuteCodeReviewRequestContext executeCodeReviewRequestContext)
        throws Exception
    {
        List<CodeReviewFile> codeReviewFiles = executeCodeReviewRequestContext.getCodeReviewFiles();
        StringBuilder stringBuilder = new StringBuilder();
        for (CodeReviewFile codeReviewFile : codeReviewFiles) {
            //系统提示词
            String systemText = "你是一个经验非常丰富的{{language}}工程师，精通该语言的实现，你非常擅长代码评审，请根据用户提交的代码进行评审";
            PromptTemplate from = PromptTemplate.from(systemText);
            Map<String, Object> objectMap = new HashMap(){{
                put("language", "java");
            }};
            Prompt apply = from.apply(objectMap);
            //系统提示词
            String promptText = apply.getText();
            ProviderSwitchConfig switchConfig = new ProviderSwitchConfig();
            switchConfig.put(CodeContextStrategyProviderEnum.FILE_CONTENT.getKey(), true);
            switchConfig.put(CodeContextStrategyProviderEnum.FILE_TYPE.getKey(), true);

            ExecuteProviderParamContext executeProviderParamContext = new ExecuteProviderParamContext();
            executeProviderParamContext.put("fileData", codeReviewFile.getFileContent());
            executeProviderParamContext.put("fileName", codeReviewFile.getFileName());
            Set<String> keySet = switchConfig.keySet();
            for (String key : keySet) {
                CodeContextStrategyProvider codeContextStrategyProvider = CodeContextStrategyProviderFactory.getCodeContextStrategyProvider(key);
                if (codeContextStrategyProvider != null) {
                    String data = codeContextStrategyProvider.executeProviderBuild(executeProviderParamContext);
                    promptText = promptText + data;
                }
            }
            //组装系统消息
            SystemMessageText systemMessageText = new SystemMessageText(promptText);
            //组装用户消息
            UserMessageText userMessageText = buildUserMessageText(codeReviewFile);
            //调用HTTP AI大模型接口
            Response<AIMessageText> response  = getString(systemMessageText, userMessageText);
            stringBuilder.append("文件名称:")
                    .append(codeReviewFile.getFileName())
                    .append(", 评审结果:")
                    .append(response.content().text()).append("\n");
        }
        return stringBuilder.toString();
    }

    private Response<AIMessageText> getString(SystemMessageText systemMessageText, UserMessageText userMessageText) {
        ZhipuAiChatModel chatModel = ZhipuAiChatModel.builder()
                .model("")
                .apiSecret(openAI.apiSecret())
                .baseUrl(openAI.getUrl()).build();
        List<ChatMessageText> chatMessageTexts = new ArrayList(){
            {
                add(systemMessageText);
                add(userMessageText);
            }
        };
       return chatModel.generate(chatMessageTexts);
    }

    private static UserMessageText buildUserMessageText(CodeReviewFile codeReviewFile) {
        //用户提示词
        String userText = "请您根据git diff记录，对代码做出评审。代码为{{diffCode}}";
        PromptTemplate userFrom = PromptTemplate.from(userText);
        Map<String, Object> userMap = new HashMap(){{
            put("diffCode", codeReviewFile.getDiff());
        }};
        Prompt userApply = userFrom.apply(userMap);
        //使用user
        UserMessageText userMessageText = new UserMessageText(userApply.getText());
        return userMessageText;
    }

    @Override
    protected String recordCodeReview(String recommend)
        throws Exception
    {
        String writeHandler = "comment";
        IWriteHandlerStrategy writeHandlerStrategy = WriteHandlerStrategeFactory.getWriteHandlerStrategy(
            writeHandler);

        writeHandlerStrategy.init(baseGitOperation);
        return writeHandlerStrategy.execute(recommend);
    }


    @Override
    protected void pushMessage(String logUrl)
        throws Exception
    {
        Map<String, Map<String, String>> data = new HashMap<>();
//        TemplateMessageDTO.put(data, TemplateMessageDTO.TemplateKey.REPO_NAME, gitCommand.getProject());
//        TemplateMessageDTO.put(data, TemplateMessageDTO.TemplateKey.BRANCH_NAME, gitCommand.getBranch());
//        TemplateMessageDTO.put(data, TemplateMessageDTO.TemplateKey.COMMIT_AUTHOR, gitCommand.getAuthor());
//        TemplateMessageDTO.put(data, TemplateMessageDTO.TemplateKey.COMMIT_MESSAGE, gitCommand.getMessage());
        iMessageStrategy.sendMessage(logUrl,data);
    }


}
