package com.example.myapplication.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;

/**
 * Production-ready image loading utility with Glide optimization
 */
public class ImageLoader {
    
    private static final RequestOptions DEFAULT_OPTIONS = new RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .centerCrop()
        .placeholder(R.mipmap.ic_launcher)
        .error(R.mipmap.ic_launcher);
    
    private static final RequestOptions POSTER_OPTIONS = new RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .centerCrop()
        .override(500, 750) // Optimize poster size
        .placeholder(R.mipmap.ic_launcher)
        .error(R.mipmap.ic_launcher);
    
    private static final RequestOptions BACKDROP_OPTIONS = new RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .centerCrop()
        .override(1280, 720) // Optimize backdrop size
        .placeholder(R.mipmap.ic_launcher)
        .error(R.mipmap.ic_launcher);
    
    public static void loadPoster(Context context, String url, ImageView imageView) {
        if (context == null || imageView == null) return;
        
        try {
            Glide.with(context)
                .load(url)
                .apply(POSTER_OPTIONS)
                .transition(DrawableTransitionOptions.withCrossFade(300))
                .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void loadBackdrop(Context context, String url, ImageView imageView) {
        if (context == null || imageView == null) return;
        
        try {
            Glide.with(context)
                .load(url)
                .apply(BACKDROP_OPTIONS)
                .transition(DrawableTransitionOptions.withCrossFade(300))
                .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void loadImage(Context context, String url, ImageView imageView) {
        if (context == null || imageView == null) return;
        
        try {
            Glide.with(context)
                .load(url)
                .apply(DEFAULT_OPTIONS)
                .transition(DrawableTransitionOptions.withCrossFade(300))
                .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void clearImage(Context context, ImageView imageView) {
        if (context == null || imageView == null) return;
        
        try {
            Glide.with(context).clear(imageView);
        } catch (Exception e) {
            // Ignore if context is destroyed
        }
    }
}
