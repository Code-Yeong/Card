package com.vector.com.card.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vector.com.card.domian.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/18.
 */
public class TaskDao implements BaseDao<Task> {

    private Database database;
    private ContentValues contentValues;
    private SQLiteDatabase sqLiteDatabase;

    public TaskDao(Context cxt) {
        database = Database.getDatabase(cxt);
        contentValues = new ContentValues();
    }

    @Override
    public long insert(Task task) {
        sqLiteDatabase = database.getWritableDatabase();
        contentValues.clear();
        contentValues.put("content", task.getContent());
        contentValues.put("status", task.getStatus());
        contentValues.put("time", task.getTime());
        contentValues.put("userid", task.getUser());
        return sqLiteDatabase.insert("tasks", null, contentValues);
    }

    @Override
    public int delete(long id) {
        sqLiteDatabase = database.getWritableDatabase();
        return sqLiteDatabase.delete("tasks", "id=?", new String[]{String.valueOf(id)});
    }

    @Override
    public int update(Task task) {
        sqLiteDatabase = database.getWritableDatabase();
        contentValues.clear();
        contentValues.put("content", task.getContent());
        contentValues.put("status", task.getStatus());
        return sqLiteDatabase.update("tasks", contentValues, "id=?", new String[]{String.valueOf(task.getId())});
    }

    public int updateStatus(Task task) {
        sqLiteDatabase = database.getWritableDatabase();
        contentValues.clear();
        contentValues.put("status", task.getStatus());
        return sqLiteDatabase.update("tasks", contentValues, "id=?", new String[]{String.valueOf(task.getId())});
    }

    @Override
    public Task queryById(long id) {
        sqLiteDatabase = database.getReadableDatabase();
        Cursor c = sqLiteDatabase.query("tasks", new String[]{"id", "content", "status", "time"}, "id=?", new String[]{String.valueOf(id)}, null, null, null, null);
        Task task = null;
        if (c.moveToNext()) {
            task = new Task(c.getInt(c.getColumnIndex("id")), c.getString(c.getColumnIndex("userid")), c.getString(c.getColumnIndex("content")), c.getString(c.getColumnIndex("status")), c.getString(c.getColumnIndex("time")));
        }
        c.close();
        return task;
    }

    public List<Task> queryByUserId(long id) {
        sqLiteDatabase = database.getReadableDatabase();
        Cursor c = sqLiteDatabase.query("tasks", new String[]{"id", "content", "status", "time", "userid"}, "userid=?", new String[]{String.valueOf(id)}, null, null, "status ASC");
        List<Task> list = new ArrayList<>();
        Task task = null;
        while (c.moveToNext()) {
            task = new Task(c.getInt(c.getColumnIndex("id")), c.getString(c.getColumnIndex("userid")), c.getString(c.getColumnIndex("content")), c.getString(c.getColumnIndex("status")), c.getString(c.getColumnIndex("time")));
            list.add(task);
        }
        c.close();
        return list;
    }
}
