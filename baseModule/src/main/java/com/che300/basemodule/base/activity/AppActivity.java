package com.che300.basemodule.base.activity;

import android.content.Context;

import com.che300.basemodule.view.NetStateView;

/**
 * @author hsh
 * @time 2017/6/9 009 下午 02:01.
 * @doc 实现了不同视图的Activity
 */
public abstract class AppActivity extends TitleActivity {

    @Override
    protected NetStateView onCreateNetStateView(Context context) {
        NetStateView netStateView = new NetStateView(context);
        return netStateView;
    }
}
