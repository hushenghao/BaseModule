package com.che300.basemodule.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.che300.basemodule.R;
import com.che300.basemodule.util.DensityUtils;

/**
 * @author gengqiqauan
 * @time 2015年4月24日
 */
public class TabTitleBar extends RelativeLayout {
    private ImageView imgleft;
    private TextView bttitle, textRight;

    public TextView getMidView() {
        return this.bttitle;
    }

    public void removeMidView() {
        this.removeView(bttitle);
    }

    public ImageView getLeftButton() {
        return this.imgleft;
    }

    public TextView getRightButton() {
        return this.textRight;
    }

    public void setTitle(String str) {
        bttitle.setText(str);
    }

    public void setRightButtonText(String str) {
        textRight.setText(str);
    }

    public void setTitle(int resID) {
        bttitle.setText(resID);
    }

    public void setLeftClickListener(OnClickListener listener) {
        imgleft.setOnClickListener(listener);
    }

    public void setRightClickListener(OnClickListener listener) {
        textRight.setOnClickListener(listener);
    }

    public void showLeft() {
        imgleft.setVisibility(View.VISIBLE);
    }

    public void showRight() {
        textRight.setVisibility(View.VISIBLE);
    }

    public void hideRight() {
        textRight.setVisibility(View.GONE);
    }

    @SuppressLint("ResourceAsColor")
    private void init() {

        // 左边图标
        imgleft = new ImageView(getContext());
        imgleft.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_arrow_2_left));
        LayoutParams params1 = new LayoutParams(DensityUtils.dp2px(getContext(), 48), DensityUtils.dp2px(
                getContext(), 48));
        params1.addRule(RelativeLayout.CENTER_VERTICAL);
        params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params1.setMargins(DensityUtils.dp2px(getContext(), 10), 0, 0, 0);
        addView(imgleft, params1);
        // 标题
        bttitle = new TextView(getContext());
        bttitle.setText("");
        bttitle.setTextSize(18);
        bttitle.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(DensityUtils.dp2px(getContext(), 68), 0, DensityUtils.dp2px(getContext(), 80), 0);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        bttitle.setSingleLine();
        addView(bttitle, params);
        // 右边图标
        textRight = new TextView(getContext());
        LayoutParams params2 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params2.addRule(RelativeLayout.CENTER_VERTICAL);
        params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params2.setMargins(0, 0, DensityUtils.dp2px(getContext(), 15), 0);
        textRight.setTextSize(16);
        textRight.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        addView(textRight, params2);
        textRight.setVisibility(View.GONE);

    }

    public TabTitleBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TabTitleBar(Context context) {
        super(context);
        init();
    }

    public TabTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @SuppressLint("NewApi")
    public TabTitleBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        init();
    }

}
