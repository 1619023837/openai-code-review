package hao.wen.tao.sdk.infrastructure.quatify.model;

import lombok.Data;

import java.util.List;

/**
 * 代码评审结果上下文
 */
@Data
public class CodeReviewResultContext {

    /**
     * 分支名称
     */
    private String brachName;

    /**
     * 评审结果
     */
    private String result;

    /**
     * 作者
     */
    private String author;

    /**
     * 评审文件 路径
     */
    private String fileList;

    /**
     * 需求/任务名称
     */
    private String  bizName;
}
