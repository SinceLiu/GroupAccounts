package com.example.lxx.groupaccounts.Fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lxx.groupaccounts.Activity.DetailsActivity;
import com.example.lxx.groupaccounts.Adapter.FriendAdapter;
import com.example.lxx.groupaccounts.Bean.Friend;
import com.example.lxx.groupaccounts.Bean.User;
import com.example.lxx.groupaccounts.DB.FriendTable;
import com.example.lxx.groupaccounts.DB.UserTable;
import com.example.lxx.groupaccounts.R;
import com.example.lxx.groupaccounts.Widget.SideBar;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FriendFragment extends Fragment{
    private ListView listView;
    private SideBar sideBar;
    private ArrayList<Friend> list;
    private View mView;
    private ImageButton addFriend;
    private String input;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState){
        mView = inflater.inflate(R.layout.fragment_friend,container,false);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        initView();
    }
    @Override
    public void onStart(){
        super.onStart();
        initData();
    }

    private void initView(){
        listView = (ListView)mView.findViewById(R.id.listview_friend);
        sideBar = (SideBar)mView.findViewById(R.id.side_bar);
        addFriend = (ImageButton)mView.findViewById(R.id.add);

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

        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFriend();
            }
        });

    }

    private void initData() {
        list = new ArrayList<>();
        //从Friend表获取数据
        List<FriendTable> friendTableList = DataSupport.where("user1Id=? or user2Id=?", User.getUserId(),User.getUserId())
                .find(FriendTable.class);
        List<String> friendIdList = new ArrayList<String>();  //friendList存取我的好友的id
        for(int i=0;i<friendTableList.size();i++){
            if(friendTableList.get(i).getUser1Id().equals(User.getUserId())) {
                friendIdList.add(friendTableList.get(i).getUser2Id());
            } else if(friendTableList.get(i).getUser2Id().equals(User.getUserId())){
                friendIdList.add(friendTableList.get(i).getUser1Id());
            }
        }
        //从User表获取我的好友
        for(int i=0;i<friendIdList.size();i++){
            UserTable user = DataSupport.where("phone=?",friendIdList.get(i)).findFirst(UserTable.class);
            Log.i("phone",friendIdList.toString());
           list.add(new Friend(user.getPhone(),user.getUserName()));
        }
        Collections.sort(list); // 对list进行排序，需要让Friend实现Comparable接口重写compareTo方法
        FriendAdapter adapter = new FriendAdapter(getActivity(), list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Friend friend = list.get(position);
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra("phone",friend.getPhone());
                intent.putExtra("name",friend.getName());
                startActivity(intent);
            }
        });
    }

    public void addFriend(){
       final EditText editText = new EditText(getActivity());
//        editText.setHint("输入手机号");
        new AlertDialog.Builder(getActivity())
                .setTitle("添加好友")
                .setIcon(null)
                .setView(editText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        input = editText.getText().toString();
                        if(input==null||input.equals("")){
                            Toast.makeText(getActivity(),"输入不能为空",Toast.LENGTH_SHORT).show();
                        }else if(input.equals(User.getUserId())){
                            Toast.makeText(getActivity(),"不能添加自己",Toast.LENGTH_SHORT).show();
                        }{
                            List<UserTable> userList = DataSupport.where("phone=?",input).find(UserTable.class);
                            if(userList.size()!=0){
                                List<FriendTable> friendTableList = DataSupport
                                        .where("user1Id=? and user2Id=? or user1Id=? and User2Id=?"
                                                ,User.getUserId(),input,input,User.getUserId())
                                        .find(FriendTable.class);
                                if(friendTableList.size()!=0){
                                    Toast.makeText(getActivity(),"已经是您的好友",Toast.LENGTH_SHORT).show();
                                }else{
                                    //Friend表添加数据
                                    FriendTable friendTable = new FriendTable();
                                    friendTable.setUser1Id(User.getUserId());
                                    friendTable.setUser2Id(input);
                                    friendTable.save();
                                    Toast.makeText(getActivity(),"添加成功",Toast.LENGTH_SHORT).show();
                                    initData();
                                }
                            }else
                                Toast.makeText(getActivity(),"帐号不存在",Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

}
