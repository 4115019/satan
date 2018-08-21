package com.satan.abcc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.satan.common.utils.HttpClientUtil;
import com.satan.common.utils.RegexpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by h
 * on 2018-08-06 13:13.
 *
 * @author h
 */
@Slf4j
public class AbccTool {

    private static BigDecimal END_KEY1_VALUE = new BigDecimal(0.04).setScale(2, BigDecimal.ROUND_HALF_UP);
    private static BigDecimal END_KEY2_VALUE = new BigDecimal(13);

    private static String KEY1 = "eth";
    private static String KEY2 = "usdt";
    private static String PAIR = KEY1 + KEY2;
    private static String bidUrl = "https://abcc.com/markets/ethusdt/order_bids";
    private static String askUrl = "https://abcc.com/markets/ethusdt/order_asks";
    private static String bidPrefix = "买入";
    private static String askPrefix = "卖出";

    public static void main(String[] args) throws Exception {

        final String headers = "accept: application/json, text/plain, */*\n" +
                "accept-language: zh-CN,zh;q=0.9,en;q=0.8\n" +
                "cookie: _ga=GA1.2.798607302.1532915241; market_id=ethusdt; _abcc_session=efb91b0985f06370f81d21bd3827c9bb; _gid=GA1.2.903562763.1534761347; _gat_gtag_UA_116957313_1=1; AWSALB=AIkeNEUvborcxu2x04WPHAWhgS/E1acg6crVI1O2dO56M/t7hIuNyW1/je6eD4exIjhwP5ldNuhL07nECR4Bjikiprsm0UXhHuVcoIXYGG9WeQhB9BJUgSU+hfe7kRQD+SLXV2JImdE7xqlIaCLSUZcGhE4niSGzDHxusWr8m9+YAEwKBpXCbIchxss2ng==; XSRF-TOKEN=5g8JTGmJLSPD33ew%2FSlG6QEwHHrQE%2FL0hpdKUjNYEaPEBIo8q2arWBnOS6%2BfOhupCJBF15LFz3ndv8iGH8z6zA%3D%3D\n" +
                "if-none-match: W/\"f90f801e85abdd781c4f44a7a4969706\"\n" +
                "referer: https://abcc.com/markets/ethusdt\n" +
                "user-agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36\n" +
                "x-csrf-token: JMQkvAepB9Yug5NKKwOKSMO6vwU2caR1xPwmAMhiAoIGz6fMxUaBrfSSr1VJENcIyhrmqHSnmfif1KTU5Pbp7Q==\n" +
                "x-xsrf-token: 5g8JTGmJLSPD33ew/SlG6QEwHHrQE/L0hpdKUjNYEaPEBIo8q2arWBnOS6+fOhupCJBF15LFz3ndv8iGH8z6zA==";
        String[] split = headers.split("\n");
        List<Header> headerList = new ArrayList<Header>();
        for (String header : split) {
            headerList.add(new BasicHeader(header.split(":")[0], header.split(":")[1]));
        }

        int i = 1;
        int priceTime = 0;
        while (true) {
            System.out.println("第" + i + "次交易开始");
            cancelTrade(headerList);

            JSONObject account = JSON.parseObject(
                    RegexpUtil.getRegexp("(?<=gon.accounts=).*(?=;gon.vouchers)",
                            HttpClientUtil.getInstance().sendGetRequestToString("https://abcc.com/markets/" + PAIR, headerList))
            );
            /**
             * 为了支付手续费usdt账户减去1usdt   .subtract(BigDecimal.ONE)
             */
            BigDecimal account1 = account.getJSONObject(KEY1).getBigDecimal("balance").setScale(2, BigDecimal.ROUND_DOWN);
            BigDecimal account2 = account.getJSONObject(KEY2).getBigDecimal("balance").setScale(2, BigDecimal.ROUND_DOWN);
            System.out.println("eth数量：" + account1 + "    usdt数量：" + account2);
            if (account1.compareTo(END_KEY1_VALUE) <= 0 && account2.compareTo(END_KEY2_VALUE) <= 0) {
                System.out.println("账户余额无法支持对冲交易，挖掘机结束");
                break;
            }

            JSONObject prices = JSON.parseObject(HttpClientUtil.getInstance().sendGetRequestToString("https://abcc.com/api/v2/depth.json?market=" + PAIR, null));
            BigDecimal askPrice = prices.getJSONArray("asks").getJSONArray(prices.getJSONArray("asks").size() - 1).getBigDecimal(0);
            BigDecimal bidPrice = prices.getJSONArray("bids").getJSONArray(0).getBigDecimal(0);
            BigDecimal tradePrice = bidPrice.add(askPrice).divide(new BigDecimal(2), 2, BigDecimal.ROUND_HALF_UP);

            BigDecimal account2TradeAmount = account2.divide(tradePrice, 2, BigDecimal.ROUND_DOWN);
            BigDecimal tradeAmount = (account2TradeAmount.compareTo(account1) > 0 ? account1 : account2TradeAmount).subtract(new BigDecimal(0.01));
            BigDecimal balanceAmount = account1.subtract(account2TradeAmount).abs().divide(new BigDecimal(2), 2, BigDecimal.ROUND_DOWN);

            System.out.println("对冲交易价格：" + tradePrice + " 交易数量：" + tradeAmount);
            System.out.println("平衡交易-"
                    + (account1.compareTo(account2TradeAmount) > 0 ? "卖出:" : "买入:")
                    + balanceAmount);

            ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);
            List<Future<Boolean>> resultList = new ArrayList<Future<Boolean>>();

            Boolean sleep = true;
            Boolean longSleep = true;

            if (tradeAmount.compareTo(END_KEY1_VALUE) >= 0) {
                longSleep = false;
                /**
                 * 对冲卖出
                 * 对冲买入
                 */
                resultList.add(executor.submit(
                        new HttpAbccTask(askUrl,
                                getAskParams(tradePrice, tradeAmount),
                                headerList,
                                askPrefix)
                ));
                resultList.add(executor.submit(
                        new HttpAbccTask(bidUrl,
                                getBidParams(tradePrice, tradeAmount),
                                headerList,
                                bidPrefix)
                ));
            }

            /**
             * 平衡对冲交易
             */
            if (balanceAmount.compareTo(END_KEY1_VALUE) >= 0) {

                if (priceTime > 3) {
                    bidPrice = tradePrice.multiply(new BigDecimal(0.3)).add(bidPrice.multiply(new BigDecimal(0.7))).setScale(2,BigDecimal.ROUND_DOWN);
                    askPrice = tradePrice.multiply(new BigDecimal(0.3)).add(askPrice.multiply(new BigDecimal(0.7))).setScale(2,BigDecimal.ROUND_HALF_UP);
                }
                if (account2TradeAmount.compareTo(account1) > 0) {
                    resultList.add(executor.submit(
                            new HttpAbccTask(bidUrl,
                                    getBidParams((new BigDecimal(0.01)).add(bidPrice),
                                            balanceAmount),
                                    headerList,
                                    bidPrefix)
                    ));
                } else {
                    resultList.add(executor.submit(
                            new HttpAbccTask(askUrl,
                                    getAskParams(askPrice.subtract(new BigDecimal(0.01)),
                                            balanceAmount),
                                    headerList,
                                    askPrefix)
                    ));
                }
            } else {
                sleep = false;
            }

            for (Future<Boolean> result : resultList) {
                result.get();
            }

            executor.shutdown();

            System.out.println("sleep");
            if (sleep) {
                Thread.sleep(8000);
                if (longSleep) {
                    priceTime++;
                    Thread.sleep(8000);
                } else {
                    priceTime = 0;
                }
            } else {
                Thread.sleep(6000);
            }

            System.out.println("第" + i++ + "次交易结束");
        }
    }

    private static String getAskParams(BigDecimal price, BigDecimal amount) {
        return "utf8=%E2%9C%93&order_ask%5Bord_type%5D=limit&order_ask%5Bprice%5D="
                + price
                + "&order_ask%5Borigin_volume%5D=" + amount
                + "&order_ask%5Bpercent%5D=0";
    }

    private static String getBidParams(BigDecimal price, BigDecimal amount) {
        return "utf8=%E2%9C%93&order_bid%5Bord_type%5D=limit&order_bid%5Bprice%5D="
                + price
                + "&order_bid%5Borigin_volume%5D=" + amount
                + "&order_ask%5Bpercent%5D=0";
    }


    private static void cancelTrade(List<Header> headerList) {
        try {
            System.out.println("撤销所有订单" + HttpClientUtil.getInstance().postFormRequest("https://abcc.com/markets/clear_all_waiting_orders", null, headerList));
        } catch (Exception e) {
            System.out.println("撤单失败！！！");
            e.printStackTrace();
        }
    }
}