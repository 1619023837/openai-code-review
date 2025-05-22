package hao.wen.tao.sdk.infrastructure.env.serivce;

import hao.wen.tao.sdk.infrastructure.env.CodeReviewPropertyConstant;
import hao.wen.tao.sdk.infrastructure.env.serivce.impl.HttpProprttiesCodeReviewPropertySource;
import hao.wen.tao.sdk.infrastructure.env.serivce.impl.LocalFileCodeReviewPropertySource;
import hao.wen.tao.sdk.infrastructure.env.serivce.impl.SystemEnvCodeReviewPropertySource;
import hao.wen.tao.sdk.infrastructure.feishu.untils.EnvUtils;

import java.util.Properties;

public class CodeReivewConfigPropertiesFacade {

    private CodeReviewProprtySource codeReviewProprtySource;

    public CodeReivewConfigPropertiesFacade() {

        String httpConfigUrl = EnvUtils.getEnv(CodeReviewPropertyConstant.HTTP_CONFIG_URL);
        String localConfigUrl = EnvUtils.getEnv(CodeReviewPropertyConstant.LOCAL_CONFIG_URL);
        if (httpConfigUrl != null && !httpConfigUrl.isEmpty()) {
            codeReviewProprtySource = new HttpProprttiesCodeReviewPropertySource();
        } else if (localConfigUrl != null && !localConfigUrl.isEmpty()) {
            codeReviewProprtySource = new LocalFileCodeReviewPropertySource();
        }else {
            codeReviewProprtySource = new SystemEnvCodeReviewPropertySource();
        }
    }

    public String getProprtties(String key) {
        return codeReviewProprtySource.getProperty(key);
    }
    public Properties asProperties() {
        return codeReviewProprtySource.asProperties();
    }
}
