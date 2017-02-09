package com.imfan.j.a91fan.main.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.imfan.j.a91fan.main.fragment.MainFragment;
import com.imfan.j.a91fan.main.model.MainTab;
import com.netease.nim.uikit.common.fragment.TFragment;

import java.util.List;

/**
 * Created by jay on 17-2-6.
 */

public class MainPagerAdapter extends FragmentPagerAdapter {

    protected final TFragment[] fragments;
    protected final Context context;
    private final ViewPager pager;
    private int lastPostion = 0;

    public MainPagerAdapter(FragmentManager fm, int count, Context context, ViewPager pager) {
        super(fm);
        this.fragments = new MainFragment[count];
        this.context = context;
        this.pager = pager;
        lastPostion = 0;

        for (MainTab tab : MainTab.values()) {
            try {
                MainFragment fragment = null;

                List<Fragment> fs = fm.getFragments();
                if (fs != null) {
                    for (Fragment f : fs) {
                        if (f.getClass() == tab.clazz) {
                            fragment = (MainFragment) f;
                            break;
                        }
                    }
                }

                if (fragment == null) {
                    fragment = tab.clazz.newInstance();
                }
                fragment.attachTabData(tab);

                fragments[tab.tabIndex] = fragment;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }



    }

    @Override
    public Fragment getItem(int position)
    {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return MainTab.values().length;
    }

    public void onPageSelected(int position) {
        TFragment fragment = getFragmentByPosition(position);

        // INSTANCE
        if (fragment == null) {
            return;
        }
        onLeave(position);
    }

    private void onLeave(int position) {
        TFragment fragment = getFragmentByPosition(lastPostion);
        lastPostion = position;
        // INSTANCE
        if (fragment == null) {
            return;
        }
    }



    public void onPageScrolled(int position) {
        TFragment fragment = getFragmentByPosition(position);

        // INSTANCE
        if (fragment == null) {
            return;
        }
        onLeave(position);
    }

    private TFragment getFragmentByPosition(int position) {
        // IDX
        if (position < 0 || position >= fragments.length) {
            return null;
        }
        return fragments[position];
    }

   /* @Override
    public void onCurrentTabClicked(int position) {

        TabFragment fragment = getFragmentByPosition(position);

        // INSTANCE
        if (fragment == null) {
            return;
        }

        fragment.onCurrentTabClicked();
    }

    @Override
    public void onCurrentTabDoubleTap(int position) {
        TabFragment fragment = getFragmentByPosition(position);

        // INSTANCE
        if (fragment == null) {
            return;
        }

        fragment.onCurrentTabDoubleTap();
    }*/
}
