package me.liujia95.instantmessaging.viewholder;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.hyphenate.chat.EMMessage;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.liujia95.instantmessaging.R;
import me.liujia95.instantmessaging.db.model.ConversationModel;
import me.liujia95.instantmessaging.utils.LogUtils;
import me.liujia95.instantmessaging.view.BubbleImageView;

/**
 * Created by Administrator on 2016/3/7 21:21.
 */
public class ConversationMyIMAGEViewHolder extends RecyclerView.ViewHolder {

    @InjectView(R.id.item_conversation_my_image_iv_img)
    BubbleImageView mIvImg;
    @InjectView(R.id.item_conversation_my_image_iv_icon)
    ImageView       mIvIcon;


    public ConversationMyIMAGEViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
    }

    public void loadData(ConversationModel model) {
        if (model.messageType == EMMessage.Type.IMAGE) {
            LogUtils.d("@@model:" + model.message);

            //显示图片的配置
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();

            String imageUrl = ImageDownloader.Scheme.FILE.wrap(model.message);
            ImageLoader.getInstance().displayImage(imageUrl, mIvImg, options);
        }
    }

}
