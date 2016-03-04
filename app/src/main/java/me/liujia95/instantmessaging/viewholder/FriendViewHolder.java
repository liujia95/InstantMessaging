package me.liujia95.instantmessaging.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.liujia95.instantmessaging.R;
import me.liujia95.instantmessaging.bean.FriendInfoBean;
import me.liujia95.instantmessaging.bean.RedPointBean;
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
        //初始化数据，只会add进去第一次的数据，后续不会重复add
        RedPointManager.getInstance().add(bean.name, new RedPointBean(false, mIvRedPoint));
        //决定红点是否显示
        if (RedPointManager.getInstance().get(bean.name).isShow) {
            mIvRedPoint.setVisibility(View.VISIBLE);
        } else {
            mIvRedPoint.setVisibility(View.GONE);
        }
        mIvIcon.setImageResource(bean.avatar);
        mTvName.setText(bean.name);
    }
}
