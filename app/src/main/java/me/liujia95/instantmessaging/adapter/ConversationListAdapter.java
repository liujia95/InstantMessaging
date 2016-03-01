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
public class ConversationListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    List<ConversationModel> mDatas;

    private OnItemClickListener mOnItemClickListener = null;

    public ConversationListAdapter(List<ConversationModel> datas) {
        this.mDatas = datas;
    }

    public void setData(List<ConversationModel> datas) {
        this.mDatas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(UIUtils.getContext()).inflate(R.layout.item_conversation_list, parent, false);
        view.setOnClickListener(this);
        return new ConversationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ConversationViewHolder viewholder = (ConversationViewHolder) holder;
        viewholder.loadData(mDatas.get(position));
        viewholder.itemView.setTag(mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        if (mDatas != null) {
            return mDatas.size();
        }
        return 0;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //获取数据
            mOnItemClickListener.onItemClick(v, (ConversationModel) v.getTag());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, ConversationModel model);
    }
}
