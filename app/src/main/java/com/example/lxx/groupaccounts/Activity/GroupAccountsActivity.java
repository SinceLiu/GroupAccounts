package com.example.lxx.groupaccounts.Activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lxx.groupaccounts.Adapter.MemberAdapter;
import com.example.lxx.groupaccounts.Bean.ActivityCollector;
import com.example.lxx.groupaccounts.Bean.Id;
import com.example.lxx.groupaccounts.Bean.Member;
import com.example.lxx.groupaccounts.Bean.User;
import com.example.lxx.groupaccounts.DB.AccountsTable;
import com.example.lxx.groupaccounts.DB.GroupTable;
import com.example.lxx.groupaccounts.DB.JoinsTable;
import com.example.lxx.groupaccounts.DB.UserTable;
import com.example.lxx.groupaccounts.R;

import org.litepal.crud.DataSupport;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class GroupAccountsActivity extends AppCompatActivity {
    private GridView gridView;
    private List<Member> list;
    private Button record;
    private LinearLayout recentAccounts;
    private String groupId;
    private TextView expenditureMonthTextView;
    private TextView incomeMonthTextView;
    private TextView surplusTextView;
    private TextView type1,type2,type3,type4;
    private TextView time1,time2,time3,time4;
    private TextView money1,money2,money3,money4;
    private TextView groupNameTextView;
    private String input;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_accounts);
        ActivityCollector.addActivity(this);
        initView();
    }
    @Override
    public void onStart(){
        super.onStart();
        initData();
    }
    private void initView(){
        expenditureMonthTextView = (TextView)findViewById(R.id.expenditureThisMonth);
        incomeMonthTextView = (TextView)findViewById(R.id.incomeThisMonth);
        surplusTextView = (TextView)findViewById(R.id.surplus);
        type1 = (TextView)findViewById(R.id.type_account1);
        type2 = (TextView)findViewById(R.id.type_account2);
        type3 = (TextView)findViewById(R.id.type_account3);
        type4 = (TextView)findViewById(R.id.type_account4);
        time1 = (TextView)findViewById(R.id.time_account1);
        time2 = (TextView)findViewById(R.id.time_account2);
        time3 = (TextView)findViewById(R.id.time_account3);
        time4 = (TextView)findViewById(R.id.time_account4);
        money1 = (TextView)findViewById(R.id.money_account1);
        money2 = (TextView)findViewById(R.id.money_account2);
        money3 = (TextView)findViewById(R.id.money_account3);
        money4 = (TextView)findViewById(R.id.money_account4);
        gridView = (GridView)findViewById(R.id.members);
        groupNameTextView = (TextView)findViewById(R.id.name_group);
        record = (Button)findViewById(R.id.record);
        recentAccounts = (LinearLayout)findViewById(R.id.recent_accounts);

        //点击成员头像查看好友信息或者点击添加成员
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0){
                    if(position<list.size()){
                        Member member = list.get(position);
                        Intent intent = new Intent(GroupAccountsActivity.this,DetailsActivity.class);
                        intent.putExtra("phone",member.getMemberId());
                        intent.putExtra("name",member.getName());
                        startActivity(intent);
                    }else {
                        ArrayList<String> oldMemberIdList = new ArrayList<String>();
                        for(int i=0;i<list.size();i++){
                            oldMemberIdList.add(list.get(i).getMemberId());
                        }
                        Intent intent = new Intent(GroupAccountsActivity.this,SelectMemberActivity.class);
                        intent.putExtra("isNewGroup",false);
                        intent.putStringArrayListExtra("oldMemberIdList",oldMemberIdList);
                        startActivityForResult(intent,1);
                    }
                }
            }
        });

        //点击记一笔
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupAccountsActivity.this,RecordActivity.class);
                intent.putExtra("ownerId",groupId);
                startActivity(intent);
            }
        });

        groupNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeGroupName();
            }
        });

        //点击最新四笔查看群账单
        recentAccounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupAccountsActivity.this,AccountsActivity.class);
                intent.putExtra("ownerId",groupId);
                startActivity(intent);
            }
        });
    }

    private void initData(){
        //显示成员
        Intent intent = getIntent();
        groupId = intent.getStringExtra("groupId");
        List<JoinsTable> joinsTableList = DataSupport.where("groupId=?",groupId).find(JoinsTable.class);//查询Joins表
        UserTable user = DataSupport.where("phone=?",User.getUserId()).findFirst(UserTable.class);
        list = new ArrayList<Member>();
        list.add(new Member(R.drawable.icon, User.getUserId(),user.getUserName())); //第一个设置为用户自己
        for(int i=0;i<joinsTableList.size();i++){
            if(joinsTableList.get(i).getPhone().equals(User.getUserId())==false){
                UserTable userTable = DataSupport.where("phone=?",joinsTableList.get(i).getPhone()).findFirst(UserTable.class);
                list.add(new Member(R.drawable.icon,userTable.getPhone(),userTable.getUserName()));
            }
        }
        MemberAdapter memberAdapter = new MemberAdapter(GroupAccountsActivity.this,list);
        gridView.setAdapter(memberAdapter);

        //最近账目
        Double expenditureMonth = 0.0;
        Double incomeMonth = 0.0;
        Calendar calendar = Calendar.getInstance();
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH)+1);
        while(month.length()<2){
            month = "0"+month;
        }
        List<AccountsTable> accountsList = DataSupport.where("ownerId =? and year=? and month =?", //从数据库获取数据
                groupId,year,month)
                .order("day desc,hour desc,minute desc")
                .find(AccountsTable.class);
        for(int i=0;i<accountsList.size();i++){
            if(accountsList.get(i).getMoney()>0)
                incomeMonth = incomeMonth + accountsList.get(i).getMoney();
            else
                expenditureMonth = expenditureMonth + accountsList.get(i).getMoney();
        }
        if(accountsList.size()>=1){
            type1.setText(accountsList.get(0).getType());
            time1.setText(accountsList.get(0).getMonth()+"-"+accountsList.get(0).getDay()+" "
                    +accountsList.get(0).getHour()+":"+accountsList.get(0).getMinute());
            money1.setText(String.valueOf(accountsList.get(0).getMoney()));
            if(accountsList.get(0).getMoney()>0)
                money1.setTextColor(getResources().getColor(R.color.colorIncome,null));
            else
                money1.setTextColor(getResources().getColor(R.color.colorExpenditure,null));
        }
        incomeMonthTextView.setText(String.valueOf(incomeMonth));
        expenditureMonthTextView.setText(String.valueOf(expenditureMonth));
        surplusTextView.setText(String.valueOf(incomeMonth+expenditureMonth));
        if(accountsList.size()>=2){
            type2.setText(accountsList.get(1).getType());
            time2.setText(accountsList.get(1).getMonth()+"-"+accountsList.get(1).getDay()+" "
                    +accountsList.get(1).getHour()+":"+accountsList.get(1).getMinute());
            money2.setText(String.valueOf(accountsList.get(1).getMoney()));
            if(accountsList.get(1).getMoney()>0)
                money2.setTextColor(getResources().getColor(R.color.colorIncome,null));
            else
                money2.setTextColor(getResources().getColor(R.color.colorExpenditure,null));
        }
        if(accountsList.size()>=3){
            type3.setText(accountsList.get(2).getType());
            time3.setText(accountsList.get(2).getMonth()+"-"+accountsList.get(2).getDay()+" "
                    +accountsList.get(2).getHour()+":"+accountsList.get(2).getMinute());
            money3.setText(String.valueOf(accountsList.get(2).getMoney()));
            if(accountsList.get(2).getMoney()>0)
                money3.setTextColor(getResources().getColor(R.color.colorIncome,null));
            else
                money3.setTextColor(getResources().getColor(R.color.colorExpenditure,null));
        }
        if(accountsList.size()>=4){
            type4.setText(accountsList.get(3).getType());
            time4.setText(accountsList.get(3).getMonth()+"-"+accountsList.get(3).getDay()+" "
                    +accountsList.get(3).getHour()+":"+accountsList.get(3).getMinute());
            money4.setText(String.valueOf(accountsList.get(3).getMoney()));
            if(accountsList.get(3).getMoney()>0)
                money4.setTextColor(getResources().getColor(R.color.colorIncome,null));
            else
                money4.setTextColor(getResources().getColor(R.color.colorExpenditure,null));
        }
    }

    public void changeGroupName(){
        final EditText editText = new EditText(GroupAccountsActivity.this);
        new AlertDialog.Builder(GroupAccountsActivity.this)
                .setTitle("修改昵称")
                .setIcon(null)
                .setView(editText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        input = editText.getText().toString();
                        if(input==null||input.equals(""))
                            Toast.makeText(GroupAccountsActivity.this,"输入不能为空",Toast.LENGTH_SHORT).show();
                        else {
                            groupNameTextView.setText(input);
                            GroupTable groupTable = new GroupTable();
                            groupTable.setGroupName(input);
                            groupTable.updateAll("groupId=?",groupId);
                            Toast.makeText(GroupAccountsActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                ArrayList<String> memberIdList = data.getStringArrayListExtra("memberIdList");
                Log.e("添加群成员",String.valueOf(memberIdList));
                //Joins表增加数据
                for(int i=0;i<memberIdList.size();i++){
                    JoinsTable joinsTable = new JoinsTable();
                    joinsTable.setGroupId(groupId);
                    joinsTable.setPhone(memberIdList.get(i));
                    joinsTable.save();
                }
            }
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
