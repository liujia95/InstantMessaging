package me.liujia95.instantmessaging.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.felipecsl.gifimageview.library.GifImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.liujia95.instantmessaging.R;
import me.liujia95.instantmessaging.db.model.ConversationModel;
import me.liujia95.instantmessaging.utils.gif.GifUtils;

/**
 * Created by Administrator on 2016/3/9 19:53.
 */
public class ConversationSendFACEViewHolder extends RecyclerView.ViewHolder {
    @InjectView(R.id.item_conversation_my_face_iv_face)
    GifImageView mGifIvIcon;
    @InjectView(R.id.item_conversation_my_face_iv_icon)
    ImageView    mIvFace;

    public ConversationSendFACEViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
    }

    public void loadData(ConversationModel model) {
        byte[] bytes = GifUtils.tusijiHandler(model.message);
        mGifIvIcon.setBytes(bytes);
        mGifIvIcon.startAnimation();
    }


}
