package me.liujia95.instantmessaging.db;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import me.liujia95.instantmessaging.db.dao.ContactDao;
import me.liujia95.instantmessaging.db.dao.ConversationDao;
import me.liujia95.instantmessaging.db.dao.MessageStateDao;
import me.liujia95.instantmessaging.db.dao.MessageTypeDao;
import me.liujia95.instantmessaging.db.dao.RecentConversationDao;
import me.liujia95.instantmessaging.utils.UIUtils;

/**
 * Created by Administrator on 2016/2/29 13:50.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME    = "instantmessaging.db";
    public static final int    DB_VERSION = 1;

    public final String CREATE_TABLE_MESSAGE_TYPE = "CREATE TABLE " + MessageTypeDao.TABLE_NAME + "(" +
            MessageTypeDao.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            MessageTypeDao.COLUMN_NAME_TYPE + " VARCHAR(20) NOT NULL)";

    public static String CREATE_TABLE_MESSAGE_STATE = "CREATE TABLE " + MessageStateDao.TABLE_NAME + "(" +
            MessageStateDao.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            MessageStateDao.COLUMN_NAME_STATE + " VARCHAR(20) NOT NULL)";

    public static String CREATE_TABLE_CONTACT = "CREATE TABLE " + ContactDao.TABLE_NAME + "(" +
            ContactDao.COLUMN_NAME_ID + "  INTEGER PRIMARY KEY AUTOINCREMENT," +
            ContactDao.COLUMN_NAME_USER_1 + " VARCHAR(60) NOT NULL," +
            ContactDao.COLUMN_NAME_USER_2 + " VARCHAR(60) NOT NULL)";

    public static String CREATE_TABLE_CONVERSATION = "CREATE TABLE " + ConversationDao.TABLE_NAME + "(" +
            ConversationDao.COLUMN_NAME_ID + "  INTEGER PRIMARY KEY AUTOINCREMENT," +
            ConversationDao.COLUMN_NAME_FROM + " VARCHAR(60) NOT NULL ," +
            ConversationDao.COLUMN_NAME_TO + " VARCHAR(60) NOT NULL ," +
            ConversationDao.COLUMN_NAME_MESSAGE_TYPE_ID + " INTEGER NOT NULL ," +
            ConversationDao.COLUMN_NAME_MESSAGE_STATE_ID + " INTEGER NOT NULL ," +
            ConversationDao.COLUMN_NAME_MESSAGE + " VARCHAR(255) NOT NULL ," +
            ConversationDao.COLUMN_NAME_DATE + " VARCHAR(255) NOT NULL )";

    public static String CREATE_TABLE_RECENT_CONVERSATION = "CREATE TABLE " + RecentConversationDao.TABLE_NAME + "(" +
            RecentConversationDao.COLUMN_NAME_ID + "  INTEGER PRIMARY KEY AUTOINCREMENT," +
            RecentConversationDao.COLUMN_NAME_FROM + " VARCHAR(60) NOT NULL ," +
            RecentConversationDao.COLUMN_NAME_TO + " VARCHAR(60) NOT NULL ," +
            RecentConversationDao.COLUMN_NAME_MESSAGE_TYPE_ID + " INTEGER NOT NULL ," +
            RecentConversationDao.COLUMN_NAME_MESSAGE_STATE_ID + " INTEGER NOT NULL ," +
            RecentConversationDao.COLUMN_NAME_MESSAGE + " VARCHAR(255) NOT NULL ," +
            RecentConversationDao.COLUMN_NAME_DATE + " VARCHAR(255) NOT NULL )";


    public static String INIT_MESSAGE_TYPE = "INSERT INTO message_type(_type) values(\"txt\");" +
            "INSERT INTO message_type(_type) values(\"image\");" +
            "INSERT INTO message_type(_type) values(\"video\");" +
            "INSERT INTO message_type(_type) values(\"location\");" +
            "INSERT INTO message_type(_type) values(\"voice\");" +
            "INSERT INTO message_type(_type) values(\"file\");" +
            "INSERT INTO message_type(_type) values(\"cmd\");";

    public static String INIT_MESSAGE_STATE = "INSERT INTO message_state(_state) values(\"unread\");" +
            "INSERT INTO message_state(_state) values(\"read\");" +
            "INSERT INTO message_state(_state) values(\"undelivered\");" +
            "INSERT INTO message_state(_state) values(\"delivered\");";

    public DatabaseHelper() {
        super(UIUtils.getContext(), DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表
        db.execSQL(CREATE_TABLE_MESSAGE_TYPE);
        db.execSQL(CREATE_TABLE_MESSAGE_STATE);
        db.execSQL(CREATE_TABLE_CONTACT);
        db.execSQL(CREATE_TABLE_CONVERSATION);
        db.execSQL(CREATE_TABLE_RECENT_CONVERSATION);

        //初始化数据库中的数据
        db.execSQL(INIT_MESSAGE_TYPE);
        db.execSQL(INIT_MESSAGE_STATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
