package hao.wen.tao.sdk.infrastructure.llmmodel.common.text;

public class UserMessageText implements ChatMessageText
{
    private final String text;

    public UserMessageText(String text)
    {
        this.text = text;
    }

    @Override
    public ChatMessageTextType type()
    {
        return ChatMessageTextType.USER;
    }

    @Override
    public String text()
    {
        return text;
    }
}
