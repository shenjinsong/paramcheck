package org.warai.paramcheck.validator;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.warai.paramcheck.annotation.ParamCheck;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.util.*;

/**
 * @Auther: わらい
 * @Time: 2021/9/26 10:53
 */
public class ParamCheckValidator<T extends Annotation> {
    /**
     * 子类重写规范项：
     * 1、重写invalid()默认返回 true
     * 2、if/else 分支中返回 true
     * 3、优先校验排序 分组判断 -> 根据参数做非空判断 -> 自定义校验
     */

    private ParamCheck paramCheck;
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


    public ParamCheckValidator<T> set(ParamCheck paramCheck, T annotation) {
        this.paramCheck = paramCheck;
        this.annotation = annotation;
        return this;
    }


    protected boolean needCheck(String[] annoGroups) {
        // 1、无分组校验（默认需要校验）
        // 2、有分组校验且为同一组
        String[] groups = paramCheck.groups();
        if (groups.length == 0 && annoGroups.length == 0) {
            return true;
        }


        List<String> list1 = new ArrayList<>(Arrays.asList(groups));
        List<String> list2 = new ArrayList<>(Arrays.asList(annoGroups));
        list1.retainAll(list2);

        return list1.size() > 0;
    }

}
