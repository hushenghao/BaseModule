package com.che300.basemodule.base.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
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
//    private View header;

    protected void initTitleBar(Context context, LinearLayout baseView) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//开启状态栏透明会引起输入框无法弹起
//            header = new View(context);
//            header.setBackgroundColor(ContextCompat.getColor(context, R.color.title_bg));
//            int statusHeight = ScreenUtils.getStatusHeight(context);
//            baseView.addView(header, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                    statusHeight));
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            baseView.setFitsSystemWindows(true);
            getWindow().setStatusBarColor(Color.TRANSPARENT);//透明状态栏
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            header.setVisibility(View.GONE);
        }
    }

    protected void showTitle() {
        tabTitleBar.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            baseView.setFitsSystemWindows(false);
            getWindow().setStatusBarColor(ContextCompat.getColor(context, R.color.title_bg));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            header.setVisibility(View.VISIBLE);
        }
    }

}
