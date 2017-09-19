package com.vector.com.card.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.vector.com.card.R;
import com.vector.com.card.database.UserDao;
import com.vector.com.card.domian.User;
import com.vector.com.card.utils.Utils;

public class LoginActivity extends BaseActivity {

    private TextInputEditText et_loginName, et_loginPassword;
    private Button btn_loginSubmit;
    private CheckBox chk_loginRemind;
    private boolean isRemind = false;
    private SharedPreferences sharedPreferences;
    private String FILENAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addActivity(this);
        setContentView(R.layout.activity_login);

        FILENAME = this.getFilesDir().getPath().toString() + "userInfo.txt";
        sharedPreferences = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        et_loginName = (TextInputEditText) findViewById(R.id.login_name);
        et_loginPassword = (TextInputEditText) findViewById(R.id.login_password);
        chk_loginRemind = (CheckBox) findViewById(R.id.login_remind);

        User user = Utils.readFromFile(FILENAME);
        if (user != null && user.getKeepLogin().equals("1")) {
            isRemind = true;
            startActivity(new Intent(this, HomeActivity.class));
        } else {
            isRemind = false;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (isRemind) {
            et_loginName.setText(sharedPreferences.getString("name", null));
            et_loginPassword.setText(sharedPreferences.getString("password", null));
            chk_loginRemind.setChecked(true);
        } else {
            et_loginName.setText(sharedPreferences.getString("name", null));
            et_loginPassword.setText(null);
            chk_loginRemind.setChecked(false);
        }
    }

    public void loginSubmit(View v) {
        String loginName = et_loginName.getText().toString().trim();
        String loginPassword = et_loginPassword.getText().toString().trim();
        if (TextUtils.isEmpty(loginName)) {
            Utils.showMsg(getWindow().getDecorView(), "请输入用户名");
            et_loginName.requestFocus();
            return;
        } else if (TextUtils.isEmpty(loginPassword) || loginPassword.length() < 6) {
            Utils.showMsg(getWindow().getDecorView(), "请输入正确的密码");
            et_loginPassword.requestFocus();
            return;
        }
        UserDao userDao = new UserDao(this);
        if (userDao.validate(loginName, loginPassword, isRemind, FILENAME)) {
            userDao.setLoginStatus(sharedPreferences.getLong("id", 0), isRemind ? 1 : 0);
            startActivity(new Intent(this, HomeActivity.class));
        } else {
            Utils.showMsg(getWindow().getDecorView(), "用户名或密码有误");
        }
    }

    public void loginNew(View v) {
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.self_login_regist_dialog, null);
        final TextInputEditText et_registName = (TextInputEditText) view.findViewById(R.id.self_login_regist_dialog_name);
        final TextInputEditText et_registPassword = (TextInputEditText) view.findViewById(R.id.self_login_regist_dialog_password);
        final TextView et_registTip = (TextView) view.findViewById(R.id.self_login_regist_dialog_tip);
        final Button btn_registSubmit = (Button) view.findViewById(R.id.self_login_regist_dialog_submit);
        ImageView iv_registClose = (ImageView) view.findViewById(R.id.self_login_regist_dialog_close);
        final Dialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .show();
        iv_registClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btn_registSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String registName = et_registName.getText().toString().trim();
                String registPassword = et_registPassword.getText().toString().trim();
                UserDao userDao = new UserDao(LoginActivity.this);
                if (TextUtils.isEmpty(registName) || TextUtils.isEmpty(registPassword)) {
                    et_registTip.setText("*请正确填写用户名和密码");
                    et_registTip.setVisibility(View.VISIBLE);
                    return;
                } else if (registPassword.length() < 6) {
                    et_registTip.setText("*密码长度不能小于6位");
                    et_registTip.setVisibility(View.VISIBLE);
                    return;
                } else if (userDao.isUserNameExist(registName)) {
                    et_registTip.setText("*该用户名已被使用");
                    et_registTip.setVisibility(View.VISIBLE);
                    return;
                } else {
                    if (userDao.insert(new User(registName, registPassword)) > 0) {
                        btn_registSubmit.setEnabled(false);
                        et_registTip.setText("*恭喜您注册成功");
                        et_registName.clearFocus();
                        et_registPassword.clearFocus();
                        et_registTip.setTextColor(Color.GREEN);
                        et_registTip.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    public void loginRemind(View v) {
        if (chk_loginRemind.isChecked()) {
            setRemindStatus(true);
        } else {
            setRemindStatus(false);
        }
    }

    public void setRemindStatus(boolean isRemindStatus) {
        isRemind = isRemindStatus;
    }
}
