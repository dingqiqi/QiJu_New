package com.dingqiqi.qijuproject.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.dingqiqi.qijuproject.mode.MenuDetailMode;
import com.dingqiqi.qijuproject.mode.MenuMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库操作相关
 * Created by 丁奇奇 on 2017/8/1.
 */
public class SqLiteUtil {
    /**
     * 数据库操作实例
     */
    private static SqLiteUtil mInstance;

    private SQLiteDatabase mDatabase;

    private SqLiteUtil(Context context) {
        SqLiteHelper mHelper = new SqLiteHelper(context);

        try {
            mDatabase = mHelper.getWritableDatabase();
        } catch (Exception e) {
            mDatabase = mHelper.getReadableDatabase();
        }
    }

    public static SqLiteUtil getInstance(Context context) {
        if (mInstance == null) {
            synchronized (SqLiteUtil.class) {
                mInstance = new SqLiteUtil(context);
            }
        }
        return mInstance;
    }

    /**
     * 收藏菜谱
     */
    public void addMenuColumn(MenuMode menuMode) {
        if (mDatabase != null) {
            Log.i("aaa", "保存菜谱数据:" + menuMode.getName());
//            String builder = menuMode.getName() +
//                    "," +
//                    menuMode.getMenuId() +
//                    "," +
//                    menuMode.getCtgTitles() +
//                    "," +
//                    menuMode.getRecipe().getImg() +
//                    "," +
//                    menuMode.getRecipe().getIngredients() +
//                    "," +
//                    menuMode.getRecipe().getMethod() +
//                    "," +
//                    menuMode.getRecipe().getSumary() +
//                    "," +
//                    menuMode.getRecipe().getTitle();
//
//            String sql = "insert into " + SqLiteHelper.mTableMenuName + " (name,menuId,ctgTitles,img,ingredients,method,sumary,title) values (" + builder + ")";
//            mDatabase.execSQL(sql);

            ContentValues values = new ContentValues();
            values.put("name", menuMode.getName());
            values.put("menuId", menuMode.getMenuId());
            values.put("ctgTitles", menuMode.getCtgTitles());
            values.put("img", menuMode.getRecipe().getImg());
            values.put("ingredients", menuMode.getRecipe().getIngredients());
            values.put("method", menuMode.getRecipe().getMethod());
            values.put("sumary", menuMode.getRecipe().getSumary());
            values.put("title", menuMode.getRecipe().getTitle());
            mDatabase.insert(SqLiteHelper.mTableMenuName, null, values);
        }
    }

    /**
     * 删除菜谱
     */
    public void deleteMenu(String menuId) {
        if (mDatabase != null) {
            //注意menuId的值,要用''
            String sql = "delete from " + SqLiteHelper.mTableMenuName + " where menuId = '" + menuId + "'";
            mDatabase.execSQL(sql);
        }
    }

    /**
     * 查询单个菜谱
     */
    public MenuMode queryMenuColumn(String id) {
        MenuMode menuMode = null;
        if (mDatabase != null) {
            String sql = "select * from " + SqLiteHelper.mTableMenuName + " where menuId = ?";
            mDatabase.execSQL(sql);

            Cursor cursor = mDatabase.rawQuery(sql, new String[]{id});
            if (cursor != null) {
                cursor.moveToFirst();
                while (cursor.moveToNext()) {
                    menuMode = new MenuMode();
                    int _id = cursor.getInt(cursor.getColumnIndex("id"));
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    String menuId = cursor.getString(cursor.getColumnIndex("menuId"));
                    String ctgTitles = cursor.getString(cursor.getColumnIndex("ctgTitles"));
                    String img = cursor.getString(cursor.getColumnIndex("img"));
                    String ingredients = cursor.getString(cursor.getColumnIndex("ingredients"));
                    String method = cursor.getString(cursor.getColumnIndex("method"));
                    String sumary = cursor.getString(cursor.getColumnIndex("sumary"));
                    String title = cursor.getString(cursor.getColumnIndex("title"));

                    menuMode.setId(_id);
                    menuMode.setName(name);
                    menuMode.setMenuId(menuId);
                    menuMode.setCtgTitles(ctgTitles);
                    menuMode.setThumbnail(img);
                    MenuDetailMode detailMode = new MenuDetailMode();
                    detailMode.setImg(img);
                    detailMode.setIngredients(ingredients);
                    detailMode.setMethod(method);
                    detailMode.setSumary(sumary);
                    detailMode.setTitle(title);

                    menuMode.setRecipe(detailMode);
                }
                cursor.close();
            }

        }

        return menuMode;
    }

