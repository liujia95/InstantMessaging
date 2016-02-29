package me.liujia95.instantmessaging.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import me.liujia95.instantmessaging.db.DatabaseHelper;

/**
 * Created by Administrator on 2016/2/29 15:40.
 */
public class MessageStateDao {
    public static final String TABLE_NAME = "message_state";

    public static final String COLUMN_NAME_ID    = "_id";
    //未送达 --> 已送达 --> 未读 --> 已读
    public static final String COLUMN_NAME_STATE = "_state";

    public static DatabaseHelper mHelper = new DatabaseHelper();

    public static int getIdFromState(String state) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_NAME_ID}, "_state=?", new String[]{state}, null, null, null);
        if (cursor.moveToNext()) {
            return cursor.getInt(0);
        }
        db.close();
        return -1;
    }
}
