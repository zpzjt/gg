package com.ntces.ticketScalping.grasp;

import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.*;

public class HttpClientDemo {
    private final static String URL_LOGIN = "https://gugong.ktmtech.cn/Login/TravelLogin";
    private final static String URL_SELECTDATE = "https://gugong.ktmtech.cn/OrderTravel/SelectDate?date=%s&amorpm=1&travel=0";

    public static void main(String[] args) throws Exception {
        HttpClientDemo httpClientDemo = new HttpClientDemo();
        httpClientDemo.execute();
    }

    public void execute() throws IOException {
        CloseableHttpClient client = openConnection();
        try {
            login(client);
            selectDate(client, new Date());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (client != null)
                client.close();
        }

    }

    private CloseableHttpClient openConnection() {
        CookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        return httpclient;
    }


    public void selectDate(CloseableHttpClient client, Date date) throws IOException {
        String url = String.format(URL_SELECTDATE, "2019-11-20");
        HttpGet httpGet = new HttpGet(url);
        System.out.println("----selectDate:" + url);
        HttpResponse httpResponse = client.execute(httpGet);
        printResponse(httpResponse);
    }

    public void login(HttpClient client) throws IOException {

        HttpPost httpPost = new HttpPost(URL_LOGIN);
        Map parameterMap = new HashMap();
        parameterMap.put("regCode", "L-BJ00971");
        parameterMap.put("passWord", "xlyt2019");
        httpPost.addHeader("Connection","keep-alive");
        UrlEncodedFormEntity postEntity = new UrlEncodedFormEntity(getParam(parameterMap), "UTF-8");
        httpPost.setEntity(postEntity);
        System.out.println(String.format("request line: [%s][%s]", URL_LOGIN, httpPost.getRequestLine()));
        // 执行post请求
        HttpResponse httpResponse = client.execute(httpPost);
        printResponse(httpResponse);

    }

    public void printResponse(HttpResponse httpResponse)
            throws ParseException, IOException {
        // 获取响应消息实体
        HttpEntity entity = httpResponse.getEntity();
        // 响应状态
        System.out.println("status:" + httpResponse.getStatusLine().getStatusCode());
        System.out.println("headers:");
        HeaderIterator iterator = httpResponse.headerIterator();
        while (iterator.hasNext()) {
            System.out.println("\t" + iterator.next());
        }
        // 判断响应实体是否为空
        if (entity != null) {
            String responseString = EntityUtils.toString(entity);
            System.out.println("response length:" + responseString.length());
            System.out.println("response content:"
                    + responseString.replace("\r\n", ""));
        }
    }


    public List<NameValuePair> getParam(Map parameterMap) {
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        Iterator it = parameterMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry parmEntry = (Map.Entry) it.next();
            param.add(new BasicNameValuePair((String) parmEntry.getKey(),
                    (String) parmEntry.getValue()));
        }
        return param;
    }

}
