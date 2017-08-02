package com.dede.basetest;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dede.basemodule.base.activity.BaseActivity;
import com.dede.basemodule.view.NetStateView;
import com.dede.basemodule.view.NetStateViewBuilder;

/**
 * 2017/6/9 009 下午 02:01.
 * 实现了自定义不同视图的Activity
 */
public abstract class AppActivity extends BaseActivity {

    private TextView loadingText;

    @Override
    protected NetStateView onCreateNetStateView(Context context) {
        LinearLayout loadingView = new LinearLayout(context);
        loadingView.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        ProgressBar progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleInverse);
        loadingView.addView(progressBar, params);
        loadingText = new TextView(context);
        loadingText.setText("加载中...");
        loadingView.addView(loadingText, params);

        return new NetStateViewBuilder(context)
                .setOnRetryClickListener(new NetStateView.OnRetryClickListener() {
                    @Override
                    public void onErrorViewClick(View errorView) {
                        showLoading();
                    }
                })
                .setLoadingView(loadingView)
                .setLoadingCancelable(true)
                .build();
    }

    public void showLoading() {
        mNetStateView.showLoading();
    }

    public void showLoading(String msg) {
        loadingText.setText(msg);
        showLoading();
    }

    public void hideLoading() {
        mNetStateView.hideLoading();
    }

    public void showError() {
        mNetStateView.showError();
    }

    public void showEmpty() {
        mNetStateView.showEmpty();
    }
}
