package com.example.memo;

import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;

/**
 * 创建单例
 * Created by ruolan on 2015/10/11.
 */
public class MemoLab {

    private ArrayList<Memo> mMemos;

    private static MemoLab sMemoLab;
    private Context mAppContext;

    private MemoLab(Context appContext) {
        mAppContext = appContext;
        mMemos = new ArrayList<Memo>();

        //在这里为了显示的直观，我们先生成100个对象
        for (int i = 0; i < 100; i++) {
            Memo m = new Memo();
            m.setTitle("Memo #" + i);
            m.setSolved(i % 3 == 0);
            mMemos.add(m);
        }
    }

    public ArrayList<Memo> getMemos() {
        return mMemos;
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

}
