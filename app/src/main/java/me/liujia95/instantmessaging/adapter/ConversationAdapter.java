package me.liujia95.instantmessaging.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import java.util.List;

import me.liujia95.instantmessaging.R;
import me.liujia95.instantmessaging.db.model.ConversationModel;
import me.liujia95.instantmessaging.utils.UIUtils;
import me.liujia95.instantmessaging.viewholder.ConversationSendFACEViewHolder;
import me.liujia95.instantmessaging.viewholder.ConversationSendIMAGEViewHolder;
import me.liujia95.instantmessaging.viewholder.ConversationSendTXTViewHolder;
import me.liujia95.instantmessaging.viewholder.ConversationReceivedIMAGEViewHolder;
import me.liujia95.instantmessaging.viewholder.ConversationReceivedTXTViewHolder;

/**
 * Created by Administrator on 2016/3/1 14:37.
 */
public class ConversationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ConversationModel> mDatas;

    private static final int TYPE_MY_TXT     = 0;
    private static final int TYPE_YOUR_TXT   = 1;
    private static final int TYPE_MY_IMAGE   = 2;
    private static final int TYPE_YOUR_IMAGE = 3;
    private static final int TYPE_MY_FACE    = 4;
    private static final int TYPE_YOUR_FACE  = 5;

    public ConversationAdapter(List<ConversationModel> list) {
        mDatas = list;
    }

    public void setDatas(List<ConversationModel> list) {
        mDatas = list;
    }

    @Override
    public int getItemViewType(int position) {
        ConversationModel model = mDatas.get(position);
        if (model.from.equals(EMClient.getInstance().getCurrentUser())) {
            //谁发的消息，就代表谁说的话
            if (model.messageType == EMMessage.Type.IMAGE) {
                return TYPE_MY_IMAGE;
            } else if (model.messageType == EMMessage.Type.CMD) {
                return TYPE_MY_FACE;
            }
            return TYPE_MY_TXT;
        } else {
            if (model.messageType == EMMessage.Type.IMAGE) {
                return TYPE_YOUR_IMAGE;
            } else if (model.messageType == EMMessage.Type.CMD) {
                return TYPE_YOUR_FACE;
            }
            return TYPE_YOUR_TXT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_MY_TXT) {
            View view = LayoutInflater.from(UIUtils.getContext()).inflate(R.layout.item_conversation_send_txt, parent, false);
            return new ConversationSendTXTViewHolder(view);
        } else if (viewType == TYPE_YOUR_TXT) {
            View view = LayoutInflater.from(UIUtils.getContext()).inflate(R.layout.item_conversation_received_txt, parent, false);
            return new ConversationReceivedTXTViewHolder(view);
        } else if (viewType == TYPE_MY_IMAGE) {
            View view = LayoutInflater.from(UIUtils.getContext()).inflate(R.layout.item_conversation_send_image, parent, false);
            return new ConversationSendIMAGEViewHolder(view);
        } else if (viewType == TYPE_YOUR_IMAGE) {
            View view = LayoutInflater.from(UIUtils.getContext()).inflate(R.layout.item_conversation_received_image, parent, false);
            return new ConversationReceivedIMAGEViewHolder(view);
        } else if (viewType == TYPE_MY_FACE) {
            View view = LayoutInflater.from(UIUtils.getContext()).inflate(R.layout.item_conversation_send_face, parent, false);
            return new ConversationSendFACEViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        ConversationModel model = mDatas.get(position);
        if (viewType == TYPE_MY_TXT) {
            ConversationSendTXTViewHolder viewholder = (ConversationSendTXTViewHolder) holder;
            viewholder.loadData(model);
        } else if (viewType == TYPE_YOUR_TXT) {
            ConversationReceivedTXTViewHolder viewholder = (ConversationReceivedTXTViewHolder) holder;
            viewholder.loadData(model);
        } else if (viewType == TYPE_MY_IMAGE) {
            ConversationSendIMAGEViewHolder viewholder = (ConversationSendIMAGEViewHolder) holder;
            viewholder.loadData(model);
        } else if (viewType == TYPE_YOUR_IMAGE) {
            ConversationReceivedIMAGEViewHolder viewholder = (ConversationReceivedIMAGEViewHolder) holder;
            viewholder.loadData(model);
        } else if (viewType == TYPE_MY_FACE) {
            ConversationSendFACEViewHolder viewholder = (ConversationSendFACEViewHolder) holder;
            viewholder.loadData(model);
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
}
