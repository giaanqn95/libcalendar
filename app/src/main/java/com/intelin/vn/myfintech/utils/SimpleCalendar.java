package com.intelin.vn.myfintech.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.intelin.vn.myfintech.R;

import java.util.Calendar;

/**
 * Copyright by Intelin.
 * Creator: Tran Do Gia An
 * Date: 02/04/2019
 * Time: 5:50 PM
 */
public class SimpleCalendar extends LinearLayout {

    private static final String CUSTOM_GREY = "#a0a0a0";
    private static final String[] ENG_MONTH_NAMES = {"January", "February", "March", "April",
            "May", "June", "July", "August",
            "September", "October", "November", "December"};

    private TextView currentDate, currentMonth, tvType, tvNameType;
    private Button selectedDayButton, endDayButton, startDayButton;
    private Button[] days;
    LinearLayout weekOneLayout;
    LinearLayout weekTwoLayout;
    LinearLayout weekThreeLayout;
    LinearLayout weekFourLayout;
    LinearLayout weekFiveLayout;
    LinearLayout weekSixLayout;
    private LinearLayout[] weeks;

    private int currentDateDay, chosenDateDay, currentDateMonth,
            chosenDateMonth, currentDateYear, chosenDateYear,
            pickedDateDay, pickedDateMonth, pickedDateYear;
    int userMonth, userYear, typeOption = 0;
    private DayClickListener mListener;
    private Drawable userDrawable;

    private Calendar calendar;
    LayoutParams defaultButtonParams;
    private LayoutParams userButtonParams;
    private Long timePickedLong;
    private Context context;

    private int dateStart, monthStart, yearStart,
            dateEnd, monthEnd, yearEnd;
    private int start, end;

    public SimpleCalendar(Context context) {
        super(context);
        this.context = context;
        init(context);
    }

