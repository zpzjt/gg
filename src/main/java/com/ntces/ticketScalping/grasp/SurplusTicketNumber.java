package com.ntces.ticketScalping.grasp;

import com.ntces.ticketScalping.entity.SurplusTicket;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SurplusTicketNumber {
    private static final Logger logger = LoggerFactory.getLogger(SurplusTicketNumber.class);
    private final static String URL_SURPLUS_TICKET_NUMBER = "https://gugong.ktmtech.cn/Home/Index";

    public static void main(String[] args) throws IOException, ParseException {
        SurplusTicketNumber surplusTicketNumber = new SurplusTicketNumber();
        CloseableHttpClient client = surplusTicketNumber.openConnection();
        List<SurplusTicket> surplusTicketList = surplusTicketNumber.selectSurplusTicketNumber(client);
        for (SurplusTicket s : surplusTicketList) {
            logger.info("日期：[{}],是否开馆：[{}],剩余票数量:[{}]", s.getDate(), s.getStatus(), s.getNumber());
        }
    }

    private CloseableHttpClient openConnection() {
        CookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        return httpclient;
    }


    public List<SurplusTicket> selectSurplusTicketNumber(HttpClient client) throws IOException, ParseException {
        HttpGet httpGet = new HttpGet(URL_SURPLUS_TICKET_NUMBER);
        HttpResponse httpResponse = client.execute(httpGet);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        HttpEntity entity = httpResponse.getEntity();
        if (statusCode == 200 && entity != null) {
            return analysisEntity(entity);
        } else {
            return null;
        }
    }

    public List<SurplusTicket> analysisEntity(HttpEntity entity) throws IOException, ParseException {
        String entityStr = EntityUtils.toString(entity);
        System.out.println(entityStr);
        Document document = Jsoup.parse(entityStr);
        //查找id为dapm的ul元素下的全部li元素
        Elements elements = document.select("ul#dapm>li");
        List<SurplusTicket> surplusTicketList = new ArrayList<>();
        Date now = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String year = simpleDateFormat.format(now).substring(0, 5);
        for (Element element : elements) {
            SurplusTicket surplusTicket = new SurplusTicket();
            //如果li元素中含有class类名代表闭馆
            if (element.hasClass("closed")&& element.children().size()!=0) {
                surplusTicket.setStatus(1);
                //li中的b中的文本代表日期
                String date = element.select("b").text().replace("月", "-");
                date = year + date.substring(0, date.length() - 1);
                surplusTicket.setDate(simpleDateFormat.parse(date));
                surplusTicket.setNumber(0);
            } else if(element.children().size()!=0){
                surplusTicket.setStatus(0);
                String date = element.select("b").text().replace("月", "-");
                date = year + date.substring(0, date.length() - 1);
                //使用element.ownText()可以获取element中的直接文本，出去element中的子元素的文本
                String liText = element.ownText();
                Integer number = Integer.valueOf(liText.substring(1, liText.length() - 1));
                surplusTicket.setDate(simpleDateFormat.parse(date));
                surplusTicket.setNumber(number);
            }
            surplusTicketList.add(surplusTicket);
        }
        return surplusTicketList;
    }
}
