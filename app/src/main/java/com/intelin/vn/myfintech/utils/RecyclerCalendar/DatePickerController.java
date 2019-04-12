package com.intelin.vn.myfintech.utils.RecyclerCalendar;

/**
 * Copyright by Intelin.
 * Creator: Tran Do Gia An
 * Date: 11/04/2019
 * Time: 11:29 AM
 */
public interface DatePickerController {

    int getMaxYear();

    void onDayOfMonthSelected(int year, int month, int day);

    void onDateRangeSelected(final SimpleMonthAdapter.SelectedDays<SimpleMonthAdapter.CalendarDay> selectedDays);
}
