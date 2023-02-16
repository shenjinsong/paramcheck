package org.warai.paramcheck.util;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
import java.util.*;

/**
 * @Auther: わらい
 * @Time: 2021/10/14 10:33
 */
public abstract class ObjectUtils {

    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        } else if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0 || Arrays.stream((Object[]) obj).anyMatch(ObjectUtils::isEmpty);
        } else if (obj instanceof CharSequence) {
            return StringUtils.isBlank((CharSequence) obj);
        } else if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty() || ((Collection) obj).stream().anyMatch(ObjectUtils::isEmpty);
        } else {
            return obj instanceof Map && ((Map) obj).isEmpty();
        }
    }
}
