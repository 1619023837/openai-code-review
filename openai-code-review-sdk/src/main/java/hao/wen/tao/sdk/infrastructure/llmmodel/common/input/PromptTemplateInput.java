package hao.wen.tao.sdk.infrastructure.llmmodel.common.input;

/**
 * 提示词模版输入内容
 */
public interface PromptTemplateInput {


    /**
     * 获取提示词模版内容
     * @return
     */
    String getTemplate();


    /**
     * 提示词模版名称
     * @return
     */
    default String getName(){
        return "template";
    }
}
