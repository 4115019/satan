package com.satan.abcc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.satan.abcc.model.Order;
import com.satan.abcc.model.Orders;
import com.satan.common.utils.CollectionUtil;
import com.satan.common.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Created by h
 * on 2018-09-10 10:55.
 *
 * @author h
 */
@Slf4j
public class AbccAutoTrade {

    private static String KEY1 = "at";
    private static String KEY2 = "usdt";
    private static String PAIR = KEY1 + KEY2;
    private static String bidUrl = "https://abcc.com/markets/" + PAIR + "/order_bids";
    private static String askUrl = "https://abcc.com/markets/" + PAIR + "/order_asks";
    private static BigDecimal DEFAULT_TRADE_AMOUNT = new BigDecimal(200);
    private static BigDecimal LEAST_TRADE_AMOUNT = new BigDecimal(4700);
    private static BigDecimal PRICE_RAISE = new BigDecimal(0.001);
    private static BigDecimal PRICE_DIFFERENCE = new BigDecimal(0.003);

    public static void main(String[] args) throws Exception {
        final String headers = "accept: application/json, text/plain, */*\n" +
                "accept-language: zh-CN,zh;q=0.9,en;q=0.8\n" +
                "cookie: _ga=GA1.2.798607302.1532915241; lang=zh-CN; market_id=atusdt; csrfToken=GIGotLe9SpFfYzuBe3CCHV4p; lang.sig=iBRUV10Xl7OhEMMprjEABOXJveKhd0z515NK2edxIzE; locale=zh-cn; _gid=GA1.2.868907128.1536546080; _abcc_session=8499334903cbd1fb1cff009609368737; _gat_gtag_UA_116957313_1=1; AWSALB=tMiJMBFVyAzaKE2zOidEMVipRJAU3RF5jD9k3KNPvVwYSmqL5Zz2H/4V0PSLyonUABBHffLMQ9edsya5s7Ey5vJa3OL9qnoD4mMYQe+fWLQqR8vaknUzHjDvpODwiULD/9J5vPnjlaT4mpQDdSbxybKKh1BfePJX3OnTdW1SexRTJ+ki2z6t6ySHozUUDw==; XSRF-TOKEN=w6Td%2BWwHEIUehqDRWC%2BFSsgS27DbDM4%2FShm13PFTsTNCelRbnNne1LS%2FKvRaMj5bYiJJzb%2FjWcdhVjltI40oGA%3D%3D\n" +
                "if-none-match: W/\"d30ca3938b0fa43f45832295e5a46a44\"\n" +
                "referer: https://abcc.com/markets/atusdt\n" +
                "user-agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.81 Safari/537.36\n" +
                "x-csrf-token: 6gufn4FtWUmQqkVbIr0PyH4JFc4u8mZTHvnsnw7lGUVr1RY9cbOXGDqTz34goLTZ1DmHs0od8as1tmAu3DuAbg==\n" +
                "x-xsrf-token: w6Td+WwHEIUehqDRWC+FSsgS27DbDM4/Shm13PFTsTNCelRbnNne1LS/KvRaMj5bYiJJzb/jWcdhVjltI40oGA==";
        List<Header> headerList = AbccUtil.getHeaders(headers);

        int i = 0;
        BigDecimal lastBid = new BigDecimal("0.7562"), lastAsk = new BigDecimal("0.7603");
        BigDecimal lastSuccessBid = lastBid, lastSuccessAsk = lastAsk;
        BigDecimal bidAmount, askAmount;
        bidAmount = askAmount = DEFAULT_TRADE_AMOUNT;

        Boolean bid = false, ask = false;
        while (++i > 0) {
            log.info("第{}次交易开始", i);

//            log.info("获取正在进行中的单子");
//            List<Order> orders = Optional.ofNullable(AbccUtil.getTradingOrders(headerList))
//                    .map(Orders::getOrders)
//                    .orElse(null);
//
//            if (CollectionUtil.isNotEmpty(orders)) {
//
//                if (orders.size() == 1) {
//
//                    BigDecimal tradeAmount = orders.get(0).getOriginVolume().subtract(orders.get(0).getVolume());
//                    BigDecimal price = orders.get(0).getAvgPrice();
//                    if (orders.get(0).isAsk()) {
//                        if (bid) {
//                            lastSuccessBid = lastBid;
//                        }
//                        if (tradeAmount.compareTo(BigDecimal.ZERO) > 0) {
//                            lastSuccessAsk = price;
//                        }
////                        askAmount = askAmount.add(bidAmount).subtract(tradeAmount);
////                        bidAmount = tradeAmount;
//                    } else {
//                        if (ask) {
//                            lastSuccessAsk = lastAsk;
//                        }
//                        if (tradeAmount.compareTo(BigDecimal.ZERO) > 0) {
//                            lastSuccessBid = price;
//                        }
////                        bidAmount = askAmount.add(bidAmount).subtract(tradeAmount);
////                        askAmount = tradeAmount;
//                    }
//                } else {
//
//                    for (Order order : orders) {
//                        BigDecimal tradeAmount = order.getOriginVolume().subtract(order.getVolume());
//                        BigDecimal price = order.getAvgPrice();
//
//                        if (tradeAmount.compareTo(BigDecimal.ZERO) > 0) {
//                            if (order.isAsk()) {
//                                lastSuccessAsk = price;
////                                askAmount = askAmount.subtract(tradeAmount);
////                                bidAmount = bidAmount.add(tradeAmount);
//                            } else {
//                                lastSuccessBid = price;
////                                askAmount = askAmount.add(tradeAmount);
////                                bidAmount = bidAmount.subtract(tradeAmount);
//                            }
//                        }
//                    }
//                }
//            } else {
//                if (ask && !bid) {
//                    lastSuccessAsk = lastAsk;
////                    askAmount = bidAmount = DEFAULT_TRADE_AMOUNT;
//                }
//
//                if (bid && !ask) {
//                    lastSuccessBid = lastBid;
////                    askAmount = bidAmount = DEFAULT_TRADE_AMOUNT;
//                }
//            }


            log.info("取消所有交易");
            AbccUtil.cancelTrade(headerList);

            /**
             * 账户余额)
             */
            JSONObject account = AbccUtil.getAccount(PAIR, headerList);
            BigDecimal account1 = account.getJSONObject(KEY1).getBigDecimal("balance").setScale(6, BigDecimal.ROUND_DOWN);
            BigDecimal account2 = account.getJSONObject(KEY2).getBigDecimal("balance").setScale(6, BigDecimal.ROUND_DOWN);
            log.info("{}: {},{}：{}", KEY1, account1, KEY2, account2);
            System.out.println(KEY1 + "数量：" + account1 + "    " + KEY2 + "数量：" + account2);
            /**
             * 市场价格
             */
            JSONObject prices = JSON.parseObject(HttpClientUtil.getInstance().sendGetRequestToString("https://abcc.com/api/v2/depth.json?market=" + PAIR, null));
            BigDecimal ask1Price = prices.getJSONArray("asks").getJSONArray(prices.getJSONArray("asks").size() - 1).getBigDecimal(0);
            BigDecimal bid1Price = prices.getJSONArray("bids").getJSONArray(0).getBigDecimal(0);
            if (ask1Price.subtract(bid1Price).compareTo(PRICE_DIFFERENCE) < 0) {
                log.info("差价太小不交易");
                log.info("第{}次交易结束", i);
                Thread.sleep(3000);
                continue;
//                ask1Price = ask1Price.add(PRICE_RAISE).setScale(4, BigDecimal.ROUND_HALF_UP);
//                bid1Price = bid1Price.subtract(PRICE_RAISE).setScale(4, BigDecimal.ROUND_DOWN);
            }
//            lastAsk = lastSuccessBid.compareTo(ask1Price) > 0 ? lastSuccessBid : ask1Price;
//            lastBid = lastSuccessAsk.compareTo(bid1Price) > 0 ? bid1Price : lastSuccessAsk;
            lastAsk = ask1Price;
            lastBid = bid1Price;

            log.info("卖价格: {},上买价格：{},最终卖价：{}", ask1Price, lastSuccessBid, lastAsk);
            log.info("买价格: {},上卖价格：{},最终买价：{}", bid1Price, lastSuccessAsk, lastBid);

            ask = bid = false;
            if (account1.compareTo(LEAST_TRADE_AMOUNT) > 0) {
                try {
                    log.info("卖出: {}:{}，结果：{}",
                            lastAsk,
                            askAmount,
                            HttpClientUtil.getInstance().postFormRequest(askUrl, AbccUtil.getAskParams(lastAsk, askAmount), headerList));
                    ask = true;
                } catch (Exception e) {
                    log.error("卖出报错");
                }
            }

            if (account2.compareTo(bidAmount.multiply(lastBid)) > 0) {
                try {
                    log.info("买入: {}:{}，结果：{}",
                            lastBid,
                            bidAmount,
                            HttpClientUtil.getInstance().postFormRequest(bidUrl, AbccUtil.getBidParams(lastBid, bidAmount), headerList));
                    bid = true;
                } catch (Exception e) {
                    log.error("买入报错");
                }
            }
            log.info("第{}次交易结束", i);
            Thread.sleep(48000);

        }
    }

}