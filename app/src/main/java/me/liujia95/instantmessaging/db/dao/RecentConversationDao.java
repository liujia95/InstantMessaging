package me.liujia95.instantmessaging.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import me.liujia95.instantmessaging.db.DatabaseHelper;
import me.liujia95.instantmessaging.db.model.ConversationModel;
import me.liujia95.instantmessaging.db.model.MessageState;
import me.liujia95.instantmessaging.utils.LogUtils;

/**
 * Created by Administrator on 2016/2/29 15:40.
 */
public class RecentConversationDao {
    public static final String TABLE_NAME = "recent_conversation";

    public static final String COLUMN_NAME_ID               = "_id";
    public static final String COLUMN_NAME_FROM             = "_from";
    public static final String COLUMN_NAME_TO               = "_to";
    public static final String COLUMN_NAME_MESSAGE_TYPE_ID  = "_message_type_id";
    public static final String COLUMN_NAME_MESSAGE_STATE_ID = "_message_state_id";
    public static final String COLUMN_NAME_MESSAGE          = "_message";
    public static final String COLUMN_NAME_DATE             = "_date";

    private static DatabaseHelper mHelper = new DatabaseHelper();

    /**
     * 是否有跟他聊过
     */
    public static boolean isChated(String username) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT count(*) FROM recent_conversation WHERE _from=? or _to=?", new String[]{username, username});
        boolean isChated = false;
        if (cursor.moveToNext()) {
            if (cursor.getInt(0) != 0) {
                isChated = true;
            }
        }
        cursor.close();
        db.close();
        LogUtils.d("*****is Chated::" + isChated);

        return isChated;
    }

    /**
     * 插入一条记录
     *
     * @param model
     * @return
     */
    public static long insert(ConversationModel model) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_FROM, model.from);
        values.put(COLUMN_NAME_TO, model.to);
        values.put(COLUMN_NAME_MESSAGE_TYPE_ID, MessageTypeDao.getIdFromType(model.messageType.toString()));
        values.put(COLUMN_NAME_MESSAGE_STATE_ID, MessageStateDao.getIdFromState(model.messageState.toString()));
        values.put(COLUMN_NAME_MESSAGE, model.message);
        values.put(COLUMN_NAME_DATE, model.date);

        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        return result;
    }

    /**
     * 更新最新的聊天记录
     *
     * @param model    一条会话记录
     * @param username 当前用户
     * @return
     */
    public static int update(ConversationModel model, String username) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_FROM, model.from);
        values.put(COLUMN_NAME_TO, model.to);
        values.put(COLUMN_NAME_MESSAGE_TYPE_ID, MessageTypeDao.getIdFromType(model.messageType.toString()));
        values.put(COLUMN_NAME_MESSAGE_STATE_ID, MessageStateDao.getIdFromState(model.messageState.toString()));
        values.put(COLUMN_NAME_MESSAGE, model.message);
        values.put(COLUMN_NAME_DATE, model.date);

        int result = db.update(TABLE_NAME, values, "_from=? or _to=?", new String[]{username, username});
        db.close();
        return result;
    }

    /**
     * 根据id修改信息状态
     *
     * @param state
     * @return
     */
    public static int updateMessageState(int id, MessageState state) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_MESSAGE_STATE_ID, MessageStateDao.getIdFromState(state.toString()));
        int result = db.update(TABLE_NAME, values, "_id=?", new String[]{id + ""});
        //TODO：测试
        LogUtils.d("recent updateMessageState result:" + result + " -- state:" + state.toString());
        db.close();
        return result;
    }

    /**
     * 根据消息接收者，查询他的所有的会话
     *
     * @return
     */
    public static List<ConversationModel> selectAll(String to) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query("(SELECT recent_conversation._id, _from, _to , _type ,_state ,_message, _date FROM recent_conversation, message_type,message_state where _message_type_id = message_type._id and _message_state_id = message_state._id )", new String[]{COLUMN_NAME_ID, COLUMN_NAME_FROM, COLUMN_NAME_TO, MessageTypeDao.COLUMN_NAME_TYPE, MessageStateDao.COLUMN_NAME_STATE, COLUMN_NAME_MESSAGE, COLUMN_NAME_DATE}, "_from =? or _to=?", new String[]{to, to}, null, null, null);
        LogUtils.d("***cursor count:::" + cursor.getCount());

        List<ConversationModel> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            ConversationModel model = new ConversationModel();
            model.id = cursor.getString(0);
            model.from = cursor.getString(1);
            model.to = cursor.getString(2);
            model.messageType = ConversationDao.getMessageType(cursor.getString(3));
            model.messageState = ConversationDao.getMessageState(cursor.getString(4));
            model.message = cursor.getString(5);
            model.date = Long.valueOf(cursor.getString(6));

            LogUtils.d("***moveToNext:" + model.toString());

            list.add(model);
        }
        cursor.close();
        db.close();

        LogUtils.d("***list size:::" + list.size());

        return list;
    }
}
