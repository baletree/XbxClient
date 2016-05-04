package com.xbx.client.ui.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xbx.client.R;
import com.xbx.client.beans.UserInfo;
import com.xbx.client.http.Api;
import com.xbx.client.jsonparse.UserInfoParse;
import com.xbx.client.linsener.AnimateFirstDisplayListener;
import com.xbx.client.utils.Constant;
import com.xbx.client.linsener.ImageLoaderConfigFactory;
import com.xbx.client.utils.SharePrefer;
import com.xbx.client.utils.TaskFlag;
import com.xbx.client.utils.Util;

import java.io.File;
import java.util.Calendar;

/**
 * Created by EricYuan on 2016/4/17.
 * 个人中心
 */
public class UserInfoActivity extends BaseActivity {
    private TextView title_txt_tv;
    private TextView title_rtxt_tv;
    private EditText user_info_nickname_text;
    private TextView user_info_sex_text;
    private TextView user_info_birthday_text;
    private EditText user_info_name_text;
    private TextView user_info_phone_text;
    private ImageView user_info_head_img;

    private UserInfo userInfo = null;
    private ImageLoader imageLoader = null;
    private ImageLoaderConfigFactory configFactory = null;
    private File headFile = null;
    private Api api = null;

    private boolean isModify = false;
    private boolean isModifyHead = false;
    private boolean isUpdate = false;
    private String sexType = "2";
    private String uid = "";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TaskFlag.REQUESTSUCCESS:
                    Util.showToast(UserInfoActivity.this, getString(R.string.modify_success));
                    isModify = false;
                    if (userInfo != null) {
                        Util.pLog("save:" + sexType);
                        userInfo.setUserSex(sexType);
                        userInfo.setUserBirthday(user_info_birthday_text.getText().toString());
                        userInfo.setNickName(user_info_nickname_text.getText().toString());
                        userInfo.setUserRealName(user_info_name_text.getText().toString());
                        SharePrefer.saveUserInfo(UserInfoActivity.this, userInfo);
                        isUpdate = true;
                    }
                    break;
                case TaskFlag.PAGEREQUESTWO:
                    String dataModi = (String) msg.obj;
                    userInfo = UserInfoParse.modifyUserInfo(userInfo, dataModi);
                    SharePrefer.saveUserInfo(UserInfoActivity.this, userInfo);
                    Util.showToast(UserInfoActivity.this, getString(R.string.suc_modify_head));
                    isModifyHead = false;
                    isUpdate = true;
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
    }

    @Override
    protected void initViews() {
        title_txt_tv = (TextView) findViewById(R.id.title_txt_tv);
        title_rtxt_tv = (TextView) findViewById(R.id.title_rtxt_tv);
        user_info_nickname_text = (EditText) findViewById(R.id.user_info_nickname_text);
        user_info_sex_text = (TextView) findViewById(R.id.user_info_sex_text);
        user_info_birthday_text = (TextView) findViewById(R.id.user_info_birthday_text);
        user_info_name_text = (EditText) findViewById(R.id.user_info_name_text);
        user_info_phone_text = (TextView) findViewById(R.id.user_info_phone_text);
        user_info_head_img = (ImageView) findViewById(R.id.user_info_head_img);

        title_txt_tv.setText(getString(R.string.user_info_title));
        title_rtxt_tv.setText(getString(R.string.user_info_title_right_text));
        title_rtxt_tv.setOnClickListener(this);
        findViewById(R.id.title_left_img).setOnClickListener(this);
        findViewById(R.id.user_info_head_layout).setOnClickListener(this);
        findViewById(R.id.user_info_nickname_layout).setOnClickListener(this);
        findViewById(R.id.user_info_sex_layout).setOnClickListener(this);
        findViewById(R.id.user_info_birthday_layout).setOnClickListener(this);
        findViewById(R.id.user_info_name_layout).setOnClickListener(this);
        findViewById(R.id.user_info_phone_layout).setOnClickListener(this);
        if (userInfo == null)
            return;
        sexType = userInfo.getUserSex();
        Util.pLog("sexType " + sexType);
        if (!Util.isNull(sexType)) {
            if (Integer.parseInt(sexType) == 0)
                user_info_sex_text.setText(getString(R.string.select_sex_male));
            else if (Integer.parseInt(sexType) == 1)
                user_info_sex_text.setText(getString(R.string.select_sex_female));
        }
        if (!Util.isNull(userInfo.getNickName())) {
            user_info_nickname_text.setText(userInfo.getNickName());
            user_info_nickname_text.setSelection(userInfo.getNickName().length());
        }
        if (!Util.isNull(userInfo.getUserRealName())) {
            user_info_name_text.setText(userInfo.getUserRealName());
            user_info_name_text.setSelection(userInfo.getUserRealName().length());
        }
        if (!Util.isNull(userInfo.getUserBirthday()) && !"0000-00-00".equals(userInfo.getUserBirthday()))
            user_info_birthday_text.setText(userInfo.getUserBirthday());
        user_info_phone_text.setText(userInfo.getUserPhone());
        imageLoader.displayImage(userInfo.getUserHead(), user_info_head_img, configFactory.getHeadImg(), new AnimateFirstDisplayListener());
    }

    @Override
    protected void initDatas() {
        api = new Api(UserInfoActivity.this, handler);
        uid = SharePrefer.getUserInfo(UserInfoActivity.this).getUid();
        imageLoader = ImageLoader.getInstance();
        configFactory = ImageLoaderConfigFactory.getInstance();
        userInfo = SharePrefer.getUserInfo(UserInfoActivity.this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.title_left_img:
                if (isUpdate)
                    setResult(RESULT_OK, new Intent());
                finish();
                break;
            case R.id.title_rtxt_tv://确定修改
                if (!user_info_nickname_text.getText().toString().equals(userInfo.getNickName()))
                    isModify = true;
                if (!user_info_name_text.getText().toString().equals(userInfo.getUserRealName()))
                    isModify = true;
                if (!isModify && !isModifyHead) {
                    Util.showToast(UserInfoActivity.this, getString(R.string.not_modify));
                    return;
                }
                if (isModifyHead && headFile != null)
                    api.modifyHead(uid, headFile, user_info_birthday_text.getText().toString());
                if (isModify)
                    api.modifyInfo(uid, user_info_name_text.getText().toString(), sexType, "", user_info_nickname_text.getText().toString(), user_info_birthday_text.getText().toString());
                break;
            case R.id.user_info_head_layout:
                intent.setClass(this, SelectAlbumActivity.class);
                intent.putExtra("type", 1);
                startActivityForResult(intent, 100);
                break;
            case R.id.user_info_nickname_layout:
                break;
            case R.id.user_info_sex_layout:
                intent.setClass(this, SelectSexActivity.class);
                startActivityForResult(intent, 102);
                break;
            case R.id.user_info_birthday_layout:
                Calendar calendar = Calendar.getInstance();
                final int cYear = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH);
                final int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePicker = new DatePickerDialog(UserInfoActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        if (year > cYear) {
                            Util.showToast(UserInfoActivity.this, getString(R.string.check_birthday));
                            return;
                        }
                        if (year == cYear && monthOfYear > month) {
                            Util.showToast(UserInfoActivity.this, getString(R.string.check_birthday));
                            return;
                        }
                        if (year == cYear && monthOfYear == month && dayOfMonth > day) {
                            Util.showToast(UserInfoActivity.this, getString(R.string.check_birthday));
                            return;
                        }
                        isModify = true;
                        user_info_birthday_text.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
                    }
                }, cYear, month, day);
                datePicker.show();
                break;
            case R.id.user_info_name_layout:
                break;
            case R.id.user_info_phone_layout:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case 100:
                    String picPath = data.getStringExtra(Constant.KEY_PHOTO_PATH);
                    Bitmap bitmap = BitmapFactory.decodeFile(picPath);
                    if (bitmap == null)
                        return;
                    isModifyHead = true;
                    user_info_head_img.setImageBitmap(bitmap);
                    headFile = new File(picPath);
                    break;
                case 102:
                    String sex = data.getStringExtra("result");
                    sexType = data.getStringExtra("resultCode");
                    Util.pLog("sexType102 " + sexType);
                    user_info_sex_text.setText(sex);
                    isModify = true;
                    break;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (isUpdate)
                setResult(RESULT_OK, new Intent());
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
