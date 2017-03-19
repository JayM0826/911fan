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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.imfan.j.a91fan.MainApplication;
import com.imfan.j.a91fan.R;
import com.imfan.j.a91fan.entity.Article;
import com.imfan.j.a91fan.entity.ArticleDao;
import com.imfan.j.a91fan.entity.DaoType;
import com.imfan.j.a91fan.entity.Draft;
import com.imfan.j.a91fan.entity.DraftDao;
import com.imfan.j.a91fan.textabout.utils.StringUtils;
import com.imfan.j.a91fan.util.CustomToast;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.sendtion.xrichtext.RichTextView;
import com.sendtion.xrichtext.SDCardUtil;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

//
//      ┏┛ ┻━━━━━┛ ┻┓
//      ┃　　　　　　 ┃
//      ┃　　　━　　　┃
//      ┃　┳┛　  ┗┳　┃
//      ┃　　　　　　 ┃
//      ┃　　　┻　　　┃
//      ┃　　　　　　 ┃
//      ┗━┓　　　┏━━━┛
//        ┃　　　┃   神兽保佑
//        ┃　　　┃   代码无BUG！
//        ┃　　　┗━━━━━━━━━┓
//        ┃　　　　　　　    ┣┓
//        ┃　　　　         ┏┛
//        ┗━┓ ┓ ┏━━━┳ ┓ ┏━┛
//          ┃ ┫ ┫   ┃ ┫ ┫
//          ┗━┻━┛   ┗━┻━┛

public class PreviewActivity extends AppCompatActivity {

    private String textTitle;
    private String groupName;
    private Long groupID;
    private DaoType daoType;
    private Long id;

    @BindView(R.id.content_layout)
    LinearLayout content_layout;

    @BindView(R.id.et_title)
    TextView et_title;

    @BindView(R.id.et_content)
    RichTextView et_content;

    @BindView(R.id.tv_time)
    TextView tv_time;

    @BindView(R.id.tv_group)
    TextView tv_group;

    // 数据库中的正文
    private String mContent;
    private ArticleDao articleDao;
    private DraftDao draftDao;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        ActionBar actionBar  = getSupportActionBar();
        Intent intent = getIntent();

        id = intent.getLongExtra("id", 0);
        if (id == 0){
            LogUtil.e("PreviewActivity获取文章的id出错", "出错了出错了");
        }

        groupID = intent.getLongExtra("groupID", 0);
        if (groupID == 0){
            LogUtil.e("PreviewActivity获取组的id出错", "出错了出错了");
        }

        groupName = intent.getStringExtra("group_name");
        daoType = (DaoType) intent.getSerializableExtra("type");
        textTitle = intent.getStringExtra("title");

        actionBar.setTitle("笔记详情");
        draftDao = ((MainApplication) getApplication()).getDaoSession().getDraftDao();
        articleDao = ((MainApplication)getApplication()).getDaoSession().getArticleDao();
        if (daoType == DaoType.ARTICLE){
            // 去把这个文章找出来
            Article article = articleDao.load(id);
            mContent = article.getContent(); // 这里的content是存储到数据库里面的
            tv_time.setText(article.getUpdateTime());
        }else {
            Draft draft = draftDao.load(id);
            mContent = draft.getContent();
            tv_time.setText(draft.getUpdateTime());
        }

        et_title.setText(textTitle);
        tv_group.setText(groupName);
        et_content.post(new Runnable() {
            @Override
            public void run() {
                et_content.clearAllLayout();
                showDataSync(mContent);
            }
        });

    }

    private void showDataSync(final String html) {

        final SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
                .setContentText("正在加载中，请稍后").setTitleText("Loading");
        sweetAlertDialog.show();


            Observable.create(new Observable.OnSubscribe<String>() {
                                  @Override
                                  public void call(rx.Subscriber<? super String> subscriber) {
                                      showEditData(subscriber, html);
                                      // subscriber.onCompleted();
                                  }
                              }
            ).onBackpressureBuffer()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<String>() {
                        @Override
                        public void onCompleted() {
                            sweetAlertDialog.dismiss();
                        }

                        @Override
                        public void onError(Throwable e) {
                            sweetAlertDialog.dismiss();
                            e.printStackTrace();
                            CustomToast.show(PreviewActivity.this, "解析错误：图片不存在或已损坏");
                        }

                        @Override
                        public void onNext(String text) {
                            if (text.contains(SDCardUtil.getPictureDir())){
                                et_content.addImageViewAtIndex(et_content.getLastIndex(), text);
                            } else {
                                et_content.addTextViewAtIndex(et_content.getLastIndex(), text);
                            }
                        }
                    });
    }


    private void showEditData(Subscriber<? super String> subscriber, String html){
        try {
            List<String> textList = StringUtils.cutStringByImgTag(html);
            for (int i = 0; i < textList.size(); i++) {
                String text = textList.get(i);
                if (text.contains("<img") && text.contains("src=")) {
                    String imagePath = StringUtils.getImgSrc(text);
                    if (new File(imagePath).exists()) {
                        subscriber.onNext(imagePath);
                    } else {
                        CustomToast.show(this, "图片"+1+"已丢失，请重新插入！");
                    }
                } else {
                    subscriber.onNext(text);
                }
            }
            subscriber.onCompleted();
        } catch (Exception e){
            e.printStackTrace();
            subscriber.onError(e);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_preview_menu, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_bar_publish:
                if (daoType == DaoType.ARTICLE)
                    CustomToast.show(this, "您已经发布成文章了，无需再发布");
                else {
                    Draft draft = draftDao.load(id);
                    Article article = new Article(null, draft.getImage_path(), draft.getTitle(),
                            draft.getContent(), draft.getGroupID(), draft.getGroupName(),
                            draft.getType(), draft.getCreateTime(), draft.getUpdateTime());

                    draftDao.deleteByKey(id);
                    articleDao.insert(article);
                    CustomToast.show(this, "发布成功");

                    Intent intent = new Intent(this, GroupListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("title", getResources().getString(R.string.user_acticle));
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("type", DaoType.ARTICLE);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();

                }

                break;
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }

}
