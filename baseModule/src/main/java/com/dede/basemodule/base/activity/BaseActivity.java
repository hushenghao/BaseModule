package com.dede.basemodule.base.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dede.basemodule.R;
import com.dede.basemodule.util.DensityUtils;
import com.dede.basemodule.util.ExitUtil;
import com.dede.basemodule.view.NetStateView;
import com.dede.basemodule.view.TabTitleBar;

import butterknife.ButterKnife;

/**
 * 2017/6/7 007 下午 02:52.
 * 实现了不同视图的切换逻辑Activity
 */
public abstract class BaseActivity extends AppCompatActivity {

    public final String TAG = getClass().getSimpleName();

    public final static String BASE_TAG = "ActivityLife";

    private String className = getClass().getName();


    protected Context mContext;
    protected LayoutInflater layoutInflater;


    protected LinearLayout mBaseView;//根布局，包括title

    public View mContentView;//content

    public NetStateView mNetStateView;//网络状态视图，如果未在布局文件中引入，或者未重写onCreateNetStateView()，则为null

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
     * @param savedInstanceState 2017/6/7 007 下午 02:04
     */
    protected abstract void initView(Bundle savedInstanceState);

    /**
     * 加载数据
     * 有些静态页面不需要加载数据，所以子类选择重写
     */
    public void initData() {
    }


    public TabTitleBar mTabTitleBar;//title
//    private View header;

    protected void hideTitle() {
        mTabTitleBar.setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBaseView.setFitsSystemWindows(true);
            getWindow().setStatusBarColor(Color.TRANSPARENT);//透明状态栏
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            header.setVisibility(View.GONE);
        }
    }

    protected void showTitle() {
        mTabTitleBar.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBaseView.setFitsSystemWindows(false);
            getWindow().setStatusBarColor(ContextCompat.getColor(mContext, R.color.title_bg));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            header.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 初始化标题栏
     */
    protected void initTitleBar() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//开启状态栏透明会引起输入框无法弹起
//            header = new View(mContext);
//            header.setBackgroundColor(ContextCompat.getColor(mContext, R.color.title_bg));
//            int statusHeight = ScreenUtils.getStatusHeight(mContext);
//            mBaseView.addView(header, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                    statusHeight));
        }
        mTabTitleBar = new TabTitleBar(mContext);
        mTabTitleBar.setBackgroundColor(ContextCompat.getColor(mContext, R.color.title_bg));
        mTabTitleBar.showLeft();
        mTabTitleBar.getLeftButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                DensityUtils.dp2px(this.mContext, 50));
        mBaseView.addView(mTabTitleBar, titleParams);
    }

    private void initBaseView() {
        //最终容器
        mBaseView = new LinearLayout(mContext);
        mBaseView.setOrientation(LinearLayout.VERTICAL);

        //标题栏
        initTitleBar();

        //主界面，成功视图
        int layoutId = getLayoutId();
        mContentView = onCreateContentView(layoutId);

        //网络状态视图
        mNetStateView = onCreateNetStateView(mContext);
        if (mNetStateView != null) {
            //内容容器
            FrameLayout rootView = new FrameLayout(mContext);
            LinearLayout.LayoutParams rootParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

            FrameLayout.LayoutParams contentParams = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            rootView.addView(mContentView, contentParams);

            //添加网络状态视图
            FrameLayout.LayoutParams loadingParams = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            loadingParams.gravity = Gravity.CENTER;
            rootView.addView(mNetStateView, loadingParams);

            mBaseView.addView(rootView, rootParams);

            onBaseViewCreate();

            setContentView(mBaseView);

            return;
        }

        //没有网络状态视图
        LinearLayout.LayoutParams contentParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mBaseView.addView(mContentView, contentParams);

        onBaseViewCreate();

        setContentView(mBaseView);
    }

    /**
     * BaseView创建完成时调用，在setContentView之前调用
     */
    protected void onBaseViewCreate() {
    }

    /**
     * 创建网络状态切换视图
     * 如果不需要或者已在布局文件中已经引入NetStateView，则不用重写该方法
     *
     * @param context
     * @return NetStateView
     */
    protected NetStateView onCreateNetStateView(Context context) {
        return null;
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

    /**
     * 根据资源id创建内容视图
     *
     * @param layoutId 资源id
     * @return View
     */
    private View onCreateContentView(@LayoutRes int layoutId) {
        return layoutInflater.inflate(layoutId, null, true);
    }

    public void toast(@NonNull String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void toast(@StringRes int res) {
        toast(getResources().getString(res));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(BASE_TAG, "onCreate: " + className);
        ExitUtil.addActivity(this);
        mContext = this;
        layoutInflater = getLayoutInflater();

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
        ExitUtil.removeActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(BASE_TAG, "onDestroy: " + className);
        ExitUtil.removeActivity(this);
    }

}
