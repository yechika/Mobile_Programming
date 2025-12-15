package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.firebase.AuthManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    
    // GANTI INI DENGAN WEB CLIENT ID DARI FIREBASE CONSOLE
    private static final String WEB_CLIENT_ID = "89467527216-dm26vcok3nvec8u9hmg5ht8pm292kms8.apps.googleusercontent.com";
    
    private Button btnGoogleSignIn;
    private ProgressBar progressBar;
    private TextView tvStatus;
    
    private AuthManager authManager;
    
    // Activity Result Launcher untuk Google Sign-In
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    handleSignInResult(task);
                } else {
                    hideLoading();
                    Toast.makeText(this, "Sign-in cancelled", Toast.LENGTH_SHORT).show();
                }
            }
    );
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        // Initialize views
        btnGoogleSignIn = findViewById(R.id.btn_google_sign_in);
        progressBar = findViewById(R.id.progress_bar);
        tvStatus = findViewById(R.id.tv_status);
        
        // Initialize AuthManager
        authManager = AuthManager.getInstance();
        authManager.initializeGoogleSignIn(this, WEB_CLIENT_ID);
        
        // Check if user is already signed in
        if (authManager.isUserSignedIn()) {
            navigateToMainActivity();
            return;
        }
        
        // Set up Google Sign-In button
        btnGoogleSignIn.setOnClickListener(v -> signInWithGoogle());
    }
    
    /**
     * Memulai proses Google Sign-In
     */
    private void signInWithGoogle() {
        try {
            showLoading();
            Intent signInIntent = authManager.getGoogleSignInIntent();
            signInLauncher.launch(signInIntent);
        } catch (Exception e) {
            hideLoading();
            Log.e(TAG, "Error launching sign-in intent", e);
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    
    /**
     * Handle hasil Google Sign-In
     */
    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        authManager.handleSignInResult(task, new AuthManager.SignInCallback() {
            @Override
            public void onSignInSuccess(FirebaseUser user) {
                hideLoading();
                Log.d(TAG, "Sign-in successful: " + user.getEmail());
                Toast.makeText(LoginActivity.this, 
                        "Welcome, " + user.getDisplayName() + "!", 
                        Toast.LENGTH_SHORT).show();
                navigateToMainActivity();
            }
            
            @Override
            public void onSignInFailure(String errorMessage) {
                hideLoading();
                Log.e(TAG, "Sign-in failed: " + errorMessage);
                Toast.makeText(LoginActivity.this, 
                        "Sign-in failed: " + errorMessage, 
                        Toast.LENGTH_LONG).show();
            }
        });
    }
    
    /**
     * Navigate ke MainActivity
     */
    private void navigateToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    
    /**
     * Tampilkan loading indicator
     */
    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        btnGoogleSignIn.setEnabled(false);
        tvStatus.setText("Signing in...");
    }
    
    /**
     * Sembunyikan loading indicator
     */
    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
        btnGoogleSignIn.setEnabled(true);
        tvStatus.setText("Sign in with Google to continue");
    }
}
