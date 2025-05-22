package hao.wen.tao.sdk.infrastructure.llmmodel.common.output;

public class Response<T>
{
    private final  T data;

    public Response(T data)
    {
        this.data = data;
    }

    public T content(){
        return data;
    }

    public static <T> Response<T> from(T data){
        return new Response<T>(data);
    }

}

