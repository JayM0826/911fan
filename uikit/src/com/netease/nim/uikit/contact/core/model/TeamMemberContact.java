package com.netease.nim.uikit.contact.core.model;

import com.netease.nim.uikit.cache.TeamDataCache;
import com.netease.nimlib.sdk.team.model.TeamMember;

/**
 * Created by huangjun on 2015/5/5.
 */

/*扩展了AbsContanct，意味着要实现IContact接口内的方法,getContactType(),getContactId(),getDisplayName();


该类将搜索出的群成员结果以用户id，用户名，组名展示出来*/

public class TeamMemberContact extends AbsContact {

    private TeamMember teamMember;

    public TeamMemberContact(TeamMember teamMember) {
        this.teamMember = teamMember;
    }

    @Override
    public String getContactId() {
        return teamMember.getAccount();
    }

    @Override
    public int getContactType() {
        return Type.TeamMember;
    }

    @Override
    public String getDisplayName() {
        return TeamDataCache.getInstance().getTeamMemberDisplayName(teamMember.getTid(), teamMember.getAccount());
    }
}
