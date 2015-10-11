package com.example.memo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.UUID;

public class MemoFragment extends Fragment {

    private Memo mMemo;
    public static final String EXTRA_MEMO_ID = "com.example.memo.memo_id";


    //定义一个EditText控件
    private EditText mTitleField;
    //定义Button按钮控件
    private Button mDateButton;
    //定义CheckBox控件
    private CheckBox mSolvedCheckBox;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //UUID memoId = (UUID) getActivity().getIntent().getSerializableExtra(EXTRA_MEMO_ID);
        UUID memoId = (UUID) getArguments().getSerializable(EXTRA_MEMO_ID);
        mMemo= MemoLab.get(getActivity()).getMemo(memoId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_memo,container,false);
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
        mDateButton.setEnabled(false);  //按钮不能点击
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
    public static MemoFragment newInstance(UUID memoId){
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_MEMO_ID, memoId);
        MemoFragment fragment = new MemoFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
