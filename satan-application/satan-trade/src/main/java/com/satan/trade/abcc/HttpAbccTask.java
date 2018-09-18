package com.satan.trade.abcc;

import com.satan.trade.common.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by h
 * on 2018-08-13 13:06.
 *
 * @author h
 */
@Slf4j
public class HttpAbccTask implements Callable<Boolean> {

    private String url;

    private String params;

    private List<Header> headers;

    private String logPrefix;

    public HttpAbccTask(String url, String params, List<Header> headers, String logPrefix) {
        this.url = url;
        this.params = params;
        this.headers = headers;
        this.logPrefix = logPrefix;
    }

    @Override
    public Boolean call() {
        try {
            System.out.println(this.logPrefix + HttpClientUtil.getInstance().postFormRequest(url, params, headers));
            return true;
        } catch (Exception e) {
            System.out.println(params);
            System.out.println(this.logPrefix + "报错，自动跳过。");
            return false;
        }
    }
}