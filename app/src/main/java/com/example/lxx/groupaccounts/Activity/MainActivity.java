package com.example.lxx.groupaccounts.Activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.lxx.groupaccounts.Bean.Accounts;
import com.example.lxx.groupaccounts.Bean.ActivityCollector;
import com.example.lxx.groupaccounts.Bean.Id;
import com.example.lxx.groupaccounts.DB.AccountsTable;
import com.example.lxx.groupaccounts.DB.GroupTable;
import com.example.lxx.groupaccounts.Fragment.FriendFragment;
import com.example.lxx.groupaccounts.Fragment.GroupAccountsFragment;
import com.example.lxx.groupaccounts.Fragment.IndividualAccountsFragment;
import com.example.lxx.groupaccounts.Fragment.PersonalFragment;
import com.example.lxx.groupaccounts.R;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {

    private BottomNavigationBar mBottomNavigationBar;
    private BadgeItem mBadgeItem;

    private IndividualAccountsFragment mIndividualAccountsFragment;
    private GroupAccountsFragment mGroupAccountsFragment;
    private FriendFragment mFriendFragment;
    private PersonalFragment mPersonalFragment;

    private List<Fragment> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCollector.addActivity(this);
        initViews();
        //设置id为当前最大id+1
        List<AccountsTable> accountsList= DataSupport.order("accountsId desc").find(AccountsTable.class);
        if(accountsList.size()!=0){
            Id.setAccountsId(Long.parseLong(accountsList.get(0).getAccountsId())+1);
        }
        List<GroupTable> groupList = DataSupport.order("groupId desc").find(GroupTable.class);
        if(groupList.size()!=0){
            Id.setGroupId(Long.parseLong(groupList.get(0).getGroupId())+1);
        }
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
        .addItem(new BottomNavigationItem(R.drawable.individual_accounts,"个人记账"))
        .addItem(new BottomNavigationItem(R.drawable.group_accounts,"群记账"))
        .addItem(new BottomNavigationItem(R.drawable.friend,"好友"))
        .addItem(new BottomNavigationItem(R.drawable.personal,"我"))
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
                if(mIndividualAccountsFragment == null){
                    mIndividualAccountsFragment = new IndividualAccountsFragment();
                    transaction.add(R.id.fragment_content,mIndividualAccountsFragment);
                    list.add(mIndividualAccountsFragment);
                }else{
                    transaction.show(mIndividualAccountsFragment);
                }
                break;
            }
            case 1:{
                if(mGroupAccountsFragment == null){
                    mGroupAccountsFragment = new GroupAccountsFragment();
                    transaction.add(R.id.fragment_content,mGroupAccountsFragment);
                    list.add(mGroupAccountsFragment);
                }else {
                    transaction.show(mGroupAccountsFragment);
                }
                break;
            }
            case 2:{
                if(mFriendFragment == null){
                    mFriendFragment = new FriendFragment();
                    transaction.add(R.id.fragment_content,mFriendFragment);
                    list.add(mFriendFragment);
                }else {
                    transaction.show(mFriendFragment);
                }
                break;
            }
            case 3:{
                if(mPersonalFragment == null){
                    mPersonalFragment = new PersonalFragment();
                    transaction.add(R.id.fragment_content,mPersonalFragment);
                    list.add(mPersonalFragment);
                }else {
                    transaction.show(mPersonalFragment);
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
}
