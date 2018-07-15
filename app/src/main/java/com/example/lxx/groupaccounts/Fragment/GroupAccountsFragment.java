package com.example.lxx.groupaccounts.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.lxx.groupaccounts.Activity.GroupAccountsActivity;
import com.example.lxx.groupaccounts.Activity.SelectMemberActivity;
import com.example.lxx.groupaccounts.Adapter.GroupAdapter;
import com.example.lxx.groupaccounts.Bean.Group;
import com.example.lxx.groupaccounts.Bean.Id;
import com.example.lxx.groupaccounts.Bean.User;
import com.example.lxx.groupaccounts.DB.GroupTable;
import com.example.lxx.groupaccounts.DB.JoinsTable;
import com.example.lxx.groupaccounts.R;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class GroupAccountsFragment extends Fragment {
    private ListView listView;
    private ArrayList<Group> groupList;
    private View mView;
    private ImageButton addGroup;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState){
        mView = inflater.inflate(R.layout.fragment_group_accounts,container,false);
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
        Log.i("onStart()","onstart");
        initData();
    }

    private void initView(){
        listView = (ListView)mView.findViewById(R.id.listview_group);
        addGroup = (ImageButton)mView.findViewById(R.id.add);

        addGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),SelectMemberActivity.class);
                intent.putExtra("isNewGroup",true);
                startActivityForResult(intent,1);
            }
        });
    }

    private void initData(){
        groupList = new ArrayList<Group>();
        //从Joins表获取数据
        List<JoinsTable> joinsTableList = DataSupport.where("phone=?",User.getUserId()).find(JoinsTable.class);
        List<String> groupIdList = new ArrayList<String>();  //groupIdList存取我所有群的id
        for(int i=0;i<joinsTableList.size();i++){
            groupIdList.add(joinsTableList.get(i).getGroupId());
            Log.i("群id",joinsTableList.get(i).getGroupId());
        }
        //从Group表获取我所有的群
        for(int i=0;i<groupIdList.size();i++){
            GroupTable group = DataSupport.where("groupId=?",groupIdList.get(i)).findFirst(GroupTable.class);
            groupList.add(new Group(group.getGroupId(),group.getGroupName()));
            Log.i("group",group.getGroupName()+" "+group.getGroupId());
        }
        GroupAdapter adapter = new GroupAdapter(getActivity(),groupList);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Group group = groupList.get(position);
                Intent intent = new Intent(getActivity(), GroupAccountsActivity.class);
                intent.putExtra("groupId",group.getId());
                startActivity(intent);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                //Group表增加数据
                GroupTable group = new GroupTable();
                String groupId = String.valueOf(Id.getGroupId());
                group.setGroupId(groupId);
                group.setGroupName(groupId);
                group.save();
                ArrayList<String> memberIdList = data.getStringArrayListExtra("memberIdList");
                //Joins表增加数据
                for(int i=0;i<memberIdList.size();i++){
                    JoinsTable joinsTable = new JoinsTable();
                    joinsTable.setGroupId(group.getGroupId());
                    joinsTable.setPhone(memberIdList.get(i));
                    joinsTable.save();
                }
                //把用户自己加进去
                JoinsTable joinsTable = new JoinsTable();
                joinsTable.setGroupId(group.getGroupId());
                joinsTable.setPhone(User.getUserId());
                joinsTable.save();
            }
        }
    }
}
