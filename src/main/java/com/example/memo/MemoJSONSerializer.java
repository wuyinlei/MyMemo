package com.example.memo;

import android.content.Context;

import com.example.memo.Memo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by ruolan on 2015/10/12.
 */
public class MemoJSONSerializer {

    private Context mContext;
    private String mFilename;

    //有参数的构造函数
    public MemoJSONSerializer(Context c, String f) {
        mContext = c;
        mFilename = f;
    }

    //实现存储的方法
    public void saveMemos(ArrayList<Memo> memos)throws JSONException,IOException{
        JSONArray array = new JSONArray();
        for (Memo m:memos
             ) {
            array.put(m.toJSON());
            Writer writer = null;
            try {
                OutputStream out = mContext.openFileOutput(mFilename,Context.MODE_PRIVATE);
                writer = new OutputStreamWriter(out);
                writer.write(array.toString());
            } finally {
                if (writer != null){
                    writer.close();
                }
            }
        }
    }

    //实现loadMemos()方法
    public ArrayList<Memo> loadMemos()throws IOException,JSONException{

        ArrayList<Memo> memos = new ArrayList<Memo>();
        BufferedReader reader = null;
        try {
            InputStream in = mContext.openFileInput(mFilename);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null){
                jsonString.append(line);
            }
            JSONArray array = (JSONArray)new JSONTokener(jsonString.toString()).nextValue();
            for (int i = 0; i < array.length(); i++) {
                memos.add(new Memo(array.getJSONObject(i)));
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } finally {
            if (reader != null){
                reader.close();
            }
        }
        return memos;
    }

}
