package com.example.lxx.groupaccounts.Activity;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lxx.groupaccounts.Adapter.AccountsAdapter;
import com.example.lxx.groupaccounts.Bean.Accounts;
import com.example.lxx.groupaccounts.Bean.ActivityCollector;
import com.example.lxx.groupaccounts.Bean.DailyAccounts;
import com.example.lxx.groupaccounts.Bean.User;
import com.example.lxx.groupaccounts.DB.AccountsTable;
import com.example.lxx.groupaccounts.Fragment.ManageExpenditureFragment;
import com.example.lxx.groupaccounts.R;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.security.auth.login.LoginException;

public class AccountsActivity extends AppCompatActivity {
    private ListView listView;
    private AccountsAdapter accountsAdapter;
    private String ownerId;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);
        listView = (ListView)findViewById(R.id.accounts);
        ActivityCollector.addActivity(this);
    }
    @Override
    protected void onStart(){
        super.onStart();
        init();
    }

    private void init(){
        ownerId = getIntent().getStringExtra("ownerId");
        List<Accounts> list = new ArrayList<Accounts>();
        Cursor cursor = DataSupport.findBySQL("select distinct year,month from AccountsTable where ownerId = ? " +
                "order by year DESC,month DESC", ownerId);
        if(cursor.moveToFirst()){
            do{
                Double expenditureMonth = 0.0;
                Double incomeMonth =0.0;
                Double surplus=0.0;
                List<AccountsTable> accountsList = DataSupport.where("ownerId =? and year=? and month =?",
                        ownerId, cursor.getString(cursor.getColumnIndex("year")),cursor.getString(cursor.getColumnIndex("month")))
                        .order("year desc,month desc,day desc,hour desc,minute desc")
                        .find(AccountsTable.class);
                List<DailyAccounts> dailyAccountsList = new ArrayList<DailyAccounts>();
                for(int i=0;i<accountsList.size();i++){
                    dailyAccountsList.add(new DailyAccounts(accountsList.get(i).getAccountsId(),accountsList.get(i).getDay(),accountsList.get(i).getType(),
                            accountsList.get(i).getHour(),accountsList.get(i).getMinute(),accountsList.get(i).getMoney(),
                            accountsList.get(i).getSurplus()));
                    Log.i("记账id",accountsList.get(i).getAccountsId());
                    if(accountsList.get(i).getMoney()<0){
                        expenditureMonth = expenditureMonth + accountsList.get(i).getMoney();
                    }else{
                        incomeMonth = incomeMonth + accountsList.get(i).getMoney();
                    }
                }
                surplus = expenditureMonth + incomeMonth;
                Collections.sort(dailyAccountsList);
                list.add(new Accounts( cursor.getString(cursor.getColumnIndex("year")), cursor.getString(cursor.getColumnIndex("month")),
                        incomeMonth,expenditureMonth,surplus,dailyAccountsList));
            }while(cursor.moveToNext());
        }
        cursor.close();
        accountsAdapter = new AccountsAdapter(this,list);
        listView.setAdapter(accountsAdapter);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
