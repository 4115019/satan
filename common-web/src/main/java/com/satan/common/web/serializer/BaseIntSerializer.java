package com.satan.common.web.serializer;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.satan.common.enums.BaseIntEnum;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by huangpin on 17/3/16.
 */
public class BaseIntSerializer implements ObjectSerializer {
    @Override
    public void write(JSONSerializer jsonSerializer, Object o, Object o1, Type type, int i) throws IOException {
        if (o != null && o instanceof BaseIntEnum) {
            BaseIntEnum baseIntEnum = (BaseIntEnum) o;
            jsonSerializer.write(baseIntEnum.getValue());
        }
    }
}
