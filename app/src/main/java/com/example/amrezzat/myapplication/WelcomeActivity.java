package com.example.amrezzat.myapplication;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class WelcomeActivity extends AppCompatActivity {

    ImageView logo;
    NetworkInfo mobileNetworkInfo, WifiNetwork;
    ConnectivityManager connectivity;
    RelativeLayout chickConnection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        chickConnection = (RelativeLayout) findViewById(R.id.chickConnection);
        connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        WifiNetwork = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        mobileNetworkInfo = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() {
               // startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
                if (mobileNetworkInfo.isConnected() || WifiNetwork.isConnected()) {
                    chickConnection.setVisibility(View.GONE);
                    Intent myIntent = new Intent(WelcomeActivity.this, MainActivity.class);
                    ActivityOptions options = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.fade_in, R.anim.fade_out);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(myIntent, options.toBundle());
                    finish();
                }
                else {
                    Snackbar.make(getWindow().getDecorView().getRootView(), "connect to the internet to start and press retry to continue", Snackbar.LENGTH_LONG).show();
                }

            }
        }, 2000);



    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void retryT(View view) {
        // startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
        if (mobileNetworkInfo.isConnected() || WifiNetwork.isConnected()) {
            chickConnection.setVisibility(View.GONE);
            Intent myIntent = new Intent(WelcomeActivity.this, MainActivity.class);
            ActivityOptions options = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.fade_in, R.anim.fade_out);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(myIntent, options.toBundle());
            finish();
        }
        else {
            Snackbar.make(getWindow().getDecorView().getRootView(), "connect to the internet to start and press retry to continue", Snackbar.LENGTH_LONG).show();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void retryI(View view) {
        // startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
        if (mobileNetworkInfo.isConnected() || WifiNetwork.isConnected()) {
            chickConnection.setVisibility(View.GONE);
            Intent myIntent = new Intent(WelcomeActivity.this, MainActivity.class);
            ActivityOptions options = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.fade_in, R.anim.fade_out);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(myIntent, options.toBundle());
            finish();
        }
        else {
            Snackbar.make(getWindow().getDecorView().getRootView(), "connect to the internet to start and press retry to continue", Snackbar.LENGTH_LONG).show();

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
