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

package com.imfan.j.a91fan.contact.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imfan.j.a91fan.R;
import com.imfan.j.a91fan.contact.constant.UserConstant;
import com.imfan.j.a91fan.contact.helper.UserUpdateHelper;
import com.imfan.j.a91fan.myserver.SuccessOfServer;
import com.imfan.j.a91fan.retrofit.RetrofitServiceInstance;
import com.imfan.j.a91fan.util.CustomToast;
import com.imfan.j.a91fan.util.Preferences;
import com.netease.nim.uikit.cache.FriendDataCache;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.ui.widget.ClearableEditTextWithIcon;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.common.util.sys.NetworkUtil;
import com.netease.nim.uikit.common.util.sys.TimeUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.friend.constant.FriendFieldEnum;
import com.netease.nimlib.sdk.friend.model.Friend;
import com.netease.nimlib.sdk.uinfo.constant.GenderEnum;
import com.netease.nimlib.sdk.uinfo.constant.UserInfoFieldEnum;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class UserProfileEditItemActivity extends SwipeBackActivity implements View.OnClickListener {

    public static final int REQUEST_CODE = 1000;
    private static final String EXTRA_KEY = "EXTRA_KEY";
    private static final String EXTRA_DATA = "EXTRA_DATA";
    // data
    private int key;
    private String data;
    private int birthYear = 1990;
    private int birthMonth = 10;
    private int birthDay = 10;
    private Map<Integer, UserInfoFieldEnum> fieldMap;

    // VIEW
    private ClearableEditTextWithIcon editText;

    // gender layout
    private RelativeLayout maleLayout;
    private RelativeLayout femaleLayout;
    private RelativeLayout otherLayout;
    private ImageView maleCheck;
    private ImageView femaleCheck;
    private ImageView otherCheck;

    // birth layout
    private RelativeLayout birthPickerLayout;
    private TextView birthText;
    private int gender;
    private ActionBar actionBar;

    public static final void startActivity(Context context, int key, String data) {
        Intent intent = new Intent();
        intent.setClass(context, UserProfileEditItemActivity.class);
        intent.putExtra(EXTRA_KEY, key);
        intent.putExtra(EXTRA_DATA, data);
        ((Activity) context).startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseIntent();
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        if (key == UserConstant.KEY_NICKNAME || key == UserConstant.KEY_PHONE || key == UserConstant.KEY_EMAIL
                || key == UserConstant.KEY_SIGNATURE || key == UserConstant.KEY_ALIAS) {
            setContentView(R.layout.user_profile_edittext_layout);
            findEditText();
        } else if (key == UserConstant.KEY_GENDER) {
            setContentView(R.layout.user_profile_gender_layout);
            findGenderViews();
        } else if (key == UserConstant.KEY_BIRTH) {
            setContentView(R.layout.user_profile_birth_layout);
            findBirthViews();
        }

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.show();
        setTitles();
    }

    @Override
    public void onBackPressed() {
        showKeyboard(false);
        super.onBackPressed();
    }

    protected void showKeyboard(boolean isShow) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (isShow) {
            if (getCurrentFocus() == null) {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            } else {
                imm.showSoftInput(getCurrentFocus(), 0);
            }
        } else {
            if (getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    private void parseIntent() {
        key = getIntent().getIntExtra(EXTRA_KEY, -1);
        data = getIntent().getStringExtra(EXTRA_DATA);
    }

    private void setTitles() {
        switch (key) {
            case UserConstant.KEY_NICKNAME:
                setTitle(R.string.nickname);
                break;
            case UserConstant.KEY_PHONE:
                setTitle(R.string.phone_number);
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case UserConstant.KEY_EMAIL:
                setTitle(R.string.email);
                break;
            case UserConstant.KEY_SIGNATURE:
                setTitle(R.string.signature);
                break;
            case UserConstant.KEY_GENDER:
                setTitle(R.string.gender);
                break;
            case UserConstant.KEY_BIRTH:
                setTitle(R.string.birthday);
                break;
            case UserConstant.KEY_ALIAS:
                setTitle(R.string.alias);
                break;
        }
    }

    private void findEditText() {
        editText = (ClearableEditTextWithIcon)findViewById(R.id.edittext);

        // 这里是设置字数限制或者其他限制
        if (key == UserConstant.KEY_NICKNAME) {
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        } else if (key == UserConstant.KEY_PHONE) {
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
        } else if (key == UserConstant.KEY_EMAIL || key == UserConstant.KEY_SIGNATURE) {
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
        } else if (key == UserConstant.KEY_ALIAS) {
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
        }


        if (key == UserConstant.KEY_ALIAS) {
            Friend friend = FriendDataCache.getInstance().getFriendByAccount(data);
            if (friend != null && !TextUtils.isEmpty(friend.getAlias())) {
                editText.setText(friend.getAlias());
            } else {
                editText.setHint("请输入备注名...");
            }
        } else {
            editText.setText(data);
        }
        editText.setDeleteImage(R.drawable.nim_grey_delete_icon);
    }

    private void findGenderViews() {
        maleLayout = (RelativeLayout)findViewById(R.id.male_layout);
        femaleLayout = (RelativeLayout)findViewById(R.id.female_layout);
        otherLayout = (RelativeLayout)findViewById(R.id.other_layout);

        maleCheck = (ImageView) findViewById(R.id.male_check);
        femaleCheck = (ImageView) findViewById(R.id.female_check);
        otherCheck = (ImageView) findViewById(R.id.other_check);

        maleLayout.setOnClickListener(this);
        femaleLayout.setOnClickListener(this);
        otherLayout.setOnClickListener(this);

        initGender();
    }

    private void initGender() {
        gender = Integer.parseInt(data);
        genderCheck(gender);
    }

    private void findBirthViews() {
        birthPickerLayout = (RelativeLayout)findViewById(R.id.birth_picker_layout);
        birthText = (TextView) findViewById(R.id.birth_value);

        birthPickerLayout.setOnClickListener(this);
        birthText.setText(data);

        if (!TextUtils.isEmpty(data)) {
            Date date = TimeUtil.getDateFromFormatString(data);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            if (date != null) {
                birthYear = cal.get(Calendar.YEAR);
                birthMonth = cal.get(Calendar.MONTH);
                birthDay = cal.get(Calendar.DATE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.male_layout:
                gender = GenderEnum.MALE.getValue();
                genderCheck(gender);
                break;
            case R.id.female_layout:
                gender = GenderEnum.FEMALE.getValue();
                genderCheck(gender);
                break;
            case R.id.other_layout:
                gender = GenderEnum.UNKNOWN.getValue();
                genderCheck(gender);
                break;
            case R.id.birth_picker_layout:
                openTimePicker();
                break;
        }
    }

    private void genderCheck(int selected) {
        otherCheck.setBackgroundResource(selected == GenderEnum.UNKNOWN.getValue() ? R.drawable.nim_contact_checkbox_checked_green : R.drawable.nim_checkbox_not_checked);
        maleCheck.setBackgroundResource(selected == GenderEnum.MALE.getValue() ? R.drawable.nim_contact_checkbox_checked_green : R.drawable.nim_checkbox_not_checked);
        femaleCheck.setBackgroundResource(selected == GenderEnum.FEMALE.getValue() ? R.drawable.nim_contact_checkbox_checked_green : R.drawable.nim_checkbox_not_checked);
    }

    private void openTimePicker() {
        MyDatePickerDialog datePickerDialog = new MyDatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                birthYear = year;
                birthMonth = monthOfYear;
                birthDay = dayOfMonth;
                updateDate();

            }
        }, birthYear, birthMonth, birthDay);
        datePickerDialog.show();
    }

    private void updateDate() {
        birthText.setText(TimeUtil.getFormatDatetime(birthYear, birthMonth, birthDay));
    }

    private void update(Serializable content) {
        // 先设置一个回调变量，回调，就是请对方做事，对方做好了就通知你做好了
        RequestCallbackWrapper callback = new RequestCallbackWrapper() {
            @Override
            public void onResult(int code, Object result, Throwable exception) {
                DialogMaker.dismissProgressDialog();
                if (code == ResponseCode.RES_SUCCESS) {
                    onUpdateCompleted(); // 设置成功
                } else if (code == ResponseCode.RES_ETIMEOUT) {
                    CustomToast.show(UserProfileEditItemActivity.this, R.string.user_info_update_failed);
                }
            }
        };
        if (key == UserConstant.KEY_ALIAS) { // 设置备注，这是只有好友才可能出现的设置，其余的是对自己可用的
            DialogMaker.showProgressDialog(this, null, true);
            Map<FriendFieldEnum, Object> map = new HashMap<>();
            map.put(FriendFieldEnum.ALIAS, content);
            NIMClient.getService(FriendService.class).updateFriendFields(data, map).setCallback(callback);
        } else {
            if (fieldMap == null) {
                fieldMap = new HashMap<>();
                fieldMap.put(UserConstant.KEY_NICKNAME, UserInfoFieldEnum.Name);
                fieldMap.put(UserConstant.KEY_PHONE, UserInfoFieldEnum.MOBILE);
                fieldMap.put(UserConstant.KEY_SIGNATURE, UserInfoFieldEnum.SIGNATURE);
                fieldMap.put(UserConstant.KEY_EMAIL, UserInfoFieldEnum.EMAIL);
                fieldMap.put(UserConstant.KEY_BIRTH, UserInfoFieldEnum.BIRTHDAY);
                fieldMap.put(UserConstant.KEY_GENDER, UserInfoFieldEnum.GENDER);
            }
            DialogMaker.showProgressDialog(this, null, true);
            UserUpdateHelper.update(fieldMap.get(key), content, callback);
        }
    }

    private void onUpdateCompleted() {
        showKeyboard(false);
        CustomToast.show(UserProfileEditItemActivity.this, R.string.user_info_update_success);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.edit_but:
                if (!NetworkUtil.isNetAvailable(UserProfileEditItemActivity.this)) {
                    CustomToast.show(UserProfileEditItemActivity.this, R.string.network_is_not_available);
                    break;
                }
                if (key == UserConstant.KEY_NICKNAME && TextUtils.isEmpty(editText.getText().toString().trim())) {
                    CustomToast.show(UserProfileEditItemActivity.this, R.string.nickname_empty);
                    break;
                }

                if (key == UserConstant.KEY_NICKNAME){
                    LogUtil.i("上传昵称到自己服务器", editText.getText().toString());
                    // 并且上传到自己的服务器
                    RetrofitServiceInstance.getInstance()
                            .updateMyNickname(Preferences.getFanId(), editText.getText().toString())
                            .onBackpressureBuffer()
                            .subscribeOn(Schedulers.io())
                            .subscribe(new Subscriber<SuccessOfServer>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    LogUtil.e("更新昵称失败", "在自己服务器上的昵称更新失败");
                                }

                                @Override
                                public void onNext(SuccessOfServer successOfServer) {

                                    if (successOfServer.getStatus() == 200){
                                        LogUtil.i("更新昵称成功", "在自己服务器上的昵称更新成功");
                                        Preferences.setWxNickname(editText.getText().toString());
                                    }else {
                                        LogUtil.i("更新昵称失败", "在自己服务器上的昵称更新失败");
                                    }
                                }
                            });
                }
                if (key == UserConstant.KEY_BIRTH) {
                    update(birthText.getText().toString());
                } else if (key == UserConstant.KEY_GENDER) {
                    update(Integer.valueOf(gender));
                } else  {
                    update(editText.getText().toString().trim());
                }
                break;
            case android.R.id.home:
                onBackPressed();
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_setting, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    private class MyDatePickerDialog extends DatePickerDialog {
        private int maxYear = 2015;
        private int minYear = 1900;
        private int currYear;
        private int currMonthOfYear;
        private int currDayOfMonth;

        public MyDatePickerDialog(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
            super(context, callBack, year, monthOfYear, dayOfMonth);
            currYear = year;
            currMonthOfYear = monthOfYear;
            currDayOfMonth = dayOfMonth;
        }

        @Override
        public void onDateChanged(DatePicker view, int year, int month, int day) {
            if(year >= minYear && year <= maxYear){
                currYear = year;
                currMonthOfYear = month;
                currDayOfMonth = day;
            } else {
                if (currYear > maxYear) {
                    currYear = maxYear;
                } else if (currYear < minYear) {
                    currYear = minYear;
                }
                updateDate(currYear, currMonthOfYear, currDayOfMonth);
            }
        }

        public void setMaxYear(int year){
            maxYear = year;
        }

        public void setMinYear(int year){
            minYear = year;
        }

        public void setTitle(CharSequence title) {
            super.setTitle("生 日");
        }
    }
}

