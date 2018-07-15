package com.example.lxx.groupaccounts.DB;

import org.litepal.crud.DataSupport;

public class JoinsTable extends DataSupport {
    private String phone,groupId;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
