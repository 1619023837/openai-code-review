package hao.wen.tao.sdk.infrastructure.git.write.impl;

import hao.wen.tao.sdk.domain.BaseGitOperation;
import hao.wen.tao.sdk.infrastructure.git.GitCommand;
import hao.wen.tao.sdk.infrastructure.git.write.IWriteHandlerStrategy;


public class RemoteWriteHandlerStrategy implements IWriteHandlerStrategy
{
    private BaseGitOperation baseGitOperation;
    @Override
    public String typeName()
    {
        return "remote";
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
        if (!(baseGitOperation instanceof GitCommand)){
            throw new RuntimeException("git对象不支持");
        }
        GitCommand gitCommand = (GitCommand)baseGitOperation;
        return gitCommand.commitAndPush(codeResult);
    }
}
