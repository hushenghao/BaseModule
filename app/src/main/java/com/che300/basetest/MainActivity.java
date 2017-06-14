package com.che300.basetest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.che300.basemodule.base.activity.TitleActivity;
import com.che300.basemodule.view.NetStateView;

import butterknife.BindView;

public class MainActivity extends TitleActivity {

    @BindView(R.id.net_state_view)
    NetStateView netStateView;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            netStateView.showSuccess();
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        tabTitleBar.setTitle(getClass().getSimpleName());

        TextView errorView = new TextView(this);
        errorView.setText("点击空白处重试");
        errorView.setTextColor(ContextCompat.getColor(context, android.R.color.black));
        ProgressBar progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleLarge);
        netStateView.setErrorView(errorView)
                .setOnRetryClickListener(new NetStateView.OnRetryClickListener() {
                    @Override
                    public void onBlankClick(View errorView) {
                        showLoading(netStateView.getLoadingView());
                    }
                })
                .setLoadingCancelable(true)
                .setLoadingView(progressBar)
                .showLoading();
    }

    public void showLoading(View v) {
        netStateView.showLoading();
//        handler.sendEmptyMessageDelayed(0, 2000);
    }


    //onClickEvent
    public void showSuccess(View v) {
        netStateView.showSuccess();
    }

    public void showError(View v) {
        netStateView.showError();
//        handler.sendEmptyMessageDelayed(0, 3000);
    }

    public void showEmpty(View v) {
        netStateView.showEmpty();
        handler.sendEmptyMessageDelayed(0, 3000);
    }

    public void hideTitle(View v) {
        hideTitle();
    }

    public void showTitle(View v) {
        showTitle();
    }

    public void goToFragmentActivity(View v) {
        startActivity(new Intent(this, FragmentActivity.class));
    }

    public void goToTest1Activity(View v) {
        startActivity(new Intent(this, Test1Activity.class));
    }

    public void goToTest2Activity(View v) {
        startActivity(new Intent(this, Test2Activity.class));
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.e(TAG, "onKeyDown: Activity的按键监听回调");
        return super.onKeyDown(keyCode, event);
    }
}
