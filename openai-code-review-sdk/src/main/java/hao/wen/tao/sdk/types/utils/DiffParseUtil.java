package hao.wen.tao.sdk.types.utils;

public class DiffParseUtil
{

    /**
     * 接受文件变更字符串,解析出来索引位置 目前使用最后一个
     * @param fileDiff
     * @return
     */
    public static int parseLastDiffPosition(String fileDiff)
    {
        String[] split = fileDiff.split("\n");
        return split.length - 1;
    }
}
