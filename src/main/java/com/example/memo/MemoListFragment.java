package com.example.memo;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
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
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            registerForContextMenu(mListView);
        } else {
            mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            //为列表视图中的操作模式回调方法
            mListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater inflater = mode.getMenuInflater();
                    inflater.inflate(R.menu.memo_list_item_context, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return true;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu_item_delete_memo:
                            MemoAdapter adapter = (MemoAdapter) getListAdapter();
                            MemoLab memoLab = MemoLab.get(getActivity());
                            for (int i = adapter.getCount() - 1; i >= 0; i--) {
                                if (getListView().isItemChecked(i)) {
                                    memoLab.deleteMemo(adapter.getItem(i));
                                }
                            }
                            mode.finish();
                            //刷新列表项
                            adapter.notifyDataSetChanged();
                            return true;
                        default:
                            return false;
                    }
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }
            });
        }

        newMemoButton = (Button) v.findViewById(R.id.newMemoButton);
        newMemoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Memo memo = new Memo();
                MemoLab.get(getActivity()).addMemo(memo);
                Intent i = new Intent(getActivity(), MemoPagerActivity.class);
                i.putExtra(MemoFragment.EXTRA_MEMO_ID, memo.getId());
                startActivityForResult(i, 0);
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


    /**
     * 实例化生成选项菜单,这个菜单就是你在点击手机menu键时会看到的菜单
     */
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

    /**
     * 这个方法只在onCreateOptionsMenu 创建的菜单被选中时才会被触发
     */
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

    /**
     * 创建上下文菜单你还需要对此菜单进行注册Activity.registerForContextMenu(View view),
     * 这个菜单是在你长按前面注册的view时看到的菜单。
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.memo_list_item_context, menu);
    }

    /**
     * 监听上下文菜单选项选择事件
     * 这个方法只在onCreateContextMenu 创建的菜单被选中时才会被触发
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int positon = info.position;
        MemoAdapter adapter = (MemoAdapter) getListAdapter();
        Memo memo = adapter.getItem(positon);
        switch (item.getItemId()) {
            case R.id.menu_item_delete_memo:
                MemoLab.get(getActivity()).deleteMemo(memo);
                adapter.notifyDataSetChanged();
                ;
                return true;
        }
        return super.onContextItemSelected(item);
    }
}
