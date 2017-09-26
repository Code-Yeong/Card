package com.vector.com.card.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vector.com.card.domian.Notice;
import com.vector.com.card.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/24.
 */
public class NoticeDao extends BaseDaoImpl<Notice> implements BaseDao<Notice> {

    private Context cxt;
    Database database;
    ContentValues contentValues;
    SQLiteDatabase sqLiteDatabase;

    public NoticeDao(Context cxt) {
        this.cxt = cxt;
        contentValues = new ContentValues();
        database = Database.getDatabase(cxt);
    }

    @Override
    public long insert(Notice notice) {
        sqLiteDatabase = database.getWritableDatabase();
        contentValues.clear();
        contentValues.put("user", notice.getUser());
        contentValues.put("content", notice.getContent());
        contentValues.put("ntype", notice.getNtype());
        contentValues.put("time", notice.getTime());
        contentValues.put("status", notice.getStatus());
        Utils.alarm(cxt, Utils.NOTICE_ALARM);
        return sqLiteDatabase.insert("notice", null, contentValues);
    }

    public int updateStatus(Notice notice) {
        sqLiteDatabase = database.getWritableDatabase();
        contentValues.clear();
        contentValues.put("status", notice.getStatus());
        return sqLiteDatabase.update("notice", contentValues, "id=?", new String[]{String.valueOf(notice.getId())});
    }

    @Override
    public Notice queryById(long id) {
        sqLiteDatabase = database.getReadableDatabase();
        Notice notice = null;
        Cursor cursor = sqLiteDatabase.query("notice", new String[]{"content", "status", "ntype", "time", "user"},
                "id=?", new String[]{String.valueOf(id)}, null, null, null, null);
        while (cursor.moveToNext()) {
            notice = new Notice(id, cursor.getString(cursor.getColumnIndex("user")), cursor.getString(cursor.getColumnIndex("content")),
                    cursor.getString(cursor.getColumnIndex("ntype")), cursor.getString(cursor.getColumnIndex("status")), cursor.getString(cursor.getColumnIndex("time")));
        }
        return notice;
    }

    public List<Notice> queryAllByUserId(long user) {
        List<Notice> list = new ArrayList<>();
        sqLiteDatabase = database.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("notice", new String[]{"id", "content", "ntype", "status", "time"},
                "user=?", new String[]{String.valueOf(user)}, null, null, "time DESC,status DESC");
        while (cursor.moveToNext()) {
            list.add(new Notice(cursor.getLong(cursor.getColumnIndex("id")), String.valueOf(user),
                    cursor.getString(cursor.getColumnIndex("content")), cursor.getString(cursor.getColumnIndex("ntype")),
                    cursor.getString(cursor.getColumnIndex("status")), cursor.getString(cursor.getColumnIndex("time"))));
        }
        return list;
    }

    @Override
    public int delete(long id) {
        sqLiteDatabase = database.getWritableDatabase();
        return sqLiteDatabase.delete("notice", "id=?", new String[]{String.valueOf(id)});
    }
}
