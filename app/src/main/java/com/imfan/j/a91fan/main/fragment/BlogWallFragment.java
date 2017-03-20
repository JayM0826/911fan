/*
 *
 *  * Created by J on  2017.
 *  * Copyright (c) 2017.  All rights reserved.
 *  *
 *  * Last modified 17-3-13 上午11:12
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

package com.imfan.j.a91fan.main.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.KeyboardUtils;
import com.imfan.j.a91fan.MainApplication;
import com.imfan.j.a91fan.R;
import com.imfan.j.a91fan.entity.Blog;
import com.imfan.j.a91fan.entity.BlogDao;
import com.imfan.j.a91fan.myserver.BlogOfServer;
import com.imfan.j.a91fan.myserver.Common;
import com.imfan.j.a91fan.myserver.User;
import com.imfan.j.a91fan.retrofit.RetrofitServiceInstance;
import com.imfan.j.a91fan.textabout.OnScrollCallback;
import com.imfan.j.a91fan.textabout.ScrollRecyclerView;
import com.imfan.j.a91fan.textabout.item.BlogItem;
import com.imfan.j.a91fan.textabout.item.BlogItemViewProvider;
import com.imfan.j.a91fan.util.CustomToast;
import com.imfan.j.a91fan.util.Preferences;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.common.util.sys.NetworkUtil;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.Collections;
import java.util.Comparator;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.blankj.utilcode.utils.StringUtils.isEmpty;

/**
 * Created by jay on 17-2-6.
 */

public class BlogWallFragment extends MainFragment {

    private PullToRefreshView pullToRefreshBlog;

    private ScrollRecyclerView listBlog;

    private EditText newBlog;

    private LinearLayout layout_blog;

    private TextView postBlog;

    public MultiTypeAdapter adapter;
    public Items items;
    static SweetAlertDialog sweetDialog;

    // 实现一个比较器，用于排序blog
    Comparator<Object> comparator = new Comparator<Object>(){
        public int compare(Object blog1, Object blog2) {
            if (((BlogItem)blog1).getTime() <((BlogItem)blog2).getTime()){
                return 1;
            }else if (((BlogItem)blog1).getTime() == ((BlogItem)blog2).getTime()){
                return 0;
            }else {
                return -1;
            }
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onCurrent();
    }

    @Override
    protected void onInit() {
        initView();
        initData();
    }

    /**
     * 从服务器加载数据,要逆序加载，即最新时间-最旧时间
     */
    private void initData() {
        if (!NetworkUtil.isNetAvailable(getActivity())) {
            CustomToast.show(getActivity(), R.string.network_is_not_available);
            return;
        }
        // 获取服务器所有的动态
        RetrofitServiceInstance.getInstance().getAllBlogFromServer()
                .onBackpressureBuffer()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<BlogOfServer>() {
                    @Override
                    public void onCompleted() {
                        if (sweetDialog != null) {
                            sweetDialog.dismiss();
                        }
                        LogUtil.i("BlogWall", "从服务器获取所有人的Blog完成");
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (sweetDialog != null) {
                            sweetDialog.dismiss();
                        }
                        e.printStackTrace();
                        LogUtil.i("BlogWall", "从服务器获取Blog时出现错误");
                        CustomToast.show(getActivity(), "从服务器获取Blog时出现错误");
                    }

                    @Override
                    public void onNext(BlogOfServer blogOfServer) {
                        if (sweetDialog != null) {
                            sweetDialog.dismiss();
                        }
                        if (blogOfServer.getStatus() == 200) {
                            if (blogOfServer.getObject().size() == 0) {
                                CustomToast.show(getActivity(), "服务器里一条动态都没有，你先吃螃蟹吧");
                            } else {
                                for (BlogOfServer.ObjectBean o : blogOfServer.getObject()) {
                                    getNicknameOfEveryBlog(o);
                                }
                            }
                        } else {
                            CustomToast.show(getActivity(), "获取失败");
                        }
                    }
                });

    }

