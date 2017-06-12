package com.che300.basemodule.base.fragment;

import android.content.Context;

import com.che300.basemodule.view.NetStateView;

/**
 * @author hsh
 * @time 2017/6/12 012 上午 11:26.
 * @doc
 */
public abstract class AppFragment extends BaseFragment{

    @Override
    protected NetStateView onCreateNetStateView(Context context) {
        NetStateView netStateView = new NetStateView(context);
        return netStateView;
    }
}
