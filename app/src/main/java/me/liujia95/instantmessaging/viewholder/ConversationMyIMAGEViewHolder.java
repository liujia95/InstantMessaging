package me.liujia95.instantmessaging.viewholder;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.hyphenate.chat.EMMessage;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.liujia95.instantmessaging.R;
import me.liujia95.instantmessaging.db.model.ConversationModel;
import me.liujia95.instantmessaging.imageloader.utils.ImageLoader;
import me.liujia95.instantmessaging.utils.LogUtils;
import me.liujia95.instantmessaging.utils.UIUtils;
import me.liujia95.instantmessaging.view.BubbleImageView;

/**
 * Created by Administrator on 2016/3/7 21:21.
 */
public class ConversationMyIMAGEViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener {

    @InjectView(R.id.item_conversation_my_image_iv_img)
    BubbleImageView mIvImg;
    @InjectView(R.id.item_conversation_my_image_iv_icon)
    ImageView       mIvIcon;

    GestureDetector mDetector;

    public ConversationMyIMAGEViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
        initListener();
    }

    private void initListener() {
        mIvImg.setOnTouchListener(this);
        mDetector = new GestureDetector(UIUtils.getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                mIvImg.setColorFilter(Color.parseColor("#77000000"));
                LogUtils.d("@@短按");
                return super.onDown(e);
            }

            @Override
            public void onLongPress(MotionEvent e) {

                LogUtils.d("@@长按");
            }
        });
    }

    public void loadData(ConversationModel model) {
        if (model.messageType == EMMessage.Type.IMAGE) {
            LogUtils.d("@@model:" + model.message);
            ImageLoader.getInstance().loadImage(model.message, mIvImg);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mDetector.onTouchEvent(event)) {
            return true;
        }
        return false;
    }
}
