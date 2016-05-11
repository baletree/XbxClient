package com.xbx.client.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by EricYuan on 2016/5/11.
 */
public class DBOpere {
    private MySQLiteOpenHelper helper;
    private SQLiteDatabase db;
    private static DBOpere dbOperate;

    private DBOpere(Context context) {
        helper = new MySQLiteOpenHelper(context.getApplicationContext());
    }

    public static DBOpere getInstance(Context context) {
        if (dbOperate == null) {
            synchronized (DBOpere.class) {
                if (dbOperate == null) {
                    dbOperate = new DBOpere(context.getApplicationContext());
                }
            }
        }
        return dbOperate;
    }

    public synchronized int saveLatlng(LatLng latLng,String timeStr) {
        if (db == null) {
            db = helper.getWritableDatabase();
        }
        int count = 0;
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put("timestr", timeStr);
            values.put("sLat", latLng.latitude);
            values.put("sLng", latLng.longitude);
            count = (int) db.insert("tableLatlng", null, values);
        }
        close();
        return count;
    }

    public synchronized List<String> getLatlng() {
        if(db == null){
            db = helper.getWritableDatabase();
        }
        List<String> list = new ArrayList<>();
        if(db.isOpen()) {
            Cursor cursor = db.query("tableLatlng", null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                String str = cursor.getString(cursor.getColumnIndex("timestr"))+","+cursor.getDouble(cursor.getColumnIndex("sLat"))+","+cursor.getDouble(cursor.getColumnIndex("sLng"));
                list.add(str);
            }
        }
        return list;
    }

    public synchronized void deleteLatlng() {
        if (db == null) {
            db = helper.getWritableDatabase();
        }
        if (db.isOpen()) {
            db.delete("tableLatlng", null, null);
        }
    }

    private void close() {
        if (db != null) {
            db.close();
            db = null;
        }
    }
}
