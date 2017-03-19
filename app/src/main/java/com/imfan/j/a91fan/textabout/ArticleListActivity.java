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

package com.imfan.j.a91fan.textabout;

import android.content.Intent;
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
import android.widget.FrameLayout;

import com.imfan.j.a91fan.MainApplication;
import com.imfan.j.a91fan.R;
import com.imfan.j.a91fan.entity.Article;
import com.imfan.j.a91fan.entity.ArticleDao;
import com.imfan.j.a91fan.entity.DaoType;
import com.imfan.j.a91fan.entity.Draft;
import com.imfan.j.a91fan.entity.DraftDao;
import com.imfan.j.a91fan.textabout.item.TextItem;
import com.imfan.j.a91fan.textabout.item.TextItemViewProvider;
import com.imfan.j.a91fan.textabout.listener.RecyclerItemClickListener;
import com.imfan.j.a91fan.util.CustomToast;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ScrollDirectionListener;
import com.netease.nim.uikit.common.util.log.LogUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Article或者Draft中某一个分组的所有文章列表
 */
public class ArticleListActivity extends AppCompatActivity {
    MultiTypeAdapter multiTypeAdapter;
    private Long groupID; // 所属组的id

    private DaoType daoType; // 所属的类型，是Article还是Blog还是Draft

    private Items items;

    private String groupName;

    @BindView(R.id.rv_text_list)
    RecyclerView mRecyclerView;

    @BindView(R.id.fab_create_text)
    FloatingActionButton mFabCreateText;

    @BindView(R.id.fl_layout)
    FrameLayout fl_layout;

    @OnClick(R.id.fab_create_text)
    void createNew() {
        Intent intent = new Intent(this, EditTextActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable("type", daoType);

        // 这是actionbar的title
        intent.putExtra("title", "新建");
        intent.putExtra("flag", "1");
        intent.putExtra("groupID", groupID);
        intent.putExtras(bundle);

        intent.putExtra("group_name", groupName);
        startActivity(intent);
    }


    private Long id; // 文章的id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        ButterKnife.bind(this);
        initView();
    }


    private void initView() {

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(groupName = getIntent().getStringExtra("title")); // groupname
        // 获取分组的类型
        daoType = (DaoType) getIntent().getSerializableExtra("type");
        groupID = getIntent().getLongExtra("groupID", 0L);
        items = new Items();
        multiTypeAdapter = new MultiTypeAdapter(items);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 给RecyclerView加监听器
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            // 单击监听
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(ArticleListActivity.this, PreviewActivity.class);


                Bundle bundle = new Bundle();
                bundle.putSerializable("type", daoType);
                intent.putExtra("id", ((TextItem) items.get(position)).id);
                // 这是actionbar的title
                intent.putExtra("title", ((TextItem) items.get(position)).name);
                intent.putExtra("groupID", groupID);
                intent.putExtras(bundle);
                intent.putExtra("group_name", groupName);
                startActivity(intent);
            }

