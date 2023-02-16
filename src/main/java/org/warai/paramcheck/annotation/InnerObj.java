package org.warai.paramcheck.annotation;

import java.lang.annotation.*;

/**
 * @Auther: わらい
 * @Time: 2021/9/30 10:41
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface InnerObj {

    Class value();
}
