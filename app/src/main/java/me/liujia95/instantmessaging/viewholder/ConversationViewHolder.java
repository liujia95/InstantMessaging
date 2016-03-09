package me.liujia95.instantmessaging.viewholder;

import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.liujia95.instantmessaging.R;
import me.liujia95.instantmessaging.bean.RedPointBean;
import me.liujia95.instantmessaging.db.model.ConversationModel;
import me.liujia95.instantmessaging.manager.RedPointManager;
import me.liujia95.instantmessaging.utils.ConversationUtils;
import me.liujia95.instantmessaging.utils.DateUtils;
import me.liujia95.instantmessaging.utils.gif.GifUtils;

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
        String chatObj = ConversationUtils.getChatObj(bean);

        //初始化数据，只会add进去第一次的数据，后续不会重复add
        RedPointManager.getInstance().add(chatObj, new RedPointBean(false, mIvRedPoint));
        //决定红点是否显示
        if (RedPointManager.getInstance().get(chatObj).isShow) {
            mIvRedPoint.setVisibility(View.VISIBLE);
        } else {
            mIvRedPoint.setVisibility(View.GONE);
        }

        //将时间转换
        Date date = new Date(Long.valueOf(bean.date));

        mTvTitle.setText(chatObj);

        SpannableStringBuilder sb = GifUtils.handler(mTvDesc, bean.message);
        mTvDesc.setText(sb);
        String dateFormat = DateUtils.getDateFormat(date.getTime());
        mTvTime.setText(dateFormat);
    }
}
