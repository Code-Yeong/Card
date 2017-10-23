package com.vector.com.card.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vector.com.card.daoimpl.BaseDaoImpl;
import com.vector.com.card.database.Database;
import com.vector.com.card.domian.Notice;
import com.vector.com.card.domian.TempData;
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
        if (isNoticeOn(notice.getNtype())) {
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
        return 0;
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

    public int deleteAllByType(long user, String type) {
        sqLiteDatabase = database.getWritableDatabase();
        return sqLiteDatabase.delete("notice", "user=? and ntype=?", new String[]{String.valueOf(user), type});
    }


    private boolean isNoticeOn(String type) {
        boolean isOn = false;
        TempData tempData = Utils.readFromFile(cxt);
        if (tempData.getaNotice().equals("on") && type.equals("A")) {
            isOn = true;
        } else if (tempData.getbNotice().equals("on") && type.equals("B")) {
            isOn = true;
        } else if (tempData.getcNotice().equals("on") && type.equals("C")) {
            isOn = true;
        } else if (tempData.getdNotice().equals("on") && type.equals("D")) {
            isOn = true;
        } else if (tempData.geteNotice().equals("on") && type.equals("E")) {
            isOn = true;
        } else if (tempData.getsNotice().equals("on") && type.equals("S")) {
            isOn = true;
        }

        Log.i("info", "notice " + type + " is " + (isOn ? "on" : "off"));
        return isOn;
    }
}
