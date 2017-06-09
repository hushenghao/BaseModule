package com.che300.basemodule.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
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

    public static final int ERROR_STATE = -1;//错误视图
    public static final int EMPTY_STATE = 0;//空视图
    public static final int LOADING_STATE = 2;//加载中视图
    public static final int SUCCESS_STATE = 1;//成功视图，隐藏自身

    @IntDef({ERROR_STATE, EMPTY_STATE, LOADING_STATE, SUCCESS_STATE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ViewState {
    }


    private Context context;

    private FrameLayout.LayoutParams params;

    private int emptyLayoutId;
    private int errorLayoutId;
    private int loadingLayoutId;

    private View emptyView;
    private View errorView;
    private View loadingView;

    private boolean cancelable = false;//loading视图是否可以返回隐藏
    private boolean dispatchEvent = true;//触摸事件是否可以传递到下层成功视图

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

        params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        showViewByState(SUCCESS_STATE);//默认成功视图，隐藏状态
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP &&
                viewState == LOADING_STATE && cancelable) {//点击进度条外自动关闭
            hideLoading();
            return true;
        }
        if (viewState != SUCCESS_STATE && dispatchEvent) {//视图显示时拦截事件
            return true;
        }
        return super.onTouchEvent(event);
    }

    private boolean inflateTag = false;//是否是刚刚inflate过

    private View inflateView(@LayoutRes int layoutId) {
        inflateTag = true;
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
        if (viewState == state && !inflateTag) {
            return;
        }
        viewState = state;
        setVisibility(state == SUCCESS_STATE ? GONE : VISIBLE);//成功时隐藏自身

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
                onRetryClickListener.OnRefreshClick(v);
            }
        } else if (v == loadingView) {
            //不作处理，只是为了点击外界关闭加载中视图，见onTouchEvent(event)
        }
    }


    private OnRetryClickListener onRetryClickListener;//错误视图点击重试监听

    public interface OnRetryClickListener {
        void OnRefreshClick(View v);
    }

    ///////////////////////////////////////////////////////////////////////////
    //                                  API
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 设置错误视图点击重试监听
     *
     * @param onRetryClickListener
     * @author hsh
     * @time 2017/6/8 008 下午 05:27
     */
    public void setOnRetryClickListener(OnRetryClickListener onRetryClickListener) {
        this.onRetryClickListener = onRetryClickListener;
    }

    /**
     * 是否拦截触摸事件
     *
     * @param dispatchEvent true 拦截， false 不拦截。默认为true
     * @author hsh
     * @time 2017/6/9 009 下午 01:39
     */
    public void setDispatchEvent(boolean dispatchEvent) {
        this.dispatchEvent = dispatchEvent;
    }

    /**
     * 加载中视图是否可以被取消 true 可以被取消，false 不可取消 ，默认为false
     *
     * @param cancelable
     * @author hsh
     * @time 2017/6/9 009 下午 01:40
     */
    public void setLoadingCancelable(boolean cancelable) {
        this.cancelable = cancelable;
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

    public void setEmptyView(@LayoutRes int emptyId) {
        this.emptyLayoutId = emptyId;
        setEmptyView(inflateView(emptyId));
    }

    public void setErrorView(@LayoutRes int errorId) {
        this.errorLayoutId = errorId;
        setErrorView(inflateView(errorId));
    }

    public void setLoadingView(@LayoutRes int loadingId) {
        this.loadingLayoutId = loadingId;
        setLoadingView(inflateView(loadingId));
    }

    public void setEmptyView(@NonNull View emptyView) {
        removeView(this.emptyView);
        this.emptyView = emptyView;
        addView(this.emptyView, params);
    }

    public void setErrorView(@NonNull View errorView) {
        removeView(this.errorView);
        this.errorView = errorView;
        addView(this.errorView, params);
    }


    public void setLoadingView(@NonNull View loadingView) {
        removeView(this.loadingView);
        this.loadingView = loadingView;
        addView(this.loadingView, params);

        this.loadingView.setOnKeyListener(this);
        this.loadingView.setOnClickListener(this);
    }

    ///////////////////////////////////////////////////////////////////////////
    //      动态填充视图，减少布局层级，优化性能
    ///////////////////////////////////////////////////////////////////////////

    public View getEmptyView() {
        if (emptyView == null) {
            emptyView = inflateView(emptyLayoutId);
            addView(emptyView, params);

            showViewByState(viewState);
        }
        return emptyView;
    }

    public View getErrorView() {
        if (errorView == null) {
            errorView = inflateView(errorLayoutId);
            errorView.setClickable(true);
            errorView.setOnClickListener(this);
            addView(errorView, params);

            showViewByState(viewState);
        }
        return errorView;
    }

    public View getLoadingView() {
        if (loadingView == null) {
            loadingView = inflateView(loadingLayoutId);
            loadingView.setClickable(true);
            addView(loadingView, params);

            loadingView.setOnKeyListener(this);
            loadingView.setOnClickListener(this);

            showViewByState(viewState);
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
