/*
 *
 *  * Created by J on  2017.
 *  * Copyright (c) 2017.  All rights reserved.
 *  *
 *  * Last modified 17-3-19 下午9:17
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

package com.imfan.j.a91fan.textabout;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.imfan.j.a91fan.R;
import com.imfan.j.a91fan.myserver.CommentOfServer;
import com.imfan.j.a91fan.myserver.User;
import com.imfan.j.a91fan.retrofit.RetrofitServiceInstance;
import com.imfan.j.a91fan.textabout.item.BlogItem;
import com.imfan.j.a91fan.textabout.item.CommentItem;
import com.imfan.j.a91fan.textabout.item.CommentItemViewProvider;
import com.imfan.j.a91fan.util.CustomToast;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.common.util.sys.NetworkUtil;

import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CommentListActivity extends AppCompatActivity {
    // 实现一个比较器
    Comparator<Object> comparator = new Comparator<Object>(){
        public int compare(Object blog1, Object blog2) {
            if (((CommentItem)blog1).getTime() <((CommentItem)blog2).getTime()){
                return 1;
            }else if (((CommentItem)blog1).getTime() == ((CommentItem)blog2).getTime()){
                return 0;
            }else {
                return -1;
            }
        }
    };

    @BindView(R.id.comment_list)
    RecyclerView comment_list;

    private Items items;
    private MultiTypeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_list);
        initView();
        initData();
    }

    /**
     * 就是直接服务器请求数据，本地不进行缓存
     */
    private void initData() {
        if (!NetworkUtil.isNetAvailable(this)) {
            CustomToast.show(this, R.string.network_is_not_available);
            return;
        }

        RetrofitServiceInstance.getInstance().
                readCommentFromServer(getIntent().getLongExtra("blogID", 0))
                .observeOn(AndroidSchedulers.mainThread()) // 使后面的subscribe运行在主线程
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io()) // 是请求在子线程
                .subscribe(new Subscriber<CommentOfServer>() {
                    @Override
                    public void onCompleted() { // 请求网络完成后回调,成功就只回调一次,失败不回调
                    }

                    @Override
                    public void onError(Throwable e) { // 失败时回调

                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(final CommentOfServer commentOfServer) { // 成功时回调,这就是我们想要的;
                        if (commentOfServer.getStatus() == 200) {
                            LogUtil.i("成功", "成功获取某个blog的所有评论:" + commentOfServer.getMessage());
                            if (commentOfServer.getObject().size() == 0) {
                                CustomToast.show(CommentListActivity.this, "评论空空如也，别看了，还是回去吧");
                            } else {
                                LogUtil.i("还是有评论的", "成功获取某该动态的评论:");
                                // 构造CommentItem
                                for (final CommentOfServer.ObjectBean o : commentOfServer.getObject()){
                                    LogUtil.i("获取昵称时的ID", o.getFanId() + "");
                                    // 获取最新的发表评论的用户昵称。
                                    RetrofitServiceInstance.getInstance()
                                            .getUserFromServerByFanID((long)o.getFanId())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribeOn(Schedulers.io())
                                            .subscribe(new Subscriber<User>() {
                                                @Override
                                                public void onCompleted() {
                                                    // 无论成功与失败，这里都会调用，只要onnext执行了，这里就一定会执行
                                                    LogUtil.e("获取Id的昵称过程完成：", "完成仅代表通信成功，不代表正确获取");
                                                }

                                                @Override
                                                public void onError(Throwable e) {
                                                    LogUtil.e("获取Id的昵称时出现错误：", o.getFanId() + "");
                                                    e.printStackTrace();
                                                }

                                                @Override
                                                public void onNext(User user) {
                                                    if (user == null){
                                                        LogUtil.e("错误", "没有成功获取到用户");
                                                    }else{
                                                        LogUtil.e("获取的User不为空", user.getStatus() + "");
                                                        if (user.getUser() == null){
                                                            LogUtil.e("获取的UsergetUser()为空", "原来只是内部的真实user为空");
                                                        }else {
                                                            items.add(new CommentItem(o.getContent(), user.getUser().getNickname(), o.getUpdateTime(), user.getUser().getWxUnion().toLowerCase()));
                                                            Collections.sort(items, comparator);
                                                            adapter.notifyDataSetChanged();
                                                        }

                                                    }

                                                }
                                            });

                                }
                            }
                        }
                    }
                });

    }

    private void initView() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("所有评论");
        ButterKnife.bind(this);
        items = new Items();
        adapter = new MultiTypeAdapter(items);
        adapter.register(CommentItem.class, new CommentItemViewProvider());
        comment_list.setLayoutManager(new LinearLayoutManager(this));
        comment_list.setAdapter(adapter);

    }
}
