package hao.wen.tao.sdk.infrastructure.git.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;


/**
 * {"body":"牛逼一下","path":"openai-code-review-sdk/src/main/java/hao/wen/tao/sdk/OpenAiCodeReview.java","position":1}
 */
@Data
public class CommitCommentRequestDTO
{

    /**
     * 评论内容
     */
    @JSONField(name = "body")
    private String body;

    /**
     * 文件路径
     */
    @JSONField(name = "path")
    private String path;


    /**
     * 行号
     */
    @JSONField(name = "position")
    private Integer position;
}
