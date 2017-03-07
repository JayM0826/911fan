package com.imfan.j.a91fan.textabout;

import android.content.Intent;
import android.icu.text.TimeZoneFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.imfan.j.a91fan.MainApplication;
import com.imfan.j.a91fan.R;
import com.imfan.j.a91fan.entity.DaoSession;
import com.imfan.j.a91fan.entity.DaoType;
import com.imfan.j.a91fan.entity.Group;
import com.imfan.j.a91fan.entity.GroupDao;
import com.imfan.j.a91fan.textabout.item.GroupItem;
import com.imfan.j.a91fan.textabout.item.GroupItemViewProvider;
import com.imfan.j.a91fan.textabout.listener.RecyclerItemClickListener;
import com.imfan.j.a91fan.util.CustomToast;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ScrollDirectionListener;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.common.util.sys.TimeUtil;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import rx.Observable;
import rx.Subscriber;

import static com.blankj.utilcode.utils.TimeUtils.getNowTimeString;
import static com.imfan.j.a91fan.util.SystemUtil.getNowDateShort;

//
//                       .::::.
//                     .::::::::.
//                    :::::::::::
//                 ..:::::::::::'
//              '::::::::::::'
//                .::::::::::
//           '::::::::::::::..
//                ..::::::::::::.
//              ``::::::::::::::::
//               ::::``:::::::::'        .:::.
//              ::::'   ':::::'       .::::::::.
//            .::::'      ::::     .:::::::'::::.
//           .:::'       :::::  .:::::::::' ':::::.
//          .::'        :::::.:::::::::'      ':::::.
//         .::'         ::::::::::::::'         ``::::.
//     ...:::           ::::::::::::'              ``::.
//    ```` ':.          ':::::::::'                  ::::..
//                       '.:::::'                    ':'````..


/**
 * 用来显示草稿和文章的组名，可复用
 */

public class GroupListActivity extends AppCompatActivity {


    private DaoType daoType;

    private static final int CREATE_GROUP = 100;
    MultiTypeAdapter multiTypeAdapter;



    private Items items;

    @BindView(R.id.rv_chatroom_list)
    RecyclerView mRecyclerView;


    @BindView(R.id.fab_create_chatroom)
    FloatingActionButton mFabCreateRoom;


    // 新建分组
    @OnClick(R.id.fab_create_chatroom)
    void createGroup() {
        Intent intent = new Intent(this, CreateGroupActivitry.class);
        startActivityForResult(intent, CREATE_GROUP);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        ButterKnife.bind(this);
        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        String title = getIntent().getStringExtra("title");
        daoType = (DaoType) getIntent().getSerializableExtra("type");
        actionBar.setTitle("分组  " + title);

        items = new Items();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        multiTypeAdapter = new MultiTypeAdapter(items);
        multiTypeAdapter.register(GroupItem.class, new GroupItemViewProvider());

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
        initDataGroup();

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(GroupListActivity.this, ArticleListActivity.class);
                String groupName = ((GroupItem)items.get(position)).groupName;
                Bundle bundle = new Bundle();
                bundle.putSerializable("type", daoType);
                intent.putExtras(bundle);
                intent.putExtra("title", groupName);
                startActivity(intent);
            }

            // 这个功能还没有弄出来
            @Override
            public void onItemLongClick(View view, int pos) {
                CustomToast.show(GroupListActivity.this, "目前不能直接删除分组，开发中。。。");
            }
        }));

    }

    /**
     * 初始化数据，这个只是在开发阶段才使用
     */
    private void initData() {

        Subscriber<List<Group>> subscriber = new Subscriber<List<Group>>() {
            @Override
            public void onCompleted() {
                LogUtil.i(getClass().getSimpleName(), "获取数据完毕");
            }

            @Override
            public void onError(Throwable e) {
                LogUtil.i(getClass().getSimpleName(), "获取数据出现错误");
            }

            @Override
            public void onNext(List<Group> groupList) {
                for (Group group : groupList){
                    GroupItem groupItem = new GroupItem(group.getGroup_name(), groupList.size(), getNowTimeString("yyyy-mm--dd"));
                    items.add(groupItem);
                }
                multiTypeAdapter.notifyDataSetChanged();
            }
        };



        Observable<List<Group>> observable = Observable.create(new Observable.OnSubscribe<List<Group>>() {
            @Override
            public void call(Subscriber<? super List<Group>> subscriber) {
                GroupDao groupDao = ((MainApplication) getApplication()).getDaoSession().getGroupDao();

                List<Group> groupList = groupDao.loadAll();
                subscriber.onNext(groupList);
            }
        });

        observable.subscribe(subscriber);
    }

    /**
     * 接收新的组名，创建组名，刷新列表
     * @param requestCode 如果该activity只有向一个activity索要数据，这个参数其实是可以忽视的，但是如果有多个，
     *                    我们就要去分类了
     * @param resultCode 返回固定值，ok与不ok
     * @param data 返回的值
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        LogUtil.i(getClass().getSimpleName(), "接收创建的分组名字");
        if (requestCode == CREATE_GROUP && resultCode == RESULT_OK) {
            GroupDao groupDao = ((MainApplication)getApplication()).getDaoSession().getGroupDao();
            // 只保存创建时间，不需要更新时间
            Group group = new Group(null, data.getStringExtra("name").toString(), getNowTimeString("yyyy-MM-dd"), null);
            groupDao.insert(group);
            // 这里的创建时间关键字写错，并且这里应该传的数量应该是分组内的文章数量，刚刚创建分组一定是0
            items.add(new  GroupItem(data.getStringExtra("name").toString(), 0, group.getCreate_name()));
            if (data == null){
                LogUtil.e(getClass().getSimpleName(), "并没有返回intent");
                return;
            }
            LogUtil.i(getClass().getSimpleName(), "接收的组名为" + data.getStringExtra("name").toString());
            multiTypeAdapter.notifyDataSetChanged();
        }
    }

    private void initDataGroup() {
        LogUtil.i("最先打印时间", getNowTimeString("yyyy-MM-dd"));
        DaoSession daoSession = ((MainApplication)getApplication()).getDaoSession();

        final GroupDao groupDao = daoSession.getGroupDao();

        List<Group> groupList = groupDao.loadAll();

        if (groupList == null || groupList.isEmpty()) {
            // 增加一个默认组
            rx.Subscriber<Group> subscriber = new rx.Subscriber<Group>() {
                @Override
                public void onCompleted() {
                    LogUtil.i(getClass().getSimpleName(), "插入成功");
                }

                @Override
                public void onError(Throwable e) {

                    LogUtil.i(getClass().getSimpleName(), "插入失败");
                }

                @Override
                public void onNext(Group group) {
                    LogUtil.i(getClass().getSimpleName(), "插入了一条数据");
                    groupDao.insert(group);
                }
            };

            Observable<Group> observable = Observable.create(new Observable.OnSubscribe<Group>() {
                @Override
                public void call(rx.Subscriber<? super Group> subscriber) {
                    subscriber.onNext(new Group(null, "默认分组", getNowTimeString("yyyy-MM-dd"), null));
                    subscriber.onCompleted();
                }
            });

            observable.subscribe(subscriber);

        }else{
            LogUtil.i(getClass().getSimpleName(), "已经有数据了");
            LogUtil.i("打印时间", getNowTimeString("yyyy-MM-dd"));

        }
        LogUtil.i("最后打印时间", getNowTimeString("yyyy-MM-dd"));
        initData();
    }

}



