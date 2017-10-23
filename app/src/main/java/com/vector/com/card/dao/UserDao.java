package com.vector.com.card.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vector.com.card.database.Database;
import com.vector.com.card.domian.User;
import com.vector.com.card.utils.UserApplication;
import com.vector.com.card.utils.Utils;

/**
 * Created by Administrator on 2017/8/18.
 */
public class UserDao implements BaseDao<User> {
    private Database database;
    private SQLiteDatabase sqLiteDatabase;
    private ContentValues contentValues;
    private Context cxt;

    public UserDao(Context cxt) {
        this.cxt = cxt;
        database = Database.getDatabase(cxt);
        contentValues = new ContentValues();
        contentValues.clear();
    }

    @Override
    public long insert(User user) {
        sqLiteDatabase = database.getWritableDatabase();
        contentValues.clear();
        contentValues.put("name", user.getName());
        contentValues.put("realname", user.getRealName());
        contentValues.put("password", user.getPassword());
        contentValues.put("time", user.getTime());
        contentValues.put("keep_login", user.getKeepLogin());
        contentValues.put("score", user.getScore());
        long id = sqLiteDatabase.insert("user", null, contentValues);
        ((UserApplication) UserApplication.getInstance()).setUserId(String.valueOf(id));
        return id;
    }

    @Override
    public int delete(long id) {
        sqLiteDatabase = database.getWritableDatabase();
        return sqLiteDatabase.delete("user", "id=?", new String[]{String.valueOf(id)});
    }

    public int update(long userid, String name, String mark) {
        sqLiteDatabase = database.getWritableDatabase();
        contentValues.clear();
        contentValues.put("realname", name);
        contentValues.put("mark", mark);
        return sqLiteDatabase.update("user", contentValues, "id=?", new String[]{String.valueOf(userid)});
    }

    @Override
    public int update(User user) {
        sqLiteDatabase = database.getWritableDatabase();
        contentValues.clear();
        contentValues.put("name", user.getName());
        contentValues.put("password", user.getPassword());
        contentValues.put("mark", user.getMark());
        return sqLiteDatabase.update("user", contentValues, "id=?", new String[]{String.valueOf(user.getId())});
    }

    public int updateScore(User user) {
        Utils.alarm(cxt, Utils.COIN_ALARM);
        sqLiteDatabase = database.getWritableDatabase();
        contentValues.clear();
        contentValues.put("score", user.getScore());
        return sqLiteDatabase.update("user", contentValues, "id=?", new String[]{String.valueOf(user.getId())});
    }

    @Override
    public User queryById(long id) {
        sqLiteDatabase = database.getReadableDatabase();
        Cursor c = sqLiteDatabase.query("user", new String[]{"id", "name", "realname", "time", "password", "mark", "score"}, "id=?", new String[]{String.valueOf(id)}, null, null, null);
        User user = null;
        if (c.moveToNext()) {
            user = new User(c.getInt(c.getColumnIndex("id")), c.getString(c.getColumnIndex("name")), c.getString(c.getColumnIndex("time"))
                    , c.getString(c.getColumnIndex("password")), c.getString(c.getColumnIndex("mark")), c.getString(c.getColumnIndex("score")),
                    c.getString(c.getColumnIndex("realname")), 0);
        }
        c.close();
        return user;
    }

    public boolean validate(String loginname, String loginPassword) {
        sqLiteDatabase = database.getReadableDatabase();
        Cursor c = sqLiteDatabase.query("user", new String[]{"id", "name", "time", "password", "mark", "keep_login", "score"}, "name=? and password=?", new String[]{loginname, loginPassword}, null, null, null);
        User user = null;
        if (c.moveToNext()) {
            user = new User(c.getInt(c.getColumnIndex("id")), c.getString(c.getColumnIndex("name")), c.getString(c.getColumnIndex("time"))
                    , c.getString(c.getColumnIndex("password")), c.getString(c.getColumnIndex("mark")), c.getString(c.getColumnIndex("keep_login")),
                    c.getString(c.getColumnIndex("score")));
            ((UserApplication) (UserApplication.getInstance())).setUserId(String.valueOf(user.getId()));
        }
        c.close();
        return user != null ? true : false;
    }

    public boolean isUserNameExist(String name) {
        sqLiteDatabase = database.getReadableDatabase();
        Cursor c = sqLiteDatabase.query("user", new String[]{"id", "name", "time", "password", "mark"}, "name=?", new String[]{name}, null, null, null);
        int count = c.getCount();
        c.close();
        return count > 0 ? true : false;
    }

    public String getSignedScore(long id) {
        sqLiteDatabase = database.getReadableDatabase();
        Cursor c = sqLiteDatabase.query("user", new String[]{"score"}, "id=?", new String[]{String.valueOf(id)}, null, null, null);
        String score = null;
        if (c.moveToNext()) {
            score = c.getString(c.getColumnIndex("score"));
        }
        c.close();
        return score;
    }
}
