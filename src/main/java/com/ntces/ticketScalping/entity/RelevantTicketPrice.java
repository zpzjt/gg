package com.ntces.ticketScalping.entity;

public class RelevantTicketPrice {

    //票据的区域(类型)：0:珍宝，1：钟表。2：大门
    private Integer ticketRegionCode;
    //票据的价格
    private Double ticketRegionPrice;

    public Integer getTicketRegionCode() {
        return ticketRegionCode;
    }

    public void setTicketRegionCode(Integer ticketRegionCode) {
        this.ticketRegionCode = ticketRegionCode;
    }

    public Double getTicketRegionPrice() {
        return ticketRegionPrice;
    }

    public void setTicketRegionPrice(Double ticketRegionPrice) {
        this.ticketRegionPrice = ticketRegionPrice;
    }

    @Override
    public String toString() {
        return "RelevantTicketPrice{" +
                "ticketRegionCode=" + ticketRegionCode +
                ", ticketRegionPrice=" + ticketRegionPrice +
                '}';
    }
}
