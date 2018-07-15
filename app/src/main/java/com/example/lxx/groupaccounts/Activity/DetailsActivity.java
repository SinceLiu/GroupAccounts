package com.example.lxx.groupaccounts.Activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lxx.groupaccounts.Bean.ActivityCollector;
import com.example.lxx.groupaccounts.Bean.User;
import com.example.lxx.groupaccounts.DB.FriendTable;
import com.example.lxx.groupaccounts.DB.UserTable;
import com.example.lxx.groupaccounts.R;

import org.litepal.crud.DataSupport;

public class DetailsActivity extends AppCompatActivity {
    private TextView nameTextView;
    private TextView phoneTextView;
    private TextView surplusTextView;
    private Button remove;
    private String input; //输出的备注
    private String phone;
    private String name;
    private boolean isUser1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ActivityCollector.addActivity(this);
        initView();
        initData();
    }

    public void  initView(){
        nameTextView = (TextView)findViewById(R.id.name);
        phoneTextView = (TextView)findViewById(R.id.phoneNumber);
        surplusTextView = (TextView)findViewById(R.id.surplus);
        remove = (Button)findViewById(R.id.remove);

        surplusTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSurplus();
            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFriend();
            }
        });
    }
    public void initData(){
        name = getIntent().getStringExtra("name");
        phone = getIntent().getStringExtra("phone");
        nameTextView.setText(name);
        phoneTextView.setText(phone);
        //访问Friend表,获取备注
        FriendTable friendTable = DataSupport.where("user1Id=? and user2Id=? or user1Id=? and User2Id=?"
                , phone,User.getUserId(),User.getUserId(),phone).findFirst(FriendTable.class);
        if(friendTable!=null){
            if(friendTable.getUser1Id().equals(phone)){
                isUser1 = true;
                surplusTextView.setText(friendTable.getUser2Surplus());
            }else {
                isUser1 = false;
                surplusTextView.setText(friendTable.getUser1Surplus());
            }
        }
    }

    public void changeSurplus(){
        final EditText editText = new EditText(DetailsActivity.this);
        new AlertDialog.Builder(DetailsActivity.this)
                .setTitle("修改备注")
                .setIcon(null)
                .setView(editText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        input = editText.getText().toString();
                        if(input==null||input.equals(""))
                            Toast.makeText(DetailsActivity.this,"输入不能为空",Toast.LENGTH_SHORT).show();
                        else{
                            surplusTextView.setText(input);
                            FriendTable friendTable = new FriendTable();
                            if(isUser1){
                                friendTable.setUser2Surplus(input);
                                friendTable.updateAll("user1Id=? and user2Id=?",phone,User.getUserId());
                            }else {
                                friendTable.setUser1Surplus(input);
                                friendTable.updateAll("user1Id=? and user2Id=?",User.getUserId(),phone);
                            }
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    public void removeFriend(){
        new AlertDialog.Builder(DetailsActivity.this)
                .setTitle("删除好友")
                .setIcon(null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                       if(isUser1){
                           DataSupport.deleteAll(FriendTable.class,"user1Id=? and user2Id=?",phone,User.getUserId());
                           finish();
                       }else {
                           DataSupport.deleteAll(FriendTable.class,"user1Id=? and user2Id=?",User.getUserId(),phone);
                           finish();
                       }
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

}
