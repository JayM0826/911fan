package com.imfan.j.a91fan.chatroom.model;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imfan.j.a91fan.R;

import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by jay on 17-2-25.
 */
public class ChatRoomItemViewProvider
        extends ItemViewProvider<ChatRoomItem, ChatRoomItemViewProvider.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_chat_room_item, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull ChatRoomItem chatRoomItem) {

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(View itemView) {
            super(itemView);
        }
    }
}