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
     * 根据消息接收者，查询他的所有的会话
     *
     * @return
     */
    public static List<ConversationModel> selectAll(String to) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query("(SELECT conversation._id, _from, _to , _type ,_state ,_message, _date FROM conversation, message_type,message_state where _message_type_id = message_type._id and _message_state_id = message_state._id )", new String[]{COLUMN_NAME_ID, COLUMN_NAME_FROM, COLUMN_NAME_TO, MessageTypeDao.COLUMN_NAME_TYPE, MessageStateDao.COLUMN_NAME_STATE, COLUMN_NAME_MESSAGE, COLUMN_NAME_DATE}, "_from =? or _to=?", new String[]{to, to}, null, null, null);
        LogUtils.d("===cursor count:::" + cursor.getCount());

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

            LogUtils.d("===moveToNext:"+model.toString());

            list.add(model);
        }
        cursor.close();
        db.close();

        LogUtils.d("===list size:::"+list.size());

        return list;
    }

    public static EMMessage.Type getMessageType(String type) {
        if (type.equals("txt")) {
            return EMMessage.Type.TXT;
        } else if (type.equals("image")) {
            return EMMessage.Type.IMAGE;
        } else if (type.equals("video")) {
            return EMMessage.Type.VIDEO;
        } else if (type.equals("location")) {
            return EMMessage.Type.LOCATION;
        } else if (type.equals("voice")) {
            return EMMessage.Type.VOICE;
        } else if (type.equals("file")) {
            return EMMessage.Type.FILE;
        } else {
            return EMMessage.Type.CMD;
        }
    }

    public static MessageState getMessageState(String state) {
        if (state.equals("unread")) {
            return MessageState.UNREAD;
        } else if (state.equals("read")) {
            return MessageState.READ;
        } else if (state.equals("delivered")) {
            return MessageState.DELIVERED;
        } else {
            return MessageState.UNDELIVERED;
        }
    }
}
