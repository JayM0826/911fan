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
import com.imfan.j.a91fan.textabout.CommentListActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by J on 2017/3/20 0020.
 */
public class UserBlogItemViewProvider
        extends ItemViewProvider<UserBlogItem, UserBlogItemViewProvider.ViewHolder> {
// item_user_blog_item
static public Context context;

    public UserBlogItemViewProvider() {
    }

    public UserBlogItemViewProvider(final Context context) {
        UserBlogItemViewProvider.context = context;
    }
    @NonNull
    @Override
    protected UserBlogItemViewProvider.ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_blog_item, parent, false);
        return new UserBlogItemViewProvider.ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull UserBlogItemViewProvider.ViewHolder holder, @NonNull UserBlogItem blogItem) {
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
            Intent intent = new Intent(context, CommentListActivity.class);
            intent.putExtra("blogID", blogID);
            context.startActivity(intent);
        }

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}