package com.example.lxx.groupaccounts.Bean;


import com.example.lxx.groupaccounts.Algorithm.Cn2Spell;

public class Friend implements Comparable<Friend> {
    private String phone; //手机号
    private String name;  //名字
    private String pinyin; // 名字对应的拼音
    private String firstLetter; //拼音的首字母

    public Friend(){
    }

    public Friend(String phone,String name){
        this.phone = phone;
        this.name = name;
        pinyin = Cn2Spell.getPinYin(name);
        firstLetter = pinyin.substring(0,1).toUpperCase(); //获取拼音首字母并转换成大写
        if(!firstLetter.matches("[A-Z]")){  //如果不再A-Z中则默认为“#”
            firstLetter = "#";
        }
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName(){
        return name;
    }
    public String getPinyin(){
        return pinyin;
    }
    public String getFirstLetter(){
        return firstLetter;
    }

    @Override
    public int compareTo(Friend another){
        if(firstLetter.equals("#")&&!another.getFirstLetter().equals("#")){
            return 1;
        }else if(!firstLetter.equals("#")&&another.getFirstLetter().equals("#")){
            return -1;
        }else {
            return pinyin.compareToIgnoreCase(another.getPinyin());
        }
    }
}
