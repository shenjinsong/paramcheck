package org.warai.paramcheck.validator;


import java.lang.annotation.Annotation;


/**
 * @Auther: わらい
 * @Time: 2021/9/26 10:53
 */
public class ParamCheckValidator<T extends Annotation> {
    /**
     * 子类重写规范项：
     * 1、重写invalid()默认返回 true
     * 2、if/else 分支中返回 true
     */
    protected T annotation;
    private String failMsg;

    public String getFailMsg() {
        return failMsg;
    }

    public void setFailMsg(String failMsg) {
        this.failMsg = failMsg;
    }

    public boolean invalid(Object value) {
        return true;
    }

    public boolean numberCheck(Object value){
        return false;
    }

    public ParamCheckValidator<T> set(T annotation) {
        this.annotation = annotation;
        return this;
    }


}
