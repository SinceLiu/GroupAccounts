package com.example.lxx.groupaccounts.Fragment;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lxx.groupaccounts.Activity.LoginActivity;
import com.example.lxx.groupaccounts.Bean.ActivityCollector;
import com.example.lxx.groupaccounts.Bean.User;
import com.example.lxx.groupaccounts.DB.UserTable;
import com.example.lxx.groupaccounts.R;

import org.litepal.crud.DataSupport;

import java.util.List;


public class PersonalFragment extends Fragment {
    private View mView;
    private LinearLayout nameView;
    private LinearLayout changeView;
    private LinearLayout exitView;
    private TextView phoneTextView;
    private TextView nameTextView;
    private String input;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState){
        mView = inflater.inflate(R.layout.fragment_personal,container,false);
        return mView;
    }
    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
    }
    public void initView(){
        phoneTextView = (TextView)mView.findViewById(R.id.phone);
        nameView = (LinearLayout)mView.findViewById(R.id.layout_name);
        changeView = (LinearLayout)mView.findViewById(R.id.layout_change);
        exitView = (LinearLayout)mView.findViewById(R.id.layout_exit);
        nameTextView = (TextView)mView.findViewById(R.id.name);

        //修改昵称
        nameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出对话框修改昵称
                changeName();
            }
        });
        //切换账号
        changeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("切换账号")
                        .setMessage("确定吗")
                        .setPositiveButton("是",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent intent = new Intent(getActivity(),LoginActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("否", null)
                        .show();
            }
        });

        //退出
        exitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("退出")
                        .setMessage("确定吗")
                        .setPositiveButton("是",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                ActivityCollector.removeAllActivities();
                            }
                        })
                        .setNegativeButton("否", null)
                        .show();
            }
        });
    }

    public void initData(){
        List<UserTable> userList = DataSupport.where("phone=?", User.getUserId()).find(UserTable.class);
        UserTable user = userList.get(0);
        phoneTextView.setText(user.getPhone());
        nameTextView.setText(user.getUserName());

    }

    //修改昵称
    public void changeName(){
        final EditText editText = new EditText(getActivity());
        new AlertDialog.Builder(getActivity())
                .setTitle("修改昵称")
                .setIcon(null)
                .setView(editText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        input = editText.getText().toString();
                        if(input==null||input.equals(""))
                            Toast.makeText(getActivity(),"输入不能为空",Toast.LENGTH_SHORT).show();
                        else {
                            nameTextView.setText(input);
                            UserTable user = new UserTable();
                            user.setUserName(input);
                            user.updateAll("phone=?",User.getUserId());
                            Toast.makeText(getActivity(),"修改成功",Toast.LENGTH_SHORT).show();
                        }
                }
                })
                .setNegativeButton("取消", null)
                .show();
    }
}
