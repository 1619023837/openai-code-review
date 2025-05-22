package hao.wen.tao.sdk.infrastructure.context.provider.service;

import hao.wen.tao.sdk.infrastructure.context.enums.CodeContextStrategyProviderEnum;
import hao.wen.tao.sdk.infrastructure.context.model.ExecuteProviderParamContext;
import hao.wen.tao.sdk.infrastructure.context.model.ProviderSwitchConfig;
import hao.wen.tao.sdk.infrastructure.context.provider.CodeContextStrategyProvider;

public class FileTypeContextStrategyProvider implements CodeContextStrategyProvider {
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
        //取出来的文件内容
        Object fileData = context.get("fileName");
        if (fileData!=null && fileData.toString()!=null && fileData.toString().length()>0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<特定文件类型说明>");
            stringBuilder.append("以下为当前审查文件的完整文件内容,评审时候可以作为参考");
            String fileName = fileData.toString();
            if (fileName.endsWith(".java")) {
                stringBuilder.append("如果当前文件类型是Java格式文件,请必须在评审结果中返回当前代码的中设计到技术知识点是什么");
            }
            if (fileName.endsWith(".xml")) {
                stringBuilder.append("如果当前文件类型是xml格式文件,请必须在评审结果中返回相关SQL是否存在慢sql");

            }
            stringBuilder.append("</特点文件类型说明>");
            return stringBuilder.toString();
        }

        return "";
    }
}
