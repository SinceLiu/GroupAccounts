package com.example.lxx.groupaccounts.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import com.example.lxx.groupaccounts.Adapter.FriendAdapter;
import com.example.lxx.groupaccounts.Adapter.SelectMemberAdapter;
import com.example.lxx.groupaccounts.Bean.ActivityCollector;
import com.example.lxx.groupaccounts.Bean.Friend;
import com.example.lxx.groupaccounts.Bean.Group;
import com.example.lxx.groupaccounts.Bean.Id;
import com.example.lxx.groupaccounts.Bean.User;
import com.example.lxx.groupaccounts.DB.FriendTable;
import com.example.lxx.groupaccounts.DB.GroupTable;
import com.example.lxx.groupaccounts.DB.JoinsTable;
import com.example.lxx.groupaccounts.DB.UserTable;
import com.example.lxx.groupaccounts.R;
import com.example.lxx.groupaccounts.Widget.SideBar;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class SelectMemberActivity extends AppCompatActivity {
    private ListView listView;
    private SideBar sideBar;
    private ArrayList<Friend> list;
    private Button okBtn;
    private HashMap<Integer,Boolean> isSelected;
    private Boolean isNewGroup;
    private ArrayList<String> oldMemberIdList;
    private SelectMemberAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_select);
        ActivityCollector.addActivity(this);
        initView();
        initData();
    }

    private void initView(){
        listView = (ListView)findViewById(R.id.select_friend);
        sideBar = (SideBar)findViewById(R.id.side_bar);
        okBtn = (Button)findViewById(R.id.btn_ok);

        sideBar.setOnStrSelectCallBack(new SideBar.ISideBarSelectCallBack(){
            @Override
            public void onSelectStr(int index,String selectStr){
                for(int i=0;i<list.size();i++){
                    if(selectStr.equalsIgnoreCase(list.get(i).getFirstLetter())){
                        listView.setSelection(i);   //选择到首字母出现的位置
                        return;
                    }
                }
            }
        });

        //点击确定
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> memberIdList = new ArrayList<String>();
                int selectNumber = 0;
                for(int i=0;i<list.size();i++){
                    boolean isSelected = SelectMemberAdapter.getIsSelected().get(i);
                    if(isSelected){
                        memberIdList.add(list.get(i).getPhone());
                        selectNumber++;
                    }
                }
                if(selectNumber>0){
                    Log.i("选择个数：",selectNumber+"");
                    Intent intent = new Intent();
                    intent.putStringArrayListExtra("memberIdList",memberIdList);//返回选择的成员的Id
                    Log.e("选择的成员id",String.valueOf(memberIdList));
                    setResult(RESULT_OK,intent);
                    finish();
                }else
                    Toast.makeText(SelectMemberActivity.this, "请选择好友", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void initData(){
        list = new ArrayList<>();
        isNewGroup = getIntent().getBooleanExtra("isNewGroup",true);
        //从Friend表获取数据
        List<FriendTable> friendTableList = DataSupport.where("user1Id=? or user2Id=?", User.getUserId(),User.getUserId())
                .find(FriendTable.class);
        List<String> friendIdList = new ArrayList<String>();  //friendList存取我的好友的id
        if (isNewGroup){
            for(int i=0;i<friendTableList.size();i++){
                if(friendTableList.get(i).getUser1Id().equals(User.getUserId())) {
                    friendIdList.add(friendTableList.get(i).getUser2Id());
                } else if(friendTableList.get(i).getUser2Id().equals(User.getUserId())){
                    friendIdList.add(friendTableList.get(i).getUser1Id());
                }
            }
        }else {  //如果不是新建群就筛选掉已经是成员的好友
            oldMemberIdList = getIntent().getStringArrayListExtra("oldMemberIdList");
            for(int i=0;i<friendTableList.size();i++){
                if(friendTableList.get(i).getUser1Id().equals(User.getUserId())) {
                    if(isOldMember(friendTableList.get(i).getUser2Id())==false)
                        friendIdList.add(friendTableList.get(i).getUser2Id());
                } else if(friendTableList.get(i).getUser2Id().equals(User.getUserId())){
                    if(isOldMember(friendTableList.get(i).getUser1Id())==false)
                        friendIdList.add(friendTableList.get(i).getUser1Id());
                }
            }
        }
        //从User表获取我的好友
        for(int i=0;i<friendIdList.size();i++){
            UserTable user = DataSupport.where("phone=?",friendIdList.get(i)).findFirst(UserTable.class);
            Log.i("phone",friendIdList.toString());
            list.add(new Friend(user.getPhone(),user.getUserName()));
        }
        Collections.sort(list); // 对list进行排序，需要让Friend实现Comparable接口重写compareTo方法
        isSelected = new HashMap<Integer,Boolean>();
        adapter = new SelectMemberAdapter(this, list,isSelected);
        listView.setAdapter(adapter);
    }

    public Boolean isOldMember(String id){
        for(int i=0;i<oldMemberIdList.size();i++){
            if (id.equals(oldMemberIdList.get(i)))
                return true;
        }
        return false;
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

}
