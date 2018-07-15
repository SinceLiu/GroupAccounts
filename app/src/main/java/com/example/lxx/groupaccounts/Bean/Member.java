package com.example.lxx.groupaccounts.Bean;

public class Member {
    private String memberId;
    private Integer image;
    private String name;

    public Member(){
    }

    public Member(Integer image,String memberId,String name){
        this.image = image;
        this.memberId = memberId;
        this.name = name;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
