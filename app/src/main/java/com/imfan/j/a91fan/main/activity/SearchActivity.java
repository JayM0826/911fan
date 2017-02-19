package com.imfan.j.a91fan.main.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.imfan.j.a91fan.R;
import com.imfan.j.a91fan.contact.activity.UserProfileActivity;
import com.imfan.j.a91fan.session.SessionHelper;
import com.imfan.j.a91fan.team.AdvancedTeamJoinActivity;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.netease.nim.uikit.contact.core.item.AbsContactItem;
import com.netease.nim.uikit.contact.core.item.ContactItem;
import com.netease.nim.uikit.contact.core.item.ItemTypes;
import com.netease.nim.uikit.contact.core.item.MsgItem;
import com.netease.nim.uikit.contact.core.model.ContactDataAdapter;
import com.netease.nim.uikit.contact.core.model.ContactGroupStrategy;
import com.netease.nim.uikit.contact.core.provider.ContactDataProvider;
import com.netease.nim.uikit.contact.core.query.IContactDataProvider;
import com.netease.nim.uikit.contact.core.viewholder.ContactHolder;
import com.netease.nim.uikit.contact.core.viewholder.LabelHolder;
import com.netease.nim.uikit.contact.core.viewholder.MsgHolder;
import com.netease.nim.uikit.model.ToolBarOptions;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.search.model.MsgIndexRecord;

/**
 * 目前只用来搜索用户
 */
public class SearchActivity extends UI implements AdapterView.OnItemClickListener {

    static public boolean hasData;

    // 第一个搜索用户，第二个不搜索用户
    IContactDataProvider dataProvider = new ContactDataProvider(ItemTypes.USER,ItemTypes.TEAM_UNJOIN, ItemTypes.FRIEND, ItemTypes.TEAM, ItemTypes.MSG);
    private ListView lvContacts;
    private SearchView searchView;
    private ContactDataAdapter adapter;

    public static final void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, SearchActivity.class);
        context.startActivity(intent);
    }

    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        final MenuItem item = menu.findItem(R.id.action_search);

        getHandler().post(new Runnable() {
            @Override
            public void run() {
                MenuItemCompat.expandActionView(item);
            }
        });

        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                finish();

                return false;
            }
        });


        searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setQueryHint(getString(R.string.search_global));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                showKeyboard(false);
                if (StringUtil.isEmpty(query)) {

                    lvContacts.setVisibility(View.GONE);
                } else {
                    lvContacts.setVisibility(View.VISIBLE);
                    LogUtil.i("onQueryTextSubmit", "开始提交搜索");
                    // 执行核心操作，对query语句进行搜索
                    adapter.query(query);
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                lvContacts.setVisibility(View.GONE);
                return true;
            }
        });
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (searchView != null) {
            searchView.clearFocus();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        ToolBarOptions options = new ToolBarOptions();
        setToolBar(R.id.toolbar, options);

        lvContacts = (ListView) findViewById(R.id.searchResultList);
        lvContacts.setVisibility(View.GONE);

        SearchGroupStrategy searchGroupStrategy = new SearchGroupStrategy();

       /* DataProvider可以分为多个类型例如UserDataProvider，TeamDataProvider, MsgDataProvider
        至少三种类型，而IContactDataProvider是这多个类型的汇总类*/

        adapter = new ContactDataAdapter(SearchActivity.this, searchGroupStrategy, dataProvider);
        adapter.addViewHolder(ItemTypes.LABEL, LabelHolder.class);
        // 增加了USER的ViewHolder
        adapter.addViewHolder(ItemTypes.USER, ContactHolder.class);
        adapter.addViewHolder(ItemTypes.FRIEND, ContactHolder.class);
        adapter.addViewHolder(ItemTypes.TEAM, ContactHolder.class);
        adapter.addViewHolder(ItemTypes.MSG, MsgHolder.class);
        adapter.addViewHolder(ItemTypes.TEAM_UNJOIN, ContactHolder.class);



        lvContacts.setAdapter(adapter);
        lvContacts.setOnItemClickListener(this);
        lvContacts.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                showKeyboard(false);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
        findViewById(R.id.global_search_root).setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    finish();
                    return true;
                }
                return false;
            }
        });
    }


    // 点击搜索出来的结果执行相应的事件

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AbsContactItem item = (AbsContactItem) adapter.getItem(position);

        switch (item.getItemType()) {

            case ItemTypes.TEAM_UNJOIN:
                AdvancedTeamJoinActivity.start(SearchActivity.this, ((ContactItem) item).getContact().getContactId());

                break;

            case ItemTypes.USER:
                // 打开搜索出来的用户的详细资料的Activity
                UserProfileActivity.start(this, ((ContactItem) item).getContact().getContactId());
                finish();
                break;
            case ItemTypes.TEAM: {
                SessionHelper.startTeamSession(this, ((ContactItem) item).getContact().getContactId());
                break;
            }

            case ItemTypes.FRIEND: {
                SessionHelper.startP2PSession(this, ((ContactItem) item).getContact().getContactId());
                break;
            }

            case ItemTypes.MSG: {
                MsgIndexRecord msgIndexRecord = ((MsgItem) item).getRecord();
                if (msgIndexRecord.getCount() > 1) {
                   SearchMsgDetailActivity.start(this, msgIndexRecord);
                } else {
                    if (msgIndexRecord.getSessionType() == SessionTypeEnum.P2P) {// 单聊中的信息
                      SessionHelper.startP2PSession(this, msgIndexRecord.getSessionId(), msgIndexRecord.getMessage());
                    } else if (msgIndexRecord.getSessionType() == SessionTypeEnum.Team) {// 群聊中的信息
                        SessionHelper.startTeamSession(this, msgIndexRecord.getSessionId(), msgIndexRecord.getMessage());
                    }
                }
                break;
            }

            default:
                break;
        }
    }

    private static class SearchGroupStrategy extends ContactGroupStrategy {
        public static final String GROUP_FRIEND = "FRIEND";
        public static final String GROUP_TEAM = "TEAM";
        public static final String GROUP_MSG = "MSG";
        public static final String GROUP_USER = "USER";
        public static final String GROUP_UNJOIN = "NEW_GROUP";

        SearchGroupStrategy() {
            add(ContactGroupStrategy.GROUP_NULL, 0, "");
            add(GROUP_USER, 1, "用户");
            add(GROUP_TEAM, 3, "已加入群组");
            add(GROUP_FRIEND, 4, "好友");
            add(GROUP_MSG, 5, "聊天记录");
            add(GROUP_UNJOIN, 2, "未加入群组");

        }

        @Override
        public String belongs(AbsContactItem item) {
            switch (item.getItemType()) {
                case ItemTypes.USER:
                    return GROUP_USER;
                case ItemTypes.FRIEND:
                    return GROUP_FRIEND;
                case ItemTypes.TEAM:
                    return GROUP_TEAM;
                case ItemTypes.MSG:
                    return GROUP_MSG;
                case ItemTypes.TEAM_UNJOIN:
                    return GROUP_UNJOIN;
                default:
                    return null;
            }
        }
    }
}
