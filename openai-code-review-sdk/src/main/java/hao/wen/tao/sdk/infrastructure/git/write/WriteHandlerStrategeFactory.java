package hao.wen.tao.sdk.infrastructure.git.write;

import hao.wen.tao.sdk.infrastructure.git.write.impl.CommitWriteHandlerStrategy;
import hao.wen.tao.sdk.infrastructure.git.write.impl.RemoteWriteHandlerStrategy;

import java.util.HashMap;
import java.util.Map;


public class WriteHandlerStrategeFactory
{

    private static Map<String, IWriteHandlerStrategy> handlerStrategyMap = new HashMap<String, IWriteHandlerStrategy>();

    static {
        handlerStrategyMap.put("comment", new CommitWriteHandlerStrategy());
        handlerStrategyMap.put("remote", new RemoteWriteHandlerStrategy());
    }

    public static IWriteHandlerStrategy getWriteHandlerStrategy(String writeHandlerType)
    {
        return handlerStrategyMap.get(writeHandlerType);
    }


}
