package hao.wen.tao.sdk.infrastructure.env;

import hao.wen.tao.sdk.infrastructure.env.serivce.CodeReivewConfigPropertiesFacade;
import hao.wen.tao.sdk.infrastructure.env.serivce.CodeReviewProprtySource;

public class CodeReivewPropertiesSourceFactory {

    public static CodeReviewProprtySource getDefault() {
        CodeReivewConfigPropertiesFacade codeReivewConfigPropertiesFacade = new CodeReivewConfigPropertiesFacade();
        return codeReivewConfigPropertiesFacade.getCodeReviewProprtySource();
    }
}
