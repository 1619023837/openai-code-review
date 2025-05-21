package hao.wen.tao.sdk.infrastructure.context.provider;

import hao.wen.tao.sdk.infrastructure.context.enums.CodeContextStrategyProviderEnum;
import hao.wen.tao.sdk.infrastructure.context.model.ExecuteProviderParamContext;
import hao.wen.tao.sdk.infrastructure.context.model.ProviderSwitchConfig;

/**
 * 统一定义代码上下文策略的标准定义
 */
public interface CodeContextStrategyProvider {

    /**
     * 类型
     * @return 类型标识
     */
    CodeContextStrategyProviderEnum getType();


    /**
     * 判断当前是否支持
     * @param statusData
     * @return true 支持
     */
    boolean support(ProviderSwitchConfig statusData);


    /**
     * 执行上下文构建
     * @param context
     * @return 上下文字符串
     */
    String executeProviderBuild(ExecuteProviderParamContext context);
}
