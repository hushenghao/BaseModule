package com.dede.basemodule.base.activity;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dede.basemodule.view.NetStateView;
import com.dede.basemodule.view.NetStateViewBuilder;

/**
 * @author hsh
 * @time 2017/6/9 009 下午 02:01.
 * @doc 实现了不同视图的Activity
 */
public abstract class AppActivity extends TitleActivity {

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
        netStateView.showLoading();
    }

    public void showLoading(String msg) {
        loadingText.setText(msg);
        showLoading();
    }

    public void hideLoading() {
        netStateView.hideLoading();
    }

    public void showError() {
        netStateView.showError();
    }

    public void showEmpty() {
        netStateView.showEmpty();
    }
}
