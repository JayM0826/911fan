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

package com.imfan.j.a91fan.team;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.imfan.j.a91fan.R;
import com.imfan.j.a91fan.util.CustomToast;
import com.netease.nim.uikit.cache.SimpleCallback;
import com.netease.nim.uikit.cache.TeamDataCache;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.model.ToolBarOptions;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.constant.TeamTypeEnum;
import com.netease.nimlib.sdk.team.model.Team;

public class AdvancedTeamJoinActivity extends UI implements View.OnClickListener {

    private static final String EXTRA_ID = "EXTRA_ID";

    private String teamId;
    private Team team;

    private TextView teamNameText;
    private TextView memberCountText;
    private TextView teamTypeText;
    private Button applyJoinButton;

    public static void start(Context context, String teamId) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_ID, teamId);
        intent.setClass(context, AdvancedTeamJoinActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_advanced_team_join);

        ToolBarOptions options = new ToolBarOptions();
        options.titleId = R.string.team_join;
        setToolBar(R.id.toolbar, options);

        findViews();
        parseIntentData();
        requestTeamInfo();
    }

    private void findViews() {
        teamNameText = (TextView) findViewById(R.id.team_name);
        memberCountText = (TextView) findViewById(R.id.member_count);
        applyJoinButton = (Button) findViewById(R.id.apply_join);
        teamTypeText = (TextView) findViewById(R.id.team_type);
        applyJoinButton.setOnClickListener(this);
    }

    private void parseIntentData() {
        teamId = getIntent().getStringExtra(EXTRA_ID);
    }

    private void requestTeamInfo() {
        Team t = TeamDataCache.getInstance().getTeamById(teamId);
        if (t != null) {
            updateTeamInfo(t);
        } else {
            TeamDataCache.getInstance().fetchTeamById(teamId, new SimpleCallback<Team>() {
                @Override
                public void onResult(boolean success, Team result) {
                    if (success && result != null) {
                        updateTeamInfo(result);
                    }
                }
            });
        }
    }

    /**
     * 更新群信息
     *
     * @param t 群
     */
    private void updateTeamInfo(final Team t) {
        if (t == null) {
            Toast.makeText(AdvancedTeamJoinActivity.this, R.string.team_not_exist, Toast.LENGTH_LONG).show();
            finish();
        } else {
            team = t;
            teamNameText.setText(team.getName());
            memberCountText.setText(team.getMemberCount() + "人");
            if (team.getType() == TeamTypeEnum.Advanced) {
                teamTypeText.setText(R.string.advanced_team);
            } else {
                teamTypeText.setText(R.string.normal_team);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (team != null) {
            NIMClient.getService(TeamService.class).applyJoinTeam(team.getId(), null).setCallback(new RequestCallback<Team>() {
                @Override
                public void onSuccess(Team team) {
                    applyJoinButton.setEnabled(false);
                    String toast = getString(R.string.team_join_success, team.getName());
                    Toast.makeText(AdvancedTeamJoinActivity.this, toast, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailed(int code) {
                    if (code == 808) {
                        applyJoinButton.setEnabled(false);
                        CustomToast.show(AdvancedTeamJoinActivity.this, R.string.team_apply_to_join_send_success);
                    } else if (code == 809) {
                        applyJoinButton.setEnabled(false);
                        CustomToast.show(AdvancedTeamJoinActivity.this, R.string.has_exist_in_team);
                    } else {
                        CustomToast.show(AdvancedTeamJoinActivity.this, "failed, error code =" + code);
                    }
                }

                @Override
                public void onException(Throwable exception) {

                }
            });
        }
    }
}
