package hao.wen.tao.sdk.infrastructure.weixin.dto;

import java.util.HashMap;
import java.util.Map;


public class TemplateMessageDTO
{
    private String touser;

    private String template_id;

    private String url ="https://weixin.qq.com";
    private Map<String, Map<String, String>> data = new HashMap<>();

    public void put(String key, String value)
    {
        data.put(key,new HashMap(){
            {
                put("value",value);
            }
        });
    }

    public TemplateMessageDTO(String template_id, String touser)
    {
        this.template_id = template_id;
        this.touser = touser;
    }

    public static void put(Map<String, Map<String, String>> data,TemplateKey key, String value)
    {
        data.put(key.code,new HashMap(){
            {
                put("value",value);
            }
        });
    }
    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, Map<String, String>> getData() {
        return data;
    }

    public void setData(Map<String, Map<String, String>> data) {
        this.data = data;
    }

    public enum TemplateKey
    {

        REPO_NAME("repo_name", "项目名称"),

        BRANCH_NAME("branch_name", "分支名称"),

        COMMIT_AUTHOR("commit_author", "提交者"),


        COMMIT_MESSAGE("commit_message", "提交信息"),
        ;

        private String code;
        private String desc;

        TemplateKey(String code, String desc)
        {
            this.code = code;
            this.desc = desc;
        }

        public String getDesc()
        {
            return desc;
        }

        public String getCode()
        {
            return code;
        }
    }
}
