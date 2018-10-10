package com.satan.trade.iquant;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.satan.trade.common.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by h
 * on 2018-09-18 17:36.
 *
 * @author h
 */
@Slf4j
public class IQuantWeapon {

    private static String KEY1 = "MBTC";
    private static String KEY2 = "MUSDT";
    private static String ORDER_URL = "https://api.5iquant.org/api/trade/iqtexCurrentOrderList";
    private static String CANCEL_ORDER_URL = "https://api.5iquant.org/api/trade/iqtexCancelOrder";
    private static String TRADE_PRICE_URL = "https://api.5iquant.org/api/market/currencyQuote";
    private static String ACCOUNT_URL = "https://api.5iquant.org/api/assets/iqtCurrencyUser";
    private static String TRADE_URL = "https://api.5iquant.org/api/trade/submitOrder";

    private static BigDecimal FLOOR_PRICE = new BigDecimal(0);
    private static BigDecimal CEIL_PRICE = new BigDecimal(0);
    private static Integer AMOUNT_PRECISION = 5;
    private static BigDecimal LEAST_TRADE_AMOUNT = new BigDecimal("0.002");
    private static BigDecimal LEAST_SUB_AMOUNT = new BigDecimal("0.0002");
    private static BigDecimal FEE_RATIO = new BigDecimal(0.002);


    public static void main(String[] args) throws Exception {

        final String token = "";

        int i = 1;
        BigDecimal tradeFee = BigDecimal.ZERO;

        while (true) {
            log.info("第: {}次交易开始", i);
            log.info("已产生手续费: {}{}", tradeFee, KEY2);

            /**
             * 取消交易
             */
            JSONArray orders = JSON.parseObject(HttpClientUtil.getInstance().postFormRequest(ORDER_URL, String.format("version=01&access_token=%s", token), null))
                    .getJSONObject("data").getJSONArray("data");

            for (int index = 0; index < orders.size(); index++) {
                JSONObject order = orders.getJSONObject(index);
                if (order.getString("pairName").equalsIgnoreCase(KEY1 + "/" + KEY2)) {
                    HttpClientUtil.getInstance().postFormRequest(CANCEL_ORDER_URL, String.format("access_token=%s&orderId=%s&version=01", token, order.getString("orderId")), null);
                }
            }

            JSONArray accounts = JSON.parseObject(HttpClientUtil.getInstance().postFormRequest(ACCOUNT_URL, String.format("version=01&access_token=%s", token), null))
                    .getJSONObject("data")
                    .getJSONArray("data");

            BigDecimal account1 = BigDecimal.ZERO;
            BigDecimal account2 = BigDecimal.ZERO;
            for (int index = 0; index < accounts.size(); index++) {
                JSONObject account = accounts.getJSONObject(index);
                if (account.getString("assetName").equalsIgnoreCase(KEY1)) {
                    account1 = account.getBigDecimal("num");
                }
                if (account.getString("assetName").equalsIgnoreCase(KEY2)) {
                    account2 = account.getBigDecimal("num");
                }
            }

            BigDecimal tradePrice = JSON.parseObject(HttpClientUtil.getInstance().postFormRequest(TRADE_PRICE_URL, "currencyName=ETH_BTC&version=01", null))
                    .getJSONObject("data").getBigDecimal("latestPrice");
            if (tradePrice.compareTo(FLOOR_PRICE) > 0 && tradePrice.compareTo(CEIL_PRICE) < 0) {
                log.info("对冲价格浮动太大，放弃交易。对冲价格：{}", tradePrice);
                log.info("第: {}次交易结束", i++);
                continue;
            }

            BigDecimal account2Amount = account2.divide(tradePrice, AMOUNT_PRECISION, BigDecimal.ROUND_DOWN);
            BigDecimal tradeAmount = (account2Amount.compareTo(account1) > 0 ? account1 : account2Amount)
                    .subtract(LEAST_SUB_AMOUNT)
                    .setScale(AMOUNT_PRECISION, BigDecimal.ROUND_DOWN);

            ExecutorService executorService = Executors.newCachedThreadPool();
            CompletionService<JSONObject> completionService = new ExecutorCompletionService<>(
                    executorService);
            if (tradeAmount.compareTo(LEAST_TRADE_AMOUNT) > 0) {
                log.info("对冲交易={}：{}", tradePrice, tradeAmount);
                /**
                 * 对冲卖出
                 * 对冲买入
                 */
                completionService.submit(
                        new HttpIQuantTask(TRADE_URL,
                                getTradeParams(tradePrice, tradeAmount, KEY1, KEY2, token, "sell"), tradePrice, tradeAmount)
                );
                completionService.submit(
                        new HttpIQuantTask(TRADE_URL,
                                getTradeParams(tradePrice, tradeAmount, KEY1, KEY2, token, "buy"), tradePrice, tradeAmount)
                );

                for (int index = 0; index < 2; index++) {
                    JSONObject jsonObject = completionService.take().get();
                    if (jsonObject.getBoolean("success")) {
                        tradeFee = tradeFee.add(jsonObject.getBigDecimal("price").multiply(jsonObject.getBigDecimal("amount")).multiply(FEE_RATIO));
                    }
                }

            }
            executorService.shutdown();

            log.info("第: {}次交易结束", i++);
        }
    }

    private static String getTradeParams(BigDecimal price, BigDecimal amount, String key1, String key2, String token, String type) {
        return String.format("access_token=%s&version=01&currencyName=%s_%s&num=%s&price=%s&type=%s", token, key1, key2, amount, price, type);
    }
}