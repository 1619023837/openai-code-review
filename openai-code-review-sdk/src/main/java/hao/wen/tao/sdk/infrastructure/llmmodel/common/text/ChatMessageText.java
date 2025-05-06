package hao.wen.tao.sdk.infrastructure.llmmodel.common.text;

public interface ChatMessageText
{

    /**
     * 标识聊天信息的枚举类
     */

    ChatMessageTextType type();
    /**
     * 提示词文本对象
     * @return
     */
    String text();
}
