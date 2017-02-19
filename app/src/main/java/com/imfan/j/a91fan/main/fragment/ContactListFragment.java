package com.imfan.j.a91fan.main.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.imfan.j.a91fan.R;
import com.imfan.j.a91fan.main.activity.BlackListActivity;
import com.imfan.j.a91fan.main.activity.SystemMessageActivity;
import com.imfan.j.a91fan.main.activity.TeamListActivity;
import com.imfan.j.a91fan.main.helper.SystemMessageUnreadManager;
import com.imfan.j.a91fan.main.model.MainTab;
import com.imfan.j.a91fan.main.reminder.ReminderId;
import com.imfan.j.a91fan.main.reminder.ReminderItem;
import com.imfan.j.a91fan.main.reminder.ReminderManager;
import com.imfan.j.a91fan.session.SessionHelper;
import com.imfan.j.a91fan.util.Cache;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.contact.ContactsCustomization;
import com.netease.nim.uikit.contact.ContactsFragment;
import com.netease.nim.uikit.contact.core.item.AbsContactItem;
import com.netease.nim.uikit.contact.core.item.ItemTypes;
import com.netease.nim.uikit.contact.core.model.ContactDataAdapter;
import com.netease.nim.uikit.contact.core.viewholder.AbsContactViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jay on 17-2-19.
 */

public class ContactListFragment extends MainFragment {

    private ContactsFragment fragment;

    public ContactListFragment() {
        setContainerId(MainTab.CONTACT.fragmentId);
    }

    /**
     * ******************************** 生命周期 ***********************************
     */

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        onCurrent(); // 触发onInit，提前加载
    }

    @Override
    protected void onInit() {
        addContactFragment();  // 集成通讯录页面
    }

    // 将通讯录列表fragment动态集成进来。 开发者也可以使用在xml中配置的方式静态集成。
    private void addContactFragment() {
        fragment = new ContactsFragment();
        fragment.setContainerId(R.id.contact_fragment);

        UI activity = (UI) getActivity();

        // 如果是activity从堆栈恢复，FM中已经存在恢复而来的fragment，此时会使用恢复来的，而new出来这个会被丢弃掉
        fragment = (ContactsFragment) activity.addFragment(fragment);

        // 功能项定制
        fragment.setContactsCustomization(new ContactsCustomization() {
            @Override
            public Class<? extends AbsContactViewHolder<? extends AbsContactItem>> onGetFuncViewHolderClass() {
                return FuncItem.FuncViewHolder.class;
            }

            @Override
            public List<AbsContactItem> onGetFuncItems() {
                return FuncItem.provide();
            }

            @Override
            public void onFuncItemClick(AbsContactItem item) {
                FuncItem.handle(getActivity(), item);
            }
        });
    }

    /**
     * ******************************** 功能项定制 ***********************************
     */
    public final static class FuncItem extends AbsContactItem {
        static final FuncItem VERIFY = new FuncItem();
        // static final FuncItem NORMAL_TEAM = new FuncItem();
        static final FuncItem ADVANCED_TEAM = new FuncItem();
        static final FuncItem BLACK_LIST = new FuncItem();
        // static final FuncItem MY_COMPUTER = new FuncItem();

        static List<AbsContactItem> provide() {
            List<AbsContactItem> items = new ArrayList<AbsContactItem>();
            items.add(VERIFY);
            // items.add(NORMAL_TEAM);
            items.add(ADVANCED_TEAM);
            items.add(BLACK_LIST);
            // items.add(MY_COMPUTER);

            return items;
        }

        static void handle(Context context, AbsContactItem item) {
            if (item == VERIFY) {
                SystemMessageActivity.start(context);
            }  else if (item == ADVANCED_TEAM) {
                TeamListActivity.start(context, ItemTypes.TEAMS.ADVANCED_TEAM);
            } else if (item == BLACK_LIST) {
                BlackListActivity.start(context);
            }
        }

        @Override
        public int getItemType() {
            return ItemTypes.FUNC;
        }

        @Override
        public String belongsGroup() {
            return null;
        }

        public static final class FuncViewHolder extends AbsContactViewHolder<FuncItem> {
            private ImageView image;
            private TextView funcName;
            private TextView unreadNum;

            @Override
            public View inflate(LayoutInflater inflater) {
                View view = inflater.inflate(R.layout.func_contacts_item, null);
                this.image = (ImageView) view.findViewById(R.id.img_head);
                this.funcName = (TextView) view.findViewById(R.id.tv_func_name);
                this.unreadNum = (TextView) view.findViewById(R.id.tab_new_msg_label);
                return view;
            }

            @Override
            public void refresh(ContactDataAdapter contactAdapter, int position, FuncItem item) {
                if (item == VERIFY) {
                    funcName.setText("验证提醒");
                    image.setImageResource(R.drawable.icon_verify_remind);
                    image.setScaleType(ImageView.ScaleType.FIT_XY);
                    int unreadCount = SystemMessageUnreadManager.getInstance().getSysMsgUnreadCount();
                    updateUnreadNum(unreadCount);

                    ReminderManager.getInstance().registerUnreadNumChangedCallback(new ReminderManager.UnreadNumChangedCallback() {
                        @Override
                        public void onUnreadNumChanged(ReminderItem item) {
                            if (item.getId() != ReminderId.CONTACT) {
                                return;
                            }

                            updateUnreadNum(item.getUnread());
                        }
                    });
                } else if (item == ADVANCED_TEAM) {
                    funcName.setText("加入的群");
                    image.setImageResource(R.drawable.ic_advanced_team);
                } else if (item == BLACK_LIST) {
                    funcName.setText("黑名单");
                    image.setImageResource(R.drawable.ic_black_list);
                }

                if (item != VERIFY) {
                    image.setScaleType(ImageView.ScaleType.FIT_XY);
                    unreadNum.setVisibility(View.GONE);
                }
            }

            private void updateUnreadNum(int unreadCount) {
                // 2.*版本viewholder复用问题
                if (unreadCount > 0 && funcName.getText().toString().equals("验证提醒")) {
                    unreadNum.setVisibility(View.VISIBLE);
                    unreadNum.setText("" + unreadCount);
                } else {
                    unreadNum.setVisibility(View.GONE);
                }
            }
        }
    }

   /* @Override
    public void onCurrentTabClicked() {
        // 点击切换到当前TAB
        if (fragment != null) {
            fragment.scrollToTop();
        }
    }*/
}
