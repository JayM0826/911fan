/*
 *
 *  * Created by J on  2017.
 *  * Copyright (c) 2017.  All rights reserved.
 *  *
 *  * Last modified 17-3-19 下午9:38
 *  *
 *  * Project name: 911fan
 *  *
 *  * Contact me:
 *  * WeChat:  worromoT_
 *  * Email: 2212131349@qq.com
 *  *
 *  * Warning:If my code is same as yours, then i copy you!
 *
 */

package com.imfan.j.a91fan.textabout.item;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.TimeUtils;
import com.imfan.j.a91fan.R;
import com.imfan.j.a91fan.contact.activity.UserProfileActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by J on 2017/3/19 0019.
 */
public class CommentItemViewProvider
        extends ItemViewProvider<CommentItem, CommentItemViewProvider.ViewHolder> {
    static private Context context;



    public CommentItemViewProvider() {
    }

    public CommentItemViewProvider(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_comment_item, parent, false);
        return new ViewHolder(root);
    }



    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull CommentItem commentItem) {
        holder.comment_content.setText(commentItem.getContent());
        // holder.head_image.setImageDrawable(); // 暂时就用默认的系统图像
        // 昵称还需要在构造CommentItem时临时去服务器取最新的来
        holder.nickname.setText(commentItem.getNickname());
        holder.time.setText(TimeUtils.millis2String(commentItem.getTime()));
        holder.account = commentItem.getWxunion().toLowerCase();

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        String account;
        @BindView(R.id.layout_avatar_nick)
        RelativeLayout layout_avatar_nick;

        @OnClick(R.id.layout_avatar_nick)
        void goToUserDetail(){
            UserProfileActivity.start(context, account);
        }

        @BindView(R.id.comment_content)
        TextView comment_content;

        @BindView(R.id.headimage)
        ImageView head_image;

        @BindView(R.id.nickname)
        TextView nickname;

        @BindView(R.id.time)
        TextView time;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}