package com.netease.nim.uikit.contact.core.provider;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.contact.core.item.ItemTypes;
import com.netease.nim.uikit.contact.core.model.ContactGroupStrategy;
import com.netease.nim.uikit.contact.core.item.AbsContactItem;
import com.netease.nim.uikit.contact.core.item.ContactItem;
import com.netease.nim.uikit.contact.core.model.TeamContact;
import com.netease.nim.uikit.contact.core.query.TextComparator;
import com.netease.nim.uikit.contact.core.query.TextQuery;
import com.netease.nim.uikit.cache.TeamDataCache;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.model.Team;

import java.util.ArrayList;
import java.util.List;

/**
 * 群数据源提供者
 * <p/>
 * Created by huangjun on 2015/3/1.
 */

/**
 *                             _ooOoo_
 *                            o8888888o
 *                            88" . "88
 *                            (| -_- |)
 *                            O\  =  /O
 *                         ____/`---'\____
 *                       .'  \\|     |//  `.
 *                      /  \\|||  :  |||//  \
 *                     /  _||||| -:- |||||-  \
 *                     |   | \\\  -  /// |   |
 *                     | \_|  ''\---/''  |   |
 *                     \  .-\__  `-`  ___/-. /
 *                   ___`. .'  /--.--\  `. . __
 *                ."" '<  `.___\_<|>_/___.'  >'"".
 *               | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 *               \  \ `-.   \_ __\ /__ _/   .-` /  /
 *          ======`-.____`-.___\_____/___.-`____.-'======
 *                             `=---='
 *          ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 *                     佛祖保佑        永无BUG
 *            佛曰:
 *                   写字楼里写字间，写字间里程序员；
 *                   程序人员写程序，又拿程序换酒钱。
 *                   酒醒只在网上坐，酒醉还来网下眠；
 *                   酒醉酒醒日复日，网上网下年复年。
 *                   但愿老死电脑间，不愿鞠躬老板前；
 *                   奔驰宝马贵者趣，公交自行程序员。
 *                   别人笑我忒疯癫，我笑自己命太贱；
 *                   不见满街漂亮妹，哪个归得程序员？
*/
public class TeamDataProvider {
    public static final List<AbsContactItem> provide(TextQuery query, int itemType) {
        List<TeamContact> sources = query(query, itemType);
        List<AbsContactItem> items = new ArrayList<>(sources.size());
        for (TeamContact t : sources) {
            items.add(createTeamItem(t));
        }

        return items;
    }

    private static AbsContactItem createTeamItem(TeamContact team) {
        return new ContactItem(team, ItemTypes.TEAM) {
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

    /**
     * * 数据查询
     */
    private static final List<TeamContact> query(TextQuery query, int itemType) {
        List<Team> teams;
        if (itemType == ItemTypes.TEAMS.ADVANCED_TEAM) {
            teams = TeamDataCache.getInstance().getAllAdvancedTeams();
        } else if (itemType == ItemTypes.TEAMS.NORMAL_TEAM) {
            teams = TeamDataCache.getInstance().getAllNormalTeams();
        } else {
            teams = TeamDataCache.getInstance().getAllTeams();
        }

        List<TeamContact> contacts = new ArrayList<>();
        for (Team t : teams) {
            if (query == null || ContactSearch.hitTeam(t, query)) {
                contacts.add(new TeamContact(t));
            }
        }

        return contacts;
    }
}

