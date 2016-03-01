package me.liujia95.instantmessaging.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.liujia95.instantmessaging.R;
import me.liujia95.instantmessaging.db.model.ConversationModel;

/**
 * Created by Administrator on 2016/3/1 15:28.
 */
public class ConversationYourTXTViewHolder extends RecyclerView.ViewHolder {

    @InjectView(R.id.item_conversation_your_txt_iv_icon)
    ImageView mIvIcon;
    @InjectView(R.id.item_conversation_your_txt_tv_message)
    TextView  mTvMessage;


    public ConversationYourTXTViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
    }

    public void loadData(ConversationModel model){
        if(model.messageType == EMMessage.Type.TXT){
            mTvMessage.setText(model.message);
        }
    }
}
