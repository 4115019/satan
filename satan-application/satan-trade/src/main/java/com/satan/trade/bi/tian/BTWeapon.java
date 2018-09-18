package com.satan.trade.bi.tian;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.satan.trade.abcc.AbccUtil;
import com.satan.trade.abcc.HttpAbccTask;
import com.satan.trade.common.utils.HttpClientUtil;
import com.satan.trade.utils.HeaderUtil;
import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIDeclaration;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by h
 * on 2018-09-18 10:28.
 *
 * @author h
 */
@Slf4j
public class BTWeapon {

    private static Integer PRICE_PRECISION = 2;
    private static Integer AMOUNT_PRECISION = 5;
    private static BigDecimal LEAST_TRADE_AMOUNT = new BigDecimal("0.00001");
    private static String KEY1 = "BTC";
    private static String KEY2 = "USDT";
    private static String PRICE_URL = "https://bitian.io/exchmarcket-web-biz/v1/exchmd/findDeepMktAndMarketLite";
    private static String ASK_URL = "";
    private static String BID_URL = "";

    public static void main(String[] args) throws Exception {

        final String headerStr = "";
        List<Header> headerList = HeaderUtil.getHeaders(headerStr);

        int i = 1;
        while (true) {
            log.info("第: {}次交易开始", i);

            /**
             * 获取账号余额
             */
//            String s = HttpClientUtil.getInstance().sendGetRequestToString("https://bitian.io/zh-cn/trade.html", null);
            BigDecimal account1 = BigDecimal.ZERO;
            BigDecimal account2 = BigDecimal.ZERO;

            /**
             * 获取交易价格
             */

            JSONObject prices = JSON.parseObject(
                    HttpClientUtil.getInstance().postFormRequest(PRICE_URL, String.format("mktCode=%s&instrumentID=%s/%s&precision=2&limit=2", KEY2, KEY1, KEY2), null)
            ).getJSONObject("data").getJSONObject("marketdata");

            BigDecimal ask1Price = prices.getJSONArray("askPrice").getBigDecimal(0);
            BigDecimal bid1Price = prices.getJSONArray("bidPrice").getBigDecimal(0);
            BigDecimal tradePrice = ask1Price.add(bid1Price).divide(new BigDecimal(2), PRICE_PRECISION, BigDecimal.ROUND_HALF_UP);

            BigDecimal account2TradeAmount = account2.divide(tradePrice, AMOUNT_PRECISION, BigDecimal.ROUND_DOWN);
            BigDecimal tradeAmount = account2TradeAmount.compareTo(account1) > 0 ? account1 : account2TradeAmount;

            BigDecimal balancePrice = account1.compareTo(account2TradeAmount) > 0 ? bid1Price : ask1Price;
            BigDecimal balanceAmount = account1.subtract(account2TradeAmount).abs().divide(new BigDecimal(2), AMOUNT_PRECISION, BigDecimal.ROUND_DOWN);

            ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);
            List<Future<Boolean>> resultList = new ArrayList<>();

            Boolean tradeSleep = false;
            Boolean balanceSleep = false;

            if (tradeAmount.compareTo(LEAST_TRADE_AMOUNT) > 0) {
                tradeSleep = true;
                log.info("卖1价格: {}，买1价格：{}，对冲交易={}：{}", ask1Price, bid1Price, tradePrice, tradeAmount);
                /**
                 * 对冲卖出
                 * 对冲买入
                 */
            }
            if (balanceAmount.compareTo(LEAST_TRADE_AMOUNT) > 0) {
                balanceSleep = true;
                log.info("平衡{}交易={}：{}", account1.compareTo(account2TradeAmount) > 0 ? "卖出" : "买入", balancePrice, balanceAmount);
            }

            for (Future<Boolean> result : resultList) {
                result.get();
            }

            if (tradeSleep) {
                Thread.sleep(3000);
            }

            if (balanceSleep) {
                Thread.sleep(6000);
            }

            if (!tradeSleep && balanceSleep) {
                Thread.sleep(16000);
            }

            log.info("第: {}次交易结束", i++);
        }
    }

}