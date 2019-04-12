package com.intelin.vn.myfintech;

import android.app.Application;

import com.intelin.vn.myfintech.activity.ActivityComponent;
import com.intelin.vn.myfintech.activity.ActivitySaved;
import com.intelin.vn.myfintech.activity.DaggerActivityComponent;
import com.intelin.vn.myfintech.utils.ListCalendar;


/**
 * Copyright by Intelin.
 * Creator: Tran Do Gia An
 * Date: 29/03/2019
 * Time: 5:11 PM
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ListCalendar calendar = new ListCalendar(this);
    }

    public static ActivitySaved createActivitiesSaved() {
        ActivityComponent activitiesSaved = DaggerActivityComponent.create();
        return activitiesSaved.getFunction();
    }
}
