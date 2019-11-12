package com.ntces;

import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.cookie.BestMatchSpecFactory;
import org.apache.http.impl.cookie.BrowserCompatSpecFactory;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.*;

public class HttpClientDemo {
    // 创建CookieStore实例
//     CookieStore cookieStore = null;
//     HttpClientContext context = null;
    private final static String URL_LOGIN = "https://gugong.ktmtech.cn/Login/TravelLogin";
    private final static String URL_SELECTDATE = "https://gugong.ktmtech.cn/OrderTravel/SelectDate?date=%s&amorpm=1&travel=0";
    ///?date=2019-11-20&amorpm=1&travel=0

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

    public HttpClientContext createContext() {
        HttpClientContext context = HttpClientContext.create();
        Registry<CookieSpecProvider> registry = RegistryBuilder
                .<CookieSpecProvider>create()
                .register(CookieSpecs.BEST_MATCH, new BestMatchSpecFactory())
                .register(CookieSpecs.BROWSER_COMPATIBILITY,
                        new BrowserCompatSpecFactory()).build();
        context.setCookieSpecRegistry(registry);
        return context;
    }

    public void selectDate(CloseableHttpClient client, Date date) throws IOException {


        // 执行get请求
        // date
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
        UrlEncodedFormEntity postEntity = new UrlEncodedFormEntity(getParam(parameterMap), "UTF-8");
        httpPost.setEntity(postEntity);


        // 执行post请求
        HttpResponse httpResponse = client.execute(httpPost);
        printResponse(httpResponse);
//        // cookie store
//        CookieStore cookieStore = setCookieStore(httpResponse);
//        // context
//        context.setCookieStore(cookieStore);
//        return context;
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

    public CookieStore setCookieStore(HttpResponse httpResponse) {

        System.out.println("----setCookieStore");

        Header[] headers = httpResponse.getHeaders("Set-Cookie");
        if (headers == null || headers.length < 1)
            return null;

        CookieStore cookieStore = new BasicCookieStore();
        for (Header o : headers) {
            System.out.println(String.format("----header [%s][%s]", o.getName(), o.getValue()));
            Cookie cookie = new BasicClientCookie(o.getName(), o.getValue());
            cookieStore.addCookie(cookie);
        }
        System.out.println("----setCookieStore end");
        return cookieStore;

    }

}
