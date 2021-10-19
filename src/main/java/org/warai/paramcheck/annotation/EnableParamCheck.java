package org.warai.paramcheck.annotation;

import org.springframework.context.annotation.Import;
import org.warai.paramcheck.ParamCheckIntercept;
import org.warai.paramcheck.config.ParamCheckFilter;
import org.warai.paramcheck.config.ParamCheckWebConfig;

import java.lang.annotation.*;

/**
 * @Auther: わらい
 * @Time: 2021/10/19 14:21
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({ParamCheckFilter.class, ParamCheckWebConfig.class, ParamCheckIntercept.SpringUtil.class})
@Documented
public @interface EnableParamCheck {
}
