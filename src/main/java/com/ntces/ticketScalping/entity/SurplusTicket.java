package com.ntces.ticketScalping.entity;

import java.util.Date;

public class SurplusTicket {
    //0是正常，1是闭馆,2是当日
    private Integer status;
    //日期
    private Date date;
    //数量
    private Integer number;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
