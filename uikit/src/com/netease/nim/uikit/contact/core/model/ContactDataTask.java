package com.netease.nim.uikit.contact.core.model;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.netease.nim.uikit.CustomToast;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.contact.core.item.AbsContactItem;
import com.netease.nim.uikit.contact.core.item.ContactItemFilter;
import com.netease.nim.uikit.contact.core.provider.ContactDataProvider;
import com.netease.nim.uikit.contact.core.query.IContactDataProvider;
import com.netease.nim.uikit.contact.core.query.TextQuery;

import java.util.List;

/**
 * 通讯录获取数据任务
 * Created by huangjun on 2015/2/10.
 */
public class ContactDataTask {

    private final IContactDataProvider dataProvider; // 数据源提供者
    private final ContactItemFilter filter; // 项过滤器
    private final TextQuery query; // 要搜索的信息，null为查询所有
    private Host host;

    private Context context;

    public ContactDataTask(Context context, TextQuery query, IContactDataProvider dataProvider, ContactItemFilter filter) {
        this.query = query;
        this.dataProvider = dataProvider;
        this.filter = filter;
        this.context = context;
    }

    private static void add(AbsContactDataList datas, List<AbsContactItem> items, ContactItemFilter filter) {
        if (items == null){
            return;
        }
        for (AbsContactItem item : items) {
            if (filter == null || !filter.filter(item)) {
                datas.add(item);
            }
        }

    }

    public final void setHost(Host host) {
        this.host = host;
    }

    protected void onPreProvide(AbsContactDataList datas) {

    }


    public final void run(final AbsContactDataList datas) {
        // CANCELLED
        if (isCancelled()) {
            return;
        }

        // PRE PROVIDE
        onPreProvide(datas);

        // CANCELLED
        if (isCancelled()) {
            return;
        }

        dataProvider.provide(query);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // PROVIDE

                List<AbsContactItem> items = ContactDataProvider.getData();

                if (items == null || items.isEmpty()) {
                    CustomToast.show(context, "冷冷清清,什么也没搜到");
                }
                    add(datas, items, filter);
                    ContactDataProvider.clearData();
                    items = null; // 清空
                    // BUILD
                    datas.build();

                    // PUBLISH ALL
                    publish(datas, true);




            }
        };

        new Handler(Looper.getMainLooper()).postDelayed(runnable, 240);

    }

    private void publish(AbsContactDataList datas, boolean all) {
        if (host != null) {
            datas.setQuery(query);

            host.onData(this, datas, all);
        }
    }

    private boolean isCancelled() {
        return host != null && host.isCancelled(this);
    }

    public interface Host {
        public void onData(ContactDataTask task, AbsContactDataList datas, boolean all); // 搜索完成，返回数据给调用方

        public boolean isCancelled(ContactDataTask task); // 判断调用方是否已经取消
    }
}