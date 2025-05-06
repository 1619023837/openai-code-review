package hao.wen.tao.sdk.infrastructure.llmmodel.common.text;

public enum ChatMessageTextType
{
    SYSTEM(SystemMessageText.class),

    USER(UserMessageText.class),

    AI(AIMessageText.class),

    ;
    private final Class<? extends ChatMessageText> messageClass;

    ChatMessageTextType(Class<? extends ChatMessageText> messageClass)
    {
        this.messageClass = messageClass;
    }

}
