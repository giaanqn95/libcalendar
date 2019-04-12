package com.intelin.vn.myfintech.utils;

import android.view.View;

/**
 * Copyright by Intelin.
 * Creator: Tran Do Gia An
 * Date: 12/04/2019
 * Time: 10:47 AM
 */
public interface CalendarClickListener {

    interface DayClick {
        void onDayClick(View view, long chooseDay);
    }

    interface DayToDay {
        void onDayToDay(View view, long dateStart, long dateEnd);
    }
}
