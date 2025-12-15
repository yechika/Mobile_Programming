package com.example.myapplication.utils;

import android.content.Context;
import android.widget.Toast;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import retrofit2.HttpException;

/**
 * Production-ready error handling utility
 */
public class ErrorHandler {
    
    public static String getErrorMessage(Throwable throwable) {
        if (throwable == null) {
            return "Unknown error occurred";
        }
        
        if (throwable instanceof UnknownHostException) {
            return "No internet connection. Please check your network.";
        } else if (throwable instanceof SocketTimeoutException) {
            return "Connection timeout. Please try again.";
        } else if (throwable instanceof HttpException) {
            HttpException httpException = (HttpException) throwable;
            int code = httpException.code();
            
            switch (code) {
                case 400:
                    return "Bad request. Please try again.";
                case 401:
                    return "Unauthorized. Please check your API key.";
                case 403:
                    return "Access forbidden.";
                case 404:
                    return "Resource not found.";
                case 500:
                    return "Server error. Please try again later.";
                case 503:
                    return "Service unavailable. Please try again later.";
                default:
                    return "Network error: " + code;
            }
        } else if (throwable.getMessage() != null) {
            return throwable.getMessage();
        }
        
        return "An unexpected error occurred";
    }
    
    public static void showError(Context context, Throwable throwable) {
        if (context == null) return;
        
        String message = getErrorMessage(throwable);
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
    
    public static void showError(Context context, String message) {
        if (context == null) return;
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
