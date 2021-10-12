package org.warai.paramcheck.constant;

import org.springframework.util.Assert;
import org.warai.paramcheck.annotation.ParamCheck;
import org.warai.paramcheck.validator.*;


import java.lang.annotation.Annotation;

/**
 * @Auther: わらい
 * @Time: 2021/9/27 17:55
 */
public enum Validator {

    MaxLength(MaxLengthValidator.class),
    MaxSize(MaxSizeValidator.class),
    MaxValue(MaxValueValidator.class),
    NonNull(NonNullValidator.class),
    MaxElementLength(MaxElementLengthValidator.class),
    Past(PastValidator.class),
    Future(FutureValidator.class),
    Pattern(PatternValidator.class),
    Length(LengthValidator.class),
    ElementLength(ElementLengthValidator.class),
    MinLength(MinLengthValidator.class),
    MinValue(MinValueValidator.class),
    ;

    public Class<? extends ParamCheckValidator> cls;

    Validator(Class<? extends ParamCheckValidator> cls) {
        this.cls = cls;
    }

    public static <T extends Annotation> ParamCheckValidator get(ParamCheck paramCheck, T annotation) {
        ParamCheckValidator<T> paramCheckValidator = null;
        for (Validator value : values()) {
            if (value.name().equals(annotation.annotationType().getSimpleName())) {
                try {
                    paramCheckValidator = value.cls.newInstance().set(paramCheck, annotation);
                    break;
                } catch (Exception ignore) {
                }
            }
        }
        Assert.notNull(paramCheckValidator, "参数校验 validator 加载失败");
        return paramCheckValidator;
    }

}
