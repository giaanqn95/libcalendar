package com.intelin.vn.myfintech.utils;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Copyright by Intelin.
 * Creator: Tran Do Gia An
 * Date: 27/03/2019
 * Time: 10:40 AM
 */
public class CustomConstraint extends ConstraintLayout {

    private int pageHeight;

    public void setPageHeight(int pageHeight) {
        this.pageHeight = pageHeight;
        requestLayout();
    }

    public CustomConstraint(Context context) {
        super(context);
    }

    public CustomConstraint(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomConstraint(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = 0, desireHeight;
        Log.v("Chart onMeasure w", MeasureSpec.toString(widthMeasureSpec));
        Log.v("Chart onMeasure h", MeasureSpec.toString(heightMeasureSpec));
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (heightMode == MeasureSpec.EXACTLY) {
            if (pageHeight <= 131) {
                height = pageHeight;
            } else {
                height = 131;
            }
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(pageHeight, heightSize);
        } else {
            height = pageHeight;
        }

        setMeasuredDimension(widthMeasureSpec, height);

//        int desiredHeight = getSuggestedMinimumHeight() + pageHeight;
//        if (pageHeight > 0) {
//            heightMeasureSpec = MeasureSpec.makeMeasureSpec(pageHeight, MeasureSpec.EXACTLY);
//        }
//        setMeasuredDimension(MeasureSpec.getMode(widthMeasureSpec), measureDimension(desiredHeight, heightMeasureSpec));
    }

    private int measureDimension(int desiredSize, int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = desiredSize;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }

        if (result < desiredSize) {
            Log.e("ChartView", "The view is too small, the content might get cut");
        }
        return result;
    }
}
