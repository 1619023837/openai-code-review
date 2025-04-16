package hao.wen.tao.sdk.infrastructure.git;


import hao.wen.tao.sdk.types.utils.RandomStringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;



public class GitCommand
{

    //提交日志仓库的uri地址
    private final String githubReviewLogUri;

    private final String githubToken;

    //项目
    private final String project;
    //分支
    private final String branch;
    //作者
    private final String author;

    private final String message;

    public GitCommand(String githubReviewLogUri, String githubToken, String project, String branch,
                      String author, String message)
    {
        this.githubReviewLogUri = githubReviewLogUri;
        this.githubToken = githubToken;
        this.project = project;
        this.branch = branch;
        this.author = author;
        this.message = message;
    }

    /**
     *
     * 获取两个提交之间的差异
     * 此方法通过git命令工具类,获取昂起分支上最新提交的HEAD 与上一次提交之间的差异
     * 差异结果以文字的形式返回
     * 处理流程如下
     *      创建一个 ProcessBuilder 执行 git log -1  表示只获取最新的一个提交记录。"--pretty=format:%H"：自定义输出的格式，%H 表示完整的提交哈希值（commit hash）。
     *      命令 获取最新的hashcode
     *      设置进程启动时的工作目录。在这里，new File(".") 表示当前目录（. 指的是运行 Java 程序的当前工作目录）。
     *      启动进程 并输出获取最新的提交的hashcode
     *      创建一个新的ProcessBuilder 实例来执行git diff命令 比较最新提交与它之前的一次提交
     *      启动程序 并输出流获取差异内容
     *      如果git diff 失败 （即进程退出码非0) 则抛出异常
     *      获取差异的内容字符串
     *
     */

    private final static Logger logger = LoggerFactory.getLogger(GitCommand.class);


