package com.example.memo;

import android.support.v4.app.Fragment;

import java.util.UUID;

public class MemoActivity extends SingleFragmentActivity {


    @Override
    public Fragment createFragment() {
        UUID memoId = (UUID) getIntent().getSerializableExtra(MemoFragment.EXTRA_MEMO_ID);
        return MemoFragment.newInstance(memoId);
    }
}