            // 长按监听
            @Override
            public void onItemLongClick(View view, final int position) {

                id = ((TextItem) items.get(position)).id;

                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(ArticleListActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Warning...").setContentText("删除前请三思").setConfirmText("删除")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(final SweetAlertDialog sweetAlertDialog) {
                                Observable.create(new Observable.OnSubscribe<Long>() {
                                                      @Override
                                                      public void call(rx.Subscriber<? super Long> subscriber) {
                                                          subscriber.onNext(id);
                                                      }
                                                  })
                                        .onBackpressureBuffer()
                                        .subscribeOn(Schedulers.io())//生产事件在io
                                        .observeOn(AndroidSchedulers.mainThread())//消费事件在UI线程
                                        .subscribe(new Observer<Long>() {
                                            @Override
                                            public void onCompleted() {
                                                sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                                sweetAlertDialog.setTitleText("Success").setContentText("删除成功")
                                                        .setConfirmText(null).setCancelText(null).show();
                                                initData();
                                            }
                                            @Override
                                            public void onError(Throwable e) {
                                                e.printStackTrace();
                                                sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                                sweetAlertDialog.setTitleText("oooooops").setContentText("删除失败")
                                                        .setConfirmText(null).setCancelText(null).show();
                                            }

                                            @Override
                                            public void onNext(Long id) {
                                                if (daoType == DaoType.ARTICLE) {
                                                    ArticleDao articleDao = ((MainApplication) getApplication()).getDaoSession().getArticleDao();
                                                    articleDao.deleteByKey(id);
                                                } else {
                                                    DraftDao draftDao = ((MainApplication) getApplication()).getDaoSession().getDraftDao();
                                                    draftDao.deleteByKey(id);
                                                }
                                                items.remove(position);
                                                multiTypeAdapter.notifyDataSetChanged();
                                            }
                                        });


                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        sweetAlertDialog.dismiss();
                                    }
                                }, 1000);

                            }
                        }).setCancelText("保留");
                sweetAlertDialog.show();
            }
        }));

        multiTypeAdapter.register(TextItem.class, new TextItemViewProvider());

        mFabCreateText.show();
        mFabCreateText.attachToRecyclerView(mRecyclerView, new ScrollDirectionListener() {
            @Override
            public void onScrollDown() {
                mFabCreateText.hide();
                LogUtil.d("ListViewFragment", "onScrollDown()");
            }

            @Override
            public void onScrollUp() {
                mFabCreateText.show();
                LogUtil.d("ListViewFragment", "onScrollUp()");
            }
        });

        // 设置数据源
        mRecyclerView.setAdapter(multiTypeAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        items.clear();
        switch (daoType) {
            case ARTICLE:
                ArticleDao articleDao = ((MainApplication) getApplication()).getDaoSession().getArticleDao();
                List<Article> articleList = articleDao.queryBuilder()
                        .where(ArticleDao.Properties.GroupID.eq(groupID)).list();

                if (articleList == null || articleList.isEmpty()) {
                    LogUtil.i(getClass().getSimpleName(), "没有Article");
                    CustomToast.show(this, "还有没记录，新建一个吧");
                    fl_layout.setBackground(getResources().getDrawable(R.drawable.login_bg));
                } else {
                    fl_layout.setBackground(null);
                    for (Article article : articleList) {
                        // 显示的都是最后一次编辑的时间
                        TextItem textItem = new TextItem(article.getId(), article.getTitle(), article.getUpdateTime());
                        items.add(textItem);
                    }
                }
                break;
            case DRAFT:
                DraftDao draftDao = ((MainApplication) getApplication()).getDaoSession().getDraftDao();
                List<Draft> draftList = draftDao.queryBuilder().where(DraftDao.Properties.GroupID.eq(groupID)).list();
                if (draftList == null || draftList.isEmpty()) {
                    CustomToast.show(this, "还有没记录，新建一个吧");
                    fl_layout.setBackground(getResources().getDrawable(R.drawable.login_bg));
                    LogUtil.i(getClass().getSimpleName(), "没有Draft");
                } else {
                    fl_layout.setBackground(null);
                    String time, title;
                    for (Draft draft : draftList) {
                        // 显示的都是最后一次编辑的时间

                        time = draft.getUpdateTime();
                        title = draft.getTitle();
                        if (time == null) {
                            LogUtil.i("加载草稿这里", "时间是空");
                        }

                        if (title == null) {
                            LogUtil.i("加载草稿这里", "Title是空");
                        }

                        TextItem textItem = new TextItem(draft.getId(), title, time);
                        items.add(textItem);
                    }
                }
                break;
            default:
                break;
        }

        // 通知数据已经改变
        multiTypeAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_article_list_menu, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 假删除
        items.clear();
        multiTypeAdapter.notifyDataSetChanged();
        return super.onOptionsItemSelected(item);
    }
}
