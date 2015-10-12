package com.example.memo;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class MemoFragment extends Fragment {

    private Memo mMemo;
    public static final String EXTRA_MEMO_ID = "com.example.memo.memo_id";
    private static final String DIALOG_DATE = "date";
    private static final int REQUEST_DATE = 0;


    //定义一个EditText控件
    private EditText mTitleField;
    //定义Button按钮控件
    private Button mDateButton;
    //定义CheckBox控件
    private CheckBox mSolvedCheckBox;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        //UUID memoId = (UUID) getActivity().getIntent().getSerializableExtra(EXTRA_MEMO_ID);
        UUID memoId = (UUID) getArguments().getSerializable(EXTRA_MEMO_ID);
        mMemo = MemoLab.get(getActivity()).getMemo(memoId);


    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.delete_menu,menu);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_memo, container, false);

        //启动向上导航按钮
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (NavUtils.getParentActivityName(getActivity()) != null) {
                getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }


        mTitleField = (EditText) view.findViewById(R.id.memo_title);
        mTitleField.setText(mMemo.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mMemo.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDateButton = (Button) view.findViewById(R.id.memo_date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss EEE");  //日期格式化
        mDateButton.setText(sdf.format(mMemo.getDate()));
//        mDateButton.setEnabled(false);  //按钮不能点击
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
//                DatePickerFragment dialog = new DatePickerFragment();
                //添加newInstance()方法
                DatePickerFragment dialog
                        = DatePickerFragment.newInstance(mMemo.getDate());
                dialog.setTargetFragment(MemoFragment.this, REQUEST_DATE);
                dialog.show(fm, DIALOG_DATE);
            }
        });
        mSolvedCheckBox = (CheckBox) view.findViewById(R.id.memo_solved);
        mSolvedCheckBox.setChecked(mMemo.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mMemo.setSolved(isChecked);
            }
        });
        return view;
    }

    //编写newInstance方法，这个方法就是实现fragment中的argument方式的，这样既可以保持MemoFragment独立性。
    public static MemoFragment newInstance(UUID memoId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_MEMO_ID, memoId);
        MemoFragment fragment = new MemoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //响应DatePicker对话框

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mMemo.setDate(date);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss EEE");
            mDateButton.setText(sdf.format(mMemo.getDate()));
        }
    }

    /**
     * 响应应用图标（Home图标)菜单项
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //实现在MemoFragment中删除数据
            case R.id.menu_delete_memo:
                MemoLab.get(getActivity()).deleteMemo(mMemo);
            case android.R.id.home:
                //响应home键，如果能够找到他的父类或者他的父类不为空的时候，就返回到他的父类
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * 在onPause（）方法中保存数据
     */
    @Override
    public void onPause() {
        super.onPause();
        MemoLab.get(getActivity()).saveMemos();
    }
}
