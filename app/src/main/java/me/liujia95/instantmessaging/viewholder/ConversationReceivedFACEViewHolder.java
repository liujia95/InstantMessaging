package me.liujia95.instantmessaging.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.liujia95.instantmessaging.R;
import me.liujia95.instantmessaging.db.model.ConversationModel;
import me.liujia95.instantmessaging.utils.DateUtils;
import me.liujia95.instantmessaging.utils.FaceUtils;
import me.liujia95.instantmessaging.utils.LogUtils;
import me.liujia95.instantmessaging.view.GifView;

/**
 * Created by Administrator on 2016/3/10 15:16.
 */
public class ConversationReceivedFACEViewHolder extends RecyclerView.ViewHolder {

    @InjectView(R.id.item_conversation_your_face_iv_icon)
    ImageView    IvIcon;
    @InjectView(R.id.item_conversation_your_face_gifv_face)
    GifView      mGifvFace;
    @InjectView(R.id.item_system_message_tv)
    TextView     mTvDate;
    @InjectView(R.id.item_system_message_container)
    LinearLayout mContainer;

    public ConversationReceivedFACEViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
    }

    public void loadData(ConversationModel model, boolean isShowTime) {
        String path = model.message;
        if(FaceUtils.isFace(model.message)){
            path = FaceUtils.unpack(path);
            mGifvFace.setGifAssets(path);
        }else{
            mGifvFace.setGifPath(path);
        }
        LogUtils.d("@@ path:"+path);
        if (isShowTime) {
            mTvDate.setText(DateUtils.getDateFormatToChatting(model.date));
            mContainer.setVisibility(View.VISIBLE);
        } else {
            mContainer.setVisibility(View.GONE);
        }
    }
}
