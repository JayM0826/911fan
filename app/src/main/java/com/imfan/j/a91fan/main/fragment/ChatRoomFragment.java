package com.imfan.j.a91fan.main.fragment;

/*Copyright 2017, Yalantis

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

import android.os.Bundle;
import android.widget.TextView;

import com.imfan.j.a91fan.R;
import com.imfan.j.a91fan.util.CustomToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jay on 17-2-6.
 * 聊天大厅页面
 */

public class ChatRoomFragment extends MainFragment {


   /* @Bind(R.id.rv_chatroom_list)
    RecyclerView username;*/

    @BindView(R.id.tv_test) TextView textView;
    @OnClick(R.id.tv_test)
    void submit(){
        CustomToast.show(getContext(), "这尼玛真坑啊");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onCurrent();
    }

    @Override
    protected void onInit() {
        textView.setText("这真他妈坑爹啊");
        ButterKnife.bind(getActivity());
    }


}
