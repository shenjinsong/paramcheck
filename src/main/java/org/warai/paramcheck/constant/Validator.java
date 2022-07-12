package org.warai.paramcheck.constant;

import org.springframework.util.Assert;
import org.warai.paramcheck.annotation.*;
import org.warai.paramcheck.validator.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: わらい
 * @Time: 2021/9/27 17:55
 */
public enum Validator {

    NonNull(NonNull.class, NonNullValidator.class),
    MaxLength(MaxLength.class, MaxLengthValidator.class),
    MaxSize(MaxSize.class, MaxSizeValidator.class),
    MinValue(MinValue.class, MinValueValidator.class),
    MaxValue(MaxValue.class, MaxValueValidator.class),
    Length(Length.class, LengthValidator.class),
    MinLength(MinLength.class, MinLengthValidator.class),
    ValueSet(ValueSet.class, ValueSetValidator.class),
    Past(Past.class, PastValidator.class),
    Future(Future.class, FutureValidator.class),
    ElementLength(ElementLength.class, ElementLengthValidator.class),
    Pattern(Pattern.class, PatternValidator.class),
    MaxElementLength(MaxElementLength.class, MaxElementLengthValidator.class),
    Numeric(Numeric.class, NumericValidator.class),
    ;

    public Class<? extends Annotation> annotation;
    public Class<? extends ParamCheckValidator> validator;

    Validator(Class<? extends Annotation> annotation, Class<? extends ParamCheckValidator> validator) {
        this.annotation = annotation;
        this.validator = validator;
    }

    public static <T extends Annotation> ParamCheckValidator get(T annotation) {
        ParamCheckValidator<T> paramCheckValidator = null;
        for (Validator value : values()) {
            if (value.annotation.equals(annotation.annotationType())) {
                try {
                    paramCheckValidator = value.validator.newInstance().set(annotation);
                    break;
                } catch (Exception ignore) {
                }
            }
        }
        Assert.notNull(paramCheckValidator, "参数校验 validator 加载失败");
        return paramCheckValidator;
    }

    public static Map<Class<? extends Annotation>, Method> initGroupsMethod() throws NoSuchMethodException {
        Map<Class<? extends Annotation>, Method> methods = new HashMap<>();
        for (Validator value : values()) {
            Method method = value.annotation.getMethod("groups");
            methods.put(value.annotation, method);
        }
        return methods;
    }

}
