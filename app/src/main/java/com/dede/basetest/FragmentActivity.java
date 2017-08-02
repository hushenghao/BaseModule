package com.dede.basetest;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.dede.basemodule.base.activity.BaseActivity;

public class FragmentActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_fragment;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTabTitleBar.setTitle(getClass().getSimpleName());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.root_view, new TestFragment());
        transaction.commit();
    }

}
