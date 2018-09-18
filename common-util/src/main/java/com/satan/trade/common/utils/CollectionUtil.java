package com.satan.trade.common.utils;

import java.util.Collection;
import java.util.Map;

/**
 * Created by huangpin on 17/3/10.
 */
public class CollectionUtil {
    public static boolean isEmpty(Collection list) {
        return !isNotEmpty(list);
    }

    public static boolean isEmpty(Map map) {
        return !isNotEmpty(map);
    }

    public static boolean isNotEmpty(Map map) {
        return map != null && !map.isEmpty();
    }

    public static boolean isNotEmpty(Collection list) {
        return list != null && !list.isEmpty();
    }
}
