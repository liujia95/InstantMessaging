package me.liujia95.instantmessaging.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.List;

import me.liujia95.instantmessaging.db.DatabaseHelper;
import me.liujia95.instantmessaging.db.model.ConversationModel;
import me.liujia95.instantmessaging.db.model.MessageState;
import me.liujia95.instantmessaging.utils.LogUtils;

/**
 * Created by Administrator on 2016/2/29 15:39.
 */
public class ConversationDao {
    public static final String TABLE_NAME = "conversation";

    public static final String COLUMN_NAME_ID               = "_id";
    public static final String COLUMN_NAME_FROM             = "_from";
    public static final String COLUMN_NAME_TO               = "_to";
    public static final String COLUMN_NAME_MESSAGE_TYPE_ID  = "_message_type_id";
    public static final String COLUMN_NAME_MESSAGE_STATE_ID = "_message_state_id";
    public static final String COLUMN_NAME_MESSAGE          = "_message";
    public static final String COLUMN_NAME_DATE             = "_date";

    private static DatabaseHelper mHelper = new DatabaseHelper();

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
     * 根据会话的对象，查询本用户和他的所有的会话
     *
     * @return
     */
    public static List<ConversationModel> selectAllByChatObj(String chatObj, String username) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String sql = "SELECT * FROM(SELECT conversation._id, _from, _to , _type ,_state ,_message, _date FROM conversation, message_type,message_state where _message_type_id = message_type._id and _message_state_id = message_state._id ) WHERE _from =? and _to=? or _from=? and _to=?";
        Cursor cursor = db.rawQuery(sql, new String[]{chatObj, username, username, chatObj});
        List<ConversationModel> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            ConversationModel model = new ConversationModel();
            model.id = cursor.getString(0);
            model.from = cursor.getString(1);
            model.to = cursor.getString(2);
            model.messageType = getMessageType(cursor.getString(3));
            model.messageState = getMessageState(cursor.getString(4));
            model.message = cursor.getString(5);
            model.date = Long.valueOf(cursor.getString(6));

            list.add(model);
        }
        cursor.close();
        db.close();

        return list;
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
        LogUtils.d("updateMessageState result:" + result + " -- state:" + state.toString());
        return result;
    }

    public static EMMessage.Type getMessageType(String type) {
        if (type.equals("TXT")) {
            return EMMessage.Type.TXT;
        } else if (type.equals("IMAGE")) {
            return EMMessage.Type.IMAGE;
        } else if (type.equals("VIDEO")) {
            return EMMessage.Type.VIDEO;
        } else if (type.equals("LOCATION")) {
            return EMMessage.Type.LOCATION;
        } else if (type.equals("VOICE")) {
            return EMMessage.Type.VOICE;
        } else if (type.equals("FILE")) {
            return EMMessage.Type.FILE;
        } else {
            return EMMessage.Type.CMD;
        }
    }

    public static MessageState getMessageState(String state) {
        if (state.equals("UNREAD")) {
            return MessageState.UNREAD;
        } else if (state.equals("READ")) {
            return MessageState.READ;
        } else if (state.equals("DELIVERED")) {
            return MessageState.DELIVERED;
        } else {
            return MessageState.UNDELIVERED;
        }
    }
}
