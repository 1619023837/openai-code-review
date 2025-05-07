package hao.wen.tao.sdk.infrastructure.llmmodel.common.input;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 默认的模版提示词
 */
public class DefaultTemplateRender implements TemplateRender{

    //. 匹配一个字符
    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\{\\{(.+?)}}");

    private final String template;

    private final Set<String> allVariables;
    public DefaultTemplateRender(String template) {
        if (template == null || template.length() ==0) {
            throw new RuntimeException("提示词模版不能为null");
        }
        this.template = template;
        this.allVariables = extractVariable(template);

    }

    private Set<String> extractVariable(String template) {
        Set<String> allVariable = new HashSet<>();
        Matcher matcher = VARIABLE_PATTERN.matcher(template);
        while (matcher.find()) {
            allVariable.add(matcher.group(1));
        }
        return allVariable;
    }


    /**
     * 变量 先要进行查找
     * @param variabies
     * @return
     */
    @Override
    public String render(Map<String, Object> variabies) {
        ensureAllVariablesProvided(variabies);
        //不改变原始的template
        String result = template;
        //进行替换
        for (Map.Entry<String, Object> stringObjectEntry : variabies.entrySet()) {
            result = replaceAll(result, stringObjectEntry.getKey(), stringObjectEntry.getValue());
        }
        return result;
    }

    private static String replaceAll(String result, String key, Object value) {
        return result.replace(inDoubleCurlyBrackets(key), String.valueOf(value));
    }

    private static String inDoubleCurlyBrackets(String key) {
        return "{{" + key + "}}";
    }

    private void ensureAllVariablesProvided(Map<String, Object> variabies) {
        for (String allVariable : allVariables) {
            if (!variabies.containsKey(allVariable)) {
                throw new RuntimeException(String.format("Value for the variabie '%s' is missing",allVariable));
            }
        }
    }

}
