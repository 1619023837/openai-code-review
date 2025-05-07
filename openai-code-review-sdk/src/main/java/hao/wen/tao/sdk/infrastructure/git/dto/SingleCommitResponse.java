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

        private String filename;

        private String raw_url;

        private String patch;
    }
}
