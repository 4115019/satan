package com.satan.trade.iquant;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.satan.trade.common.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.math.BigDecimal;

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
    private static String TRADE_PRICE_URL = "https://api.5iquant.org/api/market/currencyQuote";
    private static String ACCOUNT_URL = "https://api.5iquant.org/api/assets/iqtCurrencyUser";

    public static void main(String[] args) throws IOException {

        final String token = "";

        int i = 1;
        while (true) {
            log.info("第: {}次交易开始", i);

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
            System.out.println(tradePrice);
            log.info("第: {}次交易结束", i++);
        }
    }
}