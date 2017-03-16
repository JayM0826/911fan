package com.imfan.j.a91fan.chatroom.viewprovider;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.imfan.j.a91fan.R;
import com.imfan.j.a91fan.textabout.item.TextItem;
import com.imfan.j.a91fan.textabout.item.TextItemViewProvider;
import com.imfan.j.a91fan.util.Preferences;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by J on 2017/3/14 0014.
 */

public class MessageItemViewProvider
        extends ItemViewProvider<EMMessage, MessageItemViewProvider.ViewHolder> {

    @NonNull
    @Override
    protected MessageItemViewProvider.ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_emmessage, parent, false);
        return new MessageItemViewProvider.ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull EMMessage textItem) {
        holder.text_message.setText(((EMTextMessageBody)textItem.getBody()).getMessage());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_message)
        TextView text_message;


        ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}


