package com.example.memo;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * 创建单例
 * Created by ruolan on 2015/10/11.
 */
public class MemoLab {

    private static final String TAG = "MemoLab";
    private static final String FILENAME = "memos.json";

    private ArrayList<Memo> mMemos;
    private MemoJSONSerializer mSerializer;


    private static MemoLab sMemoLab;
    private Context mAppContext;

    private MemoLab(Context appContext) {
        mAppContext = appContext;
//        mMemos = new ArrayList<Memo>();
        mSerializer = new MemoJSONSerializer(mAppContext, FILENAME);
        try {
            mMemos = mSerializer.loadMemos();
        } catch (Exception e) {
            Log.d(TAG, "错误的加载：" + e);
        }
/*
        //在这里为了显示的直观，我们先生成100个对象
        for (int i = 0; i < 100; i++) {
            Memo m = new Memo();
            m.setTitle("Memo #" + i);
            m.setSolved(i % 3 == 0);
            mMemos.add(m);
        }*/
    }

    public ArrayList<Memo> getMemos() {
        return mMemos;
    }

    public void addMemo(Memo m) {
        mMemos.add(m);
    }

    public Memo getMemo(UUID id) {
        for (Memo m : mMemos) {
            if (m.getId().equals(id))
                return m;
        }
        return null;
    }

    public static MemoLab get(Context c) {
        if (sMemoLab == null) {
            sMemoLab = new MemoLab(c.getApplicationContext());
        }
        return sMemoLab;
    }

    //对数据进行持久保存
    public boolean saveMemos() {
        try {
            mSerializer.saveMemos(mMemos);
            Log.d(TAG, "Memos 已经保留了");
            return true;
        } catch (Exception e) {
            Log.d(TAG, "保存失败" + e);
            return false;
        }
    }

}
