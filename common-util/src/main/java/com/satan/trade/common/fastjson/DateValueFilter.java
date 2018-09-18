package com.satan.trade.common.fastjson;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.satan.trade.common.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * Created by huangpin on 17/3/16.
 */
@Slf4j
public class DateValueFilter implements ValueFilter{
    @Override
    public Object process(Object o, String s, Object o1) {
        if (o1 != null && o1 instanceof Date){
            try {
                JSONField jsonField = o.getClass().getDeclaredField(_toUpper(s)).getAnnotation(JSONField.class);
                if (jsonField != null && StringUtils.isNotEmpty(jsonField.format())) {
                    return DateUtil.getDate(jsonField.format(), (Date) o1);
                }else {
                    return DateUtil.getDate(DateUtil.DEFAULT_FORMAT, (Date) o1);
                }
            } catch (Exception e) {
                log.error("时间字段错误解析" ,e);
            }
        }
        return o1;
    }


    private String _toUpper(String s){
        if (StringUtils.isNotEmpty(s) && s.contains("_")){
            StringBuilder stringBuilder = new StringBuilder();
            for(int i = 0; i < s.length(); ++i){
                if (s.charAt(i) == '_'){
                    if (i + 1 < s.length()) {
                        stringBuilder.append(Character.toUpperCase(s.charAt(++i)));
                    }
                }else {
                    stringBuilder.append(s.charAt(i));
                }
            }
            return stringBuilder.toString();
        }
        return s;
    }

    public static void main(String[] args) {
        System.out.println(new DateValueFilter()._toUpper("create_time"));
    }
}
