package com.example.memo;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MemoListFragment extends ListFragment {

    private ArrayList<Memo> mMemos;
    private ListView mListView;
    private Button newMemoButton;

    private static final String TAG = "MemoListFragment";
    private boolean mSubtitleVisible;  //用来判断当屏幕旋转的时候没有保存实例的。

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


        //保留MemoLIstFragment并初始化变量
        setRetainInstance(true);
        mSubtitleVisible = false;

        getActivity().setTitle(R.string.memos_title);
        mMemos = MemoLab.get(getActivity()).getMemos();
        // ArrayAdapter<Memo> adapter = new ArrayAdapter<Memo>(getActivity(), android.R.layout.simple_list_item_1, mMemos);
        MemoAdapter adapter = new MemoAdapter(mMemos);
        setListAdapter(adapter);
    }

    //根据变量 mSubtitleVisible的值设置子标题


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // View v = super.onCreateView(inflater,container,savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_list_memo_empty, container, false);
        mListView = (ListView) v.findViewById(android.R.id.list);
        mListView.setEmptyView(v.findViewById(android.R.id.empty));

        newMemoButton = (Button) v.findViewById(R.id.newMemoButton);
        newMemoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Memo memo = new Memo();
                MemoLab.get(getActivity()).addMemo(memo);
                Intent i = new Intent(getActivity(),MemoPagerActivity.class);
                i.putExtra(MemoFragment.EXTRA_MEMO_ID,memo.getId());
                startActivityForResult(i,0);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (mSubtitleVisible) {
                getActivity().getActionBar().setSubtitle(R.string.subtitle);
            }
        }
        return v;
        //return view;
    }

    //添加定制的adapter内部类
    private class MemoAdapter extends ArrayAdapter<Memo> {

        public MemoAdapter(ArrayList<Memo> memos) {
            super(getActivity(), 0, memos);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.fragment_memo_list, null);
            }
            Memo m = getItem(position);
            TextView titleTextView = (TextView) convertView.findViewById(R.id.memo_list_item_titleTextView);
            titleTextView.setText(m.getTitle());
            TextView dateTextView = (TextView) convertView.findViewById(R.id.memo_list_item_dateTextView);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss EEE");
            dateTextView.setText(sdf.format(m.getDate()));
            CheckBox solvedCheckBox = (CheckBox) convertView.findViewById(R.id.memo_list_item_solvedCheckBox);
            solvedCheckBox.setChecked(m.isSolved());
            return convertView;
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
//        Memo m = (Memo) getListAdapter().getItem(position);
        Memo m = ((MemoAdapter) getListAdapter()).getItem(position);
//        Log.d(TAG, m.getTitle() + "被点击了");
        //启动MemoActivity活动，并且传递信息来确定打开的是哪一个
        //Intent i = new Intent(getActivity(), MemoActivity.class);
        Intent i = new Intent(getActivity(), MemoPagerActivity.class);
        i.putExtra(MemoFragment.EXTRA_MEMO_ID, m.getId());
        startActivity(i);
    }

    /**
     * 在onResume()中刷新列表项，可以实现当改变了里面的值得时候，退出的时候，值和原来的是一样的
     */
    @Override
    public void onResume() {
        super.onResume();
        ((MemoAdapter) getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ((MemoAdapter) getListAdapter()).notifyDataSetChanged();
    }

    //实例化生成选项菜单
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_memo_list, menu);
        //基于 mSubtitleVisible的值，正确显示菜单项标题
        MenuItem showSubtitle = menu.findItem(R.id.menu_item_show_subtitle);
        if (mSubtitleVisible && showSubtitle != null) {
            showSubtitle.setTitle(R.string.hide_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_memo:
                Memo memo = new Memo();
                MemoLab.get(getActivity()).addMemo(memo);
                Intent i = new Intent(getActivity(), MemoPagerActivity.class);
                i.putExtra(MemoFragment.EXTRA_MEMO_ID, memo.getId());
                startActivityForResult(i, 0);
                return true;
            case R.id.menu_item_show_subtitle:
                //如果操作栏上没有显示子标题，则应设置显示子标题，同时切换菜单选项标题，使其显示为Hide Subtitle
                //如果子标题已经显示了，则应设置其值为null，同时将菜单项标题切换回Show Subtitle
                if (getActivity().getActionBar().getSubtitle() == null) {
                    getActivity().getActionBar().setSubtitle(R.string.subtitle);
                    mSubtitleVisible = true;
                    item.setTitle(R.string.hide_subtitle);
                } else {
                    getActivity().getActionBar().setSubtitle(null);
                    mSubtitleVisible = false;
                    item.setTitle(R.string.show_subtitle);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
