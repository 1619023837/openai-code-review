package hao.wen.tao.sdk.infrastructure.llmmodel.common.input;

/**
 * 提示词模版工厂
 */
public interface PromptTemplateFactory {


    /**
     * 创建提示词模版
     * @param promptTemplateInput 输入参数
     * @return
     */
    TemplateRender create(PromptTemplateInput promptTemplateInput);

}
