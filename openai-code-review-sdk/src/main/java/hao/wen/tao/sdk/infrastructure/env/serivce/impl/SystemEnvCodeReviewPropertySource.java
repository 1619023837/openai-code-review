package hao.wen.tao.sdk.infrastructure.env.serivce.impl;

import hao.wen.tao.sdk.infrastructure.env.enums.PropertySourceEnum;
import hao.wen.tao.sdk.infrastructure.env.serivce.CodeReviewProprtySource;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class SystemEnvCodeReviewPropertySource implements CodeReviewProprtySource {

    private final Map<String, Object> env = new HashMap();
    @Override
    public PropertySourceEnum getType() {
        return PropertySourceEnum.SYSTEM;
    }

    @Override
    public String getProperty(String key) {
        if (env.get(key) != null) {
            return env.get(key).toString();
        }
        return null;
    }

    @Override
    public Boolean getBooleanProperty(String key) {
        return env.containsKey(key);
    }

    @Override
    public Properties asProperties() {
        Properties properties = new Properties();
        properties.putAll(env);
        return properties;
    }
}
