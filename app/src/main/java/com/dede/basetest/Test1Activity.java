package com.dede.basetest;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import butterknife.BindView;

public class Test1Activity extends AppActivity {

    @BindView(R.id.list_view)
    ListView listView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test1;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTabTitleBar.setTitle(getClass().getSimpleName());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toast("OnItemClick");
            }
        });
        bindData();
        mNetStateView.setLoadingCancelable(true);
        mNetStateView.showLoading();
    }

    private void bindData() {
        ArrayList<String> data = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            data.add(getClass().getName() + "  " + i);
        }
        listView.setAdapter(new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, data));
    }

}
