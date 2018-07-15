package com.example.lxx.groupaccounts.Fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lxx.groupaccounts.Activity.AccountsActivity;
import com.example.lxx.groupaccounts.Activity.ManageRecordActivity;
import com.example.lxx.groupaccounts.Activity.RecordActivity;
import com.example.lxx.groupaccounts.Algorithm.CashierInputFilter;
import com.example.lxx.groupaccounts.Bean.ActivityCollector;
import com.example.lxx.groupaccounts.Bean.Id;
import com.example.lxx.groupaccounts.DB.AccountsTable;
import com.example.lxx.groupaccounts.R;

import org.litepal.crud.DataSupport;

public class ManageExpenditureFragment extends Fragment {
    private static final String[] expenditureType = {"衣","食","住","行","其他"};
    private View mView;
    private Spinner spinner;
    private ArrayAdapter<String> adapter;
    private EditText moneyEditText;
    private TextView timeTextView;
    private EditText surplusEditText;
    private Button save;
    private Button remove;
    private Double money;
    private String year;
    private String month;
    private String day;
    private String hour;
    private String minute;
    private String type;
    private String surplus;
    private String accountsId;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState){
        mView = inflater.inflate(R.layout.fragment_expenditure_manage,container,false);
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
        spinner = (Spinner)mView.findViewById(R.id.expenditure);
        save = (Button)mView.findViewById(R.id.save);
        remove =(Button)mView.findViewById(R.id.remove);
        moneyEditText = (EditText)mView.findViewById(R.id.money);
        timeTextView = (TextView) mView.findViewById(R.id.time);
        surplusEditText = (EditText)mView.findViewById(R.id.surplus);
        //设置editText输入格式为金额
        InputFilter[] filters = {new CashierInputFilter()};
        moneyEditText.setFilters(filters);

        adapter = new ArrayAdapter<String>(getActivity(),R.layout.item_spinner,expenditureType);
        adapter.setDropDownViewResource(R.layout.dropdown_spinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new ManageExpenditureFragment.SpinnerSelectedListener());

        //点击选择时间
        timeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ManageRecordActivity activity = (ManageRecordActivity) getActivity();
                activity.newTime(false);
            }
        });

        //点击保存
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("修改")
                        .setMessage("确定吗")
                        .setPositiveButton("是",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                change();
                            }
                        })
                        .setNegativeButton("否", null)
                        .show();
            }
        });

        //点击删除
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("删除")
                        .setMessage("确定吗")
                        .setPositiveButton("是",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                DataSupport.deleteAll(AccountsTable.class,"accountsId=?",accountsId);
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton("否", null)
                        .show();
            }
        });
    }

    private void initData(){
        accountsId = getActivity().getIntent().getStringExtra("accountsId");
        AccountsTable account = DataSupport.where("accountsId=?",accountsId).findFirst(AccountsTable.class);
        moneyEditText.setText(String.valueOf(-account.getMoney())); //设置金额
        for(int i=0;i<adapter.getCount();i++){ //设置类型
            if(account.getType().equals(adapter.getItem(i)))
                spinner.setSelection(i);
        }
        setTime(account.getYear(),account.getMonth(),account.getDay(),account.getHour(),account.getMinute()); //设置时间
        surplusEditText.setText(account.getSurplus());
    }

    public void setTime(String year,String month,String day,String hour,String minute){
        this.year = year;
        while(month.length()<2){
            month = "0"+month;
        }
        while(day.length()<2){
            day = "0"+day;
        }
        while(hour.length()<2){
            hour = "0"+hour;
        }
        while(minute.length()<2){
            minute = "0"+minute;
        }
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        timeTextView.setText(this.year+"-"+this.month+"-"+this.day+" "+this.hour+":"+this.minute);
    }

    public void change(){
        if(moneyEditText.getText().toString().equals("") == false){
            money = -Double.parseDouble(moneyEditText.getText().toString());
            type = (String)spinner.getSelectedItem();
            year = timeTextView.getText().toString().substring(0,4);
            month = timeTextView.getText().toString().substring(5,7);
            day = timeTextView.getText().toString().substring(8,10);
            hour = timeTextView.getText().toString().substring(11,13);
            minute=timeTextView.getText().toString().substring(14,16);
            surplus = surplusEditText.getText().toString();
            AccountsTable accounts = new AccountsTable(); //数据库添加数据
            accounts.setMoney(money);
            accounts.setType(type);
            accounts.setYear(year);
            accounts.setMonth(month);
            accounts.setDay(day);
            accounts.setHour(hour);
            accounts.setMinute(minute);
            accounts.setSurplus(surplus);
            accounts.updateAll("accountsId=?",accountsId);
            Toast.makeText(getActivity(),"修改成功",Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }else
            Toast.makeText(getActivity(),"请输入金额",Toast.LENGTH_SHORT).show();
    }

    public class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener{
        public void onItemSelected(AdapterView<?> arg0,View arg1,int arg2,long arg3){
        }

        public void onNothingSelected(AdapterView<?> arg0){

        }
    }
}