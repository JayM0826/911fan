package com.imfan.j.a91fan.textabout.item;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.imfan.j.a91fan.R;
import com.imfan.j.a91fan.util.Preferences;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by jay on 17-3-5.
 */
public class TextItemViewProvider
        extends ItemViewProvider<TextItem, TextItemViewProvider.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_text_item, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull TextItem textItem) {
        holder.tv_time.setText(textItem.updateTime);
        holder.tv_text_name.setText(textItem.name);
        holder.tv_text_owner.setText(Preferences.getWxNickname());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_text_cover)
        ImageView imageView;

        @BindView(R.id.tv_text_name)
        TextView tv_text_name;

        @BindView(R.id.tv_text_owner)
        TextView tv_text_owner;

        // 在文章和草稿中我们总是显示最后一次更新的时间
        @BindView(R.id.text_create_time)
        TextView tv_time;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(itemView);
        }
    }
}