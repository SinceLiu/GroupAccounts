package com.example.lxx.groupaccounts.DB;


import org.litepal.crud.DataSupport;

public class FriendTable extends DataSupport{
    private String user1Id,user2Id;
    private String user1Surplus;
    private String user2Surplus;

    public String getUser1Id() {
        return user1Id;
    }

    public void setUser1Id(String user1Id) {
        this.user1Id = user1Id;
    }

    public String getUser2Surplus() {
        return user2Surplus;
    }

    public void setUser2Surplus(String user2Surplus) {
        this.user2Surplus = user2Surplus;
    }

    public String getUser2Id() {
        return user2Id;
    }

    public void setUser2Id(String user2Id) {
        this.user2Id = user2Id;
    }

    public String getUser1Surplus() {
        return user1Surplus;
    }

    public void setUser1Surplus(String user1Surplus) {
        this.user1Surplus = user1Surplus;
    }
}
