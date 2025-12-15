package com.example.myapplication.utils;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Production-ready helper class for managing loading states
 */
public class LoadingHelper {
    private final ProgressBar progressBar;
    private final View contentView;
    private final TextView errorView;
    private final TextView emptyView;

    private LoadingHelper(Builder builder) {
        this.progressBar = builder.progressBar;
        this.contentView = builder.contentView;
        this.errorView = builder.errorView;
        this.emptyView = builder.emptyView;
    }

    public void showLoading() {
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
        if (contentView != null) contentView.setVisibility(View.GONE);
        if (errorView != null) errorView.setVisibility(View.GONE);
        if (emptyView != null) emptyView.setVisibility(View.GONE);
    }

    public void showContent() {
        if (progressBar != null) progressBar.setVisibility(View.GONE);
        if (contentView != null) contentView.setVisibility(View.VISIBLE);
        if (errorView != null) errorView.setVisibility(View.GONE);
        if (emptyView != null) emptyView.setVisibility(View.GONE);
    }

    public void showError(String message) {
        if (progressBar != null) progressBar.setVisibility(View.GONE);
        if (contentView != null) contentView.setVisibility(View.GONE);
        if (errorView != null) {
            errorView.setVisibility(View.VISIBLE);
            if (message != null) errorView.setText(message);
        }
        if (emptyView != null) emptyView.setVisibility(View.GONE);
    }

    public void showEmpty(String message) {
        if (progressBar != null) progressBar.setVisibility(View.GONE);
        if (contentView != null) contentView.setVisibility(View.GONE);
        if (errorView != null) errorView.setVisibility(View.GONE);
        if (emptyView != null) {
            emptyView.setVisibility(View.VISIBLE);
            if (message != null) emptyView.setText(message);
        }
    }

    public static class Builder {
        private ProgressBar progressBar;
        private View contentView;
        private TextView errorView;
        private TextView emptyView;

        public Builder setProgressBar(ProgressBar progressBar) {
            this.progressBar = progressBar;
            return this;
        }

        public Builder setContentView(View contentView) {
            this.contentView = contentView;
            return this;
        }

        public Builder setErrorView(TextView errorView) {
            this.errorView = errorView;
            return this;
        }

        public Builder setEmptyView(TextView emptyView) {
            this.emptyView = emptyView;
            return this;
        }

        public LoadingHelper build() {
            return new LoadingHelper(this);
        }
    }
}
