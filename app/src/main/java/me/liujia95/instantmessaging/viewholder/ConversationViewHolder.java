package me.liujia95.instantmessaging.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.liujia95.instantmessaging.R;
import me.liujia95.instantmessaging.bean.ConversationBean;

/**
 * Created by Administrator on 2016/2/12 17:17.
 */
public class ConversationViewHolder extends RecyclerView.ViewHolder {
    @InjectView(R.id.item01_iv_icon)
    ImageView mIvIcon;
    @InjectView(R.id.item01_tv_title)
    TextView  mTvTitle;
    @InjectView(R.id.item01_tv_desc)
    TextView  mTvDesc;
    @InjectView(R.id.item01_tv_date)
    TextView  mTvDate;

    public ConversationViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
    }

    public void loadData(ConversationBean bean) {
        mIvIcon.setImageResource(bean.icon);
        mTvTitle.setText(bean.title);
        mTvDesc.setText(bean.desc);
        mTvDate.setText(bean.date);
    }
}
