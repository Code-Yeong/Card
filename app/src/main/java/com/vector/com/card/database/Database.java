package com.vector.com.card.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Administrator on 2017/8/3.
 */
public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "card";
    private static final int VERSION = 3;
    private static Database database;

    private final String CREATE_TABLE_USER = "create table user("
            + "id integer primary key autoincrement,"
            + "name varchar(10),"
            + "password varchar(10),"
            + "mark varchar(100),"
            + "score varchar(10),"
            + "keep_login varchar(2),"
            + "time varchar(20)"
            + ")";

    private final String CREATE_TABLE_TASKS = "create table tasks("
            + "id integer primary key autoincrement,"
            + "status varchar(10),"
            + "content varchar(100),"
            + "userid varchar(20),"
            + "starttime varchar(20),"
            + "stoptime varchar(20),"
            + "time varchar(20)"
            + ")";

    private final String CREATE_TABLE_DETAIL = "create table detail("
            + "id integer primary key autoincrement,"
            + "task varchar(100),"
            + "time varchar(20)"
            + ")";

    private final String CREATE_TABLE_SIGN = "create table signs("
            + "id integer primary key autoincrement,"
            + "user varchar(10),"
            + "time varchar(20)"
            + ")";

    private final String CREATE_TABLE_MEMO = "create table memo("
            + "id integer primary key autoincrement,"
            + "user varchar(10),"
            + "content varchar(200),"
            + "flag varchar(10),"
            + "time varchar(20)"
            + ")";

    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public Database(Context context) {
        this(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_DETAIL);
        sqLiteDatabase.execSQL(CREATE_TABLE_TASKS);
        sqLiteDatabase.execSQL(CREATE_TABLE_USER);
        sqLiteDatabase.execSQL(CREATE_TABLE_SIGN);
        sqLiteDatabase.execSQL(CREATE_TABLE_MEMO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    public static Database getDatabase(Context cxt) {
        if (database == null) {
            database = new Database(cxt);
        }
        return database;
    }

    public static void closeDatabase() {
        database.close();
    }
}
