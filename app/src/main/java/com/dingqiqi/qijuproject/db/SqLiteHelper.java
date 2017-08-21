package com.dingqiqi.qijuproject.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库
 * Created by 丁奇奇 on 2017/8/1.
 */
public class SqLiteHelper extends SQLiteOpenHelper {
    /**
     * 数据库版本号
     */
    private static final int mVersion = 1;
    /**
     * 数据库名称
     */
    private static final String mDBName = "QiJiDB";

    /**
     * 菜单表名称
     */
    public static final String mTableMenuName = "menu_table";

    public SqLiteHelper(Context context) {
        super(context, mDBName, null, mVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建菜谱表
        String sql = "create table if not exists " + mTableMenuName + " (id integer primary key autoincrement not null,name varchar,menuId varchar," +
                "ctgTitles varchar,img varchar,ingredients varchar,method varchar,sumary varchar,title varchar)";


        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
