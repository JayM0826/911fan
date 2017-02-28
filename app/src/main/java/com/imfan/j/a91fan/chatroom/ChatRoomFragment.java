package com.imfan.j.a91fan.chatroom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imfan.j.a91fan.R;
import com.imfan.j.a91fan.chatroom.activity.CreateChatRoomActivity;
import com.imfan.j.a91fan.chatroom.model.ChatRoomItem;
import com.imfan.j.a91fan.chatroom.model.ChatRoomItemViewProvider;
import com.imfan.j.a91fan.util.CustomToast;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ScrollDirectionListener;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.yalantis.phoenix.PullToRefreshView;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by jay on 17-2-26.
 */

public class ChatRoomFragment extends Fragment {

    MultiTypeAdapter multiTypeAdapter;

    private Items items;

    // @BindView (R.id.rv_chatroom_list)
    RecyclerView mRecyclerView;

    // @BindView(R.id.pull_to_refresh_room)
    PullToRefreshView mRefreshRoom;

    // @BindView(R.id.fab_create_chatroom)
    FloatingActionButton mFabCreateRoom;

    // @OnClick(R.id.fab_create_chatroom)
    public void createRoom(){
        Intent intent = new Intent(getContext(), CreateChatRoomActivity.class);
        startActivity(intent);
    }

    


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.e("onCreateView", "出现顺序");
        View view = inflater.inflate(R.layout.chat_room, container, false);
        // ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findViews();
        initData();
    }

    private void initData() {

        for (int i = 0; i < 20; i++) {
            items.add(new ChatRoomItem("Songs"));
        }

        // 通知数据已经改变
        multiTypeAdapter.notifyDataSetChanged();
    }


    private void findViews() {

        mRecyclerView = (RecyclerView)getView().findViewById(R.id.rv_chatroom_list);
        items = new Items();



        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        multiTypeAdapter = new MultiTypeAdapter(items);
        multiTypeAdapter.register(ChatRoomItem.class, new ChatRoomItemViewProvider());

        mFabCreateRoom = (FloatingActionButton)getView().findViewById(R.id.fab_create_chatroom);
        mFabCreateRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createRoom();
            }
        });

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

        mRefreshRoom = (PullToRefreshView)getView().findViewById(R.id.pull_to_refresh_room);
        mRefreshRoom.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefreshRoom.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshRoom.setRefreshing(false);
                        CustomToast.show(getContext(), "我刷新了我刷新了啊！");
                    }
                }, 500);
            }
        });



        // 设置数据源
        mRecyclerView.setAdapter(multiTypeAdapter);

    }








}
