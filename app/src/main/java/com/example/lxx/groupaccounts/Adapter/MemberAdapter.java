package com.example.lxx.groupaccounts.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lxx.groupaccounts.Bean.Member;
import com.example.lxx.groupaccounts.R;

import java.util.ArrayList;
import java.util.List;

public class MemberAdapter extends BaseAdapter {
    private Context context;
    private List<Member> list = new ArrayList<Member>();

    public MemberAdapter(Context context, List<Member> list) {
        this.context = context;
        this.list = list;
    }

    public int getCount() {
        return this.list.size()+1; //最后一个为添加成员
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2){
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_member, null);
            viewHolder.image = (ImageView) view.findViewById(R.id.image);
            viewHolder.name = (TextView) view.findViewById(R.id.name);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if (list!=null && position < list.size()) {
            Member member = list.get(position);
            viewHolder.image.setImageResource(member.getImage());
            viewHolder.name.setText(member.getName());
        }else {
            viewHolder.image.setImageResource(R.drawable.add_member);
            viewHolder.name.setText("添加成员");
        }


        return view;
    }


    public static class ViewHolder {
        ImageView image;
        TextView name;
    }
}