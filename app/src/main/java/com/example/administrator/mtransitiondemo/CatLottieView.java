package com.example.administrator.mtransitiondemo;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;

import com.airbnb.lottie.LottieAnimationView;
import com.mjun.mtransition.ITransitional;

/**
 * Created by huijun on 2018/4/9.
 */

public class CatLottieView extends LottieAnimationView implements ITransitional {

    private Context context;

    public CatLottieView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public CatLottieView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        useHardwareAcceleration();
        setAnimation("loading.json");
        setProgress(0f);
    }

    @Override
    public void onTransitProgress(long playTime, float progress) {
        setProgress(progress);
        if (Example6EntryActivity.hasItBeenSwitchedToTheRightArrow == false) {
            if (progress == 1.0) {
                onTransitEnd();
                Example6EntryActivity.hasItBeenSwitchedToTheRightArrow = true;
            }
        }
    }

    @Override
    public void onTransitStart() {
    }

    @Override
    public void onTransitEnd() {
        Intent intent = new Intent(Notification.CatLottieViewNotification);
        context.sendBroadcast(intent);
    }
}
