package com.example.lxx.groupaccounts.Widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import com.example.lxx.groupaccounts.R;

public class SideBar extends TextView {
    private String[] letters = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#"};
    private Paint textPaint;
    private Paint bigTextPaint;
    private Paint scaleTextPaint;

    private Canvas canvas;
    private int itemH;
    private int w;
    private int h;
    float singleTextH; //普通情况下字体高
    private float scaleWidth; //缩放离原始的宽度
    private float eventY = 0;  //滑动的Y
    private int scaleSize = 1; //缩放的倍数
    private int scaleItemCount = 6; //缩放个数item，即开口大小
    private ISideBarSelectCallBack callBack;

    public SideBar(Context context){
        this(context,null);
    }
    public SideBar(Context context, AttributeSet attributeSet){
        this(context,attributeSet,0);
    }
    public SideBar(Context context,AttributeSet attributeSet,int defStyleAttr){
        super(context,attributeSet,defStyleAttr);
        init(attributeSet);
    }
    private void init(AttributeSet attributeSet){
        if(attributeSet!=null){
            TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.SideBar);
            scaleSize = typedArray.getInteger(R.styleable.SideBar_scaleSize,1);
            scaleItemCount = typedArray.getInteger(R.styleable.SideBar_scaleItemCount,6);
            scaleWidth = typedArray.getDimensionPixelSize(R.styleable.SideBar_scaleWidth,dp(100));
            typedArray.recycle();
        }
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(getCurrentTextColor());
        textPaint.setTextSize(getTextSize());
        textPaint.setTextAlign(Paint.Align.CENTER);
        bigTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bigTextPaint.setColor(getCurrentTextColor());
        bigTextPaint.setTextSize(getTextSize()*(scaleSize+3));
        bigTextPaint.setTextAlign(Paint.Align.CENTER);
        scaleTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        scaleTextPaint.setColor(getCurrentTextColor());
        scaleTextPaint.setTextSize(getTextSize()*(scaleSize+1));
        scaleTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    public void setDataResource(String[] data){
        letters = data;
        invalidate();
    }

    public void setOnStrSelectCallBack(ISideBarSelectCallBack callBack){
        this.callBack = callBack;
    }

//    设置字体缩放比例
    public void setScaleSize(int scale){
        scaleSize = scale;
        invalidate();
    }

//    设置缩放字体的个数，即开口大小
    public void setScaleItemCount(int scaleItemCount){
        this.scaleItemCount = scaleItemCount;
        invalidate();
    }

    private int dp(int px){
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int)(px*scale+0.5f);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if(event.getX()>(w-getPaddingRight()-singleTextH-10)){
                    eventY = event.getY();
                    invalidate();
                    return true;
                }else{
                    eventY = 0;
                    invalidate();
                    break;
                }
            case MotionEvent.ACTION_CANCEL:
                eventY = 0;
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                if(event.getX()>(w-getPaddingRight()-singleTextH-10)){
                    eventY = 0;
                    invalidate();
                    return true;
                }else
                    break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas){
        this.canvas = canvas;
        DrawView(eventY);
    }

    private void DrawView(float y){
        int currentSelectIndex = -1;
        if(y != 0){
            for(int i=0;i<letters.length;i++){
                float currentItemY = itemH * i;
                float nextItemY = itemH * (i+1);
                if(y>=currentItemY && y<nextItemY){
                    currentSelectIndex = i;
                    if(callBack != null){
                        callBack.onSelectStr(currentSelectIndex,letters[i]);
                    }
                    //画大的字母
                    Paint.FontMetrics fontMetrics = bigTextPaint.getFontMetrics();
                    float bigTextSize = fontMetrics.descent - fontMetrics.ascent;
                    canvas.drawText(letters[i],w-getPaddingRight()-scaleWidth-bigTextSize,singleTextH+itemH*i,bigTextPaint);
                }
            }
        }
        drawLetters(y,currentSelectIndex);
    }

    private void drawLetters(float y,int index){
        //第一次进来没有缩放情况，默认画原图
        if(index == -1){
            w = getMeasuredWidth();
            h = getMeasuredHeight();
            itemH = h / letters.length;
            Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
            singleTextH = fontMetrics.descent - fontMetrics.ascent;
            for(int i=0;i<letters.length;i++){
                canvas.drawText(letters[i],w-getPaddingRight(),singleTextH+itemH*i,textPaint);
            }
            //触摸的时候画缩放图
        }else{
            //遍历所有字母
            for(int i=0;i<letters.length;i++){
                //要画的字母大的起始Y坐标
                float currentItemToDrawY = singleTextH + itemH * i;
                float centerItemToDrawY;
                if(index < i)
                    centerItemToDrawY = singleTextH + itemH * (index + scaleItemCount);
                else
                    centerItemToDrawY = singleTextH + itemH * (index - scaleItemCount);
                float delta = 1 - Math.abs((y-currentItemToDrawY)/(centerItemToDrawY-currentItemToDrawY));
                float maxRightX = w - getPaddingRight();
                //如果大于0，表明在y坐标上方
                scaleTextPaint.setTextSize(getTextSize()+getTextSize()*delta);
                float drawX = maxRightX - scaleWidth * delta;
                //超出边界直接画在边界上
                if(drawX > maxRightX)
                    canvas.drawText(letters[i],maxRightX,singleTextH+itemH*i,textPaint);
                else
                    canvas.drawText(letters[i],drawX,singleTextH+itemH*i,scaleTextPaint);
            }
        }
    }
    public interface ISideBarSelectCallBack{
        void onSelectStr(int index,String selectStr);
    }
}
