package hao.wen.tao.sdk.infrastructure.llmmodel.common.input;

/**
 * 提示词模版工厂  为了创建  TemplateRender
 */
public class DefaultPromptTemplateFactory implements PromptTemplateFactory{

    @Override
    public TemplateRender create(PromptTemplateInput promptTemplateInput) {
        return new DefaultTemplateRender(promptTemplateInput.getTemplate());
    }
}
