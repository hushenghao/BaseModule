package com.dede.basemodule.base.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
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


    protected Context context;
    protected Context applicationContext;
    protected LayoutInflater layoutInflater;

    private FrameLayout rootView;//根布局，如果没有网络状态视图则为null

    public View contentView;//content

    public NetStateView netStateView;

    private Unbinder unbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(BASE_TAG, "onAttach: " + className);
        this.context = context;
        applicationContext = context.getApplicationContext();
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
        netStateView = onCreateNetStateView(context);
        if (netStateView != null) {
            //内容容器
            rootView = new FrameLayout(context);

            //主界面
            int layoutId = getLayoutId();
            contentView = onCreateContentView(layoutId);
            FrameLayout.LayoutParams contentParams = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            rootView.addView(contentView, contentParams);

            FrameLayout.LayoutParams loadingParams = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            loadingParams.gravity = Gravity.CENTER;
            rootView.addView(netStateView, loadingParams);
        } else {//没有网络状态视图时直接返回，减少嵌套层级
            int layoutId = getLayoutId();
            contentView = onCreateContentView(layoutId);
        }

        initView(contentView);

        initData();

        if (netStateView != null)
            return rootView;
        else
            return contentView;
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


    @NonNull
    public void toast(@NonNull String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
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
