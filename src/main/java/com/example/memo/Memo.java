package com.example.memo;

import java.util.Date;
import java.util.UUID;

/**
 * Created by ruolan on 2015/10/11.
 */
public class Memo {

    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;

    public Memo(){
        //生成唯一的标识符
        mId = UUID.randomUUID();
        mDate = new Date();//没有这一个，就会出现时间空指针异常
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }
}
