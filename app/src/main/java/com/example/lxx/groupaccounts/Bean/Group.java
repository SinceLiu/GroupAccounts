package com.example.lxx.groupaccounts.Bean;

public class Group {
    private String groupId;
    private String name;

    public Group(){
    }

    public Group(String groupId,String name){
        this.groupId = groupId;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
