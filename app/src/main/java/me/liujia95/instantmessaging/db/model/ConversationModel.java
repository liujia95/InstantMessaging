package me.liujia95.instantmessaging.db.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.hyphenate.chat.EMMessage;

/**
 * Created by Administrator on 2016/2/29 16:30.
 */
public class ConversationModel implements Parcelable {

    public String         id;
    public String         from;   //发送者
    public String         to;     //接收者
    public EMMessage.Type messageType;    //消息类型
    public MessageState   messageState;   //消息状态
    public String         message;        //消息
    public long           date;           //时间

    public ConversationModel() {
    }

    protected ConversationModel(Parcel in) {
        id = in.readString();
        from = in.readString();
        to = in.readString();
        message = in.readString();
        date = in.readLong();
    }

    public static final Creator<ConversationModel> CREATOR = new Creator<ConversationModel>() {
        @Override
        public ConversationModel createFromParcel(Parcel in) {
            return new ConversationModel(in);
        }

        @Override
        public ConversationModel[] newArray(int size) {
            return new ConversationModel[size];
        }
    };

    @Override
    public String toString() {
        return "ConversationModel{" +
                "id='" + id + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", messageType=" + messageType +
                ", messageState=" + messageState +
                ", message='" + message + '\'' +
                ", date=" + date +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(from);
        dest.writeString(to);
        dest.writeString(message);
        dest.writeLong(date);
    }
}
