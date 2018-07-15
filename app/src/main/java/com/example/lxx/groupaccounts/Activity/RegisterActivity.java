package com.example.lxx.groupaccounts.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lxx.groupaccounts.Bean.ActivityCollector;
import com.example.lxx.groupaccounts.DB.UserTable;
import com.example.lxx.groupaccounts.R;

import org.litepal.crud.DataSupport;

import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    Button register;
    EditText userNameEditText,phoneEditText,passwordEditText,checkPasswordEditText;
    String userName,phone,password,checkPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActivityCollector.addActivity(this);

        userNameEditText = (EditText)findViewById(R.id.name);
        phoneEditText = (EditText)findViewById(R.id.phoneNumber);
        passwordEditText = (EditText)findViewById(R.id.password);
        checkPasswordEditText = (EditText)findViewById(R.id.check_password);
        register = (Button)findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

    }

    private void register(){
        userName = userNameEditText.getText().toString();
        phone = phoneEditText.getText().toString();
        password = passwordEditText.getText().toString();
        checkPassword = checkPasswordEditText.getText().toString();
        List<UserTable> userList = DataSupport.where("phone=?",phone).find(UserTable.class);
        if(!phone.equals("")){   //判断手机号是否为空
            if(!password.equals("")){  //判断密码是否为空
                if(userList.size()==0){   //判断手机号是否被注册
                    if(phone.length()==11) { //判断手机号格式是否正确
                        if (password.length() > 5 && password.length() < 16) { //判断密码是否是6到15位
                            if (password.equals(checkPassword)) { // 判断两次输入的密码是否一致
                                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                UserTable user = new UserTable();  //数据库增加用户
                                user.setPhone(phone);
                                user.setUserName(userName);
                                user.setPassword(password);
                                user.save();
                                Intent intent = new Intent();  //传递新注册的号给登陆界面
                                intent.putExtra("phone",phone);
                                intent.putExtra("password",password);
                                setResult(RESULT_OK,intent);
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(RegisterActivity.this, "密码要6到15位", Toast.LENGTH_SHORT).show();
                        }
                    } else{
                            Toast.makeText(RegisterActivity.this,"手机号格式不对",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(RegisterActivity.this,"该手机号已经被注册",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(RegisterActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(RegisterActivity.this,"手机号不能为空",Toast.LENGTH_SHORT).show();
            }
        }




    @Override
    protected void onDestroy(){
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

}
