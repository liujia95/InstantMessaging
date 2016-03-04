package me.liujia95.instantmessaging.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.liujia95.instantmessaging.R;
import me.liujia95.instantmessaging.bean.FriendInfoBean;
import me.liujia95.instantmessaging.manager.RedPointManager;

/**
 * Created by Administrator on 2016/3/2 19:30.
 */
public class FriendViewHolder extends RecyclerView.ViewHolder {

    @InjectView(R.id.item_friend_list_iv_icon)
    ImageView mIvIcon;
    @InjectView(R.id.item_friend_list_tv_name)
    TextView  mTvName;
    @InjectView(R.id.item_friend_list_iv_red_point)
    public ImageView mIvRedPoint;

    public FriendViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
    }

    public void loadData(FriendInfoBean bean) {
        //添加到红点管理类中
        RedPointManager.getInstance().add(bean.name, mIvRedPoint);
        mIvIcon.setImageResource(bean.avatar);
        mTvName.setText(bean.name);
    }
}
