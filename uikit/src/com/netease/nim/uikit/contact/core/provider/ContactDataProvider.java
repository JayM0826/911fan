package com.netease.nim.uikit.contact.core.provider;

import android.os.Handler;
import android.os.Looper;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.contact.core.item.AbsContactItem;
import com.netease.nim.uikit.contact.core.item.ContactItem;
import com.netease.nim.uikit.contact.core.item.ItemTypes;
import com.netease.nim.uikit.contact.core.query.IContactDataProvider;
import com.netease.nim.uikit.contact.core.query.TextQuery;
import com.netease.nim.uikit.contact.core.util.ContactHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * 在各种分组中搜索query语句
 */

/* 所有数据总的Provider*/
public class ContactDataProvider implements IContactDataProvider {

    /*实现了接口中的方法*/
    private static List<AbsContactItem> data;
    private int[] itemTypes;

    /*构造方法*/
    public ContactDataProvider(int... itemTypes) {
        this.itemTypes = itemTypes;
    }

    public static List<AbsContactItem> getData() {
        return data;
    }

    public static void clearData(){
        data = null;
    }

    @Override
    public List<AbsContactItem> provide(TextQuery query) {
        data = new ArrayList<>();

        /*将所有的查询的特定类型的返回结果都加入到这个data里面去，然后在ContactDataTask
        的run方法中进行provide的调用。*/
        for (int itemType : itemTypes) {
            if (itemType == 1){

                UserProvider.provide(query);
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {

                        data.add(UserProvider.items);
                        if (UserProvider.items == null){
                            LogUtil.i("ContactDataProvider:", "返回的数据是null！");
                        }else {
                            LogUtil.i("ContactDataProvider:", "这条信息在下面就不会阻塞");
                        }
                    }
                };
                new Handler(Looper.getMainLooper()).postDelayed(runnable, 165);
                LogUtil.i("检测阻塞情况", "看看会不会阻塞,其实是不会阻塞的");
            }else{
                data.addAll(provide(itemType, query));
            }
        }

        return null;

    }

    /*在特定的itemType中搜索query语句*/
    private final List<AbsContactItem> provide(int itemType, TextQuery query) {
        switch (itemType) {
            case ItemTypes.FRIEND:
                return UserDataProvider.provide(query);
            case ItemTypes.TEAM:
            case ItemTypes.TEAMS.ADVANCED_TEAM:
            case ItemTypes.TEAMS.NORMAL_TEAM:
                return TeamDataProvider.provide(query, itemType);
            case ItemTypes.MSG:
                return MsgDataProvider.provide(query);
            default:
                return new ArrayList<>();
        }
    }
}
