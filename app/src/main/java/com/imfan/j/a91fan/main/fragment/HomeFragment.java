package com.imfan.j.a91fan.main.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imfan.j.a91fan.R;
import com.imfan.j.a91fan.uiabout.ChangeColorIconWithText;
import com.imfan.j.a91fan.uiabout.TabCustomeFragment;
import com.netease.nim.uikit.common.fragment.TFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jay on 17-2-6.
 */

public class HomeFragment extends TFragment implements View.OnClickListener,
        ViewPager.OnPageChangeListener {

    Toolbar toolbar;
    private View rootView;
    private String[] mTitles = new String[]
            { "First Fragment !", "Second Fragment !", "Third Fragment !",
                    "Fourth Fragment !" };


    // 要显示的fragment集合
    private List<Fragment> fragments = new ArrayList<Fragment>();

    // fragment 适配器
    private FragmentPagerAdapter adapter;

    // fragment的page
    private ViewPager pager;

    // 下面的导航按钮兼指示器
    private List<ChangeColorIconWithText> tabIndicators = new ArrayList<ChangeColorIconWithText>();

    private ChangeColorIconWithText chatRoom, message, blogWall, myProfile;

    public HomeFragment() {
        setContainerId(R.id.home_container); // 在MainActivity的唯一组件里占上位置
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // setToolBar(R.id.toolbar, R.string.app_name, R.mipmap.icon);

       // setTitle(R.string.app_name);

        findViews();
        initData();
        setupPager();
        initEvent();
        /*registerMsgUnreadInfoObserver(true);
        registerSystemMessageObservers(true);
        requestSystemMessageUnreadCount();
        initUnreadCover();*/
    }

    /**
     * 点击Tab按钮，在主页面间进行导航
     *
     * @param v
     */
    private void clickTab(View v)
    {
        resetOtherTabs();

        switch (v.getId())
        {
            case R.id.id_indicator_one:
                tabIndicators.get(0).setIconAlpha(1.0f);
                pager.setCurrentItem(0, false);
                break;
            case R.id.id_indicator_two:
                tabIndicators.get(1).setIconAlpha(1.0f);
                pager.setCurrentItem(1, false);
                break;
            case R.id.id_indicator_three:
                tabIndicators.get(2).setIconAlpha(1.0f);
                pager.setCurrentItem(2, false);
                break;
            case R.id.id_indicator_four:
                tabIndicators.get(3).setIconAlpha(1.0f);
                pager.setCurrentItem(3, false);
                break;
        }
    }

    /**
     * 重置其他的TabIndicator的颜色
     */
    private void resetOtherTabs()
    {
        for (int i = 0; i < tabIndicators.size(); i++)
        {
            tabIndicators.get(i).setIconAlpha(0);
        }
    }


    /*
    初始化事件
     */
    private void initEvent(){
        chatRoom.setOnClickListener(this);
        message.setOnClickListener(this);
        blogWall.setOnClickListener(this);
        myProfile.setOnClickListener(this);
        pager.setOnPageChangeListener(this);
    }

    /*
    初始化数据
     */
    private void initData(){
        for (String title : mTitles)
        {
            TabCustomeFragment tabCustomeFragment = new TabCustomeFragment();
            Bundle bundle = new Bundle();
            bundle.putString(TabCustomeFragment.TITLE, title);
            tabCustomeFragment.setArguments(bundle);
            fragments.add(tabCustomeFragment);
        }



        /*把 getFragmentManager（） 方法 换成 getChildFragmentManager（）；
        因为你已经在fragment里面了，不能再次得到到外层的manager，只能用孩子的manager。*/
        adapter = new FragmentPagerAdapter(getChildFragmentManager())
        {

            @Override
            public int getCount()
            {
                return fragments.size();
            }

            @Override
            public Fragment getItem(int position)
            {
                return fragments.get(position);
            }
        };
    }

    public boolean onBackPressed() {
        return false;
    }

    /**
     * 查找页面控件
     */
    private void findViews() {
        toolbar = (Toolbar) findView(R.id.toolbar);

        toolbar.setContentInsetsAbsolute(10,0);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        pager = findView(R.id.id_viewpager);

        // 四个导航键
        chatRoom = findView(R.id.id_indicator_one);
        message = findView(R.id.id_indicator_two);
        blogWall = findView(R.id.id_indicator_three);
        myProfile = findView(R.id.id_indicator_four);
        tabIndicators.add(chatRoom);
        tabIndicators.add(message);
        tabIndicators.add(blogWall);
        tabIndicators.add(myProfile);



        chatRoom.setIconAlpha(1.0f);


    }

    /**
     * 设置viewPager
     */
    private void setupPager() {


        pager.setOffscreenPageLimit(fragments.size());


        // ADAPTER
        pager.setAdapter(adapter);

        pager.setOnPageChangeListener(this);


    }



    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (positionOffset > 0)
        {
            ChangeColorIconWithText left = tabIndicators.get(position);
            ChangeColorIconWithText right = tabIndicators.get(position + 1);
            left.setIconAlpha(1 - positionOffset);
            right.setIconAlpha(positionOffset);
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        clickTab(v);
    }
}
