package com.example.newspaper.DataBaseHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDataBaseHelper extends SQLiteOpenHelper {

    public MyDataBaseHelper(@Nullable Context context) {
        super(context, "database1.db",null,1);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        //建用户表
        String TABLE_USER = "create table user ("
                + "id integer primary key autoincrement, " //id 自增
                + "username text, "
                + "password text, "
                + "icon blob )"; //icon 的意思是头像，写在这备忘

        //建收藏表
        String TABLE_STORE = "create table store ("
                + "id integer primary key autoincrement, " //store 的 id 自增
                + "user_id integer, " //与当前用户相关联（当前用户id）
                + "newsname text, " //新闻名称
                + "news_id integer, "
                + "photo text, "
                + "url text)"; //》》》

        sqLiteDatabase.execSQL(TABLE_USER);
        sqLiteDatabase.execSQL(TABLE_STORE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //更新数据库版本
        sqLiteDatabase.execSQL("drop table if exists user");
        onCreate(sqLiteDatabase);
    }
}

