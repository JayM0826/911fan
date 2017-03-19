/*
 *
 *  * Created by J on  2017.
 *  * Copyright (c) 2017.  All rights reserved.
 *  *
 *  * Last modified 17-3-16 上午8:45
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

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.ScreenUtils;
import com.bumptech.glide.Glide;
import com.imfan.j.a91fan.MainApplication;
import com.imfan.j.a91fan.R;
import com.imfan.j.a91fan.entity.Article;
import com.imfan.j.a91fan.entity.ArticleDao;
import com.imfan.j.a91fan.entity.DaoType;
import com.imfan.j.a91fan.entity.Draft;
import com.imfan.j.a91fan.entity.DraftDao;
import com.imfan.j.a91fan.util.CustomToast;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.sendtion.xrichtext.RichTextEditor;
import com.sendtion.xrichtext.SDCardUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import me.iwf.photopicker.PhotoPicker;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.blankj.utilcode.utils.ImageUtils.getBitmap;
import static com.blankj.utilcode.utils.ScreenUtils.isScreenLock;
import static com.blankj.utilcode.utils.TimeUtils.getNowTimeString;

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


public class EditTextActivity extends AppCompatActivity {


    @BindView(R.id.content_new)
    LinearLayout content_new;

    @BindView(R.id.et_new_title)
    EditText et_new_title;

    @BindView(R.id.et_content)
    RichTextEditor et_content;

    @BindView(R.id.tv_time)
    TextView tv_time;

    @BindView(R.id.tv_group)
    TextView tv_group;


    private ArticleDao articleDao = null;
    private DraftDao draftDao = null;//笔记对象

    private ProgressDialog loadingDialog;
    private ProgressDialog insertDialog;
    private int screenWidth;
    private int screenHeight;

    // private String flag; // 是新建（1）还是已经存在的文档（2）

    private DaoType daoType; // Article ? Draft
    private String groupName;
    private Long groupID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text);
        ButterKnife.bind(this);

        initView();
    }


    private void initView() {

        screenWidth = ScreenUtils.getScreenWidth();
        screenHeight = ScreenUtils.getScreenHeight();

        insertDialog = new ProgressDialog(this);
        insertDialog.setMessage("正在插入图片...");
        insertDialog.setCanceledOnTouchOutside(false);

        loadingDialog = new ProgressDialog(this);
        loadingDialog.setMessage("图片解析中...");
        loadingDialog.setCanceledOnTouchOutside(false);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getIntent().getStringExtra("title"));

        daoType = (DaoType) getIntent().getSerializableExtra("type");
        groupName = getIntent().getStringExtra("group_name");
        groupID = getIntent().getLongExtra("groupID", 0);


        if (daoType == DaoType.DRAFT) {
            draftDao = ((MainApplication) getApplication()).getDaoSession().getDraftDao();
        } else if (daoType == DaoType.ARTICLE) {
            articleDao = ((MainApplication) getApplication()).getDaoSession().getArticleDao();
        }
        tv_time.setText(getNowTimeString());
        tv_group.setText(groupName);
    }


    /**
     * 将所有数据进行转换
     */
    private String getEditData() {
        List<RichTextEditor.EditData> editList = et_content.buildEditData();
        StringBuffer content = new StringBuffer();
        for (RichTextEditor.EditData itemData : editList) {
            if (itemData.inputStr != null) {
                content.append(itemData.inputStr);
                //Log.d("RichEditor", "commit inputStr=" + itemData.inputStr);
            } else if (itemData.imagePath != null) {
                content.append("<img src=\"").append(itemData.imagePath).append("\"/>");
                LogUtil.d("RichEditor", "commit imgePath=" + itemData.imagePath);
                // imageList.add(itemData.imagePath);
            }
        }
        return content.toString();
    }

    /**
     * 用户每次按返回时的数据操作
     */
    @Override
    public void onBackPressed() {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Warning...")
                .setContentText("不保存就退出吗？")
                .setConfirmText("保存")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        saveNoteData();
                    }
                }).setCancelText("不保存")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        finish();
                    }
                });
        sweetAlertDialog.show();
    }


    /**
     * 处理选择的图片
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                if (requestCode == PhotoPicker.REQUEST_CODE) {
                    //异步方式插入图片
                    insertImagesSync(data);
                }
            }
        }
    }

    /**
     * 异步方式插入图片
     *
     * @param data
     */
    private void insertImagesSync(final Intent data) {

        final SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("插入图片").setContentText("正在努力插入图片，请稍等");
        sweetAlertDialog.show();

        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(rx.Subscriber<? super String> subscriber) {
                try {
                    et_content.measure(0, 0);
                    int width = ScreenUtils.getScreenWidth();
                    int height = ScreenUtils.getScreenHeight();
                    // photopicker获取的是路径，然后自己再去路径去照片
                    ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                    //可以同时插入多张图片
                    for (String imagePath : photos) {
                        //Log.i("NewActivity", "###path=" + imagePath);

                        Bitmap bitmap = getBitmap(imagePath);//压缩图片
                        //bitmap = BitmapFactory.decodeFile(imagePath);
                        imagePath = SDCardUtil.saveToSdCard(bitmap);
                        //Log.i("NewActivity", "###imagePath="+imagePath);
                        subscriber.onNext(imagePath);
                    }
                    subscriber.onCompleted();
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        })
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())//生产事件在io
                .observeOn(AndroidSchedulers.mainThread())//消费事件在UI线程
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        sweetAlertDialog.setTitleText("Success").setContentText("插入图片成功");
                        sweetAlertDialog.show();

                        et_content.addEditTextAtIndex(et_content.getLastIndex(), " ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        sweetAlertDialog.setTitleText("Failed").setContentText("插入图片失败了，好伤心");
                        sweetAlertDialog.show();
                        LogUtil.e("插入图片Error onError", "图片插入失败:\n" + e.getMessage());
                    }

                    @Override
                    public void onNext(String imagePath) {
                        et_content.insertImage(imagePath, et_content.getMeasuredWidth());
                    }
                });
    }

    /**
     * 调用图库选择
     */
    private void pickPhoto() {
        PhotoPicker.builder()
                .setPhotoCount(9)//可选择图片数量
                .setShowCamera(true)//是否显示拍照按钮
                .setShowGif(true)//是否显示动态图
                .setPreviewEnabled(true)//是否可以预览
                .start(this, PhotoPicker.REQUEST_CODE);
    }

    /**
     * 核心
     * 保存数据
     */
    private void saveNoteData() {
        // 标题
        String noteTitle = et_new_title.getText().toString();
        // 内容
        String noteContent = getEditData();

        if (noteTitle.equals("") || noteTitle == null) {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setConfirmText("OK").setContentText("可能忘了填写标题")
                    .setTitleText("Warning..").show();
            return;
        }

        if (noteContent.equals("") || noteTitle == null) {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setConfirmText("OK").setContentText("可能忘了写笔记")
                    .setTitleText("Warning..").show();
            return;
        }


        if (daoType == DaoType.ARTICLE) { // 存储在Article表内
            Article article = new Article(null, null, noteTitle, noteContent, groupID, groupName, "html", getNowTimeString(), getNowTimeString("yyyy-MM-dd"));
            articleDao.insert(article);

        } else {// 存储在Draft表内
            Draft draft = new Draft(null, null, noteTitle, noteContent, groupID, groupName, "html", getNowTimeString(), getNowTimeString("yyyy-MM-dd"));
            draftDao.insert(draft);
        }
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Success")
                .setContentText("保存成功").setConfirmText("OK")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        finish();
                    }
                });
        sweetAlertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_edit_text_menu, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.insert_photo:
                pickPhoto();
                break;
            case R.id.text_save:
                saveNoteData();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //如果APP处于后台，或者手机锁屏，则启用密码锁
        if (isAppOnBackground(getApplicationContext()) ||
                isScreenLock()) {
            saveNoteData();//处于后台时保存数据
        }
    }

    /**
     * 判断应用是否处于后台
     *
     * @param context
     * @return
     */
    public static boolean isAppOnBackground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }
}
