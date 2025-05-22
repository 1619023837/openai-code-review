package hao.wen.tao.sdk.infrastructure.llmmodel.common.input;

import java.util.Map;

/**
 * 提示词渲染实现的接口
 */
public interface TemplateRender {


    /**
     * 根据变量 和提示词模版 获取提示词信息
     * @param variabies
     * @return
     */
    String render(Map<String, Object> variabies);
}
