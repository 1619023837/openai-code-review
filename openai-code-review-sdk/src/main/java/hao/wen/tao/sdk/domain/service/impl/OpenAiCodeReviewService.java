package hao.wen.tao.sdk.domain.service.impl;

import hao.wen.tao.sdk.domain.BaseGitOperation;
import hao.wen.tao.sdk.domain.service.AbstractOpenAiCodeReviewService;
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


public class OpenAiCodeReviewService extends AbstractOpenAiCodeReviewService
{

    public OpenAiCodeReviewService(BaseGitOperation baseGitOperation, IOpenAI openAI, IMessageStrategy iMessageStrategy)
    {
        super(baseGitOperation, openAI, iMessageStrategy);
    }


    @Override
    protected String getDiffCode()
        throws Exception
    {
        return  super.baseGitOperation.diff();
    }

    @Override
    protected String codeReview(String diffCode)
        throws Exception
    {
//        ChatCompletionRequestDTO chatCompletionRequest = new ChatCompletionRequestDTO();
//        chatCompletionRequest.setModel("glm-4-flash");
//        //                chatCompletionSyncResponse.setMessages(new ArrayList<ChatCompletionRequestDTO.Prompt>(){{
//        //                    add(new ChatCompletionRequestDTO.Prompt("user","你是一个高级编程架构师，精通各类场景方案、架构设计和编程语言请，请您根据git diff记录，对代码做出评审。代码为:"));
//        //                    add(new ChatCompletionRequestDTO.Prompt("user",diffCode));
//        //                }});
//        chatCompletionRequest.setMessages(new ArrayList<ChatCompletionRequestDTO.Prompt>() {
//            private static final long serialVersionUID = -7988151926241837899L;
//
//            {
//                //提示词Prompt
//                add(new ChatCompletionRequestDTO.Prompt("user", "你是一位资深编程专家，拥有深厚的编程基础和广泛的技术栈知识。你的专长在于识别代码中的低效模式、安全隐患、以及可维护性问题，并能提出针对性的优化策略。你擅长以易于理解的方式解释复杂的概念，确保即使是初学者也能跟随你的指导进行有效改进。在提供优化建议时，你注重平衡性能、可读性、安全性、逻辑错误、异常处理、边界条件，以及可维护性方面的考量，同时尊重原始代码的设计意图。\n" +
//                                                                "你总是以鼓励和建设性的方式提出反馈，致力于提升团队的整体编程水平，详尽指导编程实践，雕琢每一行代码至臻完善。用户会将仓库代码分支修改代码给你，以git diff 字符串的形式提供，你需要根据变化的代码，帮忙review本段代码。然后你review内容的返回内容必须严格遵守下面我给你的格式，包括标题内容。\n" +
//                                                                "模板中的变量内容解释：\n" +
//                                                                "变量1是给review打分，分数区间为0~100分。\n" +
//                                                                "变量2 是code review发现的问题点，包括：可能的性能瓶颈、逻辑缺陷、潜在问题、安全风险、命名规范、注释、以及代码结构、异常情况、边界条件、资源的分配与释放等等\n" +
//                                                                "变量3是具体的优化修改建议。\n" +
//                                                                "变量4是你给出的修改后的代码。 \n" +
//                                                                "变量5是代码中的优点。\n" +
//                                                                "变量6是代码的逻辑和目的，识别其在特定上下文中的作用和限制\n" +
//                                                                "\n" +
//                                                                "必须要求：\n" +
//                                                                "1. 以精炼的语言、严厉的语气指出存在的问题。\n" +
//                                                                "2. 你的反馈内容必须使用严谨的markdown格式\n" +
//                                                                "3. 不要携带变量内容解释信息。\n" +
//                                                                "4. 有清晰的标题结构\n" +
//                                                                "返回格式严格如下：\n" +
//                                                                "# 文涛： OpenAi 代码评审.\n" +
//                                                                "### \uD83D\uDE00代码评分：{变量1}\n" +
//                                                                "#### \uD83D\uDE00代码逻辑与目的：\n" +
//                                                                "{变量6}\n" +
//                                                                "#### ✅代码优点：\n" +
//                                                                "{变量5}\n" +
//                                                                "#### \uD83E\uDD14问题点：\n" +
//                                                                "{变量2}\n" +
//                                                                "#### \uD83C\uDFAF修改建议：\n" +
//                                                                "{变量3}\n" +
//                                                                "#### \uD83D\uDCBB修改后的代码：\n" +
//                                                                "{变量4}\n" +
//                                                                "`;代码如下:"));
//                add(new ChatCompletionRequestDTO.Prompt("user", diffCode));
//            }
//        });
        //系统提示词
        String systemText = "你是一个经验非常丰富的{{language}}工程师，精通该语言的实现，你非常擅长代码评审，请根据用户提交的代码进行评审";
        PromptTemplate from = PromptTemplate.from(systemText);
        Map<String, Object> objectMap = new HashMap(){{
            put("language", "java");
        }};
        Prompt apply = from.apply(objectMap);
        //系统
        SystemMessageText systemMessageText = new SystemMessageText(apply.getText());
        //用户提示词
        String userText = "请您根据git diff记录，对代码做出评审。代码为{{diffCode}}";
        PromptTemplate userFrom = PromptTemplate.from(userText);
        Map<String, Object> userMap = new HashMap(){{
            put("diffCode", diffCode);
        }};
        Prompt userApply = userFrom.apply(userMap);
        //使用user
        UserMessageText userMessageText = new UserMessageText(userApply.getText());

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
        Response<AIMessageText> generate = chatModel.generate(chatMessageTexts);
        return generate.content().text();
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
