package com.imfan.j.a91fan.textabout.item;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.imfan.j.a91fan.R;
import com.imfan.j.a91fan.textabout.ArticleListActivity;
import com.imfan.j.a91fan.util.Preferences;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by jay on 17-3-5.
 */
public class GroupItemViewProvider
        extends ItemViewProvider<GroupItem, GroupItemViewProvider.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_group_item, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull GroupItem groupItem) {
        holder.tv_group_name.setText(groupItem.groupName);
        holder.tv_text_owner.setText(Preferences.getWxNickname());
        holder.tv_text_number.setText(groupItem.number + "篇");
        holder.tv_create_time.setText(groupItem.createTime);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_group_cover)
        ImageView iv_group_cover;

        @BindView(R.id.tv_group_name)
        TextView tv_group_name;

        @BindView(R.id.tv_text_number)
        TextView tv_text_number;

        @BindView(R.id.tv_text_owner)
        TextView tv_text_owner;

        @BindView(R.id.group_time)
        TextView tv_create_time;




        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}