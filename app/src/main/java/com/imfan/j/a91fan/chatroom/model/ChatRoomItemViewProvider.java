package com.imfan.j.a91fan.chatroom.model;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMChatRoom;
import com.imfan.j.a91fan.R;
import com.imfan.j.a91fan.chatroom.ChatRoomActivity;
import com.imfan.j.a91fan.util.Preferences;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by jay on 17-2-25.
 */
public class ChatRoomItemViewProvider
        extends ItemViewProvider<EMChatRoom, ChatRoomItemViewProvider.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_chat_room_item, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull EMChatRoom chatRoomInfo) {

        holder.tv_room_name.setText(chatRoomInfo.getName());
        holder.tv_room_owner.setText(Preferences.getWxNickname());
        holder.tv_room_online_number.setText("在线人数:" + chatRoomInfo.getMemberList().size() + "");
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_room_cover)
        ImageView tv_room_cover;

        @BindView(R.id.tv_room_name)
        TextView tv_room_name;

        @BindView(R.id.tv_room_owner)
        TextView tv_room_owner;

        @BindView(R.id.tv_room_online_number)
        TextView tv_room_online_number;


        ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}