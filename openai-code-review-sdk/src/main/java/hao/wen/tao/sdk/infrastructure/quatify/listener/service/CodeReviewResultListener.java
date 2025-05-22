package hao.wen.tao.sdk.infrastructure.quatify.listener.service;

import hao.wen.tao.sdk.infrastructure.quatify.model.CodeReviewResultContext;

/**
 * 代码评审结果监听器
 */
public interface CodeReviewResultListener {


    /**
     * 代码评审的时候调用
     * @param codeReviewResultContext
     */
    void onComplete(CodeReviewResultContext codeReviewResultContext);

    //onStart方法、onError方法 生命周期,可以获取到耗时信息、开始的时候监听业务处理、出现了错误的的处理等。
}
