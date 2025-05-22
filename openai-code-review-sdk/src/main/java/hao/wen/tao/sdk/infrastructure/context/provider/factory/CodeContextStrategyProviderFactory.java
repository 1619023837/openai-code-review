package hao.wen.tao.sdk.infrastructure.context.provider.factory;

import hao.wen.tao.sdk.infrastructure.context.enums.CodeContextStrategyProviderEnum;
import hao.wen.tao.sdk.infrastructure.context.provider.CodeContextStrategyProvider;
import hao.wen.tao.sdk.infrastructure.context.provider.service.CommitMessageContextStrategyProvider;
import hao.wen.tao.sdk.infrastructure.context.provider.service.FileContextStrategyProvider;
import hao.wen.tao.sdk.infrastructure.context.provider.service.FileTypeContextStrategyProvider;

import java.util.HashMap;
import java.util.Map;

public class CodeContextStrategyProviderFactory {

    public static Map<String, CodeContextStrategyProvider> providerMap = new HashMap();
    static {
        providerMap.put(CodeContextStrategyProviderEnum.FILE_CONTENT.getKey(), new FileContextStrategyProvider());
        providerMap.put(CodeContextStrategyProviderEnum.COMMIT_MESSAGE.getKey(), new CommitMessageContextStrategyProvider());
        providerMap.put(CodeContextStrategyProviderEnum.FILE_TYPE.getKey(), new FileTypeContextStrategyProvider());
    }

    public static CodeContextStrategyProvider getCodeContextStrategyProvider(String strategy) {
        return         providerMap.get(strategy);
    }
}
