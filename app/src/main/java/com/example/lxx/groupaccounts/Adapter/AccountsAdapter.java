package com.example.lxx.groupaccounts.Adapter;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lxx.groupaccounts.Activity.AccountsActivity;
import com.example.lxx.groupaccounts.Activity.ManageRecordActivity;
import com.example.lxx.groupaccounts.Bean.Accounts;
import com.example.lxx.groupaccounts.Bean.DailyAccounts;
import com.example.lxx.groupaccounts.R;

import java.util.ArrayList;
import java.util.List;

public class AccountsAdapter extends BaseAdapter {
    private List<Accounts> list = null;
    private List<DailyAccounts> dailyAccountsList;
    private Context context;
    private boolean[] showControl;  //记录list的每个item是否要展开
    private int tag=0;

    public AccountsAdapter(Context context,List<Accounts> list){
        this.context = context;
        this.list = list;
        showControl = new boolean[list.size()];
    }
    public int getCount(){
        return this.list.size();
    }
    public Object getItem(int position){
        return list.get(position);
    }
    public long getItemId(int position){
        return position;
    }

    public View getView(int position, View view, ViewGroup arg2){
        ViewHolder viewHolder;
        Accounts accounts = list.get(position);
        if(view == null){
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_accounts,null);
            viewHolder.month = (TextView)view.findViewById(R.id.month);
            viewHolder.income = (TextView)view.findViewById(R.id.incomeThisMonth);
            viewHolder.expenditure = (TextView)view.findViewById(R.id.expenditureThisMonth);
            viewHolder.surplus = (TextView)view.findViewById(R.id.surplus);
            viewHolder.listView = (ListView)view.findViewById(R.id.accounts_day);
            viewHolder.totalMonth=(RelativeLayout)view.findViewById(R.id.total_month);
            viewHolder.upOrDown=(ImageView)view.findViewById(R.id.up_dowm);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)view.getTag();
        }

        viewHolder.totalMonth.setTag(position); //给响应点击事件的区域添加Tag
        if(showControl[position]){
            viewHolder.listView.setVisibility(View.VISIBLE);
            viewHolder.upOrDown.setBackgroundResource(R.drawable.up);
        }else {
            viewHolder.listView.setVisibility(View.GONE);
            viewHolder.upOrDown.setBackgroundResource(R.drawable.dowm);
        }
        viewHolder.totalMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tag = (Integer) v.getTag();
                if(showControl[tag]){
                    showControl[tag] = false;
                }else {
                    showControl[tag] = true;
                }
                notifyDataSetChanged(); //通知adapter数据改变需要重新加载
            }
        });
        //点击查看具体的一笔账单信息
        viewHolder.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String accountsId = list.get(tag).getList().get(position).getAccountsId();
                Double money = list.get(tag).getList().get(position).getMoney();
                Intent intent = new Intent(context, ManageRecordActivity.class);
                intent.putExtra("accountsId",accountsId);
                intent.putExtra("money",money);
                context.startActivity(intent);
            }
        });

        viewHolder.month.setText(accounts.getMonth());
        viewHolder.income.setText(String.valueOf(accounts.getIncome()));
        viewHolder.expenditure.setText(String.valueOf(accounts.getExpenditure()));
        viewHolder.surplus.setText(String.valueOf(accounts.getSurplus()));
        if(accounts.getSurplus()<0)
            viewHolder.surplus.setTextColor(view.getResources().getColor(R.color.colorExpenditure,null));
        else
            viewHolder.surplus.setTextColor(view.getResources().getColor(R.color.colorIncome,null));

        dailyAccountsList = new ArrayList<DailyAccounts>();
        dailyAccountsList.addAll(accounts.getList());
        DailyAccountsAdapter dailyAccountsAdapter = new DailyAccountsAdapter(context,dailyAccountsList);
        viewHolder.listView.setAdapter(dailyAccountsAdapter);
        return view;
    }

    public class ViewHolder{
        private TextView month;
        private TextView income;
        private TextView expenditure;
        private TextView surplus;
        private ListView listView;

        private RelativeLayout totalMonth;
        private ImageView upOrDown;
    }
}
