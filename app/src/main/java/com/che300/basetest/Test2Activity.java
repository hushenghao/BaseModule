package com.che300.basetest;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.che300.basemodule.base.activity.AppActivity;
import com.che300.basemodule.view.NetStateView;

public class Test2Activity extends AppActivity {

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            netStateView.showSuccess();
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test2;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        tabTitleBar.setTitle(getClass().getSimpleName());
    }

    @Override
    protected NetStateView onCreateNetStateView(Context context) {
        LinearLayout loadingView = new LinearLayout(context);
        loadingView.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        ProgressBar progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleInverse);
        loadingView.addView(progressBar, params);
        TextView text = new TextView(context);
        text.setText("加载中...");
        text.setTextColor(ContextCompat.getColor(context, android.R.color.black));
        loadingView.addView(text, params);

        NetStateView netStateView = super.onCreateNetStateView(context)
                .setLoadingView(loadingView)
                .setLoadingCancelable(true);
        netStateView.setOnRetryClickListener(new NetStateView.OnRetryClickListener(netStateView) {
            @Override
            public void onErrorViewClick(View errorView, NetStateView netStateView) {
                showLoading(netStateView.getLoadingView());
            }
        });
        return netStateView;
    }

    public void showLoading(View v) {
        showLoading();
    }

    public void showSuccess(View v) {
        hideLoading();
    }

    public void showError(View v) {
        showError();
    }

    public void showEmpty(View v) {
        showEmpty();
        handler.sendEmptyMessageDelayed(0, 3000);
    }

}
