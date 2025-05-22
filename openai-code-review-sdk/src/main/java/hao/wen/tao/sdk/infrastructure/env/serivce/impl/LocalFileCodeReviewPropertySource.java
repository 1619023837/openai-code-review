package hao.wen.tao.sdk.infrastructure.env.serivce.impl;

import hao.wen.tao.sdk.infrastructure.env.CodeReviewPropertyConstant;
import hao.wen.tao.sdk.infrastructure.env.enums.PropertySourceEnum;
import hao.wen.tao.sdk.infrastructure.env.serivce.CodeReviewProprtySource;
import hao.wen.tao.sdk.infrastructure.feishu.untils.EnvUtils;
import hao.wen.tao.sdk.types.utils.DefaultHttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class LocalFileCodeReviewPropertySource implements CodeReviewProprtySource {


    private static final Logger log = LoggerFactory.getLogger(HttpProprttiesCodeReviewPropertySource.class);

    private final Properties defaultProperties = new Properties();

    public LocalFileCodeReviewPropertySource() {
        String localConfigPath = EnvUtils.getEnv(CodeReviewPropertyConstant.LOCAL_CONFIG_URL);
        if (localConfigPath ==null || localConfigPath.isEmpty()) {
            throw new RuntimeException("未配置本地配置文件地址");
        }

        try(InputStream httpInputStream = new FileInputStream(localConfigPath)) {
            defaultProperties.load(httpInputStream);
            log.info("成功加载本地配置数据");
        }catch (Exception e) {
            log.error("加载本地配置异常",e);
        }
    }

    @Override
    public PropertySourceEnum getType() {
        return  PropertySourceEnum.SYSTEM;
    }

    @Override
    public String getProperty(String key) {
        return defaultProperties.getProperty(key);
    }

    @Override
    public Boolean getBooleanProperty(String key) {
        return defaultProperties.contains(key);
    }

    @Override
    public Properties asProperties() {
        Properties props = new Properties();
        props.putAll(defaultProperties);
        return props;
    }
}
