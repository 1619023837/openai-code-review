package hao.wen.tao.sdk.infrastructure.llmmodel.common.input;

import java.util.Map;

/*
 提示词模版类
 */
public class PromptTemplate
{

    private static final PromptTemplateFactory FACTORY = new DefaultPromptTemplateFactory();

    private  final TemplateRender templateRender;

    private final String template;

    public PromptTemplate(String template) {
        this.template = template;
        this.templateRender = FACTORY.create(() -> template);
    }

    public String getTemplate(){
        return template;
    }

    /**
     * 对外提供的方法,渲染变量为一个提示词对象
     */

    public Prompt apply(Map<String, Object> variable) {
        return Prompt.from(templateRender.render(variable));
    }

    /**
     * 根据模版创建一个提示词模版
     */

    public static PromptTemplate from(String template) {
        return new PromptTemplate(template);
    }

}
