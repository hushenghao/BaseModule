package com.che300.basetest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.che300.basemodule.base.activity.AppActivity;

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


    //onClickEvent
    public void showLoading(View v) {
        showLoading("拼命加载中");
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
