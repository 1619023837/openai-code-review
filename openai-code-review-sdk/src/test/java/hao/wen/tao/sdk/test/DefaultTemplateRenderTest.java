package hao.wen.tao.sdk.test;

import hao.wen.tao.sdk.infrastructure.llmmodel.common.input.DefaultTemplateRender;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class DefaultTemplateRenderTest {

    @Test
    public void test01() {
        String temple = "想使用一个提示词  你是一个经验非常丰富的{{language}}工程师，精通该语言的实现，你非常擅长代码评审，请根据用户提交的代码进行评审";
        DefaultTemplateRender defaultTemplateRender = new DefaultTemplateRender(temple);
        //外层的{}是创建一个匿名子类，内层的{}是实例初始化块。
        Map<String, Object> map = new HashMap() {{
            put("language", "java");
        }};
        System.out.println(defaultTemplateRender.render(map));
    }
}
