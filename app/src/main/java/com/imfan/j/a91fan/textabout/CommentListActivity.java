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
import android.support.v7.widget.LinearLayoutManager;

import com.imfan.j.a91fan.R;
import com.imfan.j.a91fan.myserver.CommentOfServer;
import com.imfan.j.a91fan.myserver.User;
import com.imfan.j.a91fan.retrofit.RetrofitServiceInstance;
import com.imfan.j.a91fan.textabout.item.CommentItem;
import com.imfan.j.a91fan.textabout.item.CommentItemViewProvider;
import com.imfan.j.a91fan.util.CustomToast;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.common.util.sys.NetworkUtil;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CommentListActivity extends SwipeBackActivity {
    // 实现一个比较器
    Comparator<Object> comparator = new Comparator<Object>() {
        public int compare(Object blog1, Object blog2) {
            if (((CommentItem) blog1).getTime() < ((CommentItem) blog2).getTime()) {
                return 1;
            } else if (((CommentItem) blog1).getTime() == ((CommentItem) blog2).getTime()) {
                return 0;
            } else {
                return -1;
            }
        }
    };

    @BindView(R.id.comment_list)
    ScrollRecyclerView comment_list;
    @BindView(R.id.pull_to_refresh_comment)
    PullToRefreshView pullToRefreshComment;

    private Items items;
    private MultiTypeAdapter adapter;
    private long blogID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_list);
        ButterKnife.bind(this);
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
                        LogUtil.i("连接服务器成功", "和服务器连接成功");
                    }

                    @Override
                    public void onError(Throwable e) { // 失败时回调

                        CustomToast.show(CommentListActivity.this, "服务器出现错误");
                        LogUtil.e("错误", "服务器错误");
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(final CommentOfServer commentOfServer) { // 成功时回调,这就是我们想要的;
                        if (commentOfServer.getStatus() == 200) {
                            LogUtil.i("成功", "成功获取某个blog的所有评论:" + commentOfServer.getMessage());
                            if (commentOfServer.getObject().size() == 0) {
                                CustomToast.show(CommentListActivity.this, "暂时还没有评论");
                            } else {
                                LogUtil.i("还是有评论的", "成功获取某该动态的评论:");
                                // 构造CommentItem
                                getComment(commentOfServer.getObject());
                            }
                        }
                    }
                });

    }

    private void initView() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("所有评论");
        ButterKnife.bind(this);

        blogID = getIntent().getLongExtra("blogID", 0);
        items = new Items();
        adapter = new MultiTypeAdapter(items);
        adapter.register(CommentItem.class, new CommentItemViewProvider(this));
        comment_list.setLayoutManager(new LinearLayoutManager(this));
        comment_list.setAdapter(adapter);
        pullToRefreshComment.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshComment();
                pullToRefreshComment.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pullToRefreshComment.setRefreshing(false);
                    }
                }, 200);

            }
        });

    }

    private void refreshComment() {
        LogUtil.i("自己的用来看评论的blogID", blogID + "");
        RetrofitServiceInstance.getInstance()
                .refreshCommentFromServer(blogID, ((CommentItem) items.get(0)).getTime())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .onBackpressureBuffer()
                .subscribe(new Subscriber<CommentOfServer>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.e("刷新评论成功", "有没有评论不可知啊");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e("刷新评论失败", "未知错误");
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(CommentOfServer commentOfServer) {
                        if (commentOfServer.getStatus() == 200) {
                            LogUtil.i("成功", "成功刷新某个blog的评论:" + commentOfServer.getMessage());
                            CustomToast.show(CommentListActivity.this, "有新的评论哦");
                            getComment(commentOfServer.getObject());
                        } else if (commentOfServer.getStatus() == 400) {
                            LogUtil.d("已经是最新评论了", "没人发评论了");
                            CustomToast.show(CommentListActivity.this, "这已经是最新了");
                        } else if (commentOfServer.getStatus() == 500) {
                            LogUtil.d("失败", "在服务器获取评论时出现错误");
                            CustomToast.show(CommentListActivity.this, "在服务器获取评论时出现错误");
                        }
                    }
                });
    }

    private void getComment(List<CommentOfServer.ObjectBean> object) {
        for (final CommentOfServer.ObjectBean o : object) {
            LogUtil.i("获取昵称时的ID", o.getFanId() + "");
            // 获取最新的发表评论的用户昵称。
            RetrofitServiceInstance.getInstance()
                    .getUserFromServerByFanID((long) o.getFanId())
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
                            if (user == null) {
                                LogUtil.e("错误", "没有成功获取到用户");
                            } else {
                                LogUtil.e("获取的User不为空", user.getStatus() + "");
                                if (user.getUser() == null) {
                                    LogUtil.e("获取的User.getUser()为空", "原来只是内部的真实user为空");
                                } else {
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
