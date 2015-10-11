package com.example.memo;

import android.support.v4.app.Fragment;

public class MemoActivity extends SingleFragmentActivity {


    @Override
    public Fragment createFragment() {
        return new MemoFragment();
    }
}
