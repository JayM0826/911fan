package com.imfan.j.a91fan.chatroom.model;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.imfan.j.a91fan.R;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomInfo;

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
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull ChatRoomItem chatRoomInfo) {

        holder.tv_room_id.setText("1");
        holder.tv_room_name.setText("创建房间时写的名字");
        holder.tv_room_owner.setText("本大人的房间");
        holder.tv_room_online_number.setText("在线人数:100");

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        // @BindView(R.id.tv_room_id)
        TextView tv_room_id;

        // @BindView(R.id.tv_room_name)
        TextView tv_room_name;

        // @BindView(R.id.tv_room_owner)
        TextView tv_room_owner;

        // @BindView(R.id.tv_room_online_number)
        TextView tv_room_online_number;


        ViewHolder(View itemView) {
            super(itemView);
            tv_room_id = (TextView)itemView.findViewById(R.id.tv_room_id);
            tv_room_name = (TextView)itemView.findViewById(R.id.tv_room_name);
            tv_room_owner = (TextView)itemView.findViewById(R.id.tv_room_owner);
            tv_room_online_number = (TextView)itemView.findViewById(R.id.tv_room_online_number);
            // ButterKnife.bind(this, itemView);
        }
    }
}