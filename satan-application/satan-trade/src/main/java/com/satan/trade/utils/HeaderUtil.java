package com.satan.trade.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by h
 * on 2018-09-18 10:42.
 *
 * @author h
 */
@Slf4j
public class HeaderUtil {

    public static List<Header> getHeaders(String headers) {

        if (StringUtils.isEmpty(headers)) {
            return null;
        }
        String[] split = headers.split("\n");
        List<Header> headerList = new ArrayList<Header>();
        for (String header : split) {
            headerList.add(new BasicHeader(header.split(":")[0], header.split(":")[1]));
        }
        return headerList;
    }

}