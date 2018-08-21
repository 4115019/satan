package com.satan.common.utils;

/**
 * Created by huangpin on 17/3/10.
 */
public class UnderlineUtil {
    /**
     * 头峰式转下划线
     * @param name
     * @return
     */
    public static String to_(String name){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < name.length() ; ++i){
            char c = name.charAt(i);
            if (c >= 'A' && c <= 'Z'){
                stringBuilder.append("_").append(Character.toLowerCase(c));
            }else {
                stringBuilder.append(c);
            }
        }
        return stringBuilder.toString();
    }
}
