package com.intelin.vn.myfintech.something_view.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;

import com.intelin.vn.myfintech.BaseActivity;
import com.intelin.vn.myfintech.LogDog;
import com.intelin.vn.myfintech.R;
import com.intelin.vn.myfintech.databinding.ActivitiyTestBinding;
import com.intelin.vn.myfintech.exception.CustomException;
import com.intelin.vn.myfintech.retrofit_client.CallRequestService;
import com.intelin.vn.myfintech.something_view.view_models.DataModel;
import com.intelin.vn.myfintech.utils.ListCalendar;


/**
 * Copyright by Intelin.
 * Creator: Tran Do Gia An
 * Date: 22/03/2019
 * Time: 9:29 AM
 */
public class TestActivity extends BaseActivity {

    ActivitiyTestBinding activityTestBinding;
    ConstraintLayout clMain;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogDog.d("MainActivity onCreate");
        activityTestBinding = DataBindingUtil.setContentView(this, R.layout.activitiy_test);
        getData();
        initView();
        ListCalendar simpleCalendar = findViewById(R.id.rvView);
//        simpleCalendar.onDayToDayListener(this);
//        simpleCalendar.onDayToDayListener((view, dateStart, dateEnd) -> LogDog.d("NgayDaChon" + String.valueOf(dateStart) + String.valueOf(dateEnd)));
    }

    private void getData() {
        LogDog.d("get Data");
        CallRequestService.getInstance().enqueue(0);
    }

    private void initView() {
        clMain = findViewById(R.id.clMain);
        activityTestBinding.tvDemo.setOnClickListener(v -> dialogEditText(activityTestBinding.tvDemo, this));
    }

    @Override
    public void ResponseSuccess(String body) {
        super.ResponseSuccess(body);
        LogDog.d("Call Api success");

        try {
//            Data data = JSON.decode(body, Data.class);
            activityTestBinding.setMan(new DataModel().setAction("aaaa").setName("hohohoho"));
            activityTestBinding.executePendingBindings();
        } catch (IllegalStateException e) {
            LogDog.e(e.getMessage());
            back();
        } catch (CustomException.DecodingException e) {
            LogDog.e(e.getMessage());
            back();
        } catch (Exception e) {
            LogDog.e(e.getMessage());
            back();
        }
    }

    @Override
    public void ErrorResponse(String message) {
        super.ErrorResponse(message);
        LogDog.e(message);
        back();
    }

    @Override
    public void back() {
        super.back();
        onBackPressed();
//        startActivityForResult(new Intent(this, MainActivity.class), 500);
//        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogDog.d("MainActivity onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogDog.d("MainActivity onStop");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogDog.d("MainActivity onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogDog.d("MainActivity onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogDog.d("MainActivity onPause");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
