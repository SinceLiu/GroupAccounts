package com.example.lxx.groupaccounts.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lxx.groupaccounts.Bean.DailyAccounts;
import com.example.lxx.groupaccounts.R;

import java.util.List;

public class DailyAccountsAdapter extends BaseAdapter {
    private List<DailyAccounts> list = null;
    private Context mContext;

    public DailyAccountsAdapter(Context mContext,List<DailyAccounts> list){
        this.mContext = mContext;
        this.list = list;
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
        DailyAccounts dailyAccounts = list.get(position);
        if(view == null){
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_accounts_day,null);
            viewHolder.type = (TextView)view.findViewById(R.id.type);
            viewHolder.day = (TextView)view.findViewById(R.id.date);
            viewHolder.time = (TextView)view.findViewById(R.id.time);
            viewHolder.money = (TextView)view.findViewById(R.id.money);
            viewHolder.surplus = (TextView)view.findViewById(R.id.surplus);
            viewHolder.catalog = (LinearLayout)view.findViewById(R.id.catalog);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)view.getTag();
        }
        //以day做为目录
        String day = list.get(position).getDay();
        //如果当前位置等于day的位置，认为是第一次出现
        if(position == getPositionForSection(day)){
            viewHolder.catalog.setVisibility(View.VISIBLE);
            viewHolder.day.setText(dailyAccounts.getDay());
        }else {
            viewHolder.catalog.setVisibility(View.GONE);
        }
        viewHolder.type.setText(String.valueOf(this.list.get(position).getType()));
        viewHolder.time.setText(String.valueOf(this.list.get(position).getTime()));
        viewHolder.money.setText(String.valueOf(this.list.get(position).getMoney()));
        if(this.list.get(position).getMoney()>0)
            viewHolder.money.setTextColor(view.getResources().getColor(R.color.colorIncome,null));
        else
            viewHolder.money.setTextColor(view.getResources().getColor(R.color.colorExpenditure,null));
        viewHolder.surplus.setText(this.list.get(position).getSurplus());
        return view;
    }

    //获取day首次出现的位置
    public int getPositionForSection(String day){
        for(int i=0;i<getCount();i++){
            String dayStr = list.get(i).getDay();
            if(day.equalsIgnoreCase(dayStr)){
                return i;
            }
        }
        return -1;
    }

    public class ViewHolder{
        LinearLayout catalog;
        TextView day;
        TextView type;
        TextView time;
        TextView money;
        TextView surplus;
    }
}