    public SimpleCalendar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context);
    }

    public SimpleCalendar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(context);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SimpleCalendar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        init(context);
    }

    private void init(Context context) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();

        View view = LayoutInflater.from(context).inflate(R.layout.custome_calendar, this, true);
        calendar = Calendar.getInstance();

        tvType = view.findViewById(R.id.tvType);
        tvNameType = view.findViewById(R.id.tvNameType);
        weekOneLayout = view.findViewById(R.id.calendar_week_1);
        weekTwoLayout = view.findViewById(R.id.calendar_week_2);
        weekThreeLayout = view.findViewById(R.id.calendar_week_3);
        weekFourLayout = view.findViewById(R.id.calendar_week_4);
        weekFiveLayout = view.findViewById(R.id.calendar_week_5);
        weekSixLayout = view.findViewById(R.id.calendar_week_6);
        currentDate = view.findViewById(R.id.current_date);
        currentMonth = view.findViewById(R.id.current_month);

        currentDateDay = chosenDateDay = calendar.get(Calendar.DAY_OF_MONTH);

        setText();

        if (userMonth != 0 && userYear != 0) {
            currentDateMonth = chosenDateMonth = userMonth;
            currentDateYear = chosenDateYear = userYear;
        } else {
            currentDateMonth = chosenDateMonth = calendar.get(Calendar.MONTH);
            currentDateYear = chosenDateYear = calendar.get(Calendar.YEAR);
        }

        currentDate.setText("" + currentDateDay);
        currentMonth.setText(ENG_MONTH_NAMES[currentDateMonth]);

        initializeDaysWeeks();
        if (userButtonParams != null) {
            defaultButtonParams = userButtonParams;
        } else {
            defaultButtonParams = getDaysLayoutParams();
        }
        addDaysInMonthCalendar(defaultButtonParams, context, metrics);

        initCalendarWithDate(chosenDateYear, chosenDateMonth, chosenDateDay);
        changeTypeSelection();
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

    private void initializeDaysWeeks() {
        weeks = new LinearLayout[6];
        days = new Button[6 * 7];

        weeks[0] = weekOneLayout;
        weeks[1] = weekTwoLayout;
        weeks[2] = weekThreeLayout;
        weeks[3] = weekFourLayout;
        weeks[4] = weekFiveLayout;
        weeks[5] = weekSixLayout;
    }

    //Tao 1 danh sách lịch
    private void addDaysInMonthCalendar(LayoutParams buttonParams, Context context,
                                        DisplayMetrics metrics) {
        int engDaysArrayCounter = 0;

        for (int weekNumber = 0; weekNumber < 6; ++weekNumber) {
            for (int dayInWeek = 0; dayInWeek < 7; ++dayInWeek) {
                final Button day = new Button(context);
                day.setTextColor(Color.parseColor(CUSTOM_GREY));
                day.setBackgroundColor(Color.TRANSPARENT);
                day.setLayoutParams(buttonParams);
                day.setTextSize((int) metrics.density * 8);
                day.setSingleLine();

                days[engDaysArrayCounter] = day;
                weeks[weekNumber].addView(day);

                ++engDaysArrayCounter;
            }
        }
    }

    private void initCalendarWithDate(int year, int month, int day) {
        if (calendar == null) {
            calendar = Calendar.getInstance();
        }
        calendar.set(year, month, day);

        //Get total days in month
        int daysInCurrentMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        chosenDateYear = year;
        chosenDateMonth = month;
        chosenDateDay = day;

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
            for (int i = firstDayOfCurrentMonth - 1; i <= firstDayOfCurrentMonth + daysInCurrentMonth - 1; ++i) {
                days[i].setTextColor(Color.BLACK);
                days[i].setBackgroundColor(Color.TRANSPARENT);

                //Tạo 1 mảng int chứa ngày tháng năm rồi set vào Tag. Được xem như là lưu tạm dữ liệu vào View
                int[] dateArr = new int[3];
                dateArr[0] = dayNumber;
                dateArr[1] = chosenDateMonth;
                dateArr[2] = chosenDateYear;
                days[i].setTag(dateArr);
                days[i].setText(String.valueOf(dayNumber));
                //Cái này khỏi giải thích
                if (i < firstDayOfCurrentMonth + daysInCurrentMonth - 1) {
                    int a = i;
                    days[i].setOnClickListener(v -> onDayClick(v, a));
                }
                if (currentDateMonth == chosenDateMonth && currentDateYear == chosenDateYear && dayNumber == currentDateDay) {
                    chooseDay(days[i]);
                }
                ++dayNumber;
            }
            //Trường hợp này là ngày đầu tiên trong tháng hiện tại nằm ở vị trí đầu
        } else {
            daysLeftInFirstWeek = 8;
            indexOfDayAfterLastDayOfMonth = daysLeftInFirstWeek + daysInCurrentMonth - 1;
            for (int i = 8; i < 8 + daysInCurrentMonth; ++i) {
                if (currentDateMonth == chosenDateMonth && currentDateYear == chosenDateYear && dayNumber == currentDateDay) {
                    days[i].setBackgroundColor(getResources().getColor(R.color.pink));
                    days[i].setTextColor(Color.WHITE);
                } else {
                    days[i].setTextColor(Color.BLACK);
                    days[i].setBackgroundColor(Color.TRANSPARENT);
                }

                int[] dateArr = new int[3];
                dateArr[0] = dayNumber;
                dateArr[1] = chosenDateMonth;
                dateArr[2] = chosenDateYear;
                days[i].setTag(dateArr);
                days[i].setText(String.valueOf(dayNumber));
                int a = i;
                days[i].setOnClickListener(v -> onDayClick(v, a));
                ++dayNumber;
            }
        }

        if (month > 0) {
            calendar.set(year, month - 1, 1);
        } else {
            calendar.set(year - 1, 11, 1);
        }
        //Set calendar last month
        int daysInPreviousMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        //Add days last month
        for (int i = daysLeftInFirstWeek - 1; i >= 0; --i) {
            int[] dateArr = new int[3];

            if (chosenDateMonth > 0) {
                if (currentDateMonth == chosenDateMonth - 1 && currentDateYear == chosenDateYear && daysInPreviousMonth == currentDateDay) {
                } else {
                    days[i].setBackgroundColor(Color.TRANSPARENT);
                }

                dateArr[0] = daysInPreviousMonth;
                dateArr[1] = chosenDateMonth - 1;
                dateArr[2] = chosenDateYear;
            } else {
                if (currentDateMonth == 11 && currentDateYear == chosenDateYear - 1 && daysInPreviousMonth == currentDateDay) {
                } else {
                    days[i].setBackgroundColor(Color.TRANSPARENT);
                }

                dateArr[0] = daysInPreviousMonth;
                dateArr[1] = 11;
                dateArr[2] = chosenDateYear - 1;
            }

            days[i].setTag(dateArr);
            days[i].setText(String.valueOf(daysInPreviousMonth--));
//            days[i].setOnClickListener(v -> onDayClick(v));
        }

        //Add days next month
        int nextMonthDaysCounter = 1;
        for (int i = indexOfDayAfterLastDayOfMonth + 1; i < days.length; ++i) {
            int[] dateArr = new int[3];

            if (chosenDateMonth < 11) {
                if (currentDateMonth == chosenDateMonth + 1 && currentDateYear == chosenDateYear && nextMonthDaysCounter == currentDateDay) {
                    days[i].setBackgroundColor(getResources().getColor(R.color.pink));
                } else {
                    days[i].setBackgroundColor(Color.TRANSPARENT);
                }

                dateArr[0] = nextMonthDaysCounter;
                dateArr[1] = chosenDateMonth + 1;
                dateArr[2] = chosenDateYear;
            } else {
                if (currentDateMonth == 0 && currentDateYear == chosenDateYear + 1 && nextMonthDaysCounter == currentDateDay) {
                    days[i].setBackgroundColor(getResources().getColor(R.color.pink));
                } else {
                    days[i].setBackgroundColor(Color.TRANSPARENT);
                }

                dateArr[0] = nextMonthDaysCounter;
                dateArr[1] = 0;
                dateArr[2] = chosenDateYear + 1;
            }

            days[i].setTag(dateArr);
            days[i].setTextColor(Color.parseColor(CUSTOM_GREY));
            days[i].setText(String.valueOf(nextMonthDaysCounter++));
//            days[i].setOnClickListener(v -> onDayClick(v));
        }

        //Set calendar current days
        calendar.set(chosenDateYear, chosenDateMonth, chosenDateDay);
    }

    public void onDayClick(View view, int position) {
        switch (typeOption) {
            case 1:
                dateToDate(view, position);
                break;
            default:
                chooseDay(view);
                break;
        }
    }

    private void chooseDay(View view) {
        if (selectedDayButton != null) {
            selectedDayButton.setBackgroundColor(Color.TRANSPARENT);
            selectedDayButton.setTextColor(getResources().getColor(R.color.calendar_number));
        }

        selectedDayButton = (Button) view;
        if (selectedDayButton.getTag() != null) {
            int[] dateArray = (int[]) selectedDayButton.getTag();
            pickedDateDay = dateArray[0];
            pickedDateMonth = dateArray[1];
            pickedDateYear = dateArray[2];
        }

        if (pickedDateYear == currentDateYear && pickedDateMonth == currentDateMonth && pickedDateDay == currentDateDay) {
            selectedDayButton.setBackgroundColor(getResources().getColor(R.color.pink));
            selectedDayButton.setTextColor(Color.WHITE);
        } else {
            selectedDayButton.setBackgroundColor(getResources().getColor(R.color.grey));
            if (selectedDayButton.getCurrentTextColor() != Color.RED) {
                selectedDayButton.setTextColor(Color.WHITE);
            }
        }

        setTimePickedLong(pickedDateYear, pickedDateMonth, pickedDateDay);
//        mListener.onDayClick(view, pickedDateYear, pickedDateMonth, pickedDateDay);
    }

    private void dateToDate(View view, int position) {
        Calendar calEnd = Calendar.getInstance();
        Calendar calStart = Calendar.getInstance();
        if (startDayButton == null) {
            startDayButton = (Button) view;
            if (startDayButton.getTag() != null) {
                int[] tagDateStart = (int[]) startDayButton.getTag();
                dateStart = tagDateStart[0];
                monthStart = tagDateStart[1];
                yearStart = tagDateStart[2];
            }
            selectedStartButton();
            start = position;
            calStart.set(yearStart, monthStart, dateStart);
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

            if (dateEnd + monthEnd + yearEnd < dateStart + monthStart + yearStart) {
                unSelectStartButton();
                startDayButton = (Button) view;
                if (startDayButton.getTag() != null) {
                    int[] tagDateStart = (int[]) startDayButton.getTag();
                    dateStart = tagDateStart[0];
                    monthStart = tagDateStart[1];
                    yearStart = tagDateStart[2];
                }
                selectedStartButton();
                calStart.set(yearStart, monthStart, dateStart);
                start = position;
                return;
            }

            endDayButton = (Button) view;
            selectedEndButton();
            calEnd.set(yearEnd, monthEnd, dateEnd);
            end = position;
            mListener.onDayToDay(view, calStart.getTimeInMillis(), calEnd.getTimeInMillis());
            setLineDateToDate(start, end);
        } else if (startDayButton != null && endDayButton != null) {
            unSelectStartButton();
            unSelectEndButton();
            startDayButton = null;
            endDayButton = null;
            unSetLineDateToDate();
            dateToDate(view, position);
            return;
        }
    }

    private void selectedStartButton() {
        startDayButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_border_date_start));
        startDayButton.setTextColor(getResources().getColor(R.color.white));
    }

    private void selectedEndButton() {
        endDayButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_border_date_end));
        endDayButton.setTextColor(getResources().getColor(R.color.white));
    }

    private void unSelectStartButton() {
        startDayButton.setBackgroundColor(Color.TRANSPARENT);
        startDayButton.setTextColor(getResources().getColor(R.color.calendar_number));
    }

    private void unSelectEndButton() {
        endDayButton.setBackgroundColor(Color.TRANSPARENT);
        endDayButton.setTextColor(getResources().getColor(R.color.calendar_number));
    }

    private LayoutParams getDaysLayoutParams() {
        LayoutParams buttonParams = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        buttonParams.weight = 1;
        return buttonParams;
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
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        });
    }

    private void clearSelected() {
        switch (typeOption) {
            case 1:
                unSelected();
                break;
            default:
                unSelectedStartDate();
                unSelectedEndDate();
                unSetLineDateToDate();
                break;
        }
    }

    private void unSelected() {
        if (selectedDayButton != null) {
            selectedDayButton.setBackgroundColor(Color.TRANSPARENT);
            if (selectedDayButton.getCurrentTextColor() != Color.RED) {
                selectedDayButton.setTextColor(getResources().getColor(R.color.calendar_number));
            }
        }
        selectedDayButton = null;
    }

    private void unSelectedStartDate() {
        if (startDayButton != null) {
            startDayButton.setBackgroundColor(Color.TRANSPARENT);
            if (startDayButton.getCurrentTextColor() != Color.RED) {
                startDayButton.setTextColor(getResources().getColor(R.color.calendar_number));
            }
        }
        startDayButton = null;
    }

    private void unSelectedEndDate() {
        if (endDayButton != null) {
            endDayButton.setBackgroundColor(Color.TRANSPARENT);
            if (endDayButton.getCurrentTextColor() != Color.RED) {
                endDayButton.setTextColor(getResources().getColor(R.color.calendar_number));
            }
        }
        endDayButton = null;
    }

    public void setUserDaysLayoutParams(LayoutParams userButtonParams) {
        this.userButtonParams = userButtonParams;
    }

    public void setUserCurrentMonthYear(int userMonth, int userYear) {
        this.userMonth = userMonth;
        this.userYear = userYear;
    }

    public void setDayBackground(Drawable userDrawable) {
        this.userDrawable = userDrawable;
    }

    public void setTimePickedLong(int year, int month, int date) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, date);
        this.timePickedLong = cal.getTimeInMillis();
    }

    public long getTimeToLong() {
        if (timePickedLong == 0) {
            setTimePickedLong(currentDateYear, currentDateMonth, currentDateDay);
        }
        return timePickedLong;
    }

    public interface DayClickListener {
        void onDayClick(View view, int year, int month, int day);

        void onDayToDay(View view, long dateStart, long dateEnd);
    }

    public void setCallBack(DayClickListener mListener) {
        this.mListener = mListener;
    }

    private void setLineDateToDate(int start, int end) {
        for (int i = start + 1; i < end; i++) {
            days[i].setBackgroundColor(getResources().getColor(R.color.blueLine));
            days[i].setTextColor(Color.WHITE);
        }
    }

    private void unSetLineDateToDate() {
        for (int i = start + 1; i < end; i++) {
            days[i].setBackgroundColor(Color.TRANSPARENT);
            days[i].setTextColor(getResources().getColor(R.color.calendar_number));
        }
    }
}