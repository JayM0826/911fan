/*
 *
 *  * Created by J on  2017.
 *  * Copyright (c) 2017.  All rights reserved.
 *  *
 *  * Last modified 17-3-19 上午10:39
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

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.imfan.j.a91fan.retrofit.RetrofitServiceInstance;
import com.imfan.j.a91fan.textabout.item.BlogItem;
import com.imfan.j.a91fan.textabout.item.BlogItemViewProvider;
import com.imfan.j.a91fan.util.CustomToast;
import com.imfan.j.a91fan.util.Preferences;
import com.netease.nim.uikit.common.util.log.LogUtil;

import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.blankj.utilcode.utils.StringUtils.isEmpty;

public class BlogListActivity extends SwipeBackActivity {

    // 实现一个比较器
    Comparator<Object> comparator = new Comparator<Object>() {
        public int compare(Object blog1, Object blog2) {
            if (((BlogItem) blog1).getTime() < ((BlogItem) blog2).getTime()) {
                return 1;
            } else if (((BlogItem) blog1).getTime() == ((BlogItem) blog2).getTime()) {
                return 0;
            } else {
                return -1;
            }
        }
    };

    // 适配器
    public MultiTypeAdapter multiTypeAdapter;

    // 每一条内容
    Items items;
    @BindView(R.id.list_blog)
    ScrollRecyclerView blog_list;

    @BindView(R.id.new_blog)
    EditText newBlog;

    @BindView(R.id.post_blog)
    TextView blog_new;

    @BindView(R.id.layout_blog)
    LinearLayout layoutBlog;

    @OnClick(R.id.post_blog)
    void postBlog() {
        doPostBlog();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_list);
        ButterKnife.bind(this);

        initView();

    }


    static SweetAlertDialog sweetDialog;

    private void initView() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.blog_my);
        items = new Items();

        BlogDao blogDao = ((MainApplication) getApplication()).getDaoSession().getBlogDao();
        if (blogDao.loadAll().size() == 0) { // 本地数据库无动态情况下，可能是没有动态，也可能是重新安装了,这时候去服务器拉取
            sweetDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
                    .setTitleText("加载中").setContentText("Ooops,本地数据库没有动态，正在快马去服务器拉取");
            sweetDialog.show();
            getMyBlogFromServer();

        } else { // 直接加载本地的东西，本地和服务器是同步的
            for (Blog blog : blogDao.loadAll()) {
                items.add(new BlogItem(blog.getContent(), blog.getCreateTime(), blog.getBlogID(), Preferences.getWxNickname(), Preferences.getUserAccount()));
            }
            Collections.sort(items, comparator);
        }


        multiTypeAdapter = new MultiTypeAdapter(items);
        multiTypeAdapter.register(BlogItem.class, new BlogItemViewProvider(this));
        blog_list.setLayoutManager(new LinearLayoutManager(this));
        blog_list.setAdapter(multiTypeAdapter);
        newBlog.clearFocus();
        newBlog.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    KeyboardUtils.showSoftInput(newBlog);
                } else {
                    KeyboardUtils.hideSoftInput(BlogListActivity.this);
                }
            }
        });

        blog_list.setOnScrollCallback(new OnScrollCallback() {
            @Override
            public void onStateChanged(ScrollRecyclerView recycler, int state) {
                LogUtil.i("滑啊滑", "我的滑板鞋，根本停不下来");
                // KeyboardUtils.hideSoftInput(BlogListActivity.this);
            }

            @Override
            public void onScrollUp(ScrollRecyclerView recycler, int dy) {
                layoutBlog.setVisibility(View.VISIBLE);
                // KeyboardUtils.hideSoftInput(BlogListActivity.this);
            }

            @Override
            public void onScrollToBottom() {
                // CustomToast.show(BlogListActivity.this, "谁都有底线，我也不例外");

                layoutBlog.setVisibility(View.VISIBLE);
                LogUtil.i("底部", "已经到了最底部了，不要再滑动了");
            }

            @Override
            public void onScrollDown(ScrollRecyclerView recycler, int dy) {
                layoutBlog.setVisibility(View.GONE);
                KeyboardUtils.hideSoftInput(BlogListActivity.this);
            }

            @Override
            public void onScrollToTop() {
                layoutBlog.setVisibility(View.VISIBLE);
                LogUtil.i("底部", "已经到了最顶部了，不要再滑动了");
            }
        });
    }

    /**
     * 从服务器获取自己的所有动态
     */
    private void getMyBlogFromServer() {

        Observable<BlogOfServer> observable = RetrofitServiceInstance.getInstance().getMyBlogFromServer(Preferences.getFanId());
        observable.subscribeOn(Schedulers.io())
                .onBackpressureBuffer()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BlogOfServer>() {
                    @Override
                    public void onCompleted() {
                        if (sweetDialog != null) {
                            sweetDialog.dismiss();
                        }
                        LogUtil.i("获取完毕", "从服务器获取自己的blog完成");
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (sweetDialog != null) {
                            sweetDialog.dismiss();
                        }
                        e.printStackTrace();
                        CustomToast.show(BlogListActivity.this, "获取自己的Blog时出现错误");
                    }

                    @Override
                    public void onNext(BlogOfServer blogOfServer) {
                        if (blogOfServer.getStatus() == 200) {
                            if (sweetDialog != null) {
                                sweetDialog.dismiss();
                            }
                            if (blogOfServer.getObject().size() == 0) {
                                CustomToast.show(BlogListActivity.this, "服务器里也没有欸");
                            } else {
                                for (BlogOfServer.ObjectBean o : blogOfServer.getObject()) {
                                    items.add(new BlogItem(o.getContent(), o.getUpdateTime(), o.getId(), Preferences.getWxNickname(), Preferences.getUserAccount()));
                                    addBlogToLocalDB(o.getContent(), o.getUpdateTime(), o.getId());
                                }
                                Collections.sort(items, comparator);
                                multiTypeAdapter.notifyDataSetChanged();
                            }
                        } else {
                            CustomToast.show(BlogListActivity.this, "获取失败");
                        }
                    }
                });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflate = getMenuInflater();
        inflate.inflate(R.menu.activity_blog_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        doPostBlog();
        return super.onOptionsItemSelected(item);
    }

    private void doPostBlog() {
        final String text = newBlog.getText().toString();
        if (isEmpty(text)) {
            CustomToast.show(this, "请填写动态内容");
            newBlog.requestFocus();
        } else {

            newBlog.clearFocus();


            final long time = System.currentTimeMillis();
            KeyboardUtils.hideSoftInput(this);
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
                            multiTypeAdapter.notifyDataSetChanged();
                            newBlog.setText(""); // 成功了才清空，不成功还可以继续发，这时候就不用重新打了
                            addBlogToLocalDB(text, time, blogID);
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(Common common) {
                            if (common.getStatus() == 200) {
                                CustomToast.show(BlogListActivity.this, "发表成功");
                                blogID = common.getServerBlogId();
                            } else {
                                CustomToast.show(BlogListActivity.this, "发表失败");
                            }
                        }
                    });
        }
    }

    /**
     * 将一条blog加入到本地数据库中去
     *
     * @param text   动态的详细内容
     * @param time   创建时间
     * @param blogID 服务器的blogid，不是本地的id，也不是fanid
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
                        CustomToast.show(BlogListActivity.this, "动态保存时出现错误");
                    }

                    @Override
                    public void onNext(Blog blog) {
                        BlogDao blogDao = ((MainApplication) getApplication()).getDaoSession().getBlogDao();
                        blogDao.save(blog);
                    }
                });
    }


}
