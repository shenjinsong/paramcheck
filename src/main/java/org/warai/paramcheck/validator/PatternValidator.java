package org.warai.paramcheck.validator;

import org.warai.paramcheck.util.ObjectUtils;
import org.warai.paramcheck.annotation.Pattern;
import org.warai.paramcheck.constant.ErrorMessage;


import java.util.regex.Matcher;

/**
 * @Auther: わらい
 * @Time: 2021/9/29 12:16
 */
public class PatternValidator extends ParamCheckValidator<Pattern>{

    private static boolean matches(String regex, CharSequence input) {
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(regex);
        Matcher m = p.matcher(input);
        return m.matches();
    }

    @Override
    public boolean invalid(Object value) {

        // 根据参数做非空判断
        if (ObjectUtils.isEmpty(value)){
            if (annotation.nullable()){
                return false;
            }else {
                super.setFailMsg(ErrorMessage.PARAM_CANNOT_EMPTY);
                return true;
            }
        }

        super.setFailMsg(ErrorMessage.FORMAT_NOT_MATCH);
        return !matches(annotation.value(), value.toString());
    }
}
