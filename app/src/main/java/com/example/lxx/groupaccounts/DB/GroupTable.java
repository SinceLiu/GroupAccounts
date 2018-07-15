package com.example.lxx.groupaccounts.DB;

import org.litepal.crud.DataSupport;

public class GroupTable extends DataSupport{
    private String groupId;
    private String groupName;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
