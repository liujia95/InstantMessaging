package me.liujia95.instantmessaging.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import me.liujia95.instantmessaging.db.DatabaseHelper;
import me.liujia95.instantmessaging.db.model.ContactModel;

/**
 * Created by Administrator on 2016/2/29 15:40.
 */
public class ContactDao {
    public static final String TABLE_NAME = "contact";

    public static final String COLUMN_NAME_ID     = "_id";
    public static final String COLUMN_NAME_USER_1 = "_user1";
    public static final String COLUMN_NAME_USER_2 = "_user2";

    public static DatabaseHelper mHelper = new DatabaseHelper();

    /**
     * 插入一条记录
     *
     * @param contact
     * @return 返回新添记录的行号, 与主键id无关
     */
    public static long insert(ContactModel contact) {
        SQLiteDatabase db = mHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_USER_1, contact.user1);
        values.put(COLUMN_NAME_USER_2, contact.user2);

        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        return result;
    }

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    public static long delete(int id) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int result = db.delete(TABLE_NAME, COLUMN_NAME_ID + "=?", new String[]{id + ""});
        db.close();
        return result;
    }

    /**
     * 更改
     *
     * @param contact
     * @return
     */
    public static int update(ContactModel contact) {
        SQLiteDatabase db = mHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_USER_1, contact.user1);
        values.put(COLUMN_NAME_USER_2, contact.user2);
        int result = db.update(TABLE_NAME, values, COLUMN_NAME_ID + "=?", new String[]{contact.id});
        db.close();
        return result;
    }

    /**
     * 根据一个人查询他所有的好友列表
     *
     * @param user
     * @return
     */
    public static List<String> selectAllContactByUser(String user) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_NAME_USER_2}, COLUMN_NAME_USER_1 + "=?", new String[]{user}, null, null, null);

        List<String> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            String str = cursor.getString(0);
            list.add(str);
        }
        cursor.close();
        db.close();
        return list;
    }
}
