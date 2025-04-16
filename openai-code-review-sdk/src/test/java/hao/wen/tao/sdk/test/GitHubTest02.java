package hao.wen.tao.sdk.test;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;


public class GitHubTest02
{

    public static void main(String[] args) {
        // 仓库的 HTTPS URL
        String localPath = "D:\\demo\\repo"; // 替换为你本地仓库的路径

        // GitLab 分支名称
        String branchName = "day05"; // 替换为要拉取的分支名

        // Personal Access Token (如果使用 HTTPS)
        String personalAccessToken = "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";

        try {
            pullLatestChanges(localPath, branchName, personalAccessToken);
            System.out.println("成功拉取最新更改！");
        } catch (IOException | GitAPIException e) {
            System.err.println("拉取最新更改时出错：");
            e.printStackTrace();
        }
    }



    //github 本地更新代码信息
    public static void pullLatestChanges(String localPath, String branchName, String personalAccessToken)
        throws IOException, GitAPIException {
        // 打开本地仓库
        try (Git git = Git.open(new File(localPath))) {

            // 检查当前分支
            String currentBranch = git.getRepository().getBranch();
            System.out.println("当前分支: " + currentBranch);

            // 如果需要切换分支
            if (!currentBranch.equals(branchName)) {
                System.out.println("正在切换到分支 " + branchName + "...");
                git.checkout().setName(branchName).call();
            }

            // 执行拉取操作
            PullCommand pull = git.pull();
            if (git.getRepository().getConfig().getString("remote", "origin", "url").startsWith("https")) {
                // 使用 HTTPS，设置认证信息
                pull.setCredentialsProvider(new UsernamePasswordCredentialsProvider(personalAccessToken, ""));
            }
            // 如果使用 SSH，则无需设置认证信息

            pull.call(); // 执行拉取

            System.out.println("已拉取分支 " + branchName + " 的最新更改。");
        }
    }

}
