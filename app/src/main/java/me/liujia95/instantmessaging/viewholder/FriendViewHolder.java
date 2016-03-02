package me.liujia95.instantmessaging.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.liujia95.instantmessaging.R;
import me.liujia95.instantmessaging.bean.FriendInfoBean;

/**
 * Created by Administrator on 2016/3/2 19:30.
 */
public class FriendViewHolder extends RecyclerView.ViewHolder {

    @InjectView(R.id.item_friend_list_iv_icon)
    ImageView mIvIcon;
    @InjectView(R.id.item_friend_list_tv_name)
    TextView  mTvName;


    public FriendViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
    }

    public void loadData(FriendInfoBean bean) {
        mIvIcon.setImageResource(bean.avatar);
        mTvName.setText(bean.name);
    }
}
