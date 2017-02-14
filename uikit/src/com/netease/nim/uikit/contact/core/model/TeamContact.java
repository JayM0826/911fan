package com.netease.nim.uikit.contact.core.model;

import android.text.TextUtils;

import com.netease.nimlib.sdk.team.model.Team;

/*扩展了AbsContanct，意味着要实现IContact接口内的方法,getContactType(),getContactId(),getDisplayName();


群的展示面板只显示群头像与群名字*/
public class TeamContact extends AbsContact {

    private Team team;

    public TeamContact(Team team) {
        this.team = team;
    }

    @Override
    public String getContactId() {
        return team == null ? "" : team.getId();
    }

    @Override
    public int getContactType() {
        return IContact.Type.Team;
    }

    @Override
    public String getDisplayName() {
        String name = team.getName();

        return TextUtils.isEmpty(name) ? team.getId() : name;
    }

    public Team getTeam() {
        return team;
    }
}
