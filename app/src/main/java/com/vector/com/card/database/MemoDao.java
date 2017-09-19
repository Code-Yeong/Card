package com.vector.com.card.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vector.com.card.domian.Memo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/26.
 */
public class MemoDao implements BaseDao<Memo> {

    private SQLiteDatabase sqLiteDatabase;
    private ContentValues contentValues;
    private Database database;

    public MemoDao(Context context) {
        database = Database.getDatabase(context);
        contentValues = new ContentValues();
    }

    @Override
    public long insert(Memo memo) {
        sqLiteDatabase = database.getWritableDatabase();
        contentValues.clear();
        contentValues.put("user", memo.getUser());
        contentValues.put("content", memo.getContent());
        contentValues.put("flag", memo.getFlag());
        contentValues.put("time", memo.getTime());
        return sqLiteDatabase.insert("memo", null, contentValues);
    }

    @Override
    public int delete(long id) {
        sqLiteDatabase = database.getWritableDatabase();
        return sqLiteDatabase.delete("memo", "id=?", new String[]{String.valueOf(id)});
    }

    @Override
    public int update(Memo memo) {
        return 0;
    }

    @Override
    public Memo queryById(long id) {
        sqLiteDatabase = database.getReadableDatabase();
        Memo memo = null;
        Cursor c = sqLiteDatabase.query("memo", new String[]{"id", "content", "user", "flag", "time"},
                "id=?", new String[]{String.valueOf(id)}, null, null, null);
        while (c.moveToNext()) {
            memo = new Memo(c.getLong(c.getColumnIndex("id")), c.getString(c.getColumnIndex("content")),
                    c.getString(c.getColumnIndex("user")), c.getString(c.getColumnIndex("falg")),
                    c.getString(c.getColumnIndex("time")));
        }
        return memo;
    }

    public List<Memo> queryBuUserId(long id) {
        sqLiteDatabase = database.getReadableDatabase();
        List<Memo> list = new ArrayList<>();
        Cursor c = sqLiteDatabase.query("memo", new String[]{"id", "content", "user", "flag", "time"},
                "user=?", new String[]{String.valueOf(id)}, null, null, "flag DESC,time DESC");
        Memo memo;
        while (c.moveToNext()) {
            memo = new Memo(c.getLong(c.getColumnIndex("id")), c.getString(c.getColumnIndex("user")),
                    c.getString(c.getColumnIndex("content")), c.getString(c.getColumnIndex("flag")),
                    c.getString(c.getColumnIndex("time")));
            list.add(memo);
        }
        return list;
    }

    public List<Memo> queryTopFiveBuUserId(long id) {
        sqLiteDatabase = database.getReadableDatabase();
        List<Memo> list = new ArrayList<>();
        Cursor c = sqLiteDatabase.query("memo", new String[]{"id", "content", "user", "flag", "time"},
                "user=? and flag=1", new String[]{String.valueOf(id)}, null, null, "time DESC");
        Memo memo;
        while (c.moveToNext()) {
            memo = new Memo(c.getLong(c.getColumnIndex("id")), c.getString(c.getColumnIndex("user")),
                    c.getString(c.getColumnIndex("content")), c.getString(c.getColumnIndex("flag")),
                    c.getString(c.getColumnIndex("time")));
            list.add(memo);
        }
        return list;
    }

    public int setFlag(Memo memo) {
        sqLiteDatabase = database.getReadableDatabase();
        contentValues.clear();
        contentValues.put("flag", memo.getFlag());
        return sqLiteDatabase.update("memo", contentValues, "id=?", new String[]{String.valueOf(memo.getId())});
    }
}
