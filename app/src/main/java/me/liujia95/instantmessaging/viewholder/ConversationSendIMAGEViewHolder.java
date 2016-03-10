package me.liujia95.instantmessaging.viewholder;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.liujia95.instantmessaging.R;
import me.liujia95.instantmessaging.db.model.ConversationModel;
import me.liujia95.instantmessaging.utils.DateUtils;
import me.liujia95.instantmessaging.utils.LogUtils;
import me.liujia95.instantmessaging.view.BubbleImageView;

/**
 * Created by Administrator on 2016/3/7 21:21.
 */
public class ConversationSendIMAGEViewHolder extends RecyclerView.ViewHolder {

    @InjectView(R.id.item_conversation_my_image_iv_img)
    BubbleImageView mIvImg;
    @InjectView(R.id.item_conversation_my_image_iv_icon)
    ImageView       mIvIcon;
    @InjectView(R.id.item_system_message_tv)
    TextView        mTvDate;
    @InjectView(R.id.item_system_message_container)
    LinearLayout    mContainer;

    public ConversationSendIMAGEViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
    }

    public void loadData(ConversationModel model, boolean isShowTime) {

        //显示图片的配置
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        String imageUrl = ImageDownloader.Scheme.FILE.wrap(model.message);
        ImageLoader.getInstance().displayImage(imageUrl, mIvImg, options);

        if (isShowTime) {
            mTvDate.setText(DateUtils.getDateFormat(model.date));
        } else {
            mContainer.setVisibility(View.GONE);
        }
    }

}
