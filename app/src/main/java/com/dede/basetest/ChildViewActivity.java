package com.dede.basetest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.dede.basemodule.base.activity.TitleActivity;
import com.dede.basemodule.view.NetStateView;

import butterknife.BindView;

/**
 * 2017/6/16 016 下午 02:41.
 */
public class ChildViewActivity extends TitleActivity {

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
        return R.layout.acitivty_child_view;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        netStateView.setOnRetryClickListener(new NetStateView.OnRetryClickListener() {
            @Override
            public void onErrorViewClick(View errorView) {
                showLoading(netStateView.getLoadingView());
            }
        })
                .setLoadingCancelable(true);
    }

    //onClickEvent
    public void showLoading(View v) {
        netStateView.showLoading();
    }

    public void showSuccess(View v) {
        netStateView.showSuccess();
    }

    public void showError(View v) {
        netStateView.showError();
    }

    public void showEmpty(View v) {
        netStateView.showEmpty();
        handler.sendEmptyMessageDelayed(0, 3000);
    }

}
