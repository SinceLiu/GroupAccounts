package com.example.lxx.groupaccounts.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
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
import com.example.lxx.groupaccounts.Activity.LoginActivity;
import com.example.lxx.groupaccounts.Activity.RecordActivity;
import com.example.lxx.groupaccounts.Algorithm.CashierInputFilter;
import com.example.lxx.groupaccounts.Bean.Id;
import com.example.lxx.groupaccounts.Bean.User;
import com.example.lxx.groupaccounts.DB.AccountsTable;
import com.example.lxx.groupaccounts.R;
import com.example.lxx.groupaccounts.Widget.TimePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ExpenditureFragment extends Fragment {
    private static final String[] expenditureType = {"衣","食","住","行","其他"};
    private View mView;
    private Spinner spinner;
    private ArrayAdapter<String> adapter;
    private Button save;
    private EditText moneyEditText;
    private TextView timeTextView;
    private EditText surplusEditText;
    private Double money;
    private String year;
    private String month;
    private String day;
    private String hour;
    private String minute;
    private String type;
    private String surplus;
    private String ownerId;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState){
        mView = inflater.inflate(R.layout.fragment_expenditure,container,false);
        return mView;
    }
    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        ownerId = getActivity().getIntent().getStringExtra("ownerId");
        save = (Button)mView.findViewById(R.id.save);
        moneyEditText = (EditText)mView.findViewById(R.id.money);
        timeTextView = (TextView) mView.findViewById(R.id.time);
        spinner = (Spinner)mView.findViewById(R.id.expenditure);
        surplusEditText = (EditText)mView.findViewById(R.id.et_surplus);

        //设置editText输入格式为金额
        InputFilter[] filters = {new CashierInputFilter()};
        moneyEditText.setFilters(filters);

        adapter = new ArrayAdapter<String>(getActivity(),R.layout.item_spinner,expenditureType);
        adapter.setDropDownViewResource(R.layout.dropdown_spinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new SpinnerSelectedListener());



        //点击选择时间
        timeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecordActivity activity = (RecordActivity)getActivity();
                activity.newTime(false);
            }
        });

        //点击保存
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                record();
            }
        });

    }
    public void record(){
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
            accounts.setAccountsId(String.valueOf(Id.getAccountsId()));
            accounts.setOwnerId(ownerId);
            accounts.setMoney(money);
            accounts.setType(type);
            accounts.setYear(year);
            accounts.setMonth(month);
            accounts.setDay(day);
            accounts.setHour(hour);
            accounts.setMinute(minute);
            accounts.setSurplus(surplus);
            accounts.save();
            Toast.makeText(getActivity(),"记录成功",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(),AccountsActivity.class);
            intent.putExtra("ownerId",ownerId);
            startActivity(intent);

        }else
            Toast.makeText(getActivity(),"请输入金额",Toast.LENGTH_SHORT).show();
    }

    public class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener{
        public void onItemSelected(AdapterView<?> arg0,View arg1,int arg2,long arg3){
        }

        public void onNothingSelected(AdapterView<?> arg0){

        }
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

}
