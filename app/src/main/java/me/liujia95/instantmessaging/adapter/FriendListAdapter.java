package me.liujia95.instantmessaging.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.liujia95.instantmessaging.R;
import me.liujia95.instantmessaging.bean.FriendInfoBean;
import me.liujia95.instantmessaging.utils.UIUtils;
import me.liujia95.instantmessaging.viewholder.CharacterViewHolder;
import me.liujia95.instantmessaging.viewholder.FriendViewHolder;

/**
 * Created by Administrator on 2016/3/2 19:35.
 */
public class FriendListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private List<FriendInfoBean> mDatas;
    private List<FriendInfoBean> mSystemDatas = new ArrayList<>();//系统数据

    private OnItemClickListener mOnItemClickListener;

    private static final int TYPE_SYSTEM_DATA = 2; //字母类型

    public FriendListAdapter(List<FriendInfoBean> datas) {
        mDatas = datas;
        mSystemDatas.add(new FriendInfoBean(R.drawable.em_add_public_group, UIUtils.getString(R.string.apply_and_notification), FriendInfoBean.TYPE_DATA));
        mSystemDatas.add(new FriendInfoBean(R.drawable.em_groups_icon, UIUtils.getString(R.string.group_chat), FriendInfoBean.TYPE_DATA));
        mSystemDatas.add(new FriendInfoBean(R.drawable.em_groups_icon, UIUtils.getString(R.string.chat_room), FriendInfoBean.TYPE_DATA));
        mSystemDatas.add(new FriendInfoBean(R.drawable.em_groups_icon, UIUtils.getString(R.string.huanxin_helper), FriendInfoBean.TYPE_DATA));

    }

    public void setData(List<FriendInfoBean> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position < mSystemDatas.size()) {
            return TYPE_SYSTEM_DATA;
        }
        //减去系统自带的数据个数
        position = position - mSystemDatas.size();
        return mDatas.get(position).item_type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_SYSTEM_DATA || viewType == FriendInfoBean.TYPE_DATA) {
            View view = LayoutInflater.from(UIUtils.getContext()).inflate(R.layout.item_friend_list, parent, false);
            view.setOnClickListener(this);
            return new FriendViewHolder(view);
        } else if (viewType == FriendInfoBean.TYPE_CHARACTER) {
            View view = LayoutInflater.from(UIUtils.getContext()).inflate(R.layout.item_list_character, parent, false);
            return new CharacterViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == TYPE_SYSTEM_DATA) {
            FriendViewHolder viewholder = (FriendViewHolder) holder;
            viewholder.itemView.setTag(mSystemDatas.get(position));
            viewholder.loadData(mSystemDatas.get(position));
        } else if (viewType == FriendInfoBean.TYPE_DATA) {
            //减去系统自带的数据个数
            position = position - mSystemDatas.size();

            FriendViewHolder viewholder = (FriendViewHolder) holder;
            viewholder.itemView.setTag(mDatas.get(position));
            viewholder.loadData(mDatas.get(position));
        } else if (viewType == FriendInfoBean.TYPE_CHARACTER) {
            //减去系统自带的数据个数
            position = position - mSystemDatas.size();

            CharacterViewHolder viewholder = (CharacterViewHolder) holder;
            viewholder.loadData(mDatas.get(position).name);
        }
    }

    @Override
    public int getItemCount() {
        int count = mSystemDatas.size();
        if (mDatas != null) {
            count += mDatas.size();
        }
        return count;
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
