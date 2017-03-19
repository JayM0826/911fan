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

package com.imfan.j.a91fan.textabout;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.imfan.j.a91fan.R;
import com.imfan.j.a91fan.uiabout.CustomEditText;
import com.imfan.j.a91fan.util.CustomToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateGroupActivitry extends Activity {


    @BindView(R.id.et_group_name)
    CustomEditText mGroupName;

    @BindView(R.id.btn_ok)
    Button btnOK;

    @OnClick(R.id.btn_ok)
    void create(){
        if (mGroupName.getText().toString().equals("")){
            CustomToast.show(this, "名称不能为空");
            return;
        }

        Intent intent = new Intent(this, GroupListActivity.class);
        intent.putExtra("name", mGroupName.getText().toString());
        setResult(RESULT_OK, intent);
        // 这里并没有跳转，直接finish
        finish();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group_activitry);

        ButterKnife.bind(this);


    }
}
