package com.satan.trade.common.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import org.apache.commons.codec.binary.StringUtils;

/**
 * Created by huangpin on 17/3/13.
 */
public class JsonPath {
    public static void main(String[] args) {

        System.out.println(System.currentTimeMillis());
        JSONObject data = new JSONObject();
        JSONObject subData = new JSONObject();
        subData.put("姓名","hp");
        subData.put("version",1);
        data.put("数据1符号",subData);
        JSONArray subArray = new JSONArray();
        JSONObject array1 = new JSONObject();
        array1.put("name","array1");
        array1.put("sex","male");

        JSONObject array2 = new JSONObject();
        array2.put("name","array2");
        array2.put("sex","female");
//        subArray.add(array1);
//        subArray.add(array2);
        data.put("array",subArray);
        System.out.println(JSONPath.read(data.toJSONString(),"$.arra"));
        System.out.println(JSONPath.read(data.toJSONString(),"$.array"));
        System.out.println(JSONPath.read(data.toJSONString(),"$"));

    }
}
