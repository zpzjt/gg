package com.ntces.ticketScalping.grasp;

import com.ntces.ticketScalping.entity.LoginAccount;
import com.ntces.ticketScalping.entity.RelevantTicketPrice;
import com.ntces.ticketScalping.entity.TicketPriceInfo;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TicketPrice {

    private static final Logger logger = LoggerFactory.getLogger(TicketPrice.class);
    private final static String URL_LOGIN = "https://gugong.ktmtech.cn/Login/TravelLogin";
    private final static String URL_SELECTDATE = "https://gugong.ktmtech.cn/OrderTravel/SelectDate?date=%s&amorpm=%s&travel=%s";

    public static void main(String[] args) throws Exception {
        TicketPrice ticketPrice = new TicketPrice();
        ticketPrice.execute();
    }

    public void execute() throws IOException {
        CloseableHttpClient client = openConnection();
        try {
            LoginAccount loginAccount = new LoginAccount();
            loginAccount.setRegCode("L-BJ00971");
            loginAccount.setPassWord("xlyt2019");
            String i = login(client, loginAccount);
            if (i.equals("SUCCESS")) {
                TicketPriceInfo ticketPriceInfo = new TicketPriceInfo();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = simpleDateFormat.parse("2019-11-12");
                ticketPriceInfo.setDate(date);
                ticketPriceInfo.setAmorpm(2);
                ticketPriceInfo.setTravel(0);
                List<TicketPriceInfo>  ticketPriceInfosList= selectPrice(client, ticketPriceInfo);
                for(TicketPriceInfo t:ticketPriceInfosList){
                    System.out.println(t.toString());
                }

            }
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


    public List<TicketPriceInfo> selectPrice(CloseableHttpClient client, TicketPriceInfo ticketPriceInfo) throws IOException, ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(ticketPriceInfo.getDate());
        Integer amorpm = ticketPriceInfo.getAmorpm();
        Integer travel = ticketPriceInfo.getTravel();
        String url = String.format(URL_SELECTDATE, date, amorpm, travel);
        logger.info(url);
        HttpGet httpGet = new HttpGet(url);
        HttpResponse httpResponse = client.execute(httpGet);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        HttpEntity entity = httpResponse.getEntity();
        if (statusCode == 200 && entity != null) {
            //解析HTML
            return analysisEntity(entity, ticketPriceInfo);
        } else {
            return null;
        }
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

    public List<TicketPriceInfo> analysisEntity(HttpEntity entity, TicketPriceInfo ticketPriceInfo) throws IOException {
        String entityStr = EntityUtils.toString(entity);
        Document document = Jsoup.parse(entityStr);
        Elements elements = document.select(".PriceTypeId>option");
        List<TicketPriceInfo> ticketPriceInfosList = new ArrayList<>();
        for (Element element : elements) {
            String ticketName = element.text();
            Integer ticketAppleCrowd = null;
            List<RelevantTicketPrice> relevantTicketPriceList = new ArrayList<>();
            if (ticketName.contentEquals("学生")) {
                ticketAppleCrowd = 0;
            } else if (ticketName.contentEquals("老人")) {
                ticketAppleCrowd = 1;
            } else if (ticketName.contentEquals("成人")) {
                ticketAppleCrowd = 2;
            }
            Integer ticketStatus;
            if (ticketName.contentEquals("联票")) {
                ticketStatus = 0;
                String remark = element.attr("remark");
                String[] areaAndPricesArray = remark.split("，");
                for (String areaAndPrices : areaAndPricesArray) {
                    RelevantTicketPrice relevantTicketPrice = new RelevantTicketPrice();
                    String[] areaAndPrice = areaAndPrices.split("￥");
                    Integer ticketRegionCode = null;
                    String area = areaAndPrice[0].toString();
                    if(area.equals("珍宝馆")){
                        ticketRegionCode=0;
                    }else if(area.equals("钟表馆")){
                        ticketRegionCode=1;
                    }else if(area.equals("大门")){
                        ticketRegionCode=2;
                    }
                    Double ticketRegionPrice = Double.valueOf(areaAndPrice[1]);
                    relevantTicketPrice.setTicketRegionCode(ticketRegionCode);
                    relevantTicketPrice.setTicketRegionPrice(ticketRegionPrice);
                    relevantTicketPriceList.add(relevantTicketPrice);
                }
            } else {
                ticketStatus = 1;
            }
            Integer ticketCode = Integer.valueOf(element.attr("value"));
            Double price = Double.valueOf(element.attr("price"));
            ticketPriceInfo.setTicketName(ticketName);
            ticketPriceInfo.setTicketAppleCrowd(ticketAppleCrowd);
            ticketPriceInfo.setTicketCode(ticketCode);
            ticketPriceInfo.setPrice(price);
            ticketPriceInfo.setTicketStatus(ticketStatus);
            ticketPriceInfo.setRelevantTicketPriceList(relevantTicketPriceList);
            Integer amorpm = ticketPriceInfo.getAmorpm();
            Integer ticketSellType = null;
            if(amorpm==0||amorpm==1){
                ticketSellType=1;
            }else if(amorpm==2){
                ticketSellType=0;
            }
            ticketPriceInfo.setTicketSellType(ticketSellType);
            ticketPriceInfosList.add(ticketPriceInfo);
        }
        return  ticketPriceInfosList;
    }
}
