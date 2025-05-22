package hao.wen.tao.sdk.infrastructure.context.enums;

public enum CodeContextStrategyProviderEnum {

    FILE_CONTENT("file_content","文件内容"),
    COMMIT_MESSAGE("commit_message","提交内容"),
    FILE_TYPE("file_type","文件类型"),
    ;
    private final String key;
    private final String name;


    CodeContextStrategyProviderEnum(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public String getKey(){
        return this.key;
    }

    public String getName(){
        return this.name;
    }
}
