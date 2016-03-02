package me.liujia95.instantmessaging.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import me.liujia95.instantmessaging.R;
import me.liujia95.instantmessaging.bean.FriendInfoBean;
import me.liujia95.instantmessaging.utils.UIUtils;
import me.liujia95.instantmessaging.viewholder.FriendViewHolder;

/**
 * Created by Administrator on 2016/3/2 19:35.
 */
public class FriendListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private List<FriendInfoBean> mDatas;

    private OnItemClickListener mOnItemClickListener;

    private static final int TYPE_FRIEND   = 0; //好友列表类型
    private static final int TYPE_ALPHABET = 1; //字母类型

    public FriendListAdapter(List<FriendInfoBean> datas) {
        mDatas = datas;
    }

    public void setData(List<FriendInfoBean> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        //TODO:
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(UIUtils.getContext()).inflate(R.layout.item_friend_list, parent, false);
        view.setOnClickListener(this);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FriendViewHolder viewholder = (FriendViewHolder) holder;
        viewholder.itemView.setTag(mDatas.get(position));
        viewholder.loadData(mDatas.get(position));
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
        mOnItemClickListener.onItemClick(v, (FriendInfoBean) v.getTag());
    }

    public interface OnItemClickListener {
        void onItemClick(View view, FriendInfoBean bean);
    }
}
