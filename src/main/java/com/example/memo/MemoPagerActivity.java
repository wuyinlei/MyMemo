package com.example.memo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.UUID;

public class MemoPagerActivity extends FragmentActivity {

    private ViewPager mViewPager;
    private ArrayList<Memo> mMemos;

    /**
     * 要记住使用onCreate()方法中带一个参数的方法
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);

        mMemos = MemoLab.get(this).getMemos();
        FragmentManager fm = getSupportFragmentManager();
        //接下来设置adapter为FragmentStatePagerAdapter的一个匿名实例，创建这个实例，还需要传递FragmentManager给他的
        //构造方法。
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                Memo m = mMemos.get(position);
                return MemoFragment.newInstance(m.getId());
            }

            @Override
            public int getCount() {
                return mMemos.size();
            }
        });

        /**
         * 调用mViewPager的setOnPageChangeListener（）方法，在onPageSelected（）这个方法中，判断所点击的
         * 他的标题是不是空的，如果不是空的，就设置当前要显示的fragment视图的标题为所点击的标题
         */
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Memo m = mMemos.get(position);
                if (m.getTitle() != null) {
                    setTitle(m.getTitle());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        /**
         * 下面这段代码是为了当点击了列表项后显示的是该点击列表的内容，而不是从刚开始显示的
         */
        UUID memoId = (UUID) getIntent().getSerializableExtra(MemoFragment.EXTRA_MEMO_ID);
        for (int i = 0; i < mMemos.size(); i++) {
            if (mMemos.get(i).getId().equals(memoId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
