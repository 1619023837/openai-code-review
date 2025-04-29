package hao.wen.tao.sdk.domain;


public interface BaseGitOperation
{

    /**
     * 定义一个获取变更内容的方法
     * @return
     * @throws Exception
     */
    String diff()
        throws Exception;
}
