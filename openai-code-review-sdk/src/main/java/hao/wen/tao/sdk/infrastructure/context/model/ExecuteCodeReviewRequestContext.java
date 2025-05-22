package hao.wen.tao.sdk.infrastructure.context.model;

import lombok.Data;

import java.util.List;

@Data
public class ExecuteCodeReviewRequestContext {

    /**
     * 待评价的内容
     */
    private String diffCode;

    private List<CodeReviewFile> codeReviewFiles;


}
