package com.satan.common.fastjson;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ValueFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by huangpin on 17/4/1.
 */
@Slf4j
public class BigDecimalFilter implements ValueFilter {
    @Override
    public Object process(Object o, String s, Object o1) {

        if(null != o1 && o1 instanceof BigDecimal) {
            try {
//                JSONField jsonField = o.getClass().getDeclaredField(s).getAnnotation(JSONField.class);
//                if (jsonField == null || StringUtils.isEmpty(jsonField.format())) {
                    return new BigDecimal(o1.toString()).setScale(2, RoundingMode.HALF_UP);
//                }
            } catch (Exception e) {
                log.error("时间字段错误解析" ,e);
            }
        }
        return o1;
    }
}
