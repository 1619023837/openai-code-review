package hao.wen.tao.sdk.infrastructure.quatify.listener.impl;

import hao.wen.tao.sdk.infrastructure.quatify.listener.service.CodeReviewResultListener;
import hao.wen.tao.sdk.infrastructure.quatify.model.CodeReviewResultContext;

import java.util.ArrayList;
import java.util.List;

/**
 * 监听器工厂类 维护所有的监听器对象 已经完成触发的逻辑
 */
public class CodeReviewResultListenerFactory {


    private static List<CodeReviewResultListener> codeReviewResultListeners = new ArrayList();
    static {
        codeReviewResultListeners.add(new DefaultWebhookCodeReviewListener());
    }

    /**
     * 触发的逻辑
     */
    public static void  triggerListenerOnComplete(CodeReviewResultContext codeReviewResultContext){
        //执行触发监听器对象
        codeReviewResultListeners.forEach(listener -> listener.onComplete(codeReviewResultContext));
    }



}
