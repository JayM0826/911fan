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
import android.os.Looper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.blankj.utilcode.utils.KeyboardUtils;
import com.imfan.j.a91fan.MainApplication;
import com.imfan.j.a91fan.R;
import com.imfan.j.a91fan.entity.Blog;
import com.imfan.j.a91fan.entity.BlogDao;
import com.imfan.j.a91fan.myserver.Common;
import com.imfan.j.a91fan.myserver.MyBlogOfServer;
import com.imfan.j.a91fan.retrofit.RetrofitServiceInstance;
import com.imfan.j.a91fan.textabout.item.BlogItem;
import com.imfan.j.a91fan.textabout.item.BlogItemViewProvider;
import com.imfan.j.a91fan.uiabout.CustomEditText;
import com.imfan.j.a91fan.util.CustomToast;
import com.imfan.j.a91fan.util.Preferences;
import com.netease.nim.uikit.common.util.log.LogUtil;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.blankj.utilcode.utils.StringUtils.isEmpty;

public class BlogListActivity extends AppCompatActivity {

    // 适配器
    public MultiTypeAdapter multiTypeAdapter;

    // 每一条内容
    Items items;


    ScrollRecyclerView blog_list;


    CustomEditText blog_new;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_list);
        blog_list = (ScrollRecyclerView) findViewById(R.id.list_blog);
        blog_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.i("列表的的点击事件", "键盘应该隐藏");
                KeyboardUtils.hideSoftInput(BlogListActivity.this);
            }
        });
        blog_new = (CustomEditText) findViewById(R.id.new_blog);
        initView();

    }


    static SweetAlertDialog sweetDialog;

    private void initView() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.blog_my);
        items = new Items();

        BlogDao blogDao = ((MainApplication) getApplication()).getDaoSession().getBlogDao();
        if (blogDao.loadAll().size() == 0) {
            sweetDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
                    .setTitleText("加载中").setContentText("Ooops,本地数据库没有动态，正在快马去服务器拉取");
            getMyBlogFromServer();

        } else {
            for (Blog blog : blogDao.loadAll()) {
                items.add(new BlogItem(blog.getContent(), blog.getCreateTime(), blog.getBlogID()));
            }
        }


        multiTypeAdapter = new MultiTypeAdapter(items);
        multiTypeAdapter.register(BlogItem.class, new BlogItemViewProvider(this));
        blog_list.setLayoutManager(new LinearLayoutManager(this));
        blog_list.setAdapter(multiTypeAdapter);
        blog_new.clearFocus();
        blog_new.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    KeyboardUtils.showSoftInput(blog_new);
                } else {
                    KeyboardUtils.hideSoftInput(BlogListActivity.this);
                }
            }
        });

        blog_list.setOnScrollCallback(new OnScrollCallback() {
            @Override
            public void onStateChanged(ScrollRecyclerView recycler, int state) {
                LogUtil.i("滑啊滑", "我的滑板鞋，根本停不下来");
                KeyboardUtils.hideSoftInput(BlogListActivity.this);
            }

            @Override
            public void onScrollUp(ScrollRecyclerView recycler, int dy) {
                blog_new.setVisibility(View.VISIBLE);
                KeyboardUtils.hideSoftInput(BlogListActivity.this);
            }

            @Override
            public void onScrollToBottom() {
                LogUtil.i("底部", "已经到了最底部了，不要再滑动了");
            }

            @Override
            public void onScrollDown(ScrollRecyclerView recycler, int dy) {
                blog_new.setVisibility(View.GONE);
                KeyboardUtils.hideSoftInput(BlogListActivity.this);
            }

            @Override
            public void onScrollToTop() {
                LogUtil.i("底部", "已经到了最顶部了，不要再滑动了");
            }
        });
    }

    /**
     * 从服务器获取自己的所有动态
     */
    private void getMyBlogFromServer() {

        Observable<MyBlogOfServer> observable = RetrofitServiceInstance.getInstance().getMyBlogFromServer(Preferences.getFanId());
        observable.subscribeOn(Schedulers.io())
                .onBackpressureBuffer()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MyBlogOfServer>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i("获取完毕", "从服务器获取自己的blog完成");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(MyBlogOfServer myBlogOfServer) {
                        if (myBlogOfServer.getStatus() == 200) {
                            if (sweetDialog != null) {
                                sweetDialog.dismiss();
                            }
                            if (myBlogOfServer.getObject().size() == 0) {
                                CustomToast.show(BlogListActivity.this, "服务器里也没有欸");
                            } else {
                                for (MyBlogOfServer.ObjectBean o : myBlogOfServer.getObject()) {
                                    items.add(new BlogItem(o.getContent(), o.getUpdateTime(), o.getId()));
                                    addBlogToLocalDB(o.getContent(), o.getUpdateTime(), o.getId());
                                }
                                multiTypeAdapter.notifyDataSetChanged();
                            }
                        } else {
                            CustomToast.show(BlogListActivity.this, "获取失败");
                        }
                    }
                });


    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflate = getMenuInflater();
        inflate.inflate(R.menu.activity_blog_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final String text = blog_new.getText().toString();
        if (isEmpty(text)) {
            CustomToast.show(this, "请填写动态内容");
            blog_new.requestFocus();
        } else {
            KeyboardUtils.hideSoftInput(this);
            blog_new.clearFocus();
            blog_new.setText("");
            final long time = System.currentTimeMillis();

            Observable<Common> observable = RetrofitServiceInstance.getInstance().createBlog(Preferences.getFanId(), text, time);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .onBackpressureBuffer()
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<Common>() {
                        int blogID; // 服务器的blogid

                        @Override
                        public void onCompleted() {
                            // 从服务器那里获取了blogID后，再加入到本地数据库
                            items.add(new BlogItem(text, time, blogID));
                            multiTypeAdapter.notifyDataSetChanged();
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

        return super.onOptionsItemSelected(item);
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