    //
    //获取提交操作 检出功能强的
    public String diff() throws IOException, InterruptedException{
        ProcessBuilder logProcessBuilder = new ProcessBuilder("git", "log", "-1", "--pretty=format:%H");
        logProcessBuilder.directory(new File("."));
        Process logProcess = logProcessBuilder.start();

        BufferedReader logReader = new BufferedReader(new InputStreamReader(logProcess.getInputStream()));
        //获取最后一次提交的hashcode
        String latestCommitHash = logReader.readLine();

        logReader.close();
        //等待进程完成
        logProcess.waitFor();

        //最后一次提交的hashcode 到最后一次提交的hashcode的上一次
        ProcessBuilder diffProcessBuilder = new ProcessBuilder("git", "diff", latestCommitHash +"^" ,latestCommitHash);
        diffProcessBuilder.directory(new File("."));
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

    /**
     *
     *
     * 这里面我们可以学到一个命令如下：
     *
     * git log -1 --pretty=format:%H
     * 这个命令的含义如下：
     *
     * 命令 git log -1 --pretty=format:%H 是Git版本控制系统中用来获取最近一次提交的哈希值的命令。下面是各个部分的含义：
     *
     * git log: 这是Git命令行工具中的一个基本命令，用于显示提交历史记录。
     * -1: 这是一个参数，用于指定要显示的提交历史记录的条目数量。在这里，-1 意味着只显示最近的一次提交。
     * --pretty=format:: 这是一个选项，用于控制提交信息的格式。format: 后面跟着的是一个格式化字符串。
     * %H: 这是一个格式化占位符，表示提交的哈希值。
     * 所以，这个命令的整体作用是：
     *
     * 使用 git log 获取提交历史。
     * -1 限制输出只显示一条记录，即最新的一次提交。
     * --pretty=format:%H 指示Git使用 %H 占位符来格式化输出，只输出每个提交的哈希值。
     * 在执行这个命令后，你将得到一个唯一的字符串，这个字符串代表Git仓库中最近一次提交的标识。哈希值通常是一长串字母和数字的组合，例如 e4759c3dfb2f3fd299d9b31a9e0a605f0b1b6d95。这个哈希值在Git中用于唯一标识每个提交。
     *
     * 这里面我们可以学到第二个命令如下：
     *
     * git diff e4759c3dfb2f3fd299d9b31a9e0a605f0b1b6d95^ e4759c3dfb2f3fd299d9b31a9e0a605f0b1b6d95
     * 这个含义如下：
     *
     * 命令 git diff e4759c3dfb2f3fd299d9b31a9e0a605f0b1b6d95^ e4759c3dfb2f3fd299d9b31a9e0a605f0b1b6d95 是用来比较两个提交之间的差异的Git命令。以下是命令中各个部分的含义：
     *
     * git diff: 这是Git的一个命令，用于显示两个或多个提交、分支或文件之间的差异。
     * e4759c3dfb2f3fd299d9b31a9e0a605f0b1b6d95: 这是第一个提交的哈希值，表示你想要查看其差异的提交。
     * ^: 在Git中，^ 符号用于引用当前分支的最近一次提交。如果前面有数字，它会引用该分支的第N个最近提交。在这个命令中，^ 指的是最新提交。
     * e4759c3dfb2f3fd299d9b31a9e0a605f0b1b6d95: 这是第二个提交的哈希值，也是你想要查看其差异的提交。
     * 结合以上信息，这个命令的作用如下：
     *
     * 首先比较第一个提交 e4759c3dfb2f3fd299d9b31a9e0a605f0b1b6d95 和它的上一个提交（由 ^ 表示）之间的差异。
     * 因为 e4759c3dfb2f3fd299d9b31a9e0a605f0b1b6d95 本身就是最新的提交，所以这个命令实际上是在比较最新的提交和它的前一个提交之间的差异。
     * 这个命令通常用于查看最近一次提交引入了哪些更改，因为它会显示从上一个提交到当前提交的所有变更。如果命令行工具中存在名为 e4759c3dfb2f3fd299d9b31a9e0a605f0b1b6d95 的提交，那么命令会显示从该提交的前一个提交到该提交的所有差异。如果该提交是当前HEAD（即最新提交），则显示从它之前的一次提交到当前提交的差异。
     */



    //让日志写数据
    public String commitAndPush(String recommend) throws Exception {
        //1、克隆仓库2、 设置 拉取url 3、 设置文件夹
        //new File("repo"))  new File("repo"))  必须是一个动态 这样才不会报错。

        File file = new File("repo");
        System.out.println(file.getAbsolutePath());
        Git git = Git.cloneRepository().setURI(githubReviewLogUri + ".git").setDirectory(
                file).setCredentialsProvider(
            new UsernamePasswordCredentialsProvider(githubToken, ""))
            .call();
        //创建分支
        String dateFormatName = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        //文件夹 repo/下的dateFormatName 文件夹名称
        File dirFolder = new File("repo/" + dateFormatName);
        if (!dirFolder.exists()) {
            dirFolder.mkdirs();
        }

        //工程名称
        String fileName = project + "-" + branch + "-" + "haowentao" + System.currentTimeMillis() + "-" + RandomStringUtils.getRandomString(4) + ".md";

        File newFile = new File(dirFolder, fileName);
        try( FileWriter fileWriter = new FileWriter(newFile);)
        {
           fileWriter.write(recommend);
        }

        //提交内容 addFilepattern 暂存的文件路径  文件夹/文件名
        git.add().addFilepattern(dateFormatName +"/" + fileName).call();
        git.commit().setMessage("add code review new file" + fileName).call();
        git.push().setCredentialsProvider(new UsernamePasswordCredentialsProvider(githubToken, "")).call();

        logger.info("openai-code-review-log commitAndPush success: {}",fileName);

//        不同仓库拼接方式不一样
        return githubReviewLogUri + "/blob/master/" + dirFolder + "/" + fileName;
    }

    public String getProject()
    {
        return project;
    }

    public String getBranch()
    {
        return branch;
    }

    public String getAuthor()
    {
        return author;
    }

    public String getMessage()
    {
        return message;
    }
}


