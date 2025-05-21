package hao.wen.tao.sdk.infrastructure.context.provider.service;

import hao.wen.tao.sdk.infrastructure.context.enums.CodeContextStrategyProviderEnum;
import hao.wen.tao.sdk.infrastructure.context.model.ExecuteProviderParamContext;
import hao.wen.tao.sdk.infrastructure.context.model.ProviderSwitchConfig;
import hao.wen.tao.sdk.infrastructure.context.provider.CodeContextStrategyProvider;

public class FileContextStrategyProvider implements CodeContextStrategyProvider {

    @Override
    public CodeContextStrategyProviderEnum getType() {
        return CodeContextStrategyProviderEnum.FILE_TYPE;
    }

    @Override
    public boolean support(ProviderSwitchConfig statusData) {
        return Boolean.TRUE.equals(statusData.get(getType().getKey()));
    }

    @Override
    public String executeProviderBuild(ExecuteProviderParamContext context) {


        return "";
    }
}
