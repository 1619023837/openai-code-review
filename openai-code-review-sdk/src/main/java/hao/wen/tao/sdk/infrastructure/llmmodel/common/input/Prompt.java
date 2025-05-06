package hao.wen.tao.sdk.infrastructure.llmmodel.common.input;

public class Prompt
{

    private final String text;

    public Prompt(String text)
    {
        this.text = text;
    }

    public String getText()
    {
        return text;
    }

    public static Prompt from(String text){
        return new Prompt(text);
    }
}
