package me.liujia95.instantmessaging.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.liujia95.instantmessaging.R;

/**
 * Created by Administrator on 2016/3/3 12:19.
 */
public class CharacterViewHolder extends RecyclerView.ViewHolder {

    @InjectView(R.id.tv_item_character)
    TextView mTvCharacter;

    public CharacterViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
    }

    public void loadData(String character) {
        mTvCharacter.setText(character);
    }
}
