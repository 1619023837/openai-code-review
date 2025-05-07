package hao.wen.tao.sdk.infrastructure.git;

import com.alibaba.fastjson2.JSON;
import hao.wen.tao.sdk.domain.BaseGitOperation;
import hao.wen.tao.sdk.infrastructure.git.dto.CommitCommentRequestDTO;
import hao.wen.tao.sdk.infrastructure.git.dto.SingleCommitResponse;
import hao.wen.tao.sdk.types.utils.DefaultHttpUtil;
import hao.wen.tao.sdk.types.utils.DiffParseUtil;
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

        SingleCommitResponse singleCommitResponse = getCommitResponse();

        SingleCommitResponse.CommitFile[] commitFiles = singleCommitResponse.getFiles();
        StringBuilder sb = new StringBuilder();
        for (SingleCommitResponse.CommitFile commitFile : commitFiles)
        {
            sb.append("待评审文件名称: ").append(commitFile.getFilename()).append("\n");
            sb.append("变更文件代码: ").append(commitFile.getPatch()).append("\n");
        }
        return sb.toString();
    }

    private SingleCommitResponse getCommitResponse()
        throws Exception
    {
        Map<String, String> header = new HashMap();
        header.put("Accept", "application/vnd.github+json");
        header.put("Authorization", "Bearer " + token);
        header.put("X-GitHub-Api-Version", "2022-11-28");
        String execute = DefaultHttpUtil.execute(url, header);
        logger.info("output" + execute);
        SingleCommitResponse singleCommitResponse = JSON.parseObject(execute,
            SingleCommitResponse.class);
        return singleCommitResponse;
    }

    @Override
    public String writeResult(String result)
        throws Exception
    {
        SingleCommitResponse commitResponse = getCommitResponse();
        SingleCommitResponse.CommitFile[] commitFiles = commitResponse.getFiles();
        for (SingleCommitResponse.CommitFile commitFile : commitFiles){
            String patch = commitFile.getPatch();
            int diffPosition = DiffParseUtil.parseLastDiffPosition(patch);
            CommitCommentRequestDTO commitCommentRequestDTO = new CommitCommentRequestDTO();
            commitCommentRequestDTO.setBody(result);
            commitCommentRequestDTO.setPosition(diffPosition);
            commitCommentRequestDTO.setPath(commitFile.getFilename());
            //发送http 请求
            writeCommitRequest(commitCommentRequestDTO);
            break;
        }
        return commitResponse.getHtml_url();
    }

    private String writeCommitRequest(CommitCommentRequestDTO commitCommentRequestDTO)
        throws Exception
    {
        Map<String, String> header = new HashMap();
        header.put("Accept", "application/vnd.github+json");
        header.put("Authorization", "Bearer " + token);
        header.put("X-GitHub-Api-Version", "2022-11-28");

        String execute = DefaultHttpUtil.executePostRequest(url +"/comments", header,commitCommentRequestDTO);
        logger.info("output" + execute);
        return execute;
    }
}


