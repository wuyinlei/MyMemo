package com.example.memo;


import android.support.v4.app.Fragment;

public class MemoListActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        return new MemoListFragment();
    }

}
