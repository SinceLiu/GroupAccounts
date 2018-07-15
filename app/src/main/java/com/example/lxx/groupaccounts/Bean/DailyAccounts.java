package com.example.lxx.groupaccounts.Bean;


import java.util.Date;

public class DailyAccounts implements Comparable<DailyAccounts> {
    private String accountsId;
    private String type;
    private String time;
    private Double money;
    private String surplus;
    private String day;

    public DailyAccounts(){
    }

    public DailyAccounts(String accountsId,String day, String type, String hour,String minute, Double money, String surplus){
        this.accountsId = accountsId;
        this.day = day;
        this.type = type;
        this.time = hour+":"+minute;
        this.money = money;
        this.surplus = surplus;

    }

    public String getAccountsId() {
        return accountsId;
    }

    public void setAccountsId(String accountsId) {
        this.accountsId = accountsId;
    }

    public String getSurplus() {
        return surplus;
    }

    public void setSurplus(String surplus) {
        this.surplus = surplus;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }


    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int compareTo(DailyAccounts another){
        return another.day.compareToIgnoreCase(day);
    }
}
