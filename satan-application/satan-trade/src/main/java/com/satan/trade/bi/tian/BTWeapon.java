package com.satan.trade.bi.tian;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.satan.trade.common.utils.HttpClientUtil;
import com.satan.trade.utils.HeaderUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by h
 * on 2018-09-18 10:28.
 *
 * @author h
 */
@Slf4j
public class BTWeapon {

    private static Integer AMOUNT_PRECISION = 5;
    private static BigDecimal LEAST_TRADE_AMOUNT = new BigDecimal("0.002");
    private static BigDecimal LEAST_SUB_AMOUNT = new BigDecimal("0.0002");
    private static String KEY1 = "BTC";
    private static String KEY2 = "USDT";
    private static String ACCOUNT_URL = "https://bitian.io/exchmarcket-web-biz/v1/trade/recentorderAndPersonAlasset";
    private static String PRICE_URL = "https://bitian.io/exchmarcket-web-biz/v1/exchmd/findDeepMktAndMarketLite";
    private static String TRADE_URL = "https://bitian.io/exchmarcket-web-biz/v1/trade/sendorder";
    private static String CANCEL_URL = "https://bitian.io/exchmarcket-web-biz/v1/trade/cancelorder";
    private static BigDecimal feeRatio = new BigDecimal(0.002);

