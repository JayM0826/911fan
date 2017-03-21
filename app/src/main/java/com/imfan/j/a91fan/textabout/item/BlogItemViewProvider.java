/*
 *
 *  * Created by J on  2017.
 *  * Copyright (c) 2017.  All rights reserved.
 *  *
 *  * Last modified 17-3-19 上午7:39
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
import android.content.Intent;
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
import com.imfan.j.a91fan.textabout.BlogListActivity;
import com.imfan.j.a91fan.textabout.CommentListActivity;
import com.imfan.j.a91fan.textabout.UserBlogListActivity;
import com.imfan.j.a91fan.textabout.UserCommentListActivity;
import com.imfan.j.a91fan.util.CustomToast;
import com.imfan.j.a91fan.util.Preferences;
import com.netease.nim.uikit.cache.NimUserInfoCache;
import com.netease.nim.uikit.common.util.sys.TimeUtil;
import com.netease.nim.uikit.contact.core.item.ContactItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by J on 2017/3/19 0019.
 */
public class BlogItemViewProvider
        extends ItemViewProvider<BlogItem, BlogItemViewProvider.ViewHolder> {

    static private Context context;

    public BlogItemViewProvider() {
    }

    public BlogItemViewProvider(final Context context) {
        BlogItemViewProvider.context = context;
        }
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_blog_item, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull BlogItem blogItem) {
        holder.blog_content.setText(blogItem.getContent());
        holder.time.setText(TimeUtils.millis2String(blogItem.getTime()));
        holder.nickname.setText(blogItem.getNickname());
        holder.blogID = (long)blogItem.getServerBlogID();
        holder.account = blogItem.getWxunion();
    }

      /*一般非静态外部类可以随意访问其外部类的成员变量以及方法（包括声明为private的方法），
    但是如果一个内部类被声明为static，则其在访问包括自身的外部类会有诸多的限制。
    静态内部类不能访问其外部类的非静态成员变量和方法*/

    static class ViewHolder extends RecyclerView.ViewHolder {

        long blogID;
        String account;

        @BindView(R.id.layout_avatar_nick)
        RelativeLayout layout_avatar_nick;

        @OnClick(R.id.layout_avatar_nick)
        void goToUserDetail(){
            UserProfileActivity.start(context, account);
        }

        @BindView(R.id.func_up)
        ImageView func_up;

        @BindView(R.id.func_comment)
        ImageView func_comment;


        @BindView(R.id.blog_content)
        TextView blog_content;

        @BindView(R.id.headimage)
        ImageView head_image;

        @BindView(R.id.nickname)
        TextView nickname;

        @BindView(R.id.time)
        TextView time;

        @OnClick(R.id.func_up)
        void upAction(){
            func_up.setEnabled(false);
        }

        @OnClick(R.id.func_comment)
        void createComment(){
            Intent intent;
            // 是自己的资料的时候
            if (account.toLowerCase().equals(Preferences.getUserAccount().toLowerCase())){
                intent = new Intent(context, CommentListActivity.class);
            }else{ // 是其他用户的时候
                intent = new Intent(context, UserCommentListActivity.class);
            }
            intent.putExtra("blogID", blogID);
            context.startActivity(intent);
        }

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}