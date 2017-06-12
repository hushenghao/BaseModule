package com.che300.basetest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

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
        netStateView.setOnRetryClickListener(new NetStateView.OnRetryClickListener() {
            @Override
            public void onErrorViewClick(View v) {
                showLoading(v);
            }
        });
        netStateView.setLoadingCancelable(true);
    }

    public void showLoading(View v) {
        netStateView.showLoading();
//        handler.sendEmptyMessageDelayed(0, 2000);
    }

    public void showSuccess(View v) {
        netStateView.showSuccess();
    }

    public void showError(View v) {
        netStateView.showError();
        handler.sendEmptyMessageDelayed(0, 3000);
    }

    public void showEmpty(View v) {
        netStateView.showEmpty();
        handler.sendEmptyMessageDelayed(0, 3000);
    }

}
