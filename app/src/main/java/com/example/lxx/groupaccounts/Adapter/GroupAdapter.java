package com.example.lxx.groupaccounts.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lxx.groupaccounts.Bean.Group;
import com.example.lxx.groupaccounts.R;

import java.util.List;

public class GroupAdapter extends BaseAdapter {
    Context context;
    List<Group> group;

    public GroupAdapter(Context context,List<Group> group){
        this.context = context;
        this.group = group;
    }

    @Override
    public int getCount(){
        return group.size();
    }

    @Override
    public Object getItem(int i){
        return group.get(i);
    }

    @Override
    public long getItemId(int i){
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup){
        ViewHolder viewHolder;
        if(view == null){
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_group,null);
            viewHolder.name = (TextView)view.findViewById(R.id.name);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)view.getTag();
        }
        //适配数据
        viewHolder.name.setText(group.get(i).getName());

        return view;
    }

    public static class ViewHolder{
        TextView name;
    }
}
