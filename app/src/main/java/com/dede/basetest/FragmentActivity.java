package com.dede.basetest;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.dede.basemodule.base.activity.TitleActivity;

public class FragmentActivity extends TitleActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_fragment;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        tabTitleBar.setTitle(getClass().getSimpleName());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.root_view, new TestFragment());
        transaction.commit();
    }

}
