package com.example.lxx.groupaccounts.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lxx.groupaccounts.Bean.ActivityCollector;
import com.example.lxx.groupaccounts.Bean.User;
import com.example.lxx.groupaccounts.DB.AccountsTable;
import com.example.lxx.groupaccounts.DB.FriendTable;
import com.example.lxx.groupaccounts.DB.GroupTable;
import com.example.lxx.groupaccounts.DB.JoinsTable;
import com.example.lxx.groupaccounts.DB.UserTable;
import com.example.lxx.groupaccounts.R;
import com.example.lxx.groupaccounts.Widget.TimePickerDialog;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private Button login;
    private Button register;
    private EditText phoneEditext;
    private EditText passwordEditext;
    private String phone;
    private String password;

    private TimePickerDialog timePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActivityCollector.addActivity(this);
        LitePal.getDatabase(); //创建本地数据库
        login = (Button)findViewById(R.id.login);
        register = (Button)findViewById(R.id.register);
        phoneEditext = (EditText)findViewById(R.id.phoneNumber);
        passwordEditext = (EditText)findViewById(R.id.password);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivityForResult(intent,1);
            }
        });
    }

    private void login(){
        phone = phoneEditext.getText().toString();
        password = passwordEditext.getText().toString();

        if(!phone.equals("")){   //判断手机号是否为空
            if(!password.equals("")){  //判断密码是否为空
                List<UserTable> userList = DataSupport.where("phone=?",phone).find(UserTable.class);
                if(userList.size()!=0){
                    UserTable user = userList.get(0);
                    if(password.equals(user.getPassword())){
                        User.setUserId(phone);  //记录登入的手机号
                       // 清空数据库表
//                         DataSupport.deleteAll(AccountsTable.class);
//                        DataSupport.deleteAll(GroupTable.class);
//                        DataSupport.deleteAll(FriendTable.class);
//                      DataSupport.deleteAll(JoinsTable.class);
//                        DataSupport.deleteAll(UserTable.class);
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                    }else
                        Toast.makeText(LoginActivity.this,"密码错误",Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(LoginActivity.this,"账号不存在",Toast.LENGTH_SHORT).show();
            }else
                Toast.makeText(LoginActivity.this,"请输入密码",Toast.LENGTH_SHORT).show();
        }else
            Toast.makeText(LoginActivity.this,"请输入手机号",Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                phoneEditext.setText(data.getStringExtra("phone"));
                passwordEditext.setText(data.getStringExtra("password"));
            }
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
