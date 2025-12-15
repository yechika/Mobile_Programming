package com.example.myapplication.firebase;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * Singleton class untuk mengelola Google Authentication dengan Firebase
 */
public class AuthManager {
    private static final String TAG = "AuthManager";
    private static AuthManager instance;
    
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    
    // Private constructor untuk Singleton pattern
    private AuthManager() {
        mAuth = FirebaseAuth.getInstance();
    }
    
    /**
     * Mendapatkan instance AuthManager (Singleton)
     */
    public static synchronized AuthManager getInstance() {
        if (instance == null) {
            instance = new AuthManager();
        }
        return instance;
    }
    
    /**
     * Initialize Google Sign-In Client
     */
    public void initializeGoogleSignIn(Context context, String webClientId) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(webClientId)
                .requestEmail()
                .build();
        
        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
    }
    
    /**
     * Mendapatkan Intent untuk Google Sign-In
     */
    public Intent getGoogleSignInIntent() {
        if (mGoogleSignInClient == null) {
            throw new IllegalStateException("GoogleSignInClient not initialized. Call initializeGoogleSignIn() first.");
        }
        return mGoogleSignInClient.getSignInIntent();
    }
    
    /**
     * Handle hasil sign-in dari Google
     */
    public void handleSignInResult(Task<GoogleSignInAccount> completedTask, SignInCallback callback) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                firebaseAuthWithGoogle(account.getIdToken(), callback);
            } else {
                callback.onSignInFailure("Google Sign-In failed: account is null");
            }
        } catch (ApiException e) {
            Log.e(TAG, "Google sign in failed", e);
            callback.onSignInFailure("Google Sign-In failed: " + e.getStatusCode());
        }
    }
    
    /**
     * Authenticate dengan Firebase menggunakan Google credential
     */
    private void firebaseAuthWithGoogle(String idToken, SignInCallback callback) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        callback.onSignInSuccess(user);
                    } else {
                        Log.e(TAG, "signInWithCredential:failure", task.getException());
                        callback.onSignInFailure("Authentication failed: " + 
                                (task.getException() != null ? task.getException().getMessage() : "Unknown error"));
                    }
                });
    }
    
    /**
     * Sign out dari Firebase dan Google
     */
    public void signOut(Context context, SignOutCallback callback) {
        // Sign out dari Firebase
        mAuth.signOut();
        
        // Sign out dari Google
        if (mGoogleSignInClient != null) {
            mGoogleSignInClient.signOut()
                    .addOnCompleteListener((Activity) context, task -> {
                        if (callback != null) {
                            callback.onSignOutSuccess();
                        }
                    });
        } else {
            if (callback != null) {
                callback.onSignOutSuccess();
            }
        }
    }
    
    /**
     * Cek apakah user sudah login
     */
    public boolean isUserSignedIn() {
        return mAuth.getCurrentUser() != null;
    }
    
    /**
     * Mendapatkan current user
     */
    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }
    
    /**
     * Mendapatkan user ID
     */
    public String getCurrentUserId() {
        FirebaseUser user = getCurrentUser();
        return user != null ? user.getUid() : null;
    }
    
    /**
     * Mendapatkan nama user
     */
    public String getCurrentUserName() {
        FirebaseUser user = getCurrentUser();
        return user != null ? user.getDisplayName() : null;
    }
    
    /**
     * Mendapatkan email user
     */
    public String getCurrentUserEmail() {
        FirebaseUser user = getCurrentUser();
        return user != null ? user.getEmail() : null;
    }
    
    /**
     * Mendapatkan URL foto profil user
     */
    public Uri getCurrentUserPhotoUrl() {
        FirebaseUser user = getCurrentUser();
        return user != null ? user.getPhotoUrl() : null;
    }
    
    /**
     * Callback interface untuk Sign-In
     */
    public interface SignInCallback {
        void onSignInSuccess(FirebaseUser user);
        void onSignInFailure(String errorMessage);
    }
    
    /**
     * Callback interface untuk Sign-Out
     */
    public interface SignOutCallback {
        void onSignOutSuccess();
    }
}
