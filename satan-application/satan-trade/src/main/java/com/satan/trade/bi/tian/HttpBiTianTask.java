package com.satan.trade.bi.tian;

import com.alibaba.fastjson.JSONObject;
import com.satan.trade.common.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by h
 * on 2018-08-13 13:06.
 *
 * @author h
 */
@Slf4j
public class HttpBiTianTask implements Callable<JSONObject> {

    private String url;

    private String params;

    private List<Header> headers;

    private BigDecimal price;

    private BigDecimal amount;

    public HttpBiTianTask(String url, String params, List<Header> headers, BigDecimal price, BigDecimal amount) {
        this.url = url;
        this.params = params;
        this.headers = headers;
        this.price = price;
        this.amount = amount;
    }

    @Override
    public JSONObject call() {
        JSONObject jsonResult = new JSONObject();
        jsonResult.put("success", false);
        jsonResult.put("price", this.price);
        jsonResult.put("amount", this.amount);
        try {
            String result = HttpClientUtil.getInstance().postFormRequest(url, params, headers);
            log.info(result);

            if (result.contains("sendorder成功")) {
                jsonResult.put("success", true);
            }
            return jsonResult;
        } catch (Exception e) {
            log.error("params's value : {}", params);
            return jsonResult;
        }
    }
}