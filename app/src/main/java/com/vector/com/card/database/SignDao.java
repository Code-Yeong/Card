package com.vector.com.card.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vector.com.card.domian.Sign;
import com.vector.com.card.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2017/8/22.
 */
public class SignDao implements BaseDao<Sign> {

    private Database database;
    private ContentValues contentValues;
    private SQLiteDatabase sqLiteDatabase;

    public SignDao(Context context) {
        database = Database.getDatabase(context);
        contentValues = new ContentValues();
    }

    @Override
    public long insert(Sign sign) {
        sqLiteDatabase = database.getWritableDatabase();
        contentValues.clear();
        contentValues.put("user", sign.getUser());
        contentValues.put("time", Utils.getCurrentDate());
        return sqLiteDatabase.insert("signs", null, contentValues);
    }

    @Override
    public int delete(long id) {
        return 0;
    }

    @Override
    public int update(Sign sign) {
        return 0;
    }

    @Override
    public Sign queryById(long id) {
        return null;
    }

    public List<Sign> queryByUserId(long user) {
        List<Sign> list = new ArrayList<>();
        sqLiteDatabase = database.getReadableDatabase();
        Cursor c = sqLiteDatabase.query("signs", new String[]{"id", "user", "time"}, "user=?", new String[]{String.valueOf(user)}, null, null, "time ASC");
        Sign sign;
        while (c.moveToNext()) {
            sign = new Sign(c.getLong(c.getColumnIndex("id")), c.getString(c.getColumnIndex("user")), c.getString(c.getColumnIndex("time")));
            list.add(sign);
        }
        return list;
    }

    public boolean getSignedStatus(long user) {
        sqLiteDatabase = database.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("signs", new String[]{"time"}, "user=?", new String[]{String.valueOf(user)}, null, null, "time ASC");

        boolean isEmpty = cursor.moveToLast();

        if (isEmpty && cursor.getString(cursor.getColumnIndex("time")).equals(Utils.getCurrentDate())) {
            return true;
        }
        return false;
    }

    public int getContinuesSignedCounts(long user) {
        int count = 0;
        sqLiteDatabase = database.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("signs", new String[]{"id", "user", "time"}, "user=?", new String[]{String.valueOf(user)}, null, null, "time DESC");
        if (cursor.getCount() > 0) {
            String currentDay = Utils.getCurrentDate();
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            String yesterday = sdf.format(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            List<String> list = new ArrayList<>();
            while (cursor.moveToNext()) {
                list.add(cursor.getString(cursor.getColumnIndex("time")));
            }

            if (list.contains(currentDay)) {
                //包含今天
                for (int i = 0; i < list.size(); i++) {
                    calendar.add(Calendar.DAY_OF_MONTH, -i);
                    String day = sdf.format(calendar.getTime());
                    calendar.add(Calendar.DAY_OF_MONTH, i);
                    if (list.contains(day)) {
                        count++;
                        continue;
                    } else {
                        break;
                    }
                }
            } else if (list.contains(yesterday)) {
                //不包含今天
                for (int i = 0; i < list.size(); i++) {
                    calendar.set(Calendar.DAY_OF_MONTH, -(i + 1));
                    String day = sdf.format(calendar.getTime());
                    calendar.set(Calendar.DAY_OF_MONTH, (i + 1));
                    if (list.contains(day)) {
                        count++;
                        continue;
                    } else {
                        break;
                    }
                }
            }
        } else {
            count = 0;
        }
        return count;
    }

    public List<Integer> getDates(String user, int year, int month) {
        List<Integer> dates = new ArrayList<>();
        sqLiteDatabase = database.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("signs", new String[]{"time"}, "user=?", new String[]{String.valueOf(user)}, null, null, "time ASC");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        String date = simpleDateFormat.format(calendar.getTime());
        while (cursor.moveToNext()) {
            String d = cursor.getString(cursor.getColumnIndex("time"));
            if (d.startsWith(date)) {
                dates.add(Integer.parseInt(d.substring(8, 10)));
            }
        }
        return dates;
    }
}
