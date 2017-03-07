package com.imfan.j.a91fan.textabout;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.imfan.j.a91fan.MainApplication;
import com.imfan.j.a91fan.R;
import com.imfan.j.a91fan.chatroom.model.ChatRoomItem;
import com.imfan.j.a91fan.chatroom.model.ChatRoomItemViewProvider;
import com.imfan.j.a91fan.entity.Article;
import com.imfan.j.a91fan.entity.ArticleDao;
import com.imfan.j.a91fan.entity.DaoType;
import com.imfan.j.a91fan.entity.Draft;
import com.imfan.j.a91fan.entity.DraftDao;
import com.imfan.j.a91fan.entity.Group;
import com.imfan.j.a91fan.textabout.item.GroupItemViewProvider;
import com.imfan.j.a91fan.textabout.item.TextItem;
import com.imfan.j.a91fan.textabout.item.TextItemViewProvider;
import com.imfan.j.a91fan.util.CustomToast;
import com.imfan.j.a91fan.util.Preferences;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ScrollDirectionListener;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

public class ArticleListActivity extends AppCompatActivity {
    MultiTypeAdapter multiTypeAdapter;


    private DaoType daoType;
    private Items items;

    @BindView(R.id.rv_text_list)
    RecyclerView mRecyclerView;

    @BindView(R.id.fab_create_text)
    FloatingActionButton mFabCreateRoom;

    @OnClick(R.id.fab_create_text)
    void createNew() {
        Intent intent = new Intent(this, EditTextActivity.class);
        intent.putExtra("title", "新建");
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getIntent().getStringExtra("title"));
        daoType = (DaoType)getIntent().getSerializableExtra("type");

        items = new Items();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        multiTypeAdapter = new MultiTypeAdapter(items);
        multiTypeAdapter.register(TextItem.class, new TextItemViewProvider());

        mFabCreateRoom.show();
        mFabCreateRoom.attachToRecyclerView(mRecyclerView, new ScrollDirectionListener() {
            @Override
            public void onScrollDown() {
                mFabCreateRoom.hide();
                LogUtil.d("ListViewFragment", "onScrollDown()");
            }

            @Override
            public void onScrollUp() {
                mFabCreateRoom.show();
                LogUtil.d("ListViewFragment", "onScrollUp()");
            }
        });

        // 设置数据源
        mRecyclerView.setAdapter(multiTypeAdapter);
        initData();

    }

    private void initData() {

        switch (daoType){
            case ARTICLE:
                ArticleDao articleDao = ((MainApplication)getApplication()).getDaoSession().getArticleDao();
                List<Article> articleList =  articleDao.loadAll();
                if (articleList == null || articleList.isEmpty()){
                    LogUtil.i(getClass().getSimpleName(), "没有Article");
                }else{
                    for (Article article : articleList){
                        // 显示的都是最后一次编辑的时间
                        TextItem textItem = new TextItem(article.getTitle(), article.getUpdateTime());
                        items.add(textItem);
                    }
                }

                break;
            case DRAFT:
                DraftDao draftDao = ((MainApplication)getApplication()).getDaoSession().getDraftDao();
                List<Draft> draftList =  draftDao.loadAll();
                if (draftList == null || draftList.isEmpty()){
                    LogUtil.i(getClass().getSimpleName(), "没有Draft");
                }else{
                    for (Draft draft : draftList){
                        // 显示的都是最后一次编辑的时间
                        TextItem textItem = new TextItem(draft.getTitle(), draft.getUpdateTime());
                        items.add(textItem);
                    }
                }
                break;
            default:break;
        }

        // 通知数据已经改变
        multiTypeAdapter.notifyDataSetChanged();
    }

}
