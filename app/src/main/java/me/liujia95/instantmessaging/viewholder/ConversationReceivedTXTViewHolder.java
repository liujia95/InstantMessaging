package me.liujia95.instantmessaging.viewholder;

import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.liujia95.instantmessaging.R;
import me.liujia95.instantmessaging.db.model.ConversationModel;
import me.liujia95.instantmessaging.utils.DateUtils;
import me.liujia95.instantmessaging.utils.gif.GifUtils;

/**
 * Created by Administrator on 2016/3/1 15:28.
 */
public class ConversationReceivedTXTViewHolder extends RecyclerView.ViewHolder {

    @InjectView(R.id.item_conversation_your_txt_iv_icon)
    ImageView    mIvIcon;
    @InjectView(R.id.item_conversation_your_txt_tv_message)
    TextView     mTvMessage;
    @InjectView(R.id.item_system_message_tv)
    TextView     mTvDate;
    @InjectView(R.id.item_system_message_container)
    LinearLayout mContainer;

    public ConversationReceivedTXTViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
    }

    public void loadData(ConversationModel model, boolean isShowTime) {
        SpannableStringBuilder sb = GifUtils.faceHandler(mTvMessage, model.message);
        mTvMessage.setText(sb);
        if (isShowTime) {
            mTvDate.setText(DateUtils.getDateFormatToChatting(model.date));
            mContainer.setVisibility(View.VISIBLE);
        } else {
            mContainer.setVisibility(View.GONE);
        }
    }
}
