package com.example.lxx.groupaccounts.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.lxx.groupaccounts.Bean.ActivityCollector;
import com.example.lxx.groupaccounts.Fragment.ManageExpenditureFragment;
import com.example.lxx.groupaccounts.Fragment.ManageIncomeFragment;
import com.example.lxx.groupaccounts.R;
import com.example.lxx.groupaccounts.Widget.TimePickerDialog;

import java.util.ArrayList;
import java.util.List;

public class ManageRecordActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener,
        TimePickerDialog.TimePickerDialogInterface{
    private BottomNavigationBar mBottomNavigationBar;
    private ManageExpenditureFragment mManageExpenditureFragment;
    private ManageIncomeFragment mManageIncomeFragment;

    private TimePickerDialog timePickerDialog;
    private boolean isIncome;

    private List<Fragment> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_manage);
        ActivityCollector.addActivity(this);

    }

    @Override
    protected void onStart(){
        super.onStart();
        initViews();
    }

    private void initViews(){
        list = new ArrayList<Fragment>();
        Double money = getIntent().getDoubleExtra("money",0.00);
        mBottomNavigationBar = (BottomNavigationBar)findViewById(R.id.bottom_navigation_bar);
        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED)   //该模式未选中的Item显示文字，无切换动画效果
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
                .setActiveColor("#7CFC00")    //选中颜色
                .setInActiveColor("#FFFFFF")  //未选中颜色
                .setBarBackgroundColor("#D1D1D1") //导航栏背景颜色
                //添加导航按钮
                .addItem(new BottomNavigationItem(R.drawable.expenditure,"支出"))
                .addItem(new BottomNavigationItem(R.drawable.income,"收入"))
                .setFirstSelectedPosition(money<=0?0:1)
                .initialise();
        mBottomNavigationBar.setTabSelectedListener(this);
        if(money<=0)
            onTabSelected(0);
        else
            onTabSelected(1);

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
                if(mManageExpenditureFragment == null){
                    mManageExpenditureFragment = new ManageExpenditureFragment();
                    transaction.add(R.id.fragment_content,mManageExpenditureFragment);
                    list.add(mManageExpenditureFragment);
                }else{
                    transaction.show(mManageExpenditureFragment);
                }
                break;
            }
            case 1:{
                if(mManageIncomeFragment == null){
                    mManageIncomeFragment = new ManageIncomeFragment();
                    transaction.add(R.id.fragment_content,mManageIncomeFragment);
                    list.add(mManageIncomeFragment);
                }else {
                    transaction.show(mManageIncomeFragment);
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
        timePickerDialog = new TimePickerDialog(ManageRecordActivity.this);
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
            mManageIncomeFragment.setTime(year,month,day,hour,minute); //显示时间
        }else {
           mManageExpenditureFragment.setTime(year,month,day,hour,minute); //显示时间
        }
    }

    //时间选择器-------取消
    @Override
    public void negativeListener() {
    }
}