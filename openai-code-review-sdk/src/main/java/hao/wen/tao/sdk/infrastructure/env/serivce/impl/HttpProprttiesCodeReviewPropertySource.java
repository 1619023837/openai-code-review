package hao.wen.tao.sdk.infrastructure.env.serivce.impl;

import hao.wen.tao.sdk.infrastructure.env.CodeReviewPropertyConstant;
import hao.wen.tao.sdk.infrastructure.env.enums.PropertySourceEnum;
import hao.wen.tao.sdk.infrastructure.env.serivce.CodeReviewProprtySource;
import hao.wen.tao.sdk.infrastructure.feishu.untils.EnvUtils;
import hao.wen.tao.sdk.types.utils.DefaultHttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

public class HttpProprttiesCodeReviewPropertySource implements CodeReviewProprtySource {

    private static final Logger log = LoggerFactory.getLogger(HttpProprttiesCodeReviewPropertySource.class);

    private final Properties defaultProperties = new Properties();

    public HttpProprttiesCodeReviewPropertySource() {
        String httpConfigUrl = EnvUtils.getEnv(CodeReviewPropertyConstant.HTTP_CONFIG_URL);
        if (httpConfigUrl ==null || httpConfigUrl.isEmpty()) {
            throw new RuntimeException("未配置http配置文件地址");
        }

        try(InputStream httpInputStream = DefaultHttpUtil.getHttpInputStream(httpConfigUrl)) {
            defaultProperties.load(httpInputStream);
            log.info("成功加载远程配置数据");
        }catch (Exception e) {
            log.error("读取http数据异常",e);
        }
    }

    @Override
    public PropertySourceEnum getType() {
        return PropertySourceEnum.SYSTEM;
    }

    @Override
    public String getProperty(String key) {
        return defaultProperties.getProperty(key);
    }

    @Override
    public Boolean getBooleanProperty(String key) {
        return defaultProperties.containsKey(key);
    }

    @Override
    public Properties asProperties() {
        Properties props = new Properties();
        props.putAll(defaultProperties);
        return props;
    }
}
