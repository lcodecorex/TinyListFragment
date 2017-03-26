package com.lcodecorex.views;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by lcodecore on 2017/3/26.
 */

public class FooterLayout extends FrameLayout implements IFooter {

    private ProgressView progressView;

    public FooterLayout(@NonNull Context context) {
        this(context, null);
    }

    public FooterLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FooterLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        progressView = new ProgressView(context);
        progressView.setIndicatorId(ProgressView.BallPulse);
        progressView.setIndicatorColor(0xffA0A0A0);
        setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        FrameLayout.LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, dp2px(50), Gravity.CENTER);
        progressView.setLayoutParams(params);
        addView(progressView);
    }

    public void setIndicatorColor(int color) {
        progressView.setIndicatorColor(color);
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void setEnd() {

    }

    private int dp2px(int dpValue) {
        return (int) getContext().getResources().getDisplayMetrics().density * dpValue;
    }
}
