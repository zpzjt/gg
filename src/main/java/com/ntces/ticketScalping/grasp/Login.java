package com.ntces.ticketScalping.grasp;

import com.ntces.ticketScalping.entity.LoginAccount;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * 登录
 */
public class Login {
    private static final Logger logger = LoggerFactory.getLogger(Login.class);
    private final static String URL_LOGIN = "https://gugong.ktmtech.cn/Login/TravelLogin";

    public static void main(String[] args) throws IOException {
        Login login = new Login();
        login.execute();
    }

    public void execute() throws IOException {
        CloseableHttpClient client = openConnection();
        try {
            LoginAccount loginAccount = new LoginAccount();
            loginAccount.setRegCode("L-BJ00971");
            loginAccount.setPassWord("xlyt2019");
            String i = login(client, loginAccount);
            logger.info("此次登录的状态:[{}]", i);
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


    public String login(HttpClient client, LoginAccount loginAccount) throws IOException {
        logger.info("Login Start,Login RegCode:[{}],Login Password:[{}]", loginAccount.getRegCode(), loginAccount.getPassWord());
        HttpPost httpPost = new HttpPost(URL_LOGIN);
        Map parameterMap = new HashMap();
        parameterMap.put("regCode", loginAccount.getRegCode());
        parameterMap.put("passWord", loginAccount.getPassWord());
        UrlEncodedFormEntity postEntity = new UrlEncodedFormEntity(getParam(parameterMap), "UTF-8");
        httpPost.setEntity(postEntity);
        HttpResponse httpResponse = client.execute(httpPost);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            return "SUCCESS";
        } else {
            return "Failed";
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
