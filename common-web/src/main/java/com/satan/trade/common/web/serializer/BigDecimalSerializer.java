package com.satan.trade.common.web.serializer;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;

/**
 * Created by huangpin on 17/3/16.
 */
public class BigDecimalSerializer implements ObjectSerializer {
    @Override
    public void write(JSONSerializer jsonSerializer, Object o, Object o1, Type type, int i) throws IOException {
        if (o != null && o instanceof BigDecimal) {
            BigDecimal value = (BigDecimal) o;
            jsonSerializer.write(value.setScale(2, BigDecimal.ROUND_HALF_UP));
        }
    }
}
