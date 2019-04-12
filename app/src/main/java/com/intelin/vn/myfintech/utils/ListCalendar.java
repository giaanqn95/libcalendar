package com.intelin.vn.myfintech.utils;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.intelin.vn.myfintech.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Copyright by Intelin.
 * Creator: Tran Do Gia An
 * Date: 08/04/2019
 * Time: 3:02 PM
 */
public class ListCalendar extends LinearLayout {

    private Context context;
    private RecyclerView recyclerView;
    private CalendarAdapter calendarAdapter;
    private List<myCalendar> myCalendars = new ArrayList<>();
    private int userMonth, userYear, typeOption = 0;
    private TextView tvType, tvNameType;
    private String dateFormat;
    private TypedArray ta;
    private CalendarClickListener.DayClick mListener;
    private CalendarClickListener.DayToDay mListenerTo;

    public ListCalendar(Context context) {
        super(context);
        this.context = context;
        init(context);
    }

    public void onSeletecDayListener(CalendarClickListener.DayClick mListener) {
        this.mListener = mListener;
    }

    public void onDayToDayListener(CalendarClickListener.DayToDay mListenerTo) {
        this.mListenerTo = mListenerTo;
    }

    public ListCalendar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        ta = getContext().obtainStyledAttributes(attrs, R.styleable.CalendarDateElement);
        dateFormat = ta.getString(R.styleable.CalendarDateElement_dateFormat);
        typeOption = ta.getInt(R.styleable.CalendarDateElement_typeChoose, 0);
        init(context);
    }

    public ListCalendar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        ta = getContext().obtainStyledAttributes(attrs, R.styleable.CalendarDateElement);
        dateFormat = ta.getString(R.styleable.CalendarDateElement_dateFormat);
        typeOption = ta.getInt(R.styleable.CalendarDateElement_typeChoose, 0);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ListCalendar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        ta = getContext().obtainStyledAttributes(attrs, R.styleable.CalendarDateElement);
        dateFormat = ta.getString(R.styleable.CalendarDateElement_dateFormat);
        typeOption = ta.getInt(R.styleable.CalendarDateElement_typeChoose, 0);
        init(context);
    }

    public void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_calendar, this, true);
        add();
        tvType = view.findViewById(R.id.tvType);
        tvNameType = view.findViewById(R.id.tvNameType);
        recyclerView = view.findViewById(R.id.rvCalendar);
        initAdapter();
        setText();
        changeTypeSelection();
    }

    private void initAdapter() {
        calendarAdapter = new CalendarAdapter(context, myCalendars, typeOption, recyclerView, ta, mListener, mListenerTo);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(calendarAdapter);
        calendarAdapter.notifyDataSetChanged();
    }

    private void add() {
        Calendar calendar = Calendar.getInstance();
        int day, month, year;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        if (myCalendars.size() == 0) {
            for (int i = 0; i <= 10; i++) {
                if (month == 11) {
                    myCalendars.add(new myCalendar().setDay(day).setMonth(month).setYear(year));
                    month = 0;
                    year++;
                } else {
                    myCalendars.add(new myCalendar().setDay(day).setMonth(month).setYear(year));
                    month++;
                }
            }
        }
    }

    private void changeTypeSelection() {
        String[] item = {"Ngày", "Ngày đến ngày"};
        tvType.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Chọn");
            builder.setCancelable(false);
            builder.setItems(item, (dialog, which) -> {
                typeOption = which;
                setText();
                clearSelected();
                initAdapter();
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        });
    }

    private void setText() {
        switch (typeOption) {
            case 1:
                tvNameType.setText("Ngày đến ngày");
                break;
            default:
                tvNameType.setText("Ngày");
                break;
        }
    }

    private void clearSelected() {
        switch (typeOption) {
            case 1:
                calendarAdapter.unSelected();
                break;
            default:
                calendarAdapter.unSelectedStartDate();
                calendarAdapter.unSelectedEndDate();
                break;
        }
    }
}
