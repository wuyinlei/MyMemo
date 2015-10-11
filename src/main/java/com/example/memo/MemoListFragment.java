package com.example.memo;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MemoListFragment extends ListFragment {

    private ArrayList<Memo> mMemos;

    private static final String TAG = "MemoListFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.memos_title);
        mMemos = MemoLab.get(getActivity()).getMemos();
        // ArrayAdapter<Memo> adapter = new ArrayAdapter<Memo>(getActivity(), android.R.layout.simple_list_item_1, mMemos);
        MemoAdapter adapter = new MemoAdapter(mMemos);
        setListAdapter(adapter);
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
        Intent i = new Intent(getActivity(),MemoPagerActivity.class);
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
}
