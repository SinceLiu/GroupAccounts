package com.example.lxx.groupaccounts.Bean;


import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityCollector {
    public static List<Activity> activities = new ArrayList<Activity>();

    public static void addActivity(Activity a){
        activities.add(a);
    }

    public static void removeActivity(Activity a){
        activities.remove(a);
    }

    public static void removeAllActivities(){
        for(Activity a : activities){
            if(!a.isFinishing()){
                a.finish();
            }
        }
    }
}
