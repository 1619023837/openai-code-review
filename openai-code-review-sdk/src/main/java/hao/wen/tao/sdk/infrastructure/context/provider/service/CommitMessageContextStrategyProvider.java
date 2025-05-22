package hao.wen.tao.sdk.infrastructure.context.provider.service;

import hao.wen.tao.sdk.infrastructure.context.enums.CodeContextStrategyProviderEnum;
import hao.wen.tao.sdk.infrastructure.context.model.ExecuteProviderParamContext;
import hao.wen.tao.sdk.infrastructure.context.model.ProviderSwitchConfig;
import hao.wen.tao.sdk.infrastructure.context.provider.CodeContextStrategyProvider;

public class CommitMessageContextStrategyProvider implements CodeContextStrategyProvider {
    @Override
    public CodeContextStrategyProviderEnum getType() {
        return CodeContextStrategyProviderEnum.COMMIT_MESSAGE;
    }

    @Override
    public boolean support(ProviderSwitchConfig statusData) {
        return Boolean.TRUE.equals(statusData.get(getType().getKey()));
    }

    @Override
    public String executeProviderBuild(ExecuteProviderParamContext context) {
        //取出来的文件内容
        Object fileData = context.get("commit");
        if (fileData!=null && fileData.toString()!=null && fileData.toString().length()>0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<评审内容完整内容上下文>");
            stringBuilder.append("以下为当前审查文件的完整文件内容,评审时候可以作为参考,以Java架构的角度进行分析,根据用户提交的内容进行评审");
            stringBuilder.append(fileData);
            stringBuilder.append("</评审内容完整内容上下文>");
            return stringBuilder.toString();
        }

        return "";
    }
}
