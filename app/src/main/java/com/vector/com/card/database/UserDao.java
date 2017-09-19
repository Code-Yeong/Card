package com.vector.com.card.database;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.media.audiofx.LoudnessEnhancer;
import android.util.Log;

import com.vector.com.card.domian.User;
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
        contentValues.put("password", user.getPassword());
        contentValues.put("time", user.getTime());
        contentValues.put("keep_login", user.getKeepLogin());
        contentValues.put("score", user.getScore());
        return sqLiteDatabase.insert("user", null, contentValues);
    }

    @Override
    public int delete(long id) {
        sqLiteDatabase = database.getWritableDatabase();
        return sqLiteDatabase.delete("user", "id=?", new String[]{String.valueOf(id)});
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
        sqLiteDatabase = database.getWritableDatabase();
        contentValues.clear();
        contentValues.put("score", user.getScore());
        return sqLiteDatabase.update("user", contentValues, "id=?", new String[]{String.valueOf(user.getId())});
    }

    @Override
    public User queryById(long id) {
        sqLiteDatabase = database.getReadableDatabase();
        Cursor c = sqLiteDatabase.query("user", new String[]{"id", "name", "time", "password", "mark", "score"}, "id=?", new String[]{String.valueOf(id)}, null, null, null);
        User user = null;
        if (c.moveToNext()) {
            user = new User(c.getInt(c.getColumnIndex("id")), c.getString(c.getColumnIndex("name")), c.getString(c.getColumnIndex("time"))
                    , c.getString(c.getColumnIndex("password")), c.getString(c.getColumnIndex("mark")), c.getString(c.getColumnIndex("score")));
        }
        c.close();
        return user;
    }

    public boolean setLoginStatus(long id, int status) {
        sqLiteDatabase = database.getWritableDatabase();
        contentValues.clear();
        contentValues.put("keep_login", status);
        int affectedRow = sqLiteDatabase.update("user", contentValues, "id=?", new String[]{String.valueOf(id)});
        return affectedRow > 0 ? true : false;
    }

    public boolean validate(String loginname, String loginPassword, boolean keepLogin, String fileName) {
        sqLiteDatabase = database.getReadableDatabase();
        Cursor c = sqLiteDatabase.query("user", new String[]{"id", "name", "time", "password", "mark", "keep_login", "score"}, "name=? and password=?", new String[]{loginname, loginPassword}, null, null, null);
        User user = null;
        if (c.moveToNext()) {
            user = new User(c.getInt(c.getColumnIndex("id")), c.getString(c.getColumnIndex("name")), c.getString(c.getColumnIndex("time"))
                    , c.getString(c.getColumnIndex("password")), c.getString(c.getColumnIndex("mark")), c.getString(c.getColumnIndex("keep_login")),
                    c.getString(c.getColumnIndex("score")));
            if (keepLogin) {
                Utils.writeToFile(fileName, user);
            }
            SharedPreferences sharedPreferences = cxt.getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong("id", user.getId());
            editor.putString("name", user.getName());
            editor.putString("password", user.getPassword());
            editor.putString("mark", user.getMark());
            editor.putString("time", user.getTime());
            editor.putString("keepLogin", user.getKeepLogin());
            editor.putString("score", user.getScore());
            editor.commit();
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
