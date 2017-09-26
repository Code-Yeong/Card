package com.vector.com.card.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vector.com.card.domian.Memo;
import com.vector.com.card.domian.Notice;
import com.vector.com.card.domian.Score;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/19.
 */
public class ScoreDao implements BaseDao<Score> {

    private Context cxt;
    private SQLiteDatabase sqLiteDatabase;
    private Database database;
    private ContentValues contentValues;

    public ScoreDao(Context cxt) {
        this.cxt = cxt;
        database = Database.getDatabase(cxt);
        contentValues = new ContentValues();
    }

    @Override
    public long insert(Score score) {
        sqLiteDatabase = database.getWritableDatabase();
        contentValues.clear();
        contentValues.put("userid", score.getUserid());
        contentValues.put("content", score.getContent());
        contentValues.put("score", score.getScore());
        contentValues.put("time", score.getTime());
        contentValues.put("stype", score.getStype());
        long result = sqLiteDatabase.insert("score", null, contentValues);
        new NoticeDao(cxt).insert(new Notice(String.valueOf(score.getUserid()), score.getContent() + "(" + score.getScore() + "分)", "A"));
        return result;
    }

    @Override
    public int delete(long id) {
        return 0;
    }

    @Override
    public int update(Score score) {
        return 0;
    }

    @Override
    public Score queryById(long id) {
        return null;
    }

    public List<Score> queryBuUserId(long id) {
        sqLiteDatabase = database.getReadableDatabase();
        List<Score> list = new ArrayList<>();
        Cursor c = sqLiteDatabase.query("score", new String[]{"id", "content", "userid", "score", "time", "stype"},
                "userid=?", new String[]{String.valueOf(id)}, null, null, "time DESC");
        Score score;
        while (c.moveToNext()) {
            score = new Score(c.getLong(c.getColumnIndex("id")), c.getString(c.getColumnIndex("content")),
                    c.getString(c.getColumnIndex("score")), c.getString(c.getColumnIndex("time")), c.getString(c.getColumnIndex("stype")));
            list.add(score);
        }
        return list;
    }

    public int queryByUserId(long id, String date) {
        int score = 0;
        sqLiteDatabase = database.getReadableDatabase();
        List<Score> list = new ArrayList<>();
        Cursor c = sqLiteDatabase.query("score", new String[]{"id", "content", "userid", "score", "time", "stype"},
                "userid=? and time like '" + date + "%'", new String[]{String.valueOf(id)}, null, null, "time DESC");
        while (c.moveToNext()) {
            int i = Integer.parseInt(c.getString(c.getColumnIndex("score")));
            score += ((i < 0) ? 0 : i);
        }
        return score;
    }

    public boolean isGotBonus(long userid, String type, String time) {
        boolean isGot = false;
        sqLiteDatabase = database.getReadableDatabase();
        Cursor c = sqLiteDatabase.query("score", new String[]{"id", "content", "userid", "score", "time", "stype"},
                "userid=? and time like '" + time.substring(0, 10) + "%' and stype=?", new String[]{String.valueOf(userid), type}, null, null, "time DESC");
        while (c.moveToNext()) {
            isGot = true;
            break;
        }
        return isGot;
    }
}
