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
import me.liujia95.instantmessaging.viewholder.ConversationMyIMAGEViewHolder;
import me.liujia95.instantmessaging.viewholder.ConversationMyTXTViewHolder;
import me.liujia95.instantmessaging.viewholder.ConversationYourIMAGEViewHolder;
import me.liujia95.instantmessaging.viewholder.ConversationYourTXTViewHolder;

/**
 * Created by Administrator on 2016/3/1 14:37.
 */
public class ConversationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ConversationModel> mDatas;

    private static final int TYPE_MY_TXT     = 0;
    private static final int TYPE_YOUR_TXT   = 1;
    private static final int TYPE_MY_IMAGE   = 2;
    private static final int TYPE_YOUR_IMAGE = 3;

    public ConversationAdapter(List<ConversationModel> list) {
        mDatas = list;
    }

    public void setDatas(List<ConversationModel> list){
        mDatas = list;
    }

    @Override
    public int getItemViewType(int position) {
        ConversationModel model = mDatas.get(position);
        if (model.from.equals(EMClient.getInstance().getCurrentUser())) {
            //谁发的消息，就代表谁说的话
            if (model.messageType == EMMessage.Type.IMAGE) {
                return TYPE_MY_IMAGE;
            }
            return TYPE_MY_TXT;
        } else {
            if (model.messageType == EMMessage.Type.IMAGE) {
                return TYPE_YOUR_IMAGE;
            }
            return TYPE_YOUR_TXT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_MY_TXT) {
            View view = LayoutInflater.from(UIUtils.getContext()).inflate(R.layout.item_conversation_my_txt, parent, false);
            return new ConversationMyTXTViewHolder(view);
        } else if (viewType == TYPE_YOUR_TXT) {
            View view = LayoutInflater.from(UIUtils.getContext()).inflate(R.layout.item_conversation_your_txt, parent, false);
            return new ConversationYourTXTViewHolder(view);
        } else if (viewType == TYPE_MY_IMAGE) {
            View view = LayoutInflater.from(UIUtils.getContext()).inflate(R.layout.item_conversation_my_image, parent, false);
            return new ConversationMyIMAGEViewHolder(view);
        } else if (viewType == TYPE_YOUR_IMAGE) {
            View view = LayoutInflater.from(UIUtils.getContext()).inflate(R.layout.item_conversation_your_image, parent, false);
            return new ConversationYourIMAGEViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        ConversationModel model = mDatas.get(position);
        if (viewType == TYPE_MY_TXT) {
            ConversationMyTXTViewHolder viewholder = (ConversationMyTXTViewHolder) holder;
            viewholder.loadData(model);
        } else if (viewType == TYPE_YOUR_TXT) {
            ConversationYourTXTViewHolder viewholder = (ConversationYourTXTViewHolder) holder;
            viewholder.loadData(model);
        } else if (viewType == TYPE_MY_IMAGE) {
            ConversationMyIMAGEViewHolder viewholder = (ConversationMyIMAGEViewHolder) holder;
            viewholder.loadData(model);
        } else if (viewType == TYPE_YOUR_IMAGE) {
            ConversationYourIMAGEViewHolder viewholder = (ConversationYourIMAGEViewHolder) holder;
            viewholder.loadData(model);
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
}
