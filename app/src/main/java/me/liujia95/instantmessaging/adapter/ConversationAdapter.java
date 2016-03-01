package me.liujia95.instantmessaging.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import me.liujia95.instantmessaging.R;
import me.liujia95.instantmessaging.db.model.ConversationModel;
import me.liujia95.instantmessaging.utils.UIUtils;
import me.liujia95.instantmessaging.viewholder.ConversationViewHolder;

/**
 * Created by Administrator on 2016/2/12 17:16.
 */
public class ConversationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<ConversationModel> mDatas;

    public ConversationAdapter(List<ConversationModel> datas) {
        this.mDatas = datas;
    }

    public void setData(List<ConversationModel> datas){
        this.mDatas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(UIUtils.getContext()).inflate(R.layout.item_conversation, parent, false);
        return new ConversationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ConversationViewHolder viewholder = (ConversationViewHolder) holder;
        viewholder.loadData(mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        if(mDatas!=null){
            return mDatas.size();
        }
        return 0;
    }
}
