package com.dede.basemodule.base.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.dede.basemodule.view.NetStateView;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 2017/6/7 007 下午 04:51.
 */
public abstract class BaseFragment extends Fragment {

    public final String TAG = getClass().getSimpleName();

    public final static String BASE_TAG = "FragmentLife";

    private String className = getClass().getName();


    protected Context mContext;
    protected FragmentActivity mActivity;
    protected LayoutInflater layoutInflater;

    private FrameLayout mRootView;//根布局，如果没有网络状态视图则为null

    public View mContentView;//content

    public NetStateView mNetStateView;

    private Unbinder unbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(BASE_TAG, "onAttach: " + className);
        this.mContext = context;
        mActivity = getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(BASE_TAG, "onCreate: " + className);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(BASE_TAG, "onCreateView: " + className);
        layoutInflater = inflater;

        //网络状态视图
        mNetStateView = onCreateNetStateView(mContext);
        if (mNetStateView != null) {
            //内容容器
            mRootView = new FrameLayout(mContext);

            //主界面
            int layoutId = getLayoutId();
            mContentView = onCreateContentView(layoutId);
            FrameLayout.LayoutParams contentParams = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            mRootView.addView(mContentView, contentParams);

            FrameLayout.LayoutParams loadingParams = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            loadingParams.gravity = Gravity.CENTER;
            mRootView.addView(mNetStateView, loadingParams);
        } else {//没有网络状态视图时直接返回，减少嵌套层级
            int layoutId = getLayoutId();
            mContentView = onCreateContentView(layoutId);
        }

        initView(mContentView);

        if (getUserVisibleHint())
            initData();

        if (mNetStateView != null)
            return mRootView;
        else
            return mContentView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isVisible()) {
            initData();
        }
    }

    /**
     * 获取布局资源ID
     *
     * @return 布局资源id
     * <p>
     * 2017/6/7 007 下午 02:04
     */
    @LayoutRes
    protected abstract int getLayoutId();

    /**
     * 初始化视图
     *
     * @param rootView 根据资源id填充完成后的视图
     *                 <p>
     *                 2017/6/7 007 下午 02:04
     */
    protected abstract void initView(View rootView);

    /**
     * 加载数据
     * 有些静态页面不需要加载数据，所以子类选择重写
     * <p>
     * <p>
     * 2017/6/7 007 下午 02:03
     */
    protected void initData() {
    }


    /**
     * 创建网络状态切换视图
     *
     * @param context
     * @return 2017/6/8 008 上午 11:03
     */
    protected NetStateView onCreateNetStateView(Context context) {
        return null;
    }

    /**
     * 根据资源id创建内容视图
     *
     * @param layoutId 资源id
     * @return View
     * <p>
     * 2017/6/7 007 下午 06:57
     */
    private View onCreateContentView(@LayoutRes int layoutId) {
        View contentView = layoutInflater.inflate(layoutId, null, true);
        unbinder = ButterKnife.bind(this, contentView);
        return contentView;
    }

    public void showLoading() {
        if (mNetStateView == null)
            return;
        mNetStateView.showLoading();
    }

    public void hideLoading() {
        if (mNetStateView == null)
            return;
        mNetStateView.hideLoading();
    }

    public void showError() {
        if (mNetStateView == null)
            return;
        mNetStateView.showError();
    }

    public void showEmpty() {
        if (mNetStateView == null)
            return;
        mNetStateView.showEmpty();
    }

    @NonNull
    public void toast(@NonNull String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    @NonNull
    public void toast(@StringRes int res) {
        toast(getResources().getString(res));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(BASE_TAG, "onActivityCreated: " + className);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(BASE_TAG, "onStart: " + className);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(BASE_TAG, "onResume: " + className);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(BASE_TAG, "onPause: " + className);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(BASE_TAG, "onStop: " + className);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(BASE_TAG, "onDestroyView: " + className);
        if (unbinder != null)
            unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(BASE_TAG, "onDestroy: " + className);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(BASE_TAG, "onDetach: " + className);
    }

}
