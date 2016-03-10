package me.liujia95.instantmessaging.viewholder;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.liujia95.instantmessaging.R;
import me.liujia95.instantmessaging.db.model.ConversationModel;
import me.liujia95.instantmessaging.utils.DateUtils;
import me.liujia95.instantmessaging.utils.LogUtils;
import me.liujia95.instantmessaging.utils.UIUtils;
import me.liujia95.instantmessaging.view.BubbleImageView;

/**
 * Created by Administrator on 2016/3/8 12:56.
 */
public class ConversationReceivedIMAGEViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener {

    @InjectView(R.id.item_conversation_your_image_iv_icon)
    ImageView       mIvIcon;
    @InjectView(R.id.item_conversation_your_image_iv_img)
    BubbleImageView mIvImg;
    private GestureDetector mDetector;
    @InjectView(R.id.item_system_message_tv)
    TextView     mTvDate;
    @InjectView(R.id.item_system_message_container)
    LinearLayout mContainer;

    public ConversationReceivedIMAGEViewHolder(View itemView) {
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

    public void loadData(ConversationModel model, boolean isShowTime) {
        //显示图片的配置
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        ImageLoader.getInstance().loadImage(model.message, options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                mIvImg.setImageBitmap(loadedImage);
            }
        });
        if (isShowTime) {
            mTvDate.setText(DateUtils.getDateFormat(model.date));
        } else {
            mContainer.setVisibility(View.GONE);
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