    private void getNicknameOfEveryBlog(final BlogOfServer.ObjectBean o) {

        LogUtil.i("获取昵称时的ID", o.getFanId() + "");
        if (!NetworkUtil.isNetAvailable(getActivity())) {
            CustomToast.show(getActivity(), R.string.network_is_not_available);
            return;
        }
        // 获取最新的发表评论的用户昵称。
        RetrofitServiceInstance.getInstance()
                .getUserFromServerByFanID((long) o.getFanId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<User>() {
                    @Override
                    public void onCompleted() {
                        // 无论成功与失败，这里都会调用，只要onnext执行了，这里就一定会执行
                        // LogUtil.e("获取Id的昵称成功：", o.getFanId() + "");
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
                                LogUtil.e("获取的UsergetUser()为空", "原来只是内部的真实user为空");
                            } else {
                                items.add(new BlogItem(o.getContent(), o.getUpdateTime(), o.getId(), user.getUser().getNickname(), user.getUser().getWxUnion().toLowerCase()));
                                Collections.sort(items, comparator);
                                adapter.notifyDataSetChanged();
                            }

                        }

                    }
                });

    }

    private void initView() {
        pullToRefreshBlog = (PullToRefreshView)getView().findViewById(R.id.pull_to_refresh_blog);
        layout_blog = (LinearLayout) getView().findViewById(R.id.layout_blog);
        listBlog = (ScrollRecyclerView) getView().findViewById(R.id.list_blog);
        newBlog = (EditText) getView().findViewById(R.id.edit_blog);
        postBlog = (TextView) getView().findViewById(R.id.post_blog);


        pullToRefreshBlog.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullToRefreshBlog.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pullToRefreshBlog.setRefreshing(false);
                        CustomToast.show(getContext(), "刷新了一下，看看刷出什么了");
                        // 开始刷新动态，从已有动态基础上添加，而不是再次去服务器去获取全部数据
                        if (sweetDialog == null){
                            sweetDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE)
                                    .setTitleText("加载中").setContentText("正在快马去服务器拉取动态");
                        }
                        refreshBlog();
                    }
                }, 300);
            }
        });
        newBlog.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                LogUtil.i("文字变化前方法", s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                LogUtil.i("文字变化中方法", s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    postBlog.setVisibility(View.VISIBLE);
                } else {
                    postBlog.setVisibility(View.GONE);
                }
            }
        });
        items = new Items();
        newBlog.clearFocus();
        adapter = new MultiTypeAdapter(items);
        adapter.register(BlogItem.class, new BlogItemViewProvider(getActivity()));

        listBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.i("列表的的点击事件", "键盘应该隐藏");
                KeyboardUtils.hideSoftInput(getActivity());
            }
        });

        listBlog.setLayoutManager(new LinearLayoutManager(getActivity()));
        listBlog.setAdapter(adapter);

        listBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d("点击了列表", "这时候键盘应该隐藏起来");
                newBlog.clearFocus();
                KeyboardUtils.hideSoftInput(getActivity());
            }
        });
        listBlog.setOnScrollCallback(new OnScrollCallback() {
            @Override
            public void onStateChanged(ScrollRecyclerView recycler, int state) {
                LogUtil.i("滑啊滑", "我的滑板鞋，根本停不下来");
                KeyboardUtils.hideSoftInput(getActivity());
                newBlog.clearFocus();
            }

            @Override
            public void onScrollUp(ScrollRecyclerView recycler, int dy) {
                layout_blog.setVisibility(View.VISIBLE);
                KeyboardUtils.hideSoftInput(getActivity());
                newBlog.clearFocus();
            }

            @Override
            public void onScrollToBottom() {
                newBlog.clearFocus();
                layout_blog.setVisibility(View.VISIBLE);
                KeyboardUtils.hideSoftInput(getActivity());
                LogUtil.i("底部", "已经到了最底部了，不要再滑动了");
            }

            @Override
            public void onScrollDown(ScrollRecyclerView recycler, int dy) {

                layout_blog.setVisibility(View.GONE);
                KeyboardUtils.hideSoftInput(getActivity());
                newBlog.clearFocus();
            }

            @Override
            public void onScrollToTop() {
                layout_blog.setVisibility(View.VISIBLE);
                newBlog.clearFocus();
                KeyboardUtils.hideSoftInput(getActivity());
                LogUtil.i("底部", "已经到了最顶部了，不要再滑动了");
            }
        });

        postBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String text = newBlog.getText().toString();
                if (isEmpty(text)) {
                    CustomToast.show(getActivity(), "请填写动态内容");
                    newBlog.requestFocus();
                } else {
                    newBlog.clearFocus();
                    final long time = System.currentTimeMillis();
                    KeyboardUtils.hideSoftInput(getActivity());
                    Observable<Common> observable = RetrofitServiceInstance.getInstance().createBlog(Preferences.getFanId(), text, time);
                    observable.observeOn(AndroidSchedulers.mainThread())
                            .onBackpressureBuffer()
                            .subscribeOn(Schedulers.io())
                            .subscribe(new Subscriber<Common>() {
                                int blogID; // 服务器的blogid

                                @Override
                                public void onCompleted() {
                                    // 从服务器那里获取了blogID后，再加入到本地数据库
                                    items.add(new BlogItem(text, time, blogID, Preferences.getWxNickname(), Preferences.getUserAccount()));
                                    Collections.sort(items, comparator);
                                    adapter.notifyDataSetChanged();
                                    newBlog.setText(""); // 成功了才清空，不成功还可以继续发，这时候就不用重新打了
                                    addBlogToLocalDB(text, time, blogID); // 自己发表的加入到本地数据库
                                }

                                @Override
                                public void onError(Throwable e) {
                                    LogUtil.e("postBlog.setOnClickListener", "发表动态出现错误");
                                    e.printStackTrace();
                                }

                                @Override
                                public void onNext(Common common) {
                                    if (common.getStatus() == 200) {
                                        CustomToast.show(getActivity(), "发表成功");
                                        blogID = common.getServerBlogId();
                                    } else {
                                        CustomToast.show(getActivity(), "发表失败");
                                    }
                                }
                            });
                }
            }
        });
    }

    private void refreshBlog() {
        newBlog.clearFocus();
        KeyboardUtils.hideSoftInput(getActivity());
        if (items.size() == 0){
            initData();
        }else{
            RetrofitServiceInstance.getInstance()
                    .refreshBlog(((BlogItem)items.get(0)).getTime())
                    .onBackpressureBuffer()
                    .observeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<BlogOfServer>() {
                        @Override
                        public void onCompleted() {
                            LogUtil.i("refreshBlog()中", "刷新完了，不知道有没有数据");
                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtil.e("refreshBlog()中", "刷新动态出现错误");
                            e.printStackTrace();

                        }

                        @Override
                        public void onNext(BlogOfServer blogOfServer) {
                            if (sweetDialog != null) {
                                sweetDialog.dismiss();
                            }
                            if (blogOfServer.getStatus() == 200) {
                                if (blogOfServer.getObject().size() == 0) {
                                    CustomToast.show(getActivity(), "没有更多的动态了");
                                } else {
                                    for (BlogOfServer.ObjectBean o : blogOfServer.getObject()) {
                                        getNicknameOfEveryBlog(o);
                                    }
                                }
                            } else {
                                CustomToast.show(getActivity(), "获取失败");
                            }
                        }
                    });
        }

    }

    /**
     * 将自己的动态保存到本地数据库
     *
     * @param text   动态的内容
     * @param time   动态发表的时间
     * @param blogID 服务器上面的blogID
     */
    private final void addBlogToLocalDB(final String text, final long time, final int blogID) {
        Observable.create(new Observable.OnSubscribe<Blog>() {
            @Override
            public void call(Subscriber<? super Blog> subscriber) {
                try {
                    Blog blog = new Blog(null, text, time, blogID);
                    subscriber.onNext(blog);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io())//生产事件在io
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Blog>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i("保存到本地数据库完成", "动态发表后先上传到服务器成功后又保存到了本地数据库");
                    }

                    @Override
                    public void onError(Throwable e) {
                        CustomToast.show(getActivity(), "动态保存时出现错误");
                    }

                    @Override
                    public void onNext(Blog blog) {
                        BlogDao blogDao = ((MainApplication) getActivity().getApplication()).getDaoSession().getBlogDao();
                        blogDao.save(blog);
                    }
                });
    }


}
