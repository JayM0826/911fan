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

package com.imfan.j.a91fan.main.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.imfan.j.a91fan.R;
import com.imfan.j.a91fan.session.SessionHelper;
import com.netease.nim.uikit.cache.NimUserInfoCache;
import com.netease.nim.uikit.cache.TeamDataCache;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.listview.AutoRefreshListView;
import com.netease.nim.uikit.contact.core.item.AbsContactItem;
import com.netease.nim.uikit.contact.core.item.ItemTypes;
import com.netease.nim.uikit.contact.core.item.MsgItem;
import com.netease.nim.uikit.contact.core.model.ContactDataAdapter;
import com.netease.nim.uikit.contact.core.provider.ContactDataProvider;
import com.netease.nim.uikit.contact.core.provider.MsgDataProvider;
import com.netease.nim.uikit.contact.core.query.IContactDataProvider;
import com.netease.nim.uikit.contact.core.query.TextQuery;
import com.netease.nim.uikit.contact.core.viewholder.LabelHolder;
import com.netease.nim.uikit.contact.core.viewholder.MsgHolder;
import com.netease.nim.uikit.model.ToolBarOptions;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.search.model.MsgIndexRecord;

import java.util.ArrayList;
import java.util.List;

public class SearchMsgDetailActivity extends UI implements AdapterView.OnItemClickListener {

    private static final String EXTRA_SESSION_TYPE = "EXTRA_SESSION_TYPE";

    private static final String EXTRA_SESSION_ID = "EXTRA_SESSION_ID";

    private static final String EXTRA_QUERY = "EXTRA_QUERY";

    private static final String EXTRA_RESULT_COUNT = "EXTRA_RESULT_COUNT";

    private ContactDataAdapter adapter;

    private AutoRefreshListView lvContacts;

    private String sessionId;

    private SessionTypeEnum sessionType;

    private String query;

    private int resultCount;
    private List<AbsContactItem> dataList;

    public static final void start(Context context, MsgIndexRecord record) {
        Intent intent = new Intent();
        intent.setClass(context, SearchMsgDetailActivity.class);
        intent.putExtra(EXTRA_SESSION_TYPE, record.getSessionType().getValue());
        intent.putExtra(EXTRA_SESSION_ID, record.getSessionId());
        intent.putExtra(EXTRA_QUERY, record.getQuery());
        intent.putExtra(EXTRA_RESULT_COUNT, record.getCount());

        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        parseIntent();

        setContentView(R.layout.activity_search_msg_detail);

        // title name
        ToolBarOptions options = new ToolBarOptions();
        if (sessionType == SessionTypeEnum.P2P) {
            options.titleString = NimUserInfoCache.getInstance().getUserDisplayName(sessionId);
        } else if (sessionType == SessionTypeEnum.Team) {
            options.titleString = TeamDataCache.getInstance().getTeamName(sessionId);
        }
        setToolBar(R.id.toolbar, options);

        // textView tip
        String tip = String.format("共%d条与\"%s\"相关的聊天记录", resultCount, query);
        TextView tipTextView = findView(R.id.search_result_tip);
        tipTextView.setText(tip);

        // listView adapter
        lvContacts = findView(R.id.search_result_list);
        IContactDataProvider dataProvider = new ContactDataProviderSearch(new ArrayList<AbsContactItem>(), ItemTypes.MSG);

        adapter = new ContactDataAdapter(this, null, dataProvider){
            @Override
            protected void onPostLoad(boolean empty, String query, boolean all) {
                super.onPostLoad(empty, query, all);
                lvContacts.onRefreshComplete();
            }
        };
        adapter.addViewHolder(ItemTypes.LABEL, LabelHolder.class);
        adapter.addViewHolder(ItemTypes.MSG, MsgHolder.class);

        lvContacts.setMode(AutoRefreshListView.Mode.END);
        lvContacts.setAdapter(adapter);
        lvContacts.setOnItemClickListener(this);
        lvContacts.setOnRefreshListener(new AutoRefreshListView.OnRefreshListener() {
            @Override
            public void onRefreshFromStart() {
            }

            @Override
            public void onRefreshFromEnd() {
                // query data
                if (dataList != null && dataList.size() < resultCount) {
                    TextQuery textQuery = new TextQuery(query);
                    textQuery.extra = new Object[]{sessionType, sessionId,  ((MsgItem)(dataList.get(dataList.size() - 1))).getRecord()};

                    adapter.query(textQuery);
                } else {
                    lvContacts.onRefreshComplete();
                }
            }
        });
        // query data
        TextQuery textQuery = new TextQuery(query);
        textQuery.extra = new Object[]{sessionType, sessionId, new MsgIndexRecord(null, query)};
        adapter.query(textQuery);

    }

    private void parseIntent() {
        sessionType = SessionTypeEnum.typeOfValue(getIntent().getIntExtra(EXTRA_SESSION_TYPE, 0));
        sessionId = getIntent().getStringExtra(EXTRA_SESSION_ID);
        query = getIntent().getStringExtra(EXTRA_QUERY);
        resultCount = getIntent().getIntExtra(EXTRA_RESULT_COUNT, 0);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AbsContactItem item = (AbsContactItem) adapter.getItem(position - lvContacts.getHeaderViewsCount());
        switch (item.getItemType()) {
            case ItemTypes.MSG: {
                MsgIndexRecord msgIndexRecord = ((MsgItem) item).getRecord();
                if (msgIndexRecord.getSessionType() == SessionTypeEnum.P2P) {
                    SessionHelper.startP2PSession(this, msgIndexRecord.getSessionId(), msgIndexRecord.getMessage());
                } else if (msgIndexRecord.getSessionType() == SessionTypeEnum.Team) {
                    SessionHelper.startTeamSession(this, msgIndexRecord.getSessionId(), msgIndexRecord.getMessage());
                }
                break;
            }

            default:
                break;
        }
    }

    private class ContactDataProviderSearch extends ContactDataProvider {

        public ContactDataProviderSearch(List<AbsContactItem> data, int... itemTypes) {
            super(itemTypes);
            dataList = data;
        }

        @Override
        public List<AbsContactItem> provide(TextQuery query) {
            dataList.addAll(MsgDataProvider.provide(query));
            return dataList;
        }
    }

}

