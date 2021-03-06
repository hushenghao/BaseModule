package com.dede.basetest;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.dede.basemodule.view.NetStateView;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 2017/6/7 007 下午 05:05.
 */
public class TestFragment extends AppFragment {

    @BindView(R.id.list_view)
    ListView listView;

    boolean isFirst = true;
    Handler handler = new Handler();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test;
    }

    @Override
    protected void initView(View rootView) {
    }

    @Override
    protected NetStateView onCreateNetStateView(Context context) {
        NetStateView netStateView = new NetStateView(context);
        netStateView.getErrorView().findViewById(R.id.tv_retry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
        return netStateView;
    }

    @Override
    protected void initData() {
        mNetStateView.showLoading();//加载中视图
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isFirst) {
                    isFirst = false;
                    mNetStateView.showError();//错误视图
                } else {
                    mNetStateView.showEmpty();//空视图
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mNetStateView.hideLoading();
                            bindData();//成功视图
                        }
                    }, 1000);
                }
            }
        }, 2000);
    }

    private void bindData() {
        ArrayList<String> data = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            data.add(getClass().getName() + "  " + i);
        }
        listView.setAdapter(new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, data));
    }
}
