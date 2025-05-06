package hao.wen.tao.sdk.infrastructure.llmmodel.common.text;

public class SystemMessageText implements ChatMessageText
{

    private final String text;

    public SystemMessageText(String text)
    {
        this.text = text;
    }

    @Override
    public ChatMessageTextType type()
    {
        return ChatMessageTextType.SYSTEM;
    }

    @Override
    public String text()
    {
        return text;
    }
}
