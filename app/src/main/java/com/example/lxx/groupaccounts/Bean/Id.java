package com.example.lxx.groupaccounts.Bean;



public class Id {
    static long accountsId = 1000000;  //7位;
    static long groupId = 100000;  //6位

    public static long getAccountsId() {
        return accountsId++;
    }

    public static long getGroupId() {
        return groupId++;
    }

    public static void setAccountsId(long accountsId) {
        Id.accountsId = accountsId;
    }

    public static void setGroupId(long groupId) {
        Id.groupId = groupId;
    }
}
