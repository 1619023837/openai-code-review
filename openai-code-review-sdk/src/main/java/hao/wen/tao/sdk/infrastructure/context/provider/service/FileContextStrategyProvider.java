package hao.wen.tao.sdk.infrastructure.context.provider.service;

import hao.wen.tao.sdk.infrastructure.context.enums.CodeContextStrategyProviderEnum;
import hao.wen.tao.sdk.infrastructure.context.model.ExecuteProviderParamContext;
import hao.wen.tao.sdk.infrastructure.context.model.ProviderSwitchConfig;
import hao.wen.tao.sdk.infrastructure.context.provider.CodeContextStrategyProvider;

/**
 * 文件内容上下文类
 */
public class FileContextStrategyProvider implements CodeContextStrategyProvider {

    @Override
    public CodeContextStrategyProviderEnum getType() {
        return CodeContextStrategyProviderEnum.FILE_CONTENT;
    }

    @Override
    public boolean support(ProviderSwitchConfig statusData) {
        return Boolean.TRUE.equals(statusData.get(getType().getKey()));
    }

    @Override
    public String executeProviderBuild(ExecuteProviderParamContext context) {
        //取出来的文件内容
        Object fileData = context.get("fileData");
        if (fileData!=null && fileData.toString()!=null && fileData.toString().length()>0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<评审文件完整内容上下文>");
            stringBuilder.append("以下为当前审查文件的完整文件内容,评审时候可以作为参考");
            stringBuilder.append(fileData);
            stringBuilder.append("</评审文件完整内容上下文>");
            return stringBuilder.toString();
        }
        return "";
    }
}
