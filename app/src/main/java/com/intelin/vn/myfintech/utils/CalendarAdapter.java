package com.intelin.vn.myfintech.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.intelin.vn.myfintech.LogDog;
import com.intelin.vn.myfintech.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Copyright by Intelin.
 * Creator: Tran Do Gia An
 * Date: 08/04/2019
 * Time: 3:06 PM
 */
public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.Holder> {
    private static final String CUSTOM_GREY = "#a0a0a0";
    private static final String[] ENG_MONTH_NAMES = {"January", "February", "March", "April", "May",
            "June", "July", "August", "September", "October", "November", "December"};

    private RecyclerView recyclerView;
    private TypedArray ta;
    private Context context;
    private List<myCalendar> myCalendarList;
    private DisplayMetrics metrics;

    private Calendar calendar;
    private int currentDateDay, chosenDateDay, currentDateMonth,
            chosenDateMonth, currentDateYear, chosenDateYear,
            pickedDateDay, pickedDateMonth, pickedDateYear;
    private int userMonth, userYear, typeOption = 0;
    private LinearLayout.LayoutParams defaultButtonParams;

    private Button selectedDayButton, endDayButton, startDayButton;
    private int dateStart, monthStart, yearStart,
            dateEnd, monthEnd, yearEnd;
    private Holder holderStart, holderEnd;
    private int colorSelect, colorStartDate, colorEndDate, colorLine;
    private Drawable backgroundSelect, backgroundStartDate, backgroundEndDate, backgroundLine;
    private CalendarClickListener.DayClick mListener;
    private CalendarClickListener.DayToDay mListenerTo;

    public CalendarAdapter(Context context, List<myCalendar> myCalendars, int typeOption,
                           RecyclerView recyclerView, TypedArray typedArray, CalendarClickListener.DayClick mListener,
                           CalendarClickListener.DayToDay mListenerTo) {
        this.context = context;
        this.myCalendarList = myCalendars;
        this.typeOption = typeOption;
        this.recyclerView = recyclerView;
        this.ta = typedArray;
        this.mListener = mListener;
        this.mListenerTo = mListenerTo;

        metrics = context.getResources().getDisplayMetrics();
        calendar = Calendar.getInstance();
        currentDateDay = chosenDateDay = calendar.get(Calendar.DAY_OF_MONTH);
        if (userMonth != 0 && userYear != 0) {
            currentDateMonth = chosenDateMonth = userMonth;
            currentDateYear = chosenDateYear = userYear;
        } else {
            currentDateMonth = chosenDateMonth = calendar.get(Calendar.MONTH);
            currentDateYear = chosenDateYear = calendar.get(Calendar.YEAR);
        }
        defaultButtonParams = getDaysLayoutParams();
        initXML();
    }

    private void initXML() {
        colorSelect = ta.getColor(R.styleable.CalendarDateElement_colorSelect, Color.RED);
        colorStartDate = ta.getColor(R.styleable.CalendarDateElement_colorStartDate, context.getResources().getColor(R.color.blueStart));
        colorEndDate = ta.getColor(R.styleable.CalendarDateElement_colorEndDate, context.getResources().getColor(R.color.blueEnd));
        colorLine = ta.getColor(R.styleable.CalendarDateElement_colorLine, context.getResources().getColor(R.color.blueLine));
        backgroundSelect = ta.getDrawable(R.styleable.CalendarDateElement_backgroundSelect);
        backgroundStartDate = ta.getDrawable(R.styleable.CalendarDateElement_backgroundStartDate);
        backgroundEndDate = ta.getDrawable(R.styleable.CalendarDateElement_backgroundEndDate);
        backgroundLine = ta.getDrawable(R.styleable.CalendarDateElement_backgroundLine);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.custome_calendar, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        LinearLayout[] weeks = new LinearLayout[6];
        Button[] days = new Button[6 * 7];
        myCalendar myCalendar = myCalendarList.get(position);
        holder.currentMonth.setText(ENG_MONTH_NAMES[myCalendar.getMonth()]);
        int year = myCalendar.getYear(), month = myCalendar.getMonth(), day = myCalendar.getDay();

        //Import weeks
        weeks[0] = holder.weekOneLayout;
        weeks[1] = holder.weekTwoLayout;
        weeks[2] = holder.weekThreeLayout;
        weeks[3] = holder.weekFourLayout;
        weeks[4] = holder.weekFiveLayout;
        weeks[5] = holder.weekSixLayout;

        //Set days
        int engDaysArrayCounter = 0;

        for (int weekNumber = 0; weekNumber < 6; ++weekNumber) {
            for (int dayInWeek = 0; dayInWeek < 7; ++dayInWeek) {
                final Button dayB = new Button(context);
                dayB.setTextColor(Color.parseColor(CUSTOM_GREY));
                dayB.setBackgroundColor(Color.TRANSPARENT);
                dayB.setLayoutParams(defaultButtonParams);
                dayB.setTextSize((int) metrics.density * 8);
                dayB.setSingleLine();

                days[engDaysArrayCounter] = dayB;
                weeks[weekNumber].addView(dayB);

                ++engDaysArrayCounter;
            }
        }

        if (calendar == null) {
            calendar = Calendar.getInstance();
        }
        calendar.set(year, month, day);

        //Get total days in month
        int daysInCurrentMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        calendar.set(year, month, 1);

        // Position date start in current month
        int firstDayOfCurrentMonth = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        calendar.set(year, month, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        int dayNumber = 1;

        //Add days current month of year
        if (firstDayOfCurrentMonth != 0) {
            //Sau đó dùng for chạy từ vị trí ngày đầu trong tháng và set data
            //Những ngày trong tháng hiện sẽ chữ màu đen giống như mọi lịch khác. Nếu không hiểu thì xem lịch google sẽ hiểu
            for (int i = firstDayOfCurrentMonth; i < firstDayOfCurrentMonth + daysInCurrentMonth; ++i) {
                days[i].setTextColor(Color.BLACK);
                days[i].setBackgroundColor(Color.TRANSPARENT);
                if (pickedDateYear == currentDateYear && pickedDateMonth == currentDateMonth && pickedDateDay == currentDateDay) {
                    days[i].setTypeface(days[i].getTypeface(), Typeface.BOLD);
                }
                //Tạo 1 mảng int chứa ngày tháng năm rồi set vào Tag. Được xem như là lưu tạm dữ liệu vào View
                int[] dateArr = new int[3];
                dateArr[0] = dayNumber;
                dateArr[1] = month;
                dateArr[2] = year;
                days[i].setTag(dateArr);
                days[i].setText(String.valueOf(dayNumber));

                //Cái này khỏi giải thích
                int a = i;
                int finalDayNumber = dayNumber;
                days[i].setOnClickListener(v -> {
                    LogDog.d("DateToDate" + String.valueOf(finalDayNumber) + " " + String.valueOf(month) + " " + String.valueOf(year));
                    onDayClick(v, holder);
                });
                ++dayNumber;
            }

            //Trường hợp ngày đầu tiên trong tháng hiện tại nằm ở vị trí đầu
        } else {
            for (int i = 7; i < 8 + daysInCurrentMonth - 1; ++i) {
                days[i].setTextColor(Color.BLACK);
                days[i].setBackgroundColor(Color.TRANSPARENT);
                if (pickedDateYear == currentDateYear && pickedDateMonth == currentDateMonth && pickedDateDay == currentDateDay) {
                    days[i].setTypeface(days[i].getTypeface(), Typeface.BOLD);
                }

                int[] dateArr = new int[3];
                dateArr[0] = dayNumber;
                dateArr[1] = month;
                dateArr[2] = year;
                days[i].setTag(dateArr);
                days[i].setText(String.valueOf(dayNumber));
                int a = i;
                days[i].setOnClickListener(v -> onDayClick(v, holder));
                ++dayNumber;
            }
        }
    }

    @Override
    public int getItemCount() {
        return myCalendarList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private LinearLayout.LayoutParams getDaysLayoutParams() {
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonParams.width = context.getResources().getDimensionPixelSize(R.dimen.dp59);
        return buttonParams;
    }

    private void initCalendarWithDate(int year, int month, int day,
                                      int calendarPosition, Button[] days) {
        if (calendar == null) {
            calendar = Calendar.getInstance();
        }
        calendar.set(year, month, day);

        //Get total days in month
        int daysInCurrentMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        calendar.set(year, month, 1);

        // Position date start in current month
        int firstDayOfCurrentMonth = calendar.get(Calendar.DAY_OF_WEEK);

        calendar.set(year, month, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        int dayNumber = 1;
        int daysLeftInFirstWeek = 0;
        int indexOfDayAfterLastDayOfMonth = 0;

        //Add days current month of year
        if (firstDayOfCurrentMonth != 1) {
            daysLeftInFirstWeek = firstDayOfCurrentMonth - 1;

            //Tính vị trí cuối cùng (size) của danh sách ngày (MAX 6*7 = 42)
            indexOfDayAfterLastDayOfMonth = daysLeftInFirstWeek + daysInCurrentMonth - 1;

            //Sau đó dùng for chạy từ vị trí ngày đầu trong tháng và set data
            //Những ngày trong tháng hiện sẽ chữ màu đen giống như mọi lịch khác. Nếu không hiểu thì xem lịch google sẽ hiểu
            for (int i = firstDayOfCurrentMonth - 1; i < firstDayOfCurrentMonth + daysInCurrentMonth - 1; ++i) {
                days[i].setTextColor(Color.BLACK);
                days[i].setBackgroundColor(Color.TRANSPARENT);

                //Tạo 1 mảng int chứa ngày tháng năm rồi set vào Tag. Được xem như là lưu tạm dữ liệu vào View
                int[] dateArr = new int[3];
                dateArr[0] = dayNumber;
                dateArr[1] = month;
                dateArr[2] = month;
                days[i].setTag(dateArr);
                days[i].setText(String.valueOf(dayNumber));
                //Cái này khỏi giải thích
                int a = i;
//                days[i].setOnClickListener(v -> onDayClick(v, a, calendarPosition));

                ++dayNumber;
            }

            //Trường hợp ngày đầu tiên trong tháng hiện tại nằm ở vị trí đầu
        } else {
            daysLeftInFirstWeek = 7;
            indexOfDayAfterLastDayOfMonth = daysLeftInFirstWeek + daysInCurrentMonth - 1;
            for (int i = 7; i < 8 + daysInCurrentMonth - 1; ++i) {
                days[i].setTextColor(Color.BLACK);
                days[i].setBackgroundColor(Color.TRANSPARENT);

                int[] dateArr = new int[3];
                dateArr[0] = dayNumber;
                dateArr[1] = month;
                dateArr[2] = year;
                days[i].setTag(dateArr);
                days[i].setText(String.valueOf(dayNumber));
                int a = i;
//                days[i].setOnClickListener(v -> onDayClick(v, a, calendarPosition));
                ++dayNumber;
            }
        }

//        if (month > 0) {
//            calendar.set(year, month - 1, 1);
//        } else {
//            calendar.set(year - 1, 11, 1);
//        }
//        //Set calendar last month
//        int daysInPreviousMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
//
//        //Add days last month
//        for (int i = daysLeftInFirstWeek - 1; i >= 0; --i) {
//            int[] dateArr = new int[3];
//
//            if (chosenDateMonth > 0) {
//                if (currentDateMonth == chosenDateMonth - 1 && currentDateYear == chosenDateYear && daysInPreviousMonth == currentDateDay) {
//                } else {
//                    days[i].setBackgroundColor(Color.TRANSPARENT);
//                }
//
//                dateArr[0] = daysInPreviousMonth;
//                dateArr[1] = chosenDateMonth - 1;
//                dateArr[2] = chosenDateYear;
//            } else {
//                if (currentDateMonth == 11 && currentDateYear == chosenDateYear - 1 && daysInPreviousMonth == currentDateDay) {
//                } else {
//                    days[i].setBackgroundColor(Color.TRANSPARENT);
//                }
//
//                dateArr[0] = daysInPreviousMonth;
//                dateArr[1] = 11;
//                dateArr[2] = chosenDateYear - 1;
//            }
//
//            days[i].setTag(dateArr);
//            days[i].setText(String.valueOf(daysInPreviousMonth--));
//        }

        //Add days next month
//        int nextMonthDaysCounter = 1;
//        for (int i = indexOfDayAfterLastDayOfMonth + 1; i < days.length; ++i) {
//            int[] dateArr = new int[3];
//
//            if (chosenDateMonth < 11) {
//                if (currentDateMonth == chosenDateMonth + 1 && currentDateYear == chosenDateYear && nextMonthDaysCounter == currentDateDay) {
//                    days[i].setBackgroundColor(context.getResources().getColor(R.color.pink));
//                } else {
//                    days[i].setBackgroundColor(Color.TRANSPARENT);
//                }
//
//                dateArr[0] = nextMonthDaysCounter;
//                dateArr[1] = chosenDateMonth + 1;
//                dateArr[2] = chosenDateYear;
//            } else {
//                if (currentDateMonth == 0 && currentDateYear == chosenDateYear + 1 && nextMonthDaysCounter == currentDateDay) {
//                    days[i].setBackgroundColor(context.getResources().getColor(R.color.pink));
//                } else {
//                    days[i].setBackgroundColor(Color.TRANSPARENT);
//                }
//
//                dateArr[0] = nextMonthDaysCounter;
//                dateArr[1] = 0;
//                dateArr[2] = chosenDateYear + 1;
//            }
//
//            days[i].setTag(dateArr);
//            days[i].setTextColor(Color.parseColor(CUSTOM_GREY));
//            days[i].setText(String.valueOf(nextMonthDaysCounter++));
//        }

        //Set calendar current days
        calendar.set(chosenDateYear, chosenDateMonth, chosenDateDay);
    }

    public void onDayClick(View view, Holder holder) {
        switch (typeOption) {
            case 1:
                dateToDate(view, holder);
                break;
            default:
                chooseDay(view);
                break;
        }
    }

    private void chooseDay(View view) {
        if (selectedDayButton != null) {
            selectedDayButton.setBackgroundColor(Color.TRANSPARENT);
            selectedDayButton.setTextColor(context.getResources().getColor(R.color.calendar_number));
        }

        selectedDayButton = (Button) view;
        if (selectedDayButton.getTag() != null) {
            int[] dateArray = (int[]) selectedDayButton.getTag();
            pickedDateDay = dateArray[0];
            pickedDateMonth = dateArray[1];
            pickedDateYear = dateArray[2];
        }
        selectedDayButton.setBackgroundColor(colorSelect);
        selectedDayButton.setTextColor(Color.WHITE);
//        mListener.onDayClick(view, parseLongTime(pickedDateYear, pickedDateMonth, pickedDateDay));
    }

    private void dateToDate(View view, Holder holder) {
        if (startDayButton == null) {
            startDayButton = (Button) view;
            if (startDayButton.getTag() != null) {
                int[] tagDateStart = (int[]) startDayButton.getTag();
                dateStart = tagDateStart[0];
                monthStart = tagDateStart[1];
                yearStart = tagDateStart[2];
            }
            selectedStartButton();
            holderStart = holder;
        } else if (startDayButton != null && endDayButton == null) {
            if (view.getTag() != null) {
                int[] tagDateStart = (int[]) view.getTag();
                dateEnd = tagDateStart[0];
                monthEnd = tagDateStart[1];
                yearEnd = tagDateStart[2];
            }
            if (dateEnd == dateStart && monthEnd == monthStart && yearEnd == yearStart) {
                return;
            }

            if (!checkDay(dateEnd, monthEnd, yearEnd, dateStart, monthStart, yearStart)) {
                unSelectStartButton();
                startDayButton = (Button) view;
                if (startDayButton.getTag() != null) {
                    int[] tagDateStart = (int[]) startDayButton.getTag();
                    dateStart = tagDateStart[0];
                    monthStart = tagDateStart[1];
                    yearStart = tagDateStart[2];
                }
                selectedStartButton();
                holderStart = holder;
                return;
            }

            endDayButton = (Button) view;
            selectedEndButton();
            holderEnd = holder;
//            mListenerTo.onDayToDay(view, parseLongTime(yearStart, monthStart, dateStart), parseLongTime(yearEnd, monthEnd, dateEnd));
            setLineDateToDate(parseLongTime(yearStart, monthStart, dateStart), parseLongTime(yearEnd, monthEnd, dateEnd), holderStart, holderEnd);
        } else if (startDayButton != null && endDayButton != null) {
            unSelectStartButton();
            unSelectEndButton();
            startDayButton = null;
            endDayButton = null;
            unSetLineDateToDate(parseLongTime(yearStart, monthStart, dateStart), parseLongTime(yearEnd, monthEnd, dateEnd), holderStart, holderEnd);
            dateToDate(view, holder);
            return;
        }
    }

    private void selectedStartButton() {
        if (backgroundStartDate != null)
            startDayButton.setBackgroundDrawable(backgroundStartDate);
        else
            startDayButton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.background_border_date_start));
        startDayButton.setTextColor(context.getResources().getColor(R.color.white));
    }

    private void selectedEndButton() {
        if (backgroundEndDate != null)
            endDayButton.setBackgroundDrawable(backgroundEndDate);
        else
            endDayButton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.background_border_date_end));
        endDayButton.setTextColor(context.getResources().getColor(R.color.white));
    }

    private void unSelectStartButton() {
        startDayButton.setBackgroundColor(Color.TRANSPARENT);
        startDayButton.setTextColor(context.getResources().getColor(R.color.calendar_number));
    }

    private void unSelectEndButton() {
        endDayButton.setBackgroundColor(Color.TRANSPARENT);
        endDayButton.setTextColor(context.getResources().getColor(R.color.calendar_number));
    }

    private void clearSelected() {
        switch (typeOption) {
            case 1:
                unSelected();
                break;
            default:
                unSelectedStartDate();
                unSelectedEndDate();
                break;
        }
    }

    public void unSelected() {
        if (selectedDayButton != null) {
            selectedDayButton.setBackgroundColor(Color.TRANSPARENT);
            selectedDayButton.setTextColor(context.getResources().getColor(R.color.calendar_number));
        }
        selectedDayButton = null;
    }

    public void unSelectedStartDate() {
        if (startDayButton != null) {
            startDayButton.setBackgroundColor(Color.TRANSPARENT);
            startDayButton.setTextColor(context.getResources().getColor(R.color.calendar_number));

        }
        startDayButton = null;
    }

    public void unSelectedEndDate() {
        if (endDayButton != null) {
            endDayButton.setBackgroundColor(Color.TRANSPARENT);
            if (endDayButton.getCurrentTextColor() != Color.RED) {
                endDayButton.setTextColor(context.getResources().getColor(R.color.calendar_number));
            }
        }
        endDayButton = null;
    }

    private void setLineDateToDate(long timeStart, long timeEnd, Holder holderStart, Holder
            holderEnd) {
        for (int i = 0; i < holderStart.llCalendar.getChildCount(); i++) {
            LinearLayout linearLayout = (LinearLayout) holderStart.llCalendar.getChildAt(i);
            for (int j = 0; j < linearLayout.getChildCount(); j++) {
                if (linearLayout.getChildAt(j) == null) {
                    continue;
                }
                Button days = (Button) linearLayout.getChildAt(j);
                if (days.getTag() == null) {
                    continue;
                }
                int[] tag = (int[]) days.getTag();

                if (parseLongTime(tag[2], tag[1], tag[0]) > timeStart && parseLongTime(tag[2], tag[1], tag[0]) < timeEnd) {
                    days.setBackgroundColor(context.getResources().getColor(R.color.blueLine));
                    days.setTextColor(Color.WHITE);
                }
            }
        }

        for (int i = 0; i < holderEnd.llCalendar.getChildCount(); i++) {
            LinearLayout linearLayout = (LinearLayout) holderEnd.llCalendar.getChildAt(i);
            for (int j = 0; j < linearLayout.getChildCount(); j++) {
                if (linearLayout.getChildAt(j) == null) {
                    continue;
                }
                Button days = (Button) linearLayout.getChildAt(j);
                if (days.getTag() == null) {
                    continue;
                }
                int[] tag = (int[]) days.getTag();
                if (parseLongTime(tag[2], tag[1], tag[0]) > timeStart && parseLongTime(tag[2], tag[1], tag[0]) < timeEnd) {
                    days.setBackgroundColor(context.getResources().getColor(R.color.blueLine));
                    days.setTextColor(Color.WHITE);
                }
            }
        }

        if (holderEnd.getLayoutPosition() - holderStart.getLayoutPosition() > 1) {
            for (int a = holderStart.getLayoutPosition() + 1; a < holderEnd.getLayoutPosition(); a++) {
                Holder holder = (Holder) recyclerView.findViewHolderForLayoutPosition(a);
                for (int i = 0; i < holder.llCalendar.getChildCount(); i++) {
                    LinearLayout linearLayout = (LinearLayout) holder.llCalendar.getChildAt(i);
                    for (int j = 0; j < linearLayout.getChildCount(); j++) {
                        if (linearLayout.getChildAt(j) == null) {
                            continue;
                        }
                        Button days = (Button) linearLayout.getChildAt(j);
                        if (days.getTag() == null) {
                            continue;
                        }
                        int[] tag = (int[]) days.getTag();
                        if (parseLongTime(tag[2], tag[1], tag[0]) > timeStart && parseLongTime(tag[2], tag[1], tag[0]) < timeEnd) {
                            days.setBackgroundColor(context.getResources().getColor(R.color.blueLine));
                            days.setTextColor(Color.WHITE);
                        }
                    }
                }
            }
        }
    }

    public void unSetLineDateToDate(long timeStart, long timeEnd, Holder holderStart, Holder
            holderEnd) {
        for (int i = 0; i < holderStart.llCalendar.getChildCount(); i++) {
            LinearLayout linearLayout = (LinearLayout) holderStart.llCalendar.getChildAt(i);
            for (int j = 0; j < linearLayout.getChildCount(); j++) {
                if (linearLayout.getChildAt(j) == null) {
                    continue;
                }
                Button days = (Button) linearLayout.getChildAt(j);
                if (days.getTag() == null) {
                    continue;
                }
                int[] tag = (int[]) days.getTag();
                if (parseLongTime(tag[2], tag[1], tag[0]) > timeStart && parseLongTime(tag[2], tag[1], tag[0]) < timeEnd) {
                    days.setBackgroundColor(Color.TRANSPARENT);
                    days.setTextColor(Color.BLACK);
                }
            }
        }

        for (int i = 0; i < holderEnd.llCalendar.getChildCount(); i++) {
            LinearLayout linearLayout = (LinearLayout) holderEnd.llCalendar.getChildAt(i);
            for (int j = 0; j < linearLayout.getChildCount(); j++) {
                if (linearLayout.getChildAt(j) == null) {
                    continue;
                }
                Button days = (Button) linearLayout.getChildAt(j);
                if (days.getTag() == null) {
                    continue;
                }
                int[] tag = (int[]) days.getTag();
                if (parseLongTime(tag[2], tag[1], tag[0]) > timeStart && parseLongTime(tag[2], tag[1], tag[0]) < timeEnd) {
                    days.setBackgroundColor(Color.TRANSPARENT);
                    days.setTextColor(Color.BLACK);
                }
            }
        }

        if (holderEnd.getLayoutPosition() - holderStart.getLayoutPosition() > 1) {
            for (int a = holderStart.getLayoutPosition() + 1; a < holderEnd.getLayoutPosition(); a++) {
                Holder holder = (Holder) recyclerView.findViewHolderForLayoutPosition(a);
                for (int i = 0; i < holder.llCalendar.getChildCount(); i++) {
                    LinearLayout linearLayout = (LinearLayout) holder.llCalendar.getChildAt(i);
                    for (int j = 0; j < linearLayout.getChildCount(); j++) {
                        if (linearLayout.getChildAt(j) == null) {
                            continue;
                        }
                        Button days = (Button) linearLayout.getChildAt(j);
                        if (days.getTag() == null) {
                            continue;
                        }
                        int[] tag = (int[]) days.getTag();
                        if (parseLongTime(tag[2], tag[1], tag[0]) > timeStart && parseLongTime(tag[2], tag[1], tag[0]) < timeEnd) {
                            days.setBackgroundColor(Color.TRANSPARENT);
                            days.setTextColor(Color.BLACK);
                        }
                    }
                }
            }
        }
    }

    private long parseLongTime(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        String timeStamp = new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime());
        try {
            Date d = new SimpleDateFormat("dd/MM/yyyy").parse(timeStamp);
            long milliseconds = d.getTime();
            return milliseconds;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private boolean checkDay(int dateEnd, int monthEnd, int yearEnd, int dateStart,
                             int monthStart, int yearStart) {
        Calendar calendarStart = Calendar.getInstance();
        Calendar calendarEnd = Calendar.getInstance();
        calendarStart.set(yearStart, monthStart, dateStart);
        calendarEnd.set(yearEnd, monthEnd, dateEnd);


        return calendarEnd.getTimeInMillis() >= calendarStart.getTimeInMillis();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private LinearLayout weekOneLayout;
        private LinearLayout weekTwoLayout;
        private LinearLayout weekThreeLayout;
        private LinearLayout weekFourLayout;
        private LinearLayout weekFiveLayout;
        private LinearLayout weekSixLayout;
        private TextView currentDate, currentMonth;
        private LinearLayout llCalendar;

        public Holder(@NonNull View itemView) {
            super(itemView);
            weekOneLayout = itemView.findViewById(R.id.calendar_week_1);
            weekTwoLayout = itemView.findViewById(R.id.calendar_week_2);
            weekThreeLayout = itemView.findViewById(R.id.calendar_week_3);
            weekFourLayout = itemView.findViewById(R.id.calendar_week_4);
            weekFiveLayout = itemView.findViewById(R.id.calendar_week_5);
            weekSixLayout = itemView.findViewById(R.id.calendar_week_6);
            currentDate = itemView.findViewById(R.id.current_date);
            currentMonth = itemView.findViewById(R.id.current_month);
            llCalendar = itemView.findViewById(R.id.llCalendar);
        }
    }
}
