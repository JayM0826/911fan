package com.imfan.j.a91fan.main.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.ViewConfiguration;

import com.imfan.j.a91fan.R;
import com.imfan.j.a91fan.main.fragment.HomeFragment;
import com.imfan.j.a91fan.uiabout.ChangeColorIconWithText;
import com.netease.nim.uikit.common.activity.UI;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends UI {

    private final String TAG = getClass().getSimpleName();
    private HomeFragment homeFragment;
    private ViewPager mViewPager;
    private List<Fragment> mTabs = new ArrayList<Fragment>();
    private String[] mTitles = new String[]
            { "First Fragment !", "Second Fragment !", "Third Fragment !",
                    "Fourth Fragment !" };
    private FragmentPagerAdapter mAdapter;
    // 下面的导航栏
    private List<ChangeColorIconWithText> mTabIndicators = new ArrayList<ChangeColorIconWithText>();

    public static void start(Context context) {
        start(context, null);
    }

    public static void start(Context context, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(context, MainActivity.class);
        // 日后如果有问题记着改参数
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        if (extras != null) {
            intent.putExtras(extras);
        }
        context.startActivity(intent);  // 启动MainActivity

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        if (homeFragment == null && !isDestroyedCompatible()) {
            homeFragment = new HomeFragment();
            switchFragmentContent(homeFragment);
        }

         // setOverflowButtonAlways();
         // getActionBar().setDisplayShowHomeEnabled(false);




    }






    private void setOverflowButtonAlways()
    {
        try
        {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKey = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            menuKey.setAccessible(true);
            menuKey.setBoolean(config, false);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }


}
