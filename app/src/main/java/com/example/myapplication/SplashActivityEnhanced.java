package com.example.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Production-ready Splash Activity with animations and proper lifecycle management
 * Features:
 * - Fade-in animation for smooth entry
 * - Proper memory leak prevention
 * - Error handling
 * - Configurable splash duration
 */
public class SplashActivityEnhanced extends AppCompatActivity {
    private static final int SPLASH_DURATION = 2500; // 2.5 seconds
    private static final int ANIMATION_DURATION = 800; // 0.8 seconds
    
    private Handler handler;
    private Runnable navigationRunnable;
    private LinearLayout splashContainer;
    private boolean isNavigating = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        try {
            setContentView(R.layout.activity_splash);
            
            splashContainer = findViewById(R.id.splashContainer);
            
            // Start fade-in animation
            animateSplash();
            
            // Schedule navigation
            scheduleNavigation();
            
        } catch (Exception e) {
            e.printStackTrace();
            navigateToMain();
        }
    }
    
    private void animateSplash() {
        if (splashContainer != null) {
            splashContainer.animate()
                .alpha(1f)
                .setDuration(ANIMATION_DURATION)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();
        }
    }
    
    private void scheduleNavigation() {
        handler = new Handler(Looper.getMainLooper());
        navigationRunnable = new Runnable() {
            @Override
            public void run() {
                if (!isFinishing() && !isNavigating) {
                    isNavigating = true;
                    animateExitAndNavigate();
                }
            }
        };
        handler.postDelayed(navigationRunnable, SPLASH_DURATION);
    }
    
    private void animateExitAndNavigate() {
        if (splashContainer != null) {
            splashContainer.animate()
                .alpha(0f)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        navigateToMain();
                    }
                })
                .start();
        } else {
            navigateToMain();
        }
    }
    
    private void navigateToMain() {
        try {
            if (!isFinishing()) {
                Intent intent = new Intent(SplashActivityEnhanced.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Prevent memory leaks
        if (handler != null && navigationRunnable != null) {
            handler.removeCallbacks(navigationRunnable);
        }
        handler = null;
        navigationRunnable = null;
    }
    
    @Override
    public void onBackPressed() {
        // Disable back button on splash screen
    }
}
