package com.example.lxx.groupaccounts.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lxx.groupaccounts.Activity.AccountsActivity;
import com.example.lxx.groupaccounts.Activity.RecordActivity;
import com.example.lxx.groupaccounts.Bean.Friend;
import com.example.lxx.groupaccounts.Bean.Id;
import com.example.lxx.groupaccounts.Bean.User;
import com.example.lxx.groupaccounts.DB.AccountsTable;
import com.example.lxx.groupaccounts.R;

import org.litepal.crud.DataSupport;

import java.util.Calendar;
import java.util.List;

public class IndividualAccountsFragment extends Fragment {
    private View mView;
    private Button record;
    private LinearLayout recentAccounts;
    private TextView expenditureMonthTextView;
    private TextView incomeMonthTextView;
    private TextView type1,type2,type3,type4;
    private TextView time1,time2,time3,time4;
    private TextView money1,money2,money3,money4;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState){
        mView = inflater.inflate(R.layout.fragment_individual_accounts,container,false);
        return mView;
    }
    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        record = (Button)mView.findViewById(R.id.record);
        recentAccounts = (LinearLayout)mView.findViewById(R.id.recent_accounts) ;
        expenditureMonthTextView = (TextView)mView.findViewById(R.id.expenditureThisMonth);
        incomeMonthTextView = (TextView)mView.findViewById(R.id.incomeThisMonth);
        type1 = (TextView)mView.findViewById(R.id.type_account1);
        type2 = (TextView)mView.findViewById(R.id.type_account2);
        type3 = (TextView)mView.findViewById(R.id.type_account3);
        type4 = (TextView)mView.findViewById(R.id.type_account4);
        time1 = (TextView)mView.findViewById(R.id.time_account1);
        time2 = (TextView)mView.findViewById(R.id.time_account2);
        time3 = (TextView)mView.findViewById(R.id.time_account3);
        time4 = (TextView)mView.findViewById(R.id.time_account4);
        money1 = (TextView)mView.findViewById(R.id.money_account1);
        money2 = (TextView)mView.findViewById(R.id.money_account2);
        money3 = (TextView)mView.findViewById(R.id.money_account3);
        money4 = (TextView)mView.findViewById(R.id.money_account4);

        //点击记一笔
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),RecordActivity.class);
                Friend friend = new Friend();
                intent.putExtra("ownerId",User.getUserId());
                startActivity(intent);
            }
        });

        //点击最近的账目查看总账单
        recentAccounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),AccountsActivity.class);
                intent.putExtra("ownerId",User.getUserId());
                startActivity(intent);
            }
        });
    }
    @Override
    public void onStart(){
        super.onStart();
        initData();
    }

    public void initData(){
        Double expenditureMonth = 0.0;
        Double incomeMonth = 0.0;
        Calendar calendar = Calendar.getInstance();
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH)+1);
        while(month.length()<2){
            month = "0"+month;
        }
        //从数据库获取数据
        List<AccountsTable> accountsList = DataSupport.where("ownerId =? and year=? and month =?",
                User.getUserId(),year,month)
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
                money1.setTextColor(mView.getResources().getColor(R.color.colorIncome,null));
            else
                money1.setTextColor(mView.getResources().getColor(R.color.colorExpenditure,null));
        }
        incomeMonthTextView.setText("+"+incomeMonth);
        expenditureMonthTextView.setText(String.valueOf(expenditureMonth));
        if(accountsList.size()>=2){
            type2.setText(accountsList.get(1).getType());
            time2.setText(accountsList.get(1).getMonth()+"-"+accountsList.get(1).getDay()+" "
                    +accountsList.get(1).getHour()+":"+accountsList.get(1).getMinute());
            money2.setText(String.valueOf(accountsList.get(1).getMoney()));
            if(accountsList.get(1).getMoney()>0)
                money2.setTextColor(mView.getResources().getColor(R.color.colorIncome,null));
            else
                money2.setTextColor(mView.getResources().getColor(R.color.colorExpenditure,null));
        }
        if(accountsList.size()>=3){
            type3.setText(accountsList.get(2).getType());
            time3.setText(accountsList.get(2).getMonth()+"-"+accountsList.get(2).getDay()+" "
                    +accountsList.get(2).getHour()+":"+accountsList.get(2).getMinute());
            money3.setText(String.valueOf(accountsList.get(2).getMoney()));
            if(accountsList.get(2).getMoney()>0)
                money3.setTextColor(mView.getResources().getColor(R.color.colorIncome,null));
            else
                money3.setTextColor(mView.getResources().getColor(R.color.colorExpenditure,null));
        }
        if(accountsList.size()>=4){
            type4.setText(accountsList.get(3).getType());
            time4.setText(accountsList.get(3).getMonth()+"-"+accountsList.get(3).getDay()+" "
                    +accountsList.get(3).getHour()+":"+accountsList.get(3).getMinute());
            money4.setText(String.valueOf(accountsList.get(3).getMoney()));
            if(accountsList.get(3).getMoney()>0)
                money4.setTextColor(mView.getResources().getColor(R.color.colorIncome,null));
            else
                money4.setTextColor(mView.getResources().getColor(R.color.colorExpenditure,null));
        }



    }


}