    /**
     * 查询全部菜谱
     */
    public List<MenuMode> queryAllMenu() {
        List<MenuMode> list = new ArrayList<>();
        if (mDatabase != null) {
            Cursor cursor = mDatabase.query(SqLiteHelper.mTableMenuName, null, null, null, null, null, "id asc");
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    int _id = cursor.getInt(cursor.getColumnIndex("id"));
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    String menuId = cursor.getString(cursor.getColumnIndex("menuId"));
                    String ctgTitles = cursor.getString(cursor.getColumnIndex("ctgTitles"));
                    String img = cursor.getString(cursor.getColumnIndex("img"));
                    String ingredients = cursor.getString(cursor.getColumnIndex("ingredients"));
                    String method = cursor.getString(cursor.getColumnIndex("method"));
                    String sumary = cursor.getString(cursor.getColumnIndex("sumary"));
                    String title = cursor.getString(cursor.getColumnIndex("title"));

//                    Log.i("aaa", "id = " + _id + "  name = " + name);

                    MenuMode menuMode = new MenuMode();
                    menuMode.setId(_id);
                    menuMode.setName(name);
                    menuMode.setMenuId(menuId);
                    menuMode.setCtgTitles(ctgTitles);
                    menuMode.setThumbnail(img);
                    MenuDetailMode detailMode = new MenuDetailMode();
                    detailMode.setImg(img);
                    detailMode.setIngredients(ingredients);
                    detailMode.setMethod(method);
                    detailMode.setSumary(sumary);
                    detailMode.setTitle(title);
                    menuMode.setRecipe(detailMode);
                    list.add(menuMode);
                }

                cursor.close();
            }

        }
        Log.i("aaa", "total size = " + list.size());

        return list;
    }

    /**
     * 查询全部菜谱
     */
    public List<MenuMode> queryPageMenu(String id, int pageSize) {
        List<MenuMode> list = new ArrayList<>();
        if (mDatabase != null) {
            if (TextUtils.isEmpty(id)) {
                id = "-1";
            }
//            String sql = "select top(" + pageSize + ") * from " + SqLiteHelper.mTableMenuName + " where id >= ? order by id asc";
//            Cursor cursor = mDatabase.rawQuery(sql, new String[]{id});

            //取pageSize条记录
            String limit = pageSize + "";
            Cursor cursor = mDatabase.query(SqLiteHelper.mTableMenuName, null, "id >= ?", new String[]{id}, null, null, "id asc", limit);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    int _id = cursor.getInt(cursor.getColumnIndex("id"));
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    String menuId = cursor.getString(cursor.getColumnIndex("menuId"));
                    String ctgTitles = cursor.getString(cursor.getColumnIndex("ctgTitles"));
                    String img = cursor.getString(cursor.getColumnIndex("img"));
                    String ingredients = cursor.getString(cursor.getColumnIndex("ingredients"));
                    String method = cursor.getString(cursor.getColumnIndex("method"));
                    String sumary = cursor.getString(cursor.getColumnIndex("sumary"));
                    String title = cursor.getString(cursor.getColumnIndex("title"));

//                    Log.i("aaa", "id = " + _id + "  name = " + name + "  img = " + img);

                    MenuMode menuMode = new MenuMode();
                    menuMode.setId(_id);
                    menuMode.setName(name);
                    menuMode.setMenuId(menuId);
                    menuMode.setCtgTitles(ctgTitles);
                    menuMode.setThumbnail(img);
                    MenuDetailMode detailMode = new MenuDetailMode();
                    detailMode.setImg(img);
                    detailMode.setIngredients(ingredients);
                    detailMode.setMethod(method);
                    detailMode.setSumary(sumary);
                    detailMode.setTitle(title);
                    menuMode.setRecipe(detailMode);
                    list.add(menuMode);
                }

                cursor.close();
            }

        }

        return list;
    }

}
