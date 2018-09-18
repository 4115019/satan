package com.satan.trade.abcc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.satan.trade.abcc.model.Orders;
import com.satan.trade.common.utils.HttpClientUtil;
import com.satan.trade.common.utils.RegexpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by h
 * on 2018-09-10 10:50.
 *
 * @author h
 */
@Slf4j
public class AbccUtil {

    public static void cancelTrade(List<Header> headerList) {
        try {
            log.info("撤销所有订单: {}", HttpClientUtil.getInstance().postFormRequest("https://abcc.com/markets/clear_all_waiting_orders", null, headerList));
        } catch (Exception e) {
            log.error("撤单失败！！！");
            e.printStackTrace();
        }
    }

    public static JSONObject getAccount(String pair, List<Header> headerList) throws Exception {
        return JSON.parseObject(
                RegexpUtil.getRegexp("(?<=gon.accounts=).*(?=;gon.vouchers)",
                        HttpClientUtil.getInstance().sendGetRequestToString("https://abcc.com/markets/" + pair, headerList))
        );
    }

    public static String getAskParams(BigDecimal price, BigDecimal amount) {
        return "utf8=%E2%9C%93&order_ask%5Bord_type%5D=limit&order_ask%5Bprice%5D="
                + price
                + "&order_ask%5Borigin_volume%5D=" + amount
                + "&order_ask%5Bpercent%5D=0";
    }

    public static String getBidParams(BigDecimal price, BigDecimal amount) {
        return "utf8=%E2%9C%93&order_bid%5Bord_type%5D=limit&order_bid%5Bprice%5D="
                + price
                + "&order_bid%5Borigin_volume%5D=" + amount
                + "&order_ask%5Bpercent%5D=0";
    }

    public static Orders getTradingOrders(List<Header> headers) {
        try {
            return JSON.parseObject(HttpClientUtil.getInstance().sendGetRequestToString("https://abcc.com/history/orders.json?state=wait&per_page=100", headers), Orders.class);
        } catch (Exception e) {
            log.error("撤单失败！！！");
            e.printStackTrace();
        }

        return null;
    }

}