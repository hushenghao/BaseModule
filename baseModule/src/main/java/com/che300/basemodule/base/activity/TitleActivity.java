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
import com.che300.basemodule.view.TabTitleBar;

/**
 * @author hsh
 * @time 2017/6/7 007 下午 02:52.
 * @doc 实现了Title和状态栏样式的Activity
 */
public abstract class TitleActivity extends BaseActivity {

    public TabTitleBar tabTitleBar;//title

    protected void initTitleBar(Context context, LinearLayout baseView) {
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            baseView.setFitsSystemWindows(true);
            getWindow().setStatusBarColor(Color.TRANSPARENT);//透明状态栏
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    protected void showTitle() {
        tabTitleBar.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            baseView.setFitsSystemWindows(false);
            getWindow().setStatusBarColor(ContextCompat.getColor(context, R.color.title_bg));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

}