    public static void main(String[] args) throws Exception {

        final String headerStr = "accept: application/json, text/javascript, */*; q=0.01\n" +
                "accept-language: zh-CN,zh;q=0.9,en;q=0.8\n" +
                "content-type: application/x-www-form-urlencoded; charset=UTF-8\n" +
                "cookie: tfsid=06978374-4e46-4a72-a678-d82deb06f3af; __cfduid=d6fb988b746a00e5f3c1c41fb64c7cc2b1537169304; Hm_lvt_f48d1bf16b386b38381d726ae4080177=1537169308; __zlcmid=oRheGvROnpajJx; _ga=GA1.2.1125130533.1537170405; mobile=18210036590; _gid=GA1.2.1521465802.1537409771; userids=1809171543262510088; usercellnub=182****6590; Hm_lpvt_f48d1bf16b386b38381d726ae4080177=1537509130; _gat=1\n" +
                "origin: https://bitian.io\n" +
                "referer: https://bitian.io/zh-cn/mockTrade.html\n" +
                "user-agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.81 Safari/537.36\n" +
                "x-requested-with: XMLHttpRequest";
        List<Header> headerList = HeaderUtil.getHeaders(headerStr);

        BigDecimal tradeFee = BigDecimal.ZERO;

        int i = 1;
        while (true) {
            log.info("第: {}次交易开始", i);
            log.info("已产生手续费: {}{}", tradeFee, KEY2);

            /**
             * 获取账号余额
             */
            JSONArray orders = JSON.parseObject(HttpClientUtil.getInstance().postFormRequest(ACCOUNT_URL, String.format("orderstatus=0,1,2,3&digecciCodes=%s/%s", KEY1, KEY2), headerList))
                    .getJSONObject("data").getJSONArray("order");

            for (int index = 0; index < orders.size(); index++) {
                JSONObject order = orders.getJSONObject(index);
                if (order.getString("instrumentID").equalsIgnoreCase(KEY1 + "/" + KEY2)) {
                    tradeFee = tradeFee.subtract(order.getBigDecimal("volumeRemain").multiply(order.getBigDecimal("limitPrice")).multiply(feeRatio));
                    HttpClientUtil.getInstance().postFormRequest(CANCEL_URL, String.format("ordersysid=%s&userid=1809171543262510088", order.getString("orderSysID")), headerList);
                }
            }

            JSONArray accounts = JSON.parseObject(HttpClientUtil.getInstance().postFormRequest(ACCOUNT_URL, String.format("orderstatus=0,1,2,3&digecciCodes=%s/%s", KEY1, KEY2), headerList))
                    .getJSONObject("data").getJSONArray("personalAsset");

            BigDecimal account1 = accounts.getJSONObject(0).getBigDecimal("available");
            BigDecimal account2 = accounts.getJSONObject(1).getBigDecimal("available");

            /**
             * 获取交易价格
             */
            JSONObject prices = JSON.parseObject(
                    HttpClientUtil.getInstance().postFormRequest(PRICE_URL, String.format("mktCode=%s&instrumentID=%s/%s&precision=2&limit=2", KEY2, KEY1, KEY2), null)
            ).getJSONObject("data").getJSONObject("marketdata");

            BigDecimal ask1Price = prices.getJSONArray("askPrice").getBigDecimal(0);
            BigDecimal bid1Price = prices.getJSONArray("bidPrice").getBigDecimal(0);
            BigDecimal tradePrice = prices.getBigDecimal("lastPrice");

            BigDecimal account2TradeAmount = account2.divide(tradePrice, AMOUNT_PRECISION, BigDecimal.ROUND_DOWN);
            BigDecimal tradeAmount = (account2TradeAmount.compareTo(account1) > 0 ? account1 : account2TradeAmount)
                    .subtract(LEAST_SUB_AMOUNT)
                    .setScale(AMOUNT_PRECISION, BigDecimal.ROUND_DOWN);

            BigDecimal balancePrice = account1.compareTo(account2TradeAmount) > 0 ? bid1Price : ask1Price;
            BigDecimal balanceAmount = account1.subtract(account2TradeAmount).abs()
                    .subtract(LEAST_SUB_AMOUNT)
                    .divide(new BigDecimal(2), AMOUNT_PRECISION, BigDecimal.ROUND_DOWN);

            ExecutorService executorService = Executors.newCachedThreadPool();
            CompletionService<JSONObject> completionService = new ExecutorCompletionService<>(
                    executorService);

            int threadNum = 0;

            Boolean trade = false;
            Boolean banlance = false;

            if (balanceAmount.compareTo(LEAST_TRADE_AMOUNT) > 0) {
                banlance = true;
                log.info("平衡{}交易={}：{}", account1.compareTo(account2TradeAmount) > 0 ? "卖出" : "买入", balancePrice, balanceAmount);
                if (account2TradeAmount.compareTo(account1) > 0) {
                    completionService.submit(
                            new HttpBiTianTask(TRADE_URL,
                                    getBidParams(balancePrice, balanceAmount, KEY1, KEY2),
                                    headerList, balancePrice, balanceAmount)
                    );
                } else {
                    completionService.submit(
                            new HttpBiTianTask(TRADE_URL,
                                    getAskParams(balancePrice, balanceAmount, KEY1, KEY2),
                                    headerList, balancePrice, balanceAmount)
                    );
                }
                threadNum += 1;
            }

            if (tradePrice.compareTo(ask1Price) < 0
                    && tradePrice.compareTo(bid1Price) > 0
                    && tradeAmount.compareTo(LEAST_TRADE_AMOUNT) > 0) {
                trade = true;
                log.info("卖1价格: {}，买1价格：{}，对冲交易={}：{}", ask1Price, bid1Price, tradePrice, tradeAmount);
                /**
                 * 对冲卖出
                 * 对冲买入
                 */
                completionService.submit(
                        new HttpBiTianTask(TRADE_URL,
                                getAskParams(tradePrice, tradeAmount, KEY1, KEY2),
                                headerList, tradePrice, tradeAmount)
                );
                completionService.submit(
                        new HttpBiTianTask(TRADE_URL,
                                getBidParams(tradePrice, tradeAmount, KEY1, KEY2),
                                headerList, tradePrice, tradeAmount)
                );
                threadNum += 2;

            }

            for (int index = 0; index < threadNum; index++) {
                JSONObject jsonObject = completionService.take().get();
                if (jsonObject.getBoolean("success")) {
                    tradeFee = tradeFee.add(jsonObject.getBigDecimal("price").multiply(jsonObject.getBigDecimal("amount")).multiply(feeRatio));
                }
            }
            executorService.shutdown();
            if (!trade && banlance) {
                Thread.sleep(2000);
            }
            log.info("第: {}次交易结束", i++);
        }
    }

    private static String getAskParams(BigDecimal price, BigDecimal amount, String key1, String key2) {
        return String.format("direction=1&instrumentID=%s/%s&price=%s&volumn=%s", key1, key2, price, amount);
    }

    private static String getBidParams(BigDecimal price, BigDecimal amount, String key1, String key2) {
        return String.format("direction=0&instrumentID=%s/%s&price=%s&volumn=%s", key1, key2, price, amount);
    }

}