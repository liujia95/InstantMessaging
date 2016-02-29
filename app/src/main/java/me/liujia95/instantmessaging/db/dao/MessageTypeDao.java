package me.liujia95.instantmessaging.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import me.liujia95.instantmessaging.db.DatabaseHelper;

/**
 * Created by Administrator on 2016/2/29 15:40.
 */
public class MessageTypeDao {
    public static final String TABLE_NAME = "message_type";

    public static final String COLUMN_NAME_ID   = "_id";
    public static final String COLUMN_NAME_TYPE = "_type";

    public static DatabaseHelper mHelper = new DatabaseHelper();

    public static int getIdFromType(String type) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_NAME_ID}, "_type=?", new String[]{type}, null, null, null);
        if (cursor.moveToNext()) {
            return cursor.getInt(0);
        }
        db.close();
        return -1;
    }
}
