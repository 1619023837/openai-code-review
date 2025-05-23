package hao.wen.tao.sdk.infrastructure.quatify.listener.impl;

import com.sun.org.apache.bcel.internal.classfile.Code;
import hao.wen.tao.sdk.infrastructure.env.CodeReivewPropertiesSourceFactory;
import hao.wen.tao.sdk.infrastructure.env.serivce.CodeReviewProprtySource;
import hao.wen.tao.sdk.infrastructure.quatify.listener.service.CodeReviewResultListener;
import hao.wen.tao.sdk.infrastructure.quatify.model.CodeReviewQuantityResult;
import hao.wen.tao.sdk.infrastructure.quatify.model.CodeReviewResultContext;

/**
 * 默认的webhook 方式进行通知的
 *
 */
public class DefaultWebhookCodeReviewListener implements CodeReviewResultListener {

    @Override
    public void onComplete(CodeReviewResultContext codeReviewResultContext) {
//        CodeRe

        CodeReviewProprtySource codeReviewProprtySource = CodeReivewPropertiesSourceFactory.getDefault();
        String webHookUrl = codeReviewProprtySource.getProperty("WEB_HOOK_URL");
        if (webHookUrl != null && webHookUrl.trim().length() > 0) {
            //发送http请求 将当前代码发送出去, 服务端进行上报
            // http请求
            CodeReviewQuantityResult codeReviewQuantityResult = new CodeReviewQuantityResult();
        }
    }
}
