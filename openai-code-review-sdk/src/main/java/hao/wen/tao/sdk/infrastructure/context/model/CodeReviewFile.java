package hao.wen.tao.sdk.infrastructure.context.model;

import lombok.Data;

@Data
public class CodeReviewFile {

    private String fileName;

    private String diff;

    private String fileContent;
}
