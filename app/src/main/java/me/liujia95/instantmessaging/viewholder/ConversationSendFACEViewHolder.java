package me.liujia95.instantmessaging.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.liujia95.instantmessaging.R;
import me.liujia95.instantmessaging.db.model.ConversationModel;
import me.liujia95.instantmessaging.view.GifView;

/**
 * Created by Administrator on 2016/3/9 19:53.
 */
public class ConversationSendFACEViewHolder extends RecyclerView.ViewHolder {
    @InjectView(R.id.item_conversation_my_face_gifv_face)
    GifView   mGifvFace;
    @InjectView(R.id.item_conversation_my_face_iv_icon)
    ImageView mIvFace;

    public ConversationSendFACEViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
    }

    public void loadData(ConversationModel model) {
        String gif = "tusiji/gif/" + model.message.replace("t_static_", "t_").replace(".png", ".gif");
        mGifvFace.setGifAssets(gif);
    }
}
