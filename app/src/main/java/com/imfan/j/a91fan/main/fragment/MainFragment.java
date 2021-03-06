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

package com.imfan.j.a91fan.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imfan.j.a91fan.R;
import com.imfan.j.a91fan.main.model.MainTab;
import com.netease.nim.uikit.common.fragment.TFragment;


/**
 * Created by jay on 17-2-8.
 */

public abstract class MainFragment extends TFragment {
    private boolean loaded = false;

    private MainTab tabData;

    protected abstract void onInit();

    protected boolean inited() {
        return loaded;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_tab_fragment_container, container, false);

        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void attachTabData(MainTab tabData) {
        this.tabData = tabData;
    }



    // 执行加载真实的布局，然后通过onInit的抽象方法在具体fragment实现出来，
    // 进行页面的初始化
    public void onCurrent() {
        if (!loaded && loadRealLayout()) {
            loaded = true;
            onInit();
        }
    }

    // 加载真实的布局
    private boolean loadRealLayout() {
        ViewGroup root = (ViewGroup) getView();
        if (root != null) {
            root.removeAllViewsInLayout();
            View.inflate(root.getContext(), tabData.layoutId, root);

        }
        return root != null;
    }
}
