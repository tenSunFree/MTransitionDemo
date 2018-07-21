package com.example.administrator.mtransitiondemo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.Toast;

import com.mjun.mtransition.ITransitPrepareListener;
import com.mjun.mtransition.MTransition;
import com.mjun.mtransition.MTransitionManager;
import com.mjun.mtransition.MTransitionView;
import com.mjun.mtransition.MTranstionUtil;
import com.mjun.mtransition.TransitListenerAdapter;

/**
 * Created by huijun on 2018/4/8.
 */

public class Example6DetailActivity extends Activity {

    private ImageView rightArrowImageView;
    private View myContainer;
    private CatLottieView animView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example6_detail);

        init();
        initializeBroadcast();
    }

    private void init() {
        myContainer = findViewById(R.id.container);
        animView = findViewById(R.id.anim_view);
        rightArrowImageView = findViewById(R.id.rightArrowImageView);
        animView.setProgress(1f);
        final MTransition transition = MTransitionManager.getInstance().getTransition("example");
        transition.toPage().setContainer(myContainer, new ITransitPrepareListener() {
            @Override
            public void onPrepare(MTransitionView container) {
                int width = container.getWidth();
                container.translateX(width, 0);
                MTransitionView toAnimView = transition.toPage().addTransitionView("animView", animView);
                toAnimView.hideDuringTrasition();
                MTransitionView fromAnimView = transition.fromPage().getTransitionView("animView");
                CatLottieView replace = new CatLottieView(Example6DetailActivity.this);
                fromAnimView.replaceBy(replace, new LayoutParams(animView.getWidth(), animView.getHeight()));
                fromAnimView.translateXTo(toAnimView.getSourceView().getLeft() - fromAnimView.getSourceView().getLeft());
                fromAnimView.above(container);
                transition.setDuration(animView.getDuration());
                transition.start();
            }
        });

        transition.setOnTransitListener(new TransitListenerAdapter() {
            @Override
            public void onTransitEnd(MTransition transition, boolean reverse) {
                if (reverse) {
                    Intent intent = new Intent(Notification.Example6DetailActivityNotification);
                    sendBroadcast(intent);
                    finish();
                    MTranstionUtil.removeActivityAnimation(Example6DetailActivity.this);
                }
            }
        });

        rightArrowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rightArrowImageView.setVisibility(View.GONE);
                animView.setVisibility(View.VISIBLE);
                animView.performClick();
            }
        });

        animView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animView.setVisibility(View.VISIBLE);
                rightArrowImageView.setVisibility(View.GONE);
                reverse();
            }
        });
    }

    private void initializeBroadcast() {
        IntentFilter filter = new IntentFilter(Notification.CatLottieViewNotification);
        registerReceiver(broadcastReceiver, filter);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "Example6DetailActivity", Toast.LENGTH_SHORT).show();
            animView.setVisibility(View.GONE);
            rightArrowImageView.setVisibility(View.VISIBLE);
        }
    };

    private void reverse() {
        final MTransition transition = MTransitionManager.getInstance().getTransition("example");
        transition.reverse();
    }

    @Override
    public void onBackPressed() {
        reverse();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MTransitionManager.getInstance().destoryTransition("example");
        unregisterReceiver(broadcastReceiver);
    }
}
