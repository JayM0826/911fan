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
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.imfan.j.a91fan.main.activity.MainActivity;
import com.imfan.j.a91fan.textabout.item.BlogItem;
import com.imfan.j.a91fan.textabout.item.BlogItemViewProvider;
import com.imfan.j.a91fan.util.CustomToast;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nimlib.sdk.Observer;



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


    EditText blog_new;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_list);
        blog_list = (ScrollRecyclerView) findViewById(R.id.list_blog);
        blog_new = (EditText) findViewById(R.id.new_blog);
        initView();
    }



    private void initView() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.blog_my);
        items = new Items();
        for (int i = 0; i < 10; ++i){
            items.add(new BlogItem("你现在已经看到使用约束系统的整个系列: " +
                    "创建手工约束, 使用自动连接约束, 还有使用推理引擎约束.\n 自动连接和推理通过布局引" +
                    "擎断定如何为布局中各个元素创建约束协助你. 然后你可以进一步按照你认为" +
                    "合适的方式自由地修改这些约束, 无论它们是由自动连接还是推理引擎生成的.", System.currentTimeMillis()));
        }
        multiTypeAdapter = new MultiTypeAdapter(items);
        multiTypeAdapter.register(BlogItem.class, new BlogItemViewProvider(this));
        blog_list.setLayoutManager(new LinearLayoutManager(this));
        blog_list.setAdapter(multiTypeAdapter);
        blog_new.clearFocus();
        blog_new.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    KeyboardUtils.showSoftInput(blog_new);
                }else {
                    KeyboardUtils.hideSoftInput(BlogListActivity.this);
                }
            }
        });

        blog_list.setOnScrollCallback(new OnScrollCallback() {
            @Override
            public void onStateChanged(ScrollRecyclerView recycler, int state) {
                LogUtil.i("滑啊滑", "我的滑板鞋，根本停不下来");
            }

            @Override
            public void onScrollUp(ScrollRecyclerView recycler, int dy) {
                blog_new.setVisibility(View.VISIBLE);
            }

            @Override
            public void onScrollToBottom() {
                LogUtil.i("底部","已经到了最底部了，不要再滑动了");
            }

            @Override
            public void onScrollDown(ScrollRecyclerView recycler, int dy) {
                blog_new.setVisibility(View.GONE);
            }

            @Override
            public void onScrollToTop() {
                LogUtil.i("底部","已经到了最顶部了，不要再滑动了");
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
        if (isEmpty(text)){
            CustomToast.show(this, "请填写动态内容");
            blog_new.requestFocus();
        }else {
            KeyboardUtils.hideSoftInput(this);
            blog_new.clearFocus();
            blog_new.setText("");
            final long time = System.currentTimeMillis();
            items.add(new BlogItem(text, time));
            multiTypeAdapter.notifyDataSetChanged();
            Observable.create(new Observable.OnSubscribe<Blog>() {
                @Override
                public void call(Subscriber<? super Blog> subscriber) {
                    try{
                        Blog blog = new Blog(null, text, time);
                        subscriber.onNext(blog);
                        subscriber.onCompleted();
                    }catch (Exception e){
                        e.printStackTrace();
                        subscriber.onError(e);
                    }
                }
            }).subscribeOn(Schedulers.io())//生产事件在io
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Blog>() {
                        @Override
                        public void onCompleted() {
                            BlogDao blogDao = ((MainApplication)getApplication()).getDaoSession().getBlogDao();
                            CustomToast.show(BlogListActivity.this, "共有动态内容" + blogDao.loadAll().size());
                        }

                        @Override
                        public void onError(Throwable e) {
                            CustomToast.show(BlogListActivity.this, "动态保存时出现错误");
                        }

                        @Override
                        public void onNext(Blog blog) {
                            BlogDao blogDao = ((MainApplication)getApplication()).getDaoSession().getBlogDao();
                            blogDao.save(blog);
                        }
                    });
        }

        return super.onOptionsItemSelected(item);
    }
}
