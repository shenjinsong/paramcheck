package org.warai.paramcheck.annotation;

import org.warai.paramcheck.constant.ErrorMessage;
import java.lang.annotation.*;

/**
 * @author 大叔
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ParamCheck {

    String[] value() default {};

    Class[] classes() default {};

    String errorCode() default "400154";

    String msg() default ErrorMessage.PARAM_ERROR;

    int httpCode() default 200;

    String[] groups() default {};

}
