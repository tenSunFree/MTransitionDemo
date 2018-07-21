package com.example.administrator.mtransitiondemo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.mjun.mtransition.ITransitPrepareListener;
import com.mjun.mtransition.MTransition;
import com.mjun.mtransition.MTransitionManager;
import com.mjun.mtransition.MTransitionView;
import com.mjun.mtransition.MTranstionUtil;

/**
 * Created by huijun on 2018/4/8.
 */

public class Example6EntryActivity extends Activity {

    private ImageView leftArrowImageView;
    private View myContainer;
    private CatLottieView animView;

    public static boolean hasItBeenSwitchedToTheRightArrow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example6_entry);

        init();
        initializeBroadcast();
    }

    private void init() {

        myContainer = findViewById(R.id.container);
        animView = findViewById(R.id.anim_view);
        leftArrowImageView = findViewById(R.id.leftArrowImageView);
        animView.setVisibility(View.GONE);
        leftArrowImageView.setVisibility(View.VISIBLE);

        leftArrowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hasItBeenSwitchedToTheRightArrow = false;
                leftArrowImageView.setVisibility(View.GONE);
                animView.setVisibility(View.VISIBLE);
                animView.performClick();
            }
        });

        animView.setProgress(0f);
        animView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MTransition transition =
                        MTransitionManager.getInstance().createTransition("example");
                transition.fromPage().setContainer(myContainer, new ITransitPrepareListener() {
                    @Override
                    public void onPrepare(MTransitionView container) {
                        transition.fromPage().addTransitionView("animView", animView);
                    }
                });
                Intent intent = new Intent(Example6EntryActivity.this, Example6DetailActivity.class);
                startActivity(intent);
                MTranstionUtil.removeActivityAnimation(Example6EntryActivity.this);
            }
        });
    }

    private void initializeBroadcast() {
        IntentFilter filter = new IntentFilter(Notification.Example6DetailActivityNotification);
        registerReceiver(broadcastReceiver, filter);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "Example6EntryActivity", Toast.LENGTH_SHORT).show();
            leftArrowImageView.setVisibility(View.VISIBLE);
            animView.setVisibility(View.GONE);
        }
    };

    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}
