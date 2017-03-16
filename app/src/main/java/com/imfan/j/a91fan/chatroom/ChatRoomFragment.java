package com.imfan.j.a91fan.chatroom;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.exceptions.HyphenateException;
import com.imfan.j.a91fan.R;
import com.imfan.j.a91fan.chatroom.activity.CreateChatRoomActivity;
import com.imfan.j.a91fan.chatroom.model.ChatRoomItemViewProvider;
import com.imfan.j.a91fan.main.activity.MainActivity;
import com.imfan.j.a91fan.textabout.listener.RecyclerItemClickListener;
import com.imfan.j.a91fan.util.CustomToast;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ScrollDirectionListener;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.yalantis.phoenix.PullToRefreshView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

import static com.imfan.j.a91fan.chatroom.EaseConstant.CHATTYPE_CHATROOM;
import static com.imfan.j.a91fan.chatroom.EaseConstant.EXTRA_USER_ID;

/**
 * Created by jay on 17-2-26.
 */

public class ChatRoomFragment extends Fragment {

    final private int CREATE_CHATROOM = 100;

    MultiTypeAdapter multiTypeAdapter;

    private Items items;

    @BindView(R.id.rv_chatroom_list)
    RecyclerView mRecyclerView;

    @BindView(R.id.pull_to_refresh_room)
    PullToRefreshView mRefreshRoom;

    @BindView(R.id.fab_create_chatroom)
    FloatingActionButton mFabCreateRoom;

    @OnClick(R.id.fab_create_chatroom)
    void createRoom() {
        Intent intent = new Intent(getContext(), CreateChatRoomActivity.class);
        startActivityForResult(intent, CREATE_CHATROOM);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.e("onCreateView", "出现顺序");
        View view = inflater.inflate(R.layout.chat_room, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findViews();

    }

    private void initData() {
        items.clear();
        Thread thread = new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                try {
                    EMCursorResult<EMChatRoom> result = EMClient.getInstance().chatroomManager().fetchPublicChatRoomsFromServer(5, null);
                    items.addAll(result.getData());
                    // 通知数据已经改变
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            multiTypeAdapter.notifyDataSetChanged();
                        }
                    });
                } catch (HyphenateException e) {
                    LogUtil.e("ChatRoomFragment的获取聊天室", "获取聊天室失败");
                    LogUtil.e("ChatRoomFragment的获取聊天室", e.getDescription());
                    LogUtil.e("ChatRoomFragment的获取聊天室", e.getErrorCode() + "");
                    e.printStackTrace();
                }
                Looper.loop();
            }
        };
        thread.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void findViews() {
        items = new Items();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getContext(), ChatActivity.class);
                intent.putExtra("roomname", ((EMChatRoom) items.get(position)).getName());
                intent.putExtra("title", ((EMChatRoom) items.get(position)).getName());
                intent.putExtra("owner", ((EMChatRoom) items.get(position)).getOwner());
                intent.putExtra(EXTRA_USER_ID, CHATTYPE_CHATROOM);
                intent.putExtra(EaseConstant.EXTRA_USER_ID, ((EMChatRoom) items.get(position)).getId());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                // 无操作
            }
        }));
        multiTypeAdapter = new MultiTypeAdapter(items);
        multiTypeAdapter.register(EMChatRoom.class, new ChatRoomItemViewProvider());

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

        mRefreshRoom.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefreshRoom.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshRoom.setRefreshing(false);
                        CustomToast.show(getContext(), "刷新成功");
                        if (items.isEmpty())
                            initData();
                    }
                }, 500);
            }
        });
        // 设置数据源
        mRecyclerView.setAdapter(multiTypeAdapter);
    }


}
