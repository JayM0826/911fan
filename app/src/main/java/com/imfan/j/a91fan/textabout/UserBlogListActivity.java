/*
 *
 *  * Created by J on  2017.
 *  * Copyright (c) 2017.  All rights reserved.
 *  *
 *  * Last modified 17-3-20 下午8:40
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
import com.imfan.j.a91fan.myserver.BlogOfServer;
import com.imfan.j.a91fan.retrofit.RetrofitServiceInstance;
import com.imfan.j.a91fan.textabout.item.UserBlogItem;
import com.imfan.j.a91fan.textabout.item.UserBlogItemViewProvider;
import com.imfan.j.a91fan.util.CustomToast;
import com.imfan.j.a91fan.util.Preferences;
import com.netease.nim.uikit.common.util.log.LogUtil;

import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class UserBlogListActivity extends SwipeBackActivity {
    // 实现一个比较器
    Comparator<Object> comparator = new Comparator<Object>() {
        public int compare(Object blog1, Object blog2) {
            if (((UserBlogItem) blog1).getTime() < ((UserBlogItem) blog2).getTime()) {
                return 1;
            } else if (((UserBlogItem) blog1).getTime() == ((UserBlogItem) blog2).getTime()) {
                return 0;
            } else {
                return -1;
            }
        }
    };

    @BindView(R.id.list_blog)
    ScrollRecyclerView listBlog;

    private String account;

    public Items items;

    private String nickname;

    public MultiTypeAdapter multiTypeAdapter;


    static SweetAlertDialog sweetDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_blog_list);
        ButterKnife.bind(this);
        account = getIntent().getStringExtra("account");
        nickname = getIntent().getStringExtra("nickname");
        initView();
        initData();
    }

    private void initData() {
        sweetDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("加载中").setContentText("疯狂去服务器拉取,等着哈");
        sweetDialog.show();
        getUserBlogFromServer();
    }

    private void getUserBlogFromServer() {
        Observable<BlogOfServer> observable = RetrofitServiceInstance.getInstance().getBlogByWxunion(account);
        observable.subscribeOn(Schedulers.io())
                .onBackpressureBuffer()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BlogOfServer>() {
                    @Override
                    public void onCompleted() {
                        if (sweetDialog != null) {
                            sweetDialog.dismiss();
                        }
                        LogUtil.i("UserBlogListActivity获取完毕", "从服务器获取用户的blog完成");
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (sweetDialog != null) {
                            sweetDialog.dismiss();
                        }
                        e.printStackTrace();
                        CustomToast.show(UserBlogListActivity.this, "获取用户的Blog时出现错误");
                    }

                    @Override
                    public void onNext(BlogOfServer blogOfServer) {
                        if (sweetDialog != null) {
                            sweetDialog.dismiss();
                        }
                        if (blogOfServer.getStatus() == 200) {

                            if (blogOfServer.getObject().size() == 0) {
                                CustomToast.show(UserBlogListActivity.this, "空空如也,走吧");
                            } else {
                                for (BlogOfServer.ObjectBean o : blogOfServer.getObject()) {
                                    items.add(new UserBlogItem(o.getContent(), o.getUpdateTime(), o.getId(), Preferences.getWxNickname(), Preferences.getUserAccount()));
                                }
                                Collections.sort(items, comparator);
                                multiTypeAdapter.notifyDataSetChanged();
                            }
                        } else {
                            CustomToast.show(UserBlogListActivity.this, "获取失败");
                        }
                    }
                });
    }

    private void initView() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(nickname + "的全部动态");

        items = new Items();
        multiTypeAdapter = new MultiTypeAdapter(items);
        multiTypeAdapter.register(UserBlogItem.class, new UserBlogItemViewProvider(this));
        listBlog.setLayoutManager(new LinearLayoutManager(this));
        listBlog.setAdapter(multiTypeAdapter);
    }
}
