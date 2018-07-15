package com.example.lxx.groupaccounts.Activity;


import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.lxx.groupaccounts.Bean.ActivityCollector;
import com.example.lxx.groupaccounts.Fragment.ExpenditureFragment;
import com.example.lxx.groupaccounts.Fragment.IncomeFragment;
import com.example.lxx.groupaccounts.R;
import com.example.lxx.groupaccounts.Widget.TimePickerDialog;

import java.util.ArrayList;
import java.util.List;

public class RecordActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener,
        TimePickerDialog.TimePickerDialogInterface{
    private BottomNavigationBar mBottomNavigationBar;
    private ExpenditureFragment mExpenditureFragment;
    private IncomeFragment mIncomeFragment;
    private boolean isIncome;

    private TimePickerDialog timePickerDialog;

    private List<Fragment> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        ActivityCollector.addActivity(this);
        initViews();
    }

    private void initViews(){
        list = new ArrayList<Fragment>();
        mBottomNavigationBar = (BottomNavigationBar)findViewById(R.id.bottom_navigation_bar);
        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED)   //该模式未选中的Item显示文字，无切换动画效果
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
                .setActiveColor("#7CFC00")    //选中颜色
                .setInActiveColor("#FFFFFF")  //未选中颜色
                .setBarBackgroundColor("#D1D1D1") //导航栏背景颜色
                //添加导航按钮
                .addItem(new BottomNavigationItem(R.drawable.expenditure,"支出"))
                .addItem(new BottomNavigationItem(R.drawable.income,"收入"))
                .setFirstSelectedPosition(0)
                .initialise();
        mBottomNavigationBar.setTabSelectedListener(this);

//        因为首次进入不会主动回调选中页面的监听，所以这里手动调用一遍，初始化第一个页面
        onTabSelected(0);
    }
    //设置选中的Fragment事件
    @Override
    public void onTabSelected(int position){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //每次添加之前隐藏所有正在显示的Fragment
        hideFragment(transaction);
        switch(position){
            case 0:{
                if(mExpenditureFragment == null){
                    mExpenditureFragment = new ExpenditureFragment();
                    transaction.add(R.id.fragment_content,mExpenditureFragment);
                    list.add(mExpenditureFragment);
                }else{
                    transaction.show(mExpenditureFragment);
                }
                break;
            }
            case 1:{
                if(mIncomeFragment == null){
                    mIncomeFragment = new IncomeFragment();
                    transaction.add(R.id.fragment_content,mIncomeFragment);
                    list.add(mIncomeFragment);
                }else {
                    transaction.show(mIncomeFragment);
                }
                break;
            }
            default:{
                break;
            }
        }
        transaction.commit();
    }

    //设置未选中的Fragment事件
    @Override
    public void onTabUnselected(int positon){

    }

    //设置再次选中的Fragment事件
    @Override
    public void onTabReselected(int position){

    }

    //隐藏所有正在显示的Fragment
    private void hideFragment(FragmentTransaction transaction){
        for(Fragment fragment:list){
            transaction.hide(fragment);
        }
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }


    //时间选择对话框
    public void newTime(boolean isIncome){
        this.isIncome = isIncome;
        timePickerDialog = new TimePickerDialog(RecordActivity.this);
        timePickerDialog.showDateAndTimePickerDialog();
    }

    //时间选择器---点击确定
    @Override
    public void positiveListener() {
        String year = String.valueOf(timePickerDialog.getYear());
        String month = String.valueOf(timePickerDialog.getMonth());
        String day = String.valueOf(timePickerDialog.getDay());
        String hour = String.valueOf(timePickerDialog.getHour());
        String minute = String.valueOf(timePickerDialog.getMinute());
        if(isIncome){
            mIncomeFragment.setTime(year,month,day,hour,minute); //editText显示时间
        }else {
            mExpenditureFragment.setTime(year,month,day,hour,minute); //editText显示时间
        }
    }

    //时间选择器-------取消
    @Override
    public void negativeListener() {
    }
}
