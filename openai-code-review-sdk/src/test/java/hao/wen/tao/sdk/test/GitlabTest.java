package hao.wen.tao.sdk.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;


// 一般 都是下拉取到本地  之后获取数据
public class GitlabTest
{

    public static void main(String[] args)
    {
        String gitLabUrl = "https://github.com/1619023837/openai-code-review.git"; // 替换为你的 GitLab 仓库 URL
        String localPath = "D:\\demo\\repo"; // 替换为你本地存储库的路径
        String branchName = "05"; // 替换为要拉取的分支名
        String personalAccessToken = "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"; // 替换为你的 Personal Access Token

        try {
            cloneRepository(gitLabUrl, localPath, personalAccessToken);
            String latestCommitHash = fetchLatestCommitHash(localPath, branchName);
            System.out.println("Latest Commit Hash: " + latestCommitHash);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void cloneRepository(String gitLabUrl, String localPath, String personalAccessToken) throws
        IOException, InterruptedException {
        // 构建带 Token 的克隆 URL
        String authGitLabUrl = gitLabUrl.replace("https://", "https://" + personalAccessToken + "@");

        ProcessBuilder processBuilder = new ProcessBuilder("git", "clone", authGitLabUrl, localPath);
        processBuilder.redirectErrorStream(true); // 合并错误流到输出流

        Process process = processBuilder.start();

        // 读取命令输出
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Git clone failed with exit code: " + exitCode);
        }
    }


    public static String fetchLatestCommitHash(String localPath, String branchName) throws IOException, InterruptedException {
        ProcessBuilder logProcessBuilder = new ProcessBuilder("git", "log", "-1", "--pretty=format:%H");
        logProcessBuilder.directory(new File("D:\\demo\\repo"));
        Process logProcess = logProcessBuilder.start();

        BufferedReader logReader = new BufferedReader(new InputStreamReader(logProcess.getInputStream()));
        //获取最后一次提交的hashcode
        String latestCommitHash = logReader.readLine();

        logReader.close();
        //等待进程完成
        logProcess.waitFor();
        //最后一次提交的hashcode 到最后一次提交的hashcode的上一次
        ProcessBuilder diffProcessBuilder = new ProcessBuilder("git", "diff", latestCommitHash +"^" ,latestCommitHash);
        diffProcessBuilder.directory(new File("D:\\demo\\repo"));
        Process diffProcess = diffProcessBuilder.start();

        StringBuilder diffCode = new StringBuilder();
        BufferedReader diffReader = new BufferedReader(new InputStreamReader(diffProcess.getInputStream()));
        String line;
        while ((line = diffReader.readLine()) != null) {
            diffCode.append(line).append("\n");
        }
        diffReader.close();

        //等待执行完成
        int exitCode = diffProcess.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Failed to get diff, exit code:" + exitCode);
        }

        return diffCode.toString();
    }
}
