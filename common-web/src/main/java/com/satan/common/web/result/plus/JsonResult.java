package com.satan.common.web.result.plus;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.*;
import com.satan.common.exception.IErrorCode;
import com.satan.common.web.result.Result;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by huangpin on 17/7/25.
 */
public class JsonResult extends Result<JSONObject> {
    private JsonResult(IErrorCode iErrorCode, boolean success, JSONObject data) {
        this(iErrorCode.getCode(), iErrorCode.getMessage(), success, data);
    }

    private JsonResult(String code, String message, boolean success, JSONObject data) {
        super.setCode(code);
        super.setMessage(message);
        super.setSuccess(success);
        super.setData(data);
    }

    public JsonResult put(String key, Object value) {
        if (super.getData() == null) {
            super.setData(new JSONObject());
        }
        super.getData().put(key, value);
        return this;
    }

    public static JsonResult ok(JSONObject object) {
        return new JsonResult(IErrorCode.OK, true, object);
    }

    public static JsonResult ok(Object object) {
        return ok(JSON.parseObject(toJSONString(object, null, null, null)));
    }

    public String buildJsonString() {
        return JSON.toJSONString(this);
    }

    public static String toJSONString(Object object, SerializeConfig config, SerializeFilter[] filters,
                                      SerializerFeature[] features) {
        SerializeWriter out = new SerializeWriter();

        try {
            // SerializeConfig可以理解为一个Map<Type,ObjectSerializer>,
            // 内部保存着类型与对应的对象序列化器之间的映射关系
            if (config == null) {
                config = SerializeConfig.getGlobalInstance();
                config.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
                //        SerializeConfig.getGlobalInstance()
//                .propertyNamingStrategy = PropertyNamingStrategy.PascalCase;
            }
            // 核心序列化器，主要负责调用SerializeConfig根据value选择合适的对象序列化器
            // 内部还保存着输出流对象，以及各种过滤器
            JSONSerializer serializer = new JSONSerializer(out, config);
            // 所有特性最终会合成为一个int值，保存在输出流对象中
            if (features != null)
                for (SerializerFeature feature : features) {
                    serializer.config(feature, true);
                }

            if (filters != null && filters.length > 0) {
                setFilter(serializer, filters);
            }
            // 序列化过程的入口方法
            serializer.write(object);

            return out.toString();
        } finally {
            out.close();
        }
    }

    private static void setFilter(JSONSerializer serializer, SerializeFilter... filters) {
        for (SerializeFilter filter : filters) {
            setFilter(serializer, filter);
        }
    }

    private static void setFilter(JSONSerializer serializer, SerializeFilter filter) {
        if (filter == null) {
            return;
        }

        if (filter instanceof PropertyPreFilter) {
            serializer.getPropertyPreFilters().add((PropertyPreFilter) filter);
        }

        if (filter instanceof NameFilter) {
            serializer.getNameFilters().add((NameFilter) filter);
        }

        if (filter instanceof ValueFilter) {
            serializer.getValueFilters().add((ValueFilter) filter);
        }

        if (filter instanceof PropertyFilter) {
            serializer.getPropertyFilters().add((PropertyFilter) filter);
        }

        if (filter instanceof BeforeFilter) {
            serializer.getBeforeFilters().add((BeforeFilter) filter);
        }

        if (filter instanceof AfterFilter) {
            serializer.getAfterFilters().add((AfterFilter) filter);
        }

        if (filter instanceof LabelFilter) {
            serializer.getLabelFilters().add((LabelFilter) filter);
        }
    }

    public static void main(String[] args) {

        TestDTO testDTO = TestDTO.builder()
                .name("测试")
                .idCard("411425199211137216")
                .time(new Date())
                .one(new BigDecimal("11.11111"))
                .two(new BigDecimal("22.22222"))
                .build();
        TestDTO.TestSubDTO subDTO = testDTO.new TestSubDTO();
        subDTO.setName("测试");
        subDTO.setIdCard("411425199211137216");
        subDTO.setTime(new Date());
        subDTO.setOne(new BigDecimal("11.11111"));
        subDTO.setTwo(new BigDecimal("22.22222"));
        testDTO.setSubDTO(subDTO);
        System.out.println(JsonResult.ok(testDTO).buildJsonString());
    }
}
