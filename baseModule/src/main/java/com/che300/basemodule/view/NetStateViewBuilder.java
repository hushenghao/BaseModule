package com.che300.basemodule.view;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;

/**
 * @author hsh
 * @time 2017/6/16 016 下午 05:27.
 * @doc 网络状态视图构造器
 */
public class NetStateViewBuilder {

    private View emptyView;
    private View errorView;
    private View loadingView;

    private boolean cancelable = false;
    private boolean dispatchEvent = true;

    private NetStateView.OnRetryClickListener onRetryClickListener;

    private Context context;

    public NetStateViewBuilder(Context context) {
        this.context = context;
    }

    public NetStateViewBuilder setOnRetryClickListener(NetStateView.OnRetryClickListener onRetryClickListener) {
        this.onRetryClickListener = onRetryClickListener;
        return this;
    }

    public NetStateViewBuilder setEmptyView(@LayoutRes int emptyLayoutId) {
        emptyView = LayoutInflater.from(context).inflate(emptyLayoutId, null, true);
        return this;
    }

    public NetStateViewBuilder setErrorView(@LayoutRes int errorLayoutId) {
        errorView = LayoutInflater.from(context).inflate(errorLayoutId, null, true);
        return this;
    }

    public NetStateViewBuilder setLoadingView(@LayoutRes int loadingLayoutId) {
        loadingView = LayoutInflater.from(context).inflate(loadingLayoutId, null, true);
        return this;
    }

    public NetStateViewBuilder setEmptyView(View emptyView) {
        this.emptyView = emptyView;
        return this;
    }

    public NetStateViewBuilder setErrorView(View errorView) {
        this.errorView = errorView;
        return this;
    }

    public NetStateViewBuilder setLoadingView(View loadingView) {
        this.loadingView = loadingView;
        return this;
    }

    public NetStateViewBuilder setLoadingCancelable(boolean cancelable) {
        this.cancelable = cancelable;
        return this;
    }

    public NetStateViewBuilder setDispatchEvent(boolean dispatchEvent) {
        this.dispatchEvent = dispatchEvent;
        return this;
    }

    public NetStateView build() {
        NetStateView netStateView = new NetStateView(context);
        if (emptyView != null)
            netStateView.setEmptyView(emptyView);
        if (loadingView != null)
            netStateView.setLoadingView(loadingView);
        if (errorView != null)
            netStateView.setErrorView(errorView);
        return netStateView
                .setOnRetryClickListener(onRetryClickListener)
                .setLoadingCancelable(cancelable)
                .setDispatchEvent(dispatchEvent);
    }
}
