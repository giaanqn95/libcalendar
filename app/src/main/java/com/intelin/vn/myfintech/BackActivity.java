package com.intelin.vn.myfintech;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.intelin.vn.myfintech.utils.RecyclerCalendar.DatePickerController;
import com.intelin.vn.myfintech.utils.RecyclerCalendar.DayPickerView;
import com.intelin.vn.myfintech.utils.RecyclerCalendar.SimpleMonthAdapter;

/**
 * Copyright by Intelin.
 * Creator: Tran Do Gia An
 * Date: 11/04/2019
 * Time: 11:47 AM
 */
public class BackActivity extends BaseActivity implements DatePickerController {

    private DayPickerView dayPickerView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back);
        dayPickerView = findViewById(R.id.rvPicker);
        dayPickerView.setController(this);
    }

    @Override
    public int getMaxYear() {
        return 2020;
    }

    @Override
    public void onDayOfMonthSelected(int year, int month, int day) {

    }

    @Override
    public void onDateRangeSelected(SimpleMonthAdapter.SelectedDays<SimpleMonthAdapter.CalendarDay> selectedDays) {

    }
}
