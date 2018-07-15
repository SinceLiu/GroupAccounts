package com.example.lxx.groupaccounts.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lxx.groupaccounts.Bean.Friend;
import com.example.lxx.groupaccounts.R;

import java.util.List;

public class FriendAdapter extends BaseAdapter{
    private List<Friend> list = null;
    private Context mContext;

    public FriendAdapter(Context mContext, List<Friend> list){
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

    public View getView(final int position, View view, ViewGroup arg2){
        ViewHolder viewHolder;
        Friend friend = list.get(position);
        if(view == null){
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_friend,null);
            viewHolder.name = (TextView)view.findViewById(R.id.name);
            viewHolder.catalog = (TextView)view.findViewById(R.id.catalog);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        //根据position获取首字母作为目录catalog
        String catalog = list.get(position).getFirstLetter();

        //如果当前位置等于该分类首字母的Char的位置，则认为是第一次出现
        if(position == getPositionForSection(catalog)){
            viewHolder.catalog.setVisibility(View.VISIBLE);
            viewHolder.catalog.setText(friend.getFirstLetter().toUpperCase());
        }else{
            viewHolder.catalog.setVisibility(View.GONE);
        }

        viewHolder.name.setText(list.get(position).getName());
        return view;
    }
    //获取catalog首次出现的位置
    public int getPositionForSection(String catalog){
        for(int i=0;i<getCount();i++){
            String sortStr = list.get(i).getFirstLetter();
            if(catalog.equalsIgnoreCase(sortStr)){
                return i;
            }
        }
        return -1;
    }
    final static class ViewHolder{
        TextView catalog;
        TextView name;
    }
}
