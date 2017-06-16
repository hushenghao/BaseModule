package com.che300.basemodule.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.che300.basemodule.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author hsh
 * @time 2017/6/8 008 上午 10:42.
 * @doc 封装了不同视图的View
 */
public class NetStateView extends FrameLayout implements View.OnClickListener, View.OnKeyListener {

    private static final String TAG = "NetStateView";

    public static final int ERROR_STATE = -1;//错误视图
    public static final int EMPTY_STATE = 0;//空视图
    public static final int LOADING_STATE = 2;//加载中视图
    public static final int SUCCESS_STATE = 1;//成功视图，隐藏自身

    @IntDef({ERROR_STATE, EMPTY_STATE, LOADING_STATE, SUCCESS_STATE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ViewState {
    }


    private Context context;

    private FrameLayout.LayoutParams childViewparams;

    private int emptyLayoutId;
    private int errorLayoutId;
    private int loadingLayoutId;

    private View emptyView;
    private View errorView;
    private View loadingView;//未调用getView或者视图未显示时为null

    private boolean cancelable = false;//loading视图是否可以返回隐藏
    private boolean dispatchEvent = true;//触摸事件是否可以传递到下层成功视图
    //setFilterTouchesWhenObscured(true);//也可以使用此api实现

    @ViewState
    private int viewState = SUCCESS_STATE;


    public NetStateView(Context context) {
        this(context, null);
    }

    public NetStateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NetStateView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NetStateView);

        emptyLayoutId = typedArray.getResourceId(R.styleable.NetStateView_layout_empty, R.layout.layout_empty);
        errorLayoutId = typedArray.getResourceId(R.styleable.NetStateView_layout_error, R.layout.layout_error);
        loadingLayoutId = typedArray.getResourceId(R.styleable.NetStateView_layout_loading, R.layout.layout_loading);

        typedArray.recycle();

        childViewparams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        childViewparams.gravity = Gravity.CENTER;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        if (childCount != 3 && childCount != 0) {
            throw new IllegalStateException("NetStateView的直接子View必须等于3或0");
        }
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            childView.setLayoutParams(childViewparams);
            Object tag = childView.getTag();
            if (tag == null || !(tag instanceof String)) {
                throw new IllegalArgumentException("NetStateView的子View必须设置tag android:tag=@string/loading_view");
            }
            String tagStr = (String) tag;
            if (tagStr.equals(getString(R.string.empty_view))) {
                this.emptyView = childView;
            } else if (tagStr.equals(getString(R.string.loading_view))) {
                this.loadingView = childView;
                this.loadingView.setClickable(true);
                this.loadingView.setOnKeyListener(this);
                this.loadingView.setOnClickListener(this);
            } else if (tagStr.equals(getString(R.string.error_view))) {
                this.errorView = childView;
                this.errorView.setClickable(true);
                this.errorView.setOnClickListener(this);
            } else {
                throw new IllegalArgumentException("NetStateView子View的tag必须为 " +
                        "android:tag=@string/loading_view " +
                        "android:tag=@string/empty_view " +
                        "android:tag=@string/error_view");
            }
        }
        showViewByState(viewState);//默认成功视图，隐藏状态
    }

    private String getString(@StringRes int stringId) {
        return context.getResources().getString(stringId);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (viewState == LOADING_STATE && cancelable) {//点击进度条外自动关闭
                    hideLoading();
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (viewState == ERROR_STATE && onRetryClickListener != null) {//错误视图点击回调
                    onRetryClickListener.onBlankClick(errorView);
                    return true;
                }
                break;
        }
        if (viewState != SUCCESS_STATE && dispatchEvent) {//视图显示时消费事件
            return true;
        }
        return super.onTouchEvent(event);
    }

    private boolean addViewTag = false;//是否是刚刚inflate过

    private View inflateView(@LayoutRes int layoutId) {
        return LayoutInflater.from(context).inflate(layoutId, null, true);
    }

    /**
     * 设置视图显示状态
     *
     * @param state ViewState
     * @author hsh
     * @time 2017/6/9 009 下午 01:38
     */
    private void showViewByState(@ViewState int state) {
        Log.d("showViewByState", "StateChange  " + state);
        if (viewState == state && !addViewTag) {
            return;
        }
        addViewTag = false;
        viewState = state;
        setVisibility(viewState == SUCCESS_STATE ? GONE : VISIBLE);//成功时隐藏自身
        if (viewState == SUCCESS_STATE)
            return;

        if (emptyView != null) {
            emptyView.setVisibility(viewState == EMPTY_STATE ? VISIBLE : GONE);
        }

        if (errorView != null) {
            errorView.setVisibility(viewState == ERROR_STATE ? VISIBLE : GONE);
        }

        if (loadingView != null) {
            loadingView.setVisibility(viewState == LOADING_STATE ? VISIBLE : GONE);

            if (viewState == LOADING_STATE) {//获取到焦点才会响应按键事件
                loadingView.setFocusable(true);
                loadingView.setFocusableInTouchMode(true);
                loadingView.requestFocus();
            }
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            //监听返回键按下事件来处理是否需要隐藏loading视图
            if (viewState == LOADING_STATE && cancelable) {
                hideLoading();
                return true;
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v == errorView) {
            if (onRetryClickListener != null) {
                onRetryClickListener.onErrorViewClick(errorView);
            }
        } else if (v == loadingView) {
            //不作处理，只是为了点击外界关闭加载中视图，见onTouchEvent(event)
        }
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        addViewTag = true;//添加标记，调整视图显示状态
        showViewByState(viewState);
    }

    private OnRetryClickListener onRetryClickListener;//错误视图点击重试监听

    public abstract static class OnRetryClickListener {

        /**
         * 点击错误视图的回调方法
         *
         * @param errorView 错误视图
         * @author hsh
         * @time 2017/6/12 012 上午 09:43
         */
        public void onErrorViewClick(View errorView) {
        }

        /**
         * 点击错误视图空白处的回调
         *
         * @param errorView 错误视图
         * @author hsh
         * @time 2017/6/12 012 上午 09:44
         */
        public void onBlankClick(View errorView) {
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    //                                  API
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 设置错误视图点击重试监听
     * 如果需要自定义错误视图，此方法必须在setErrorView()后调用
     *
     * @param onRetryClickListener
     * @author hsh
     * @time 2017/6/8 008 下午 05:27
     */
    public NetStateView setOnRetryClickListener(OnRetryClickListener onRetryClickListener) {
        this.onRetryClickListener = onRetryClickListener;
        return this;
    }

    /**
     * 是否拦截触摸事件
     *
     * @param dispatchEvent true 拦截， false 不拦截。默认为true
     * @author hsh
     * @time 2017/6/9 009 下午 01:39
     */
    public NetStateView setDispatchEvent(boolean dispatchEvent) {
        this.dispatchEvent = dispatchEvent;
        return this;
    }

    /**
     * 加载中视图是否可以被取消 true 可以被取消，false 不可取消 ，默认为false
     *
     * @param cancelable
     * @author hsh
     * @time 2017/6/9 009 下午 01:40
     */
    public NetStateView setLoadingCancelable(boolean cancelable) {
        this.cancelable = cancelable;
        return this;
    }

    /**
     * 获取视图显示状态
     *
     * @return viewState
     * @author hsh
     * @time 2017/6/9 009 下午 01:41
     */
    @ViewState
    public int getViewState() {
        return viewState;
    }

    public NetStateView setEmptyView(@LayoutRes int emptyId) {
        this.emptyLayoutId = emptyId;
        setEmptyView(inflateView(emptyId));
        return this;
    }

    public NetStateView setErrorView(@LayoutRes int errorId) {
        this.errorLayoutId = errorId;
        setErrorView(inflateView(errorId));
        return this;
    }

    public NetStateView setLoadingView(@LayoutRes int loadingId) {
        this.loadingLayoutId = loadingId;
        setLoadingView(inflateView(loadingId));
        return this;
    }

    public NetStateView setEmptyView(@NonNull View emptyView) {
        if (this.emptyView != null)
            removeView(this.emptyView);
        this.emptyView = emptyView;

        addView(this.emptyView, childViewparams);
        return this;
    }

    public NetStateView setErrorView(@NonNull View errorView) {
        if (this.errorView != null)
            removeView(this.errorView);
        this.errorView = errorView;

        addView(this.errorView, childViewparams);

        this.errorView.setClickable(true);
        this.errorView.setOnClickListener(this);
        return this;
    }


    public NetStateView setLoadingView(@NonNull View loadingView) {
        if (this.loadingView != null)
            removeView(this.loadingView);
        this.loadingView = loadingView;

        addView(this.loadingView, childViewparams);

        this.loadingView.setClickable(true);
        this.loadingView.setOnKeyListener(this);
        this.loadingView.setOnClickListener(this);
        return this;
    }

    ///////////////////////////////////////////////////////////////////////////
    //      动态填充视图，减少布局层级，优化性能
    ///////////////////////////////////////////////////////////////////////////

    public View getEmptyView() {
        if (emptyView == null) {
            emptyView = inflateView(emptyLayoutId);

            addView(emptyView, childViewparams);
        }
        return emptyView;
    }

    public View getErrorView() {
        if (errorView == null) {
            errorView = inflateView(errorLayoutId);

            errorView.setClickable(true);
            errorView.setOnClickListener(this);

            addView(errorView, childViewparams);
        }
        return errorView;
    }

    public View getLoadingView() {
        if (loadingView == null) {
            loadingView = inflateView(loadingLayoutId);

            loadingView.setClickable(true);
            loadingView.setOnKeyListener(this);
            loadingView.setOnClickListener(this);

            addView(loadingView, childViewparams);
        }
        return loadingView;
    }


    public void showLoading() {
        if (loadingView == null) {
            viewState = LOADING_STATE;
            getLoadingView();
        } else {
            showViewByState(LOADING_STATE);
        }
    }

    public void hideLoading() {
        showSuccess();
    }

    public void showSuccess() {
        showViewByState(SUCCESS_STATE);
    }

    public void showError() {
        if (errorView == null) {
            viewState = ERROR_STATE;
            getErrorView();
        } else {
            showViewByState(ERROR_STATE);
        }
    }

    public void showEmpty() {
        if (emptyView == null) {
            viewState = EMPTY_STATE;
            getEmptyView();
        } else {
            showViewByState(EMPTY_STATE);
        }
    }

}
