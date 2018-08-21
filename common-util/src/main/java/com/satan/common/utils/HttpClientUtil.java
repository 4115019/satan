package com.satan.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Created by huangpin on 16/11/1.
 */
@Slf4j
public class HttpClientUtil {

    private final static int TIMEOUT = 3000;

    private RequestConfig requestConfig = RequestConfig.custom()
            .setSocketTimeout(TIMEOUT)
            .setConnectTimeout(TIMEOUT)
            .setConnectionRequestTimeout(TIMEOUT)
            .build();

    private static HttpClientUtil instance = null;

    private HttpClientUtil() {
    }

    public static HttpClientUtil getInstance() {
        if (instance == null) {
            instance = new HttpClientUtil();
        }
        return instance;
    }

    public String postFormRequest(String reqURL, String param, List<Header> headers) throws IOException {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(reqURL);
            // 打开和URL之间的连接
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 1087));
            URLConnection conn = realUrl.openConnection(proxy);
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            if (CollectionUtil.isNotEmpty(headers)) {
                for (Header header : headers) {
                    conn.setRequestProperty(header.getName(), header.getValue());
                }
            }
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            log.info("POST 请求出现异常！" + e);
            e.printStackTrace();
            throw e;
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public String postJsonStringRequest(String reqURL, String params, List<Header> headers) {
        String responseContent = null;
        HttpClient httpClient = new DefaultHttpClient();

        HttpPost httpPost = new HttpPost(reqURL);
        if (CollectionUtil.isNotEmpty(headers)) {
            for (Header header : headers) {
                httpPost.addHeader(header);
            }
        }
        try {

            StringEntity entity = new StringEntity(params, "UTF-8");
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            httpPost.setConfig(requestConfig);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entityRsponse = response.getEntity();
            if (null != entityRsponse) {
                responseContent = EntityUtils.toString(entityRsponse, "UTF-8");
                EntityUtils.consume(entityRsponse);
            }
        } catch (Exception e) {
            log.error("[" + reqURL + "]An exception occurred in the service when handling the control request", e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return responseContent;
    }

    public String postXmlStringRequest(String reqURL, String xml, List<Header> headers) {
        String responseContent = null;
        CloseableHttpClient httpClient = new HttpClientGenerator().generateClient();

        HttpPost httpPost = new HttpPost(reqURL);

        // TODO: 17/4/24
        for (Header one : headers) {
            httpPost.addHeader(one);
        }
        try {
            StringEntity entity = new StringEntity(xml, "UTF-8");
            entity.setContentEncoding("UTF-8");
            entity.setContentType("multipart/form-data");
            httpPost.setEntity(entity);
            httpPost.setConfig(requestConfig);
            log.info("REQUEST URL:{}", reqURL);
            log.info("REQUEST XML:{}", xml);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entityRsponse = response.getEntity();
            if (null != entityRsponse) {
                responseContent = EntityUtils.toString(entityRsponse, "UTF-8");
                EntityUtils.consume(entityRsponse);
            }
            log.info("REQUEST BODY:{}", responseContent);
        } catch (Exception e) {
            log.error("[" + reqURL + "]An exception occurred in the service when handling the control request", e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return responseContent;
    }

    public String sendGetRequestToString(String reqURL, List<Header> headers) throws Exception {
        String str = new String(sendGetRequestToBytes(reqURL, headers));
        log.info("result = {}", str);
        return str;
    }

    public byte[] sendGetRequestToBytes(String reqURL, List<Header> headers) throws Exception {
        long responseLength = 0;
        byte[] responseContent = null;
        HttpClient httpClient = new DefaultHttpClient();
        HttpHost proxy = new HttpHost("127.0.0.1", 1087, null);
        httpClient.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY, proxy);
        HttpGet httpGet = new HttpGet(reqURL);
        if (headers != null) {
            for (Header header : headers) {
                httpGet.addHeader(header);
            }
        }
        try {
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                responseLength = entity.getContentLength();
                responseContent = EntityUtils.toByteArray(entity);
                EntityUtils.consume(entity); // Consume response content
            }
            log.info("REQUEST URI: " + httpGet.getURI());
            log.info("RESPONSE STATUS: " + response.getStatusLine());
            log.info("RESPONSE LENGTH: " + responseLength);
            log.info("RESPONSE DETAIL: " + responseContent);
        } catch (ClientProtocolException e) {
            log.error(
                    "A protocol error",
                    e);
            throw e;
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (IOException e) {
            log.error("Network anomaly", e);
            throw e;
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return responseContent;
    }
}
