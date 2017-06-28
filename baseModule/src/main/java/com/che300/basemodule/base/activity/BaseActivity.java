package com.che300.basemodule.base.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.che300.basemodule.view.NetStateView;

import butterknife.ButterKnife;

/**
 * @author hsh
 * @time 2017/6/7 007 下午 02:52.
 * @doc 实现了不同视图的切换逻辑Activity
 */
public abstract class BaseActivity extends AppCompatActivity {

    public final String TAG = getClass().getSimpleName();

    public final static String BASE_TAG = "ActivityLife";

    private String className = getClass().getName();


    protected Context context;
    protected Context applicationContext;
    protected LayoutInflater layoutInflater;


    protected LinearLayout baseView;//根布局，包括title

    public View contentView;//content

    public NetStateView netStateView;//网络状态视图，如果未在布局文件中引入，或者未重写onCreateNetStateView()，则为null

    @NonNull
    public void toast(@NonNull String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @NonNull
    public void toast(@StringRes int res) {
        toast(getResources().getString(res));
    }

    /**
     * 获取布局资源ID
     *
     * @return 布局资源id
     * @author hsh
     * @time 2017/6/7 007 下午 02:04
     */
    @LayoutRes
    protected abstract int getLayoutId();

    /**
     * 初始化视图
     *
     * @param savedInstanceState
     * @author hsh
     * @time 2017/6/7 007 下午 02:04
     */
    protected abstract void initView(Bundle savedInstanceState);

    /**
     * 加载数据
     * 有些静态页面不需要加载数据，所以子类选择重写
     *
     * @author hsh
     * @time 2017/6/7 007 下午 02:03
     */
    public void initData() {
    }

    /**
     * 初始化状态栏，状态栏背景颜色，是否显示等操作
     *
     * @author hsh
     * @time 2017/6/7 007 下午 03:22
     */
    protected void initStatusBar(){
    }

    /**
     * 初始化标题栏，需要将标题添加到baseView内
     *
     * @param baseView 标题栏容器
     * @param context
     * @author hsh
     * @time 2017/6/7 007 下午 03:24
     */
    protected void initTitleBar(Context context, @Nullable LinearLayout baseView){
    }

    private void initBaseView() {
        //最终容器
        baseView = new LinearLayout(context);
        baseView.setOrientation(LinearLayout.VERTICAL);

        //标题栏
        initTitleBar(context, baseView);

        //主界面，成功视图
        int layoutId = getLayoutId();
        contentView = onCreateContentView(layoutId);

        //网络状态视图
        netStateView = onCreateNetStateView(context);
        if (netStateView != null) {
            //内容容器
            FrameLayout rootView = new FrameLayout(context);
            LinearLayout.LayoutParams rootParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

            FrameLayout.LayoutParams contentParams = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            rootView.addView(contentView, contentParams);

            //添加网络状态视图
            FrameLayout.LayoutParams loadingParams = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            loadingParams.gravity = Gravity.CENTER;
            rootView.addView(netStateView, loadingParams);

            baseView.addView(rootView, rootParams);

            onBaseViewCreate();

            setContentView(baseView);

            return;
        }

        //没有网络状态视图
        LinearLayout.LayoutParams contentParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        baseView.addView(contentView, contentParams);

        onBaseViewCreate();

        setContentView(baseView);
    }

    /**
     * BaseView创建完成时调用，在setContentView之前调用
     *
     * @author hsh
     * @time 2017/6/12 012 上午 11:16
     */
    protected void onBaseViewCreate() {
    }

    /**
     * 创建网络状态切换视图
     * 如果不需要或者已在布局文件中已经引入NetStateView，则不用重写该方法
     *
     * @param context
     * @return NetStateView
     * @author hsh
     * @time 2017/6/8 008 上午 11:03
     */
    protected NetStateView onCreateNetStateView(Context context) {
        return null;
    }

    /**
     * 根据资源id创建内容视图
     *
     * @param layoutId 资源id
     * @return View
     * @author hsh
     * @time 2017/6/7 007 下午 06:57
     */
    private View onCreateContentView(@LayoutRes int layoutId) {
        return layoutInflater.inflate(layoutId, null, true);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(BASE_TAG, "onCreate: " + className);
        context = this;
        applicationContext = getApplicationContext();
        layoutInflater = getLayoutInflater();

        initStatusBar();

        initBaseView();

        ButterKnife.bind(this);

        initView(savedInstanceState);

        initData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(BASE_TAG, "onStart: " + className);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(BASE_TAG, "onResume: " + className);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(BASE_TAG, "onPause: " + className);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(BASE_TAG, "onStop: " + className);
    }

    @Override
    public void finish() {
        super.finish();
        Log.d(BASE_TAG, "finish: " + className);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(BASE_TAG, "onDestroy: " + className);
    }

}
