package hao.wen.tao.sdk.infrastructure.git;

import com.alibaba.fastjson2.JSON;
import hao.wen.tao.sdk.domain.BaseGitOperation;
import hao.wen.tao.sdk.infrastructure.git.dto.SingleCommitResponse;
import hao.wen.tao.sdk.types.utils.DefaultHttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * 操作api的类
 */
public class GitRestAPIOperation implements BaseGitOperation
{

    private final Logger logger = LoggerFactory.getLogger(GitRestAPIOperation.class.getName());

    private final String url;

    private final String token;

    public GitRestAPIOperation(String url, String token)
    {
        this.url = url;
        this.token = token;
    }

    @Override
    public String diff()
        throws Exception
    {

        Map<String, String> header = new HashMap();
        header.put("Accept", "application/vnd.github+json");
        header.put("Authorization", "Bearer " + token);
        header.put("X-GitHub-Api-Version", "X-GitHub-Api-Version");
        String execute = DefaultHttpUtil.execute(url, header);
        SingleCommitResponse singleCommitResponse = JSON.parseObject(execute,
            SingleCommitResponse.class);

        SingleCommitResponse.CommitFile[] commitFiles = singleCommitResponse.getCommitFiles();
        StringBuilder sb = new StringBuilder();
        for (SingleCommitResponse.CommitFile commitFile : commitFiles){
            sb.append("待评审文件名称: ").append(commitFile.getFilename()).append("\n");
            sb.append("变更文件代码: ").append(commitFile.getPatch()).append("\n");
        }
        return sb.toString();
    }
}
