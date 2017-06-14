package com.che300.basemodule.base.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.che300.basemodule.R;
import com.che300.basemodule.util.DensityUtils;
import com.che300.basemodule.util.ScreenUtils;
import com.che300.basemodule.view.TabTitleBar;

import static android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
import static android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN;

/**
 * @author hsh
 * @time 2017/6/7 007 下午 02:52.
 * @doc 实现了Title和状态栏样式的Activity
 */
public abstract class TitleActivity extends BaseActivity {

    public TabTitleBar tabTitleBar;//title
    private View header;

    /**
     * 初始化状态栏
     */
    protected void initStatusBar() {
        //透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            getWindow().getDecorView().setSystemUiVisibility(option);//使布局延伸到状态栏底部
            getWindow().setStatusBarColor(Color.TRANSPARENT);//透明状态栏
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        getWindow().setSoftInputMode(SOFT_INPUT_ADJUST_RESIZE | SOFT_INPUT_STATE_HIDDEN);
    }

    protected void initTitleBar(Context context, LinearLayout baseView) {
        //由于布局延伸到状态栏底部，要在状态栏底部添加占位视图，所以将baseView作为参数传递过来
        header = new View(context);
        header.setBackgroundColor(ContextCompat.getColor(context, R.color.title_bg));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            baseView.addView(header, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    ScreenUtils.getStatusHeight(context)));
            header.setFitsSystemWindows(true);
        }

        tabTitleBar = new TabTitleBar(context);
        tabTitleBar.setBackgroundColor(ContextCompat.getColor(context, R.color.title_bg));
        tabTitleBar.showLeft();
        tabTitleBar.getLeftButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                DensityUtils.dp2px(this.context, 50));
        baseView.addView(tabTitleBar, titleParams);
    }

    protected void hideTitle() {
        tabTitleBar.setVisibility(View.GONE);
        header.setVisibility(View.GONE);
    }

    protected void showTitle() {
        tabTitleBar.setVisibility(View.VISIBLE);
        header.setVisibility(View.VISIBLE);
    }

}
