package com.dede.basemodule.base.fragment;

import android.content.Context;

import com.dede.basemodule.view.NetStateView;

/**
 * 2017/6/12 012 上午 11:26.
 */
public abstract class AppFragment extends BaseFragment {

    @Override
    protected NetStateView onCreateNetStateView(Context context) {
        NetStateView netStateView = new NetStateView(context);
        return netStateView;
    }
}
