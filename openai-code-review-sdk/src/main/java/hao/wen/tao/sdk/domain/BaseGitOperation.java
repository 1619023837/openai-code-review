package hao.wen.tao.sdk.domain;


import hao.wen.tao.sdk.infrastructure.context.model.CodeReviewFile;

import java.util.List;

public interface BaseGitOperation
{

    /**
     * 定义一个获取变更内容的方法
     * @return
     * @throws Exception
     */
    String diff()
        throws Exception;


    /**
     * 定义方法获取待评审的文件列表
     * @return 文件列表
     * @throws Exception
     */
    List<CodeReviewFile> diffFileList() throws Exception;

    String writeResult(String result)
        throws Exception;
}
