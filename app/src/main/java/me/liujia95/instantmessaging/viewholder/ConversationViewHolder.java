package me.liujia95.instantmessaging.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.liujia95.instantmessaging.R;
import me.liujia95.instantmessaging.db.model.ConversationModel;
import me.liujia95.instantmessaging.db.model.MessageState;
import me.liujia95.instantmessaging.utils.DateUtils;

/**
 * Created by Administrator on 2016/2/12 17:17.
 */
public class ConversationViewHolder extends RecyclerView.ViewHolder {

    @InjectView(R.id.item_conversation_iv_icon)
    ImageView mIvIcon;
    @InjectView(R.id.item_conversation_tv_title)
    TextView  mTvTitle;
    @InjectView(R.id.item_conversation_tv_desc)
    TextView  mTvDesc;
    @InjectView(R.id.item_conversation_tv_time)
    TextView  mTvTime;
    @InjectView(R.id.item_conversation_red_point)
    ImageView mIvRedPoint;

    public ConversationViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
    }

    public void loadData(ConversationModel bean) {
        mTvTitle.setText(bean.from);
        mTvDesc.setText(bean.message);
        //将时间转换
        Date date = new Date(Long.valueOf(bean.date));
        String dateFormat = DateUtils.getDateFormat(date.getTime());
        mTvTime.setText(dateFormat);

        if(bean.messageState == MessageState.UNREAD){

        }
    }
}
