package hao.wen.tao.sdk.infrastructure.git.write.impl;

import hao.wen.tao.sdk.domain.BaseGitOperation;
import hao.wen.tao.sdk.infrastructure.git.GitCommand;
import hao.wen.tao.sdk.infrastructure.git.GitRestAPIOperation;
import hao.wen.tao.sdk.infrastructure.git.write.IWriteHandlerStrategy;


public class CommitWriteHandlerStrategy implements IWriteHandlerStrategy
{
    private BaseGitOperation baseGitOperation;
    @Override
    public String typeName()
    {
        return "comment";
    }

    @Override
    public void init(BaseGitOperation baseGitOperation)
    {
        this.baseGitOperation = baseGitOperation;
    }

    @Override
    public String execute(String codeResult)
        throws Exception
    {

        if (!(baseGitOperation instanceof GitRestAPIOperation)){
            throw new RuntimeException("git对象不支持");
        }
        GitRestAPIOperation gitRestAPIOperation = (GitRestAPIOperation)baseGitOperation;
        return gitRestAPIOperation.writeResult(codeResult);
    }
}
