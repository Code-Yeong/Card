package com.vector.com.card.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.vector.com.card.domian.Detail;
import com.vector.com.card.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2017/8/18.
 */
public class DetailDao implements BaseDao<Detail> {

    private Database database;
    private ContentValues contentValues;
    private SQLiteDatabase sqLiteDatabase;

    public DetailDao(Context cxt) {
        database = Database.getDatabase(cxt);
        contentValues = new ContentValues();
    }

    @Override
    public int update(Detail detail) {
        sqLiteDatabase = database.getWritableDatabase();
        contentValues.clear();
        contentValues.put("time", detail.getTime());
        return sqLiteDatabase.update("Detail", contentValues, "id=?", new String[]{String.valueOf(detail.getId())});
    }

    @Override
    public Detail queryById(long id) {
        sqLiteDatabase = database.getReadableDatabase();
        Cursor c = sqLiteDatabase.query("Detail", new String[]{"id", "task", "time"}, "id=?", null, null, null, null);
        Detail detail = null;
        if (c.moveToNext()) {
            detail = new Detail(c.getInt(c.getColumnIndex("id")), c.getString(c.getColumnIndex("taks")), c.getString(c.getColumnIndex("time")));
        }
        c.close();
        return detail;
    }

    public List<Detail> queryByTaskId(long id, String startTime, String stopTime) {
        List<Detail> list = new ArrayList<>();
        sqLiteDatabase = database.getReadableDatabase();
        Cursor c_detail = sqLiteDatabase.query("detail", new String[]{"id", "time", "task"}, "task=?", new String[]{String.valueOf(id)}, null, null, "time ASC");
        Detail detail = null;
        while (c_detail.moveToNext()) {
            detail = new Detail(c_detail.getLong(c_detail.getColumnIndex("id")), c_detail.getString(c_detail.getColumnIndex("task")),
                    c_detail.getString(c_detail.getColumnIndex("time")), true);
            list.add(detail);
        }
        return addTheNextDay(list, startTime, stopTime);
    }

    public List<Detail> queryAllByUserId(long user) {
        List<Detail> list = new ArrayList<>();
        sqLiteDatabase = database.getReadableDatabase();
        Cursor c_tasks = sqLiteDatabase.query("tasks", new String[]{"id", "content", "status"}, "userid=?", new String[]{String.valueOf(user)}, null, null, null);
        Detail detail = null;
        while (c_tasks.moveToNext()) {
            String id = String.valueOf(c_tasks.getLong(c_tasks.getColumnIndex("id")));
            String content = String.valueOf(c_tasks.getString(c_tasks.getColumnIndex("content")));
            //status:0-未完成，1-已完成
            boolean status = c_tasks.getString(c_tasks.getColumnIndex("status")).equals("0") ? false : true;
            if (!status) {
                Cursor c_detail = sqLiteDatabase.query("detail", new String[]{"id", "time"}, "task=? AND time=?",
                        new String[]{id, Utils.getCurrentDate()}, null, null, null);
                if (c_detail.getCount() == 0) {
                    detail = new Detail(0, id, content, "UNFINISHED");
                } else {
                    c_detail.moveToNext();
                    detail = new Detail(c_detail.getLong(c_detail.getColumnIndex("id")), id, content, "FINISHED");
                }
                list.add(detail);
                c_detail.close();
            }
        }
        c_tasks.close();
        return sort(list);
    }

    public int deleteAll(long userid) {
        sqLiteDatabase = database.getReadableDatabase();
        int affectRowCounts = 0;
        Cursor c_tasks = sqLiteDatabase.query("tasks", new String[]{"id", "content", "status"}, "userid=?", new String[]{String.valueOf(userid)}, null, null, null);
        while (c_tasks.moveToNext()) {
            String taskId = String.valueOf(c_tasks.getLong(c_tasks.getColumnIndex("id")));
            affectRowCounts += sqLiteDatabase.delete("detail", "task=? AND time=?", new String[]{taskId, Utils.getCurrentDate()});
        }
        c_tasks.close();
        return affectRowCounts;
    }

    @Override
    public int delete(long id) {
        sqLiteDatabase = database.getWritableDatabase();
        return sqLiteDatabase.delete("detail", "id=?", new String[]{String.valueOf(id)});
    }

    @Override
    public long insert(Detail detail) {
        sqLiteDatabase = database.getWritableDatabase();
        contentValues.clear();
        contentValues.put("time", detail.getTime());
        contentValues.put("task", detail.getTask());
        return sqLiteDatabase.insert("detail", null, contentValues);
    }

    private List<Detail> sort(List<Detail> list) {
        List<Detail> sortedList = new ArrayList<>();
        for (Detail d : list) {
            if (d.getTime().equals("UNFINISHED")) {
                sortedList.add(d);
            }
        }
        for (Detail d : list) {
            if (d.getTime().equals("FINISHED")) {
                sortedList.add(d);
            }
        }
        return sortedList;
    }

    public List<Detail> addTheNextDay(List<Detail> list, String startTime, String stopTime) {
        List<Detail> newExsitTimeList = new ArrayList<>();
        List<String> exsitTimeList = new ArrayList<>();
        for (Detail d : list) {
            exsitTimeList.add(d.getTime());
        }
        String firstDay = startTime;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        String today = dateFormat.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        String nextDay = dateFormat.format(calendar.getTime());
        try {
            calendar.setTime(dateFormat.parse(firstDay));
            String currentDay = dateFormat.format(calendar.getTime());
            Log.i("info", "Today:" + today + "----" + "nextDay:" + nextDay + "----" + "stopDay:" + stopTime);
            if (stopTime != null && stopTime.compareTo(today) <= 0) {
                Log.i("info", "yes...");
                nextDay = stopTime;
            }
            while (currentDay.compareTo(nextDay) <= 0) {
                if (exsitTimeList.contains(currentDay)) {
                    newExsitTimeList.add(new Detail(0, null, currentDay, true));
                } else {
                    newExsitTimeList.add(new Detail(0, null, currentDay, false));
                }
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                currentDay = dateFormat.format(calendar.getTime());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newExsitTimeList;
    }
}
