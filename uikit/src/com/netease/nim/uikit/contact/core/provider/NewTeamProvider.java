package com.netease.nim.uikit.contact.core.provider;

import android.os.Handler;
import android.os.Looper;

import com.netease.nim.uikit.cache.TeamDataCache;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.contact.core.item.AbsContactItem;
import com.netease.nim.uikit.contact.core.item.ContactItem;
import com.netease.nim.uikit.contact.core.item.ItemTypes;
import com.netease.nim.uikit.contact.core.model.ContactGroupStrategy;
import com.netease.nim.uikit.contact.core.model.TeamContact;
import com.netease.nim.uikit.contact.core.query.TextComparator;
import com.netease.nim.uikit.contact.core.query.TextQuery;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.model.Team;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jay on 17-2-18.
 */

public class NewTeamProvider {

    /**
     * * 数据查询
     */

    static AbsContactItem items;

    private static AbsContactItem createTeamItem(TeamContact team) {
        return new ContactItem(team, ItemTypes.TEAM_UNJOIN) {
            @Override
            public int compareTo(ContactItem item) {
                return compareTeam((TeamContact) getContact(), (TeamContact) (item.getContact()));
            }

            @Override
            public String belongsGroup() {
                return ContactGroupStrategy.GROUP_TEAM;
            }
        };
    }

    private static int compareTeam(TeamContact lhs, TeamContact rhs) {
        return TextComparator.compareIgnoreCase(lhs.getDisplayName(), rhs.getDisplayName());
    }

    public static final void provide(TextQuery query) {

        NIMClient.getService(TeamService.class).searchTeam(query.text).setCallback(new RequestCallback<Team>() {
            @Override
            public void onSuccess(Team team) {
                items = null;
                LogUtil.e("搜索群时搜到了", "搜到了搜到了");
                items = createTeamItem(new TeamContact(team));
            }

            @Override
            public void onFailed(int code) {
                items = null;
                LogUtil.e("搜索群时没搜到,失败", "真的什么群也没搜到" + code);
            }

            @Override
            public void onException(Throwable exception) {
                items = null;
                LogUtil.e("搜索群时出现异常", "出现异常");
            }
        });


    }
}
