package hao.wen.tao.sdk.domain;

import java.util.HashMap;
import java.util.Map;


public class Message
{
    private String touser;

    private String template_id;

    private String url;
    private Map<String, Map<String, String>> data = new HashMap<>();

    public void put(String key, String value)
    {
        data.put(key,new HashMap(){
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
}
