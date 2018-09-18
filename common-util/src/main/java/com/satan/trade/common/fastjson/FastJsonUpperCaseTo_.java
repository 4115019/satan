package com.satan.trade.common.fastjson;

import com.alibaba.fastjson.serializer.NameFilter;
import com.satan.trade.common.utils.UnderlineUtil;

/**
 * Created by huangpin on 17/3/10.
 */
public class FastJsonUpperCaseTo_ implements NameFilter {
    @Override
    public String process(Object o, String s, Object o1) {
        return UnderlineUtil.to_(s);
    }
}
