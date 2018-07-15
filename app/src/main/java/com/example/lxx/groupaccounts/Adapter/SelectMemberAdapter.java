package com.example.lxx.groupaccounts.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lxx.groupaccounts.Bean.Friend;
import com.example.lxx.groupaccounts.R;

import java.util.HashMap;
import java.util.List;

public class SelectMemberAdapter extends BaseAdapter{
    private List<Friend> list = null;
    private Context mContext;

    private static HashMap<Integer,Boolean> isSelected;  //用来控制CheckBox的选中状态

    public SelectMemberAdapter(Context mContext, List<Friend> list,HashMap<Integer,Boolean> isSelected){
        this.mContext = mContext;
        this.list = list;
        this.isSelected = isSelected;
        init();
    }
    private void init(){
        for(int i=0;i<list.size();i++){
            getIsSelected().put(i,false);
        }
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
    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }
    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        SelectMemberAdapter.isSelected = isSelected;
    }

    public View getView(final int position, View view, ViewGroup arg2){
        ViewHolder viewHolder;
        final Friend friend = list.get(position);
        if(view == null){
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_friend_select,null);
            viewHolder.name = (TextView)view.findViewById(R.id.name);
            viewHolder.checkBox=(CheckBox)view.findViewById(R.id.checkbox);
            viewHolder.catalog = (TextView)view.findViewById(R.id.catalog);
            viewHolder.linearLayout = (LinearLayout)view.findViewById(R.id.linear_friend);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSelected.get(position)){
                    isSelected.put(position,false);
                    setIsSelected(isSelected);
                }else {
                    isSelected.put(position,true);
                    setIsSelected(isSelected);
                }
                notifyDataSetChanged();
            }
        });
        //根据position获取首字母作为目录catalog
        String catalog = list.get(position).getFirstLetter();
        //如果当前位置等于该分类首字母的Char的位置，则认为是第一次出现
        if(position == getPositionForSection(catalog)){
            viewHolder.catalog.setVisibility(View.VISIBLE);
            viewHolder.catalog.setText(friend.getFirstLetter().toUpperCase());
        }else{
            viewHolder.catalog.setVisibility(View.GONE);
        }
        viewHolder.name.setText(this.list.get(position).getName());
        viewHolder.checkBox.setChecked(getIsSelected().get(position)); //根据isSelected来设置CheckBox的选中状态
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
        CheckBox checkBox;
        LinearLayout linearLayout;
    }
}
