package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_DURATION = 2500; // 2.5 seconds
    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        try {
            setContentView(R.layout.activity_splash);

            // Using Handler with Looper to delay the transition to MainActivity
            handler = new Handler(Looper.getMainLooper());
            runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish(); // Close splash activity
                    } catch (Exception e) {
                        e.printStackTrace();
                        finish();
                    }
                }
            };
            handler.postDelayed(runnable, SPLASH_DURATION);
        } catch (Exception e) {
            e.printStackTrace();
            // If splash fails, go directly to MainActivity
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove callbacks to prevent memory leak
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }
}
