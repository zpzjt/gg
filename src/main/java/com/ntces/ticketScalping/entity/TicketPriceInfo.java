package com.ntces.ticketScalping.entity;

import java.util.Date;
import java.util.List;

public class TicketPriceInfo {
    //购票日期
    private Date date;
    //上午还是下午：0是上午，1是下午,2是当日票
    private Integer amorpm;
    //购票类型来源:0是旅行社。1是快速购买
    private Integer travel;
    //票据全称
    private String ticketName;
    //票据在页面的标识
    private Integer ticketCode;
    //票价
    private Double price;
    //票据使用人群：0是学生，1是老人，2是成人
    private Integer ticketAppleCrowd;
    //票据相关区域及价格；
    private List<RelevantTicketPrice> RelevantTicketPriceList;
    //票据类型：0是今日票，1是预售票
    private Integer ticketSellType;
    //是否是联票，如果是联票，则该票据包含的相关区域的相关票价也必须全额支付；0:是联票。1：不是联票
    private Integer ticketStatus;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getAmorpm() {
        return amorpm;
    }

    public void setAmorpm(Integer amorpm) {
        this.amorpm = amorpm;
    }

    public String getTicketName() {
        return ticketName;
    }

    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
    }

    public Integer getTicketCode() {
        return ticketCode;
    }

    public void setTicketCode(Integer ticketCode) {
        this.ticketCode = ticketCode;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getTicketAppleCrowd() {
        return ticketAppleCrowd;
    }

    public void setTicketAppleCrowd(Integer ticketAppleCrowd) {
        this.ticketAppleCrowd = ticketAppleCrowd;
    }

    public List<RelevantTicketPrice> getRelevantTicketPriceList() {
        return RelevantTicketPriceList;
    }

    public void setRelevantTicketPriceList(List<RelevantTicketPrice> relevantTicketPriceList) {
        RelevantTicketPriceList = relevantTicketPriceList;
    }

    public Integer getTicketSellType() {
        return ticketSellType;
    }

    public void setTicketSellType(Integer ticketSellType) {
        this.ticketSellType = ticketSellType;
    }

    public Integer getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(Integer ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public Integer getTravel() {
        return travel;
    }

    public void setTravel(Integer travel) {
        this.travel = travel;
    }

    @Override
    public String toString() {
        return "TicketPriceInfo{" +
                "date=" + date +
                ", amorpm=" + amorpm +
                ", travel=" + travel +
                ", ticketName='" + ticketName + '\'' +
                ", ticketCode=" + ticketCode +
                ", price=" + price +
                ", ticketAppleCrowd=" + ticketAppleCrowd +
                ", RelevantTicketPriceList=" + RelevantTicketPriceList +
                ", ticketSellType=" + ticketSellType +
                ", ticketStatus=" + ticketStatus +
                '}';
    }
}
