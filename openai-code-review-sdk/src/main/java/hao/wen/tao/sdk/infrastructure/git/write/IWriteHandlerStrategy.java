package hao.wen.tao.sdk.infrastructure.git.write;

import hao.wen.tao.sdk.domain.BaseGitOperation;


public interface IWriteHandlerStrategy
{

    String typeName();

    void init(BaseGitOperation baseGitOperation);

    String execute(String codeResult)
        throws Exception;
}
