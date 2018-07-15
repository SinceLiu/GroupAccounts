package com.example.lxx.groupaccounts.Widget;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class CustomerListView extends ListView{
    public CustomerListView(Context context){
        super(context);
    }

    public CustomerListView(Context context, AttributeSet attrs,int defStyle){
        super(context,attrs,defStyle);
    }

    public CustomerListView(Context context,AttributeSet attrs){
        super(context,attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec,expandSpec);
    }
}
