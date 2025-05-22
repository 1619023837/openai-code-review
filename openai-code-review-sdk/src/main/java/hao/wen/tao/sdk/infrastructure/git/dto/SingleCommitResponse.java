package hao.wen.tao.sdk.infrastructure.git.dto;

import lombok.Data;



@Data
public class SingleCommitResponse
{

    private String sha;

    private Commit commit;

    private String html_url;

    private CommitFile[] files;
    @Data
    public static class Commit
    {
        private String message;
    }

    @Data
    public class CommitFile
    {

        //提交的文件
        private String filename;

        //评审文件的代码内容
        private String raw_url;
        //content_url：评审文件的基本信息
        private String content_url;

        //评审文件的变更字符串
        private String patch;
    }
}
