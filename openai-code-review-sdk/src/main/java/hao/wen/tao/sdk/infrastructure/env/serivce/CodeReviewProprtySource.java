package hao.wen.tao.sdk.infrastructure.env.serivce;

import hao.wen.tao.sdk.infrastructure.env.enums.PropertySourceEnum;
import javafx.beans.property.Property;

import java.util.Properties;

public interface CodeReviewProprtySource {

    /**
     * 获取类型枚举
     * @return
     */
    PropertySourceEnum getType();

    /**
     * 获取属性
     * @param key
     * @return
     */
    String getProperty(String key);

    Boolean getBooleanProperty(String key);

    /**
     * 转换属性为对象
     * @return
     */
    Properties asProperties();
}
