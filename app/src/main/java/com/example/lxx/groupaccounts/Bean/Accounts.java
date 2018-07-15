package com.example.lxx.groupaccounts.Bean;


import java.util.List;

public class Accounts {
    private String month;
    private Double income;
    private Double expenditure;
    private Double surplus;
    private List<DailyAccounts> list;

    public Accounts(String year,String month,Double income,Double expenditure,Double surplus,List<DailyAccounts> list){

        this.month = year+"年"+month+"月";
        this.income = income;
        this.expenditure = expenditure;
        this.surplus = surplus;
        this.list = list;
    }


    public Double getExpenditure() {
        return expenditure;
    }

    public void setExpenditure(Double expenditure) {
        this.expenditure = expenditure;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Double getIncome() {
        return income;
    }

    public void setIncome(Double income) {
        this.income = income;
    }

    public Double getSurplus() {
        return surplus;
    }

    public void setSurplus(Double surplus) {
        this.surplus = surplus;
    }

    public List<DailyAccounts> getList() {
        return list;
    }

    public void setList(List<DailyAccounts> list) {
        this.list = list;
    }
}
