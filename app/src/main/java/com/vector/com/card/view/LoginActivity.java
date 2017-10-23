package com.vector.com.card.view;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.vector.com.card.R;
import com.vector.com.card.adapter.RecyclerViewAdapterForUsers;
import com.vector.com.card.daoimpl.TempDataDaoImpl;
import com.vector.com.card.dao.UserDao;
import com.vector.com.card.domian.TempData;
import com.vector.com.card.domian.User;
import com.vector.com.card.utils.UserApplication;
import com.vector.com.card.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends BaseActivity {

    private TextInputEditText et_loginName, et_loginPassword;
    private CheckBox chk_loginRemind;
    private boolean isRemind = false;
    private List<Map<String, String>> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addActivity(this);
        setContentView(R.layout.activity_login);

        list = new ArrayList<>();
        et_loginName = (TextInputEditText) findViewById(R.id.login_name);
        et_loginPassword = (TextInputEditText) findViewById(R.id.login_password);
        chk_loginRemind = (CheckBox) findViewById(R.id.login_remind);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
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
        if (userDao.validate(loginName, loginPassword)) {
            TempData tempData = Utils.readFromFile(this);
            if (tempData == null) {
                tempData = new TempData(Long.parseLong(((UserApplication) (UserApplication.getInstance())).getUserId()), loginName, loginPassword,
                        "on", "on", "on", "on", "on", "on", "on", "on");
            } else {
                tempData.setName(loginName);
                if (isRemind) {
                    tempData.setPassword(loginPassword);
                } else {
                    tempData.setPassword("");
                }
            }
            new TempDataDaoImpl().saveLoginInfo(this, tempData);
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
//                        new NoticeDao(LoginActivity.this).insert(new Notice(String.valueOf(Utils.getUserId(LoginActivity.this)), "恭喜您注册成功", "S"));
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

    public void initStoredUsers(View v) {
        View view = getLayoutInflater().inflate(R.layout.self_login_user_selec, null);
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.self_login_user_select_list);
        rv.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewAdapterForUsers adapter = new RecyclerViewAdapterForUsers(getData());
        final PopupWindow popupWindow = new PopupWindow(view, et_loginName.getWidth(), LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        adapter.setListener(new RecyclerViewAdapterForUsers.OnItemClickListener() {
            @Override
            public void onNameClicked(String name) {
                popupWindow.dismiss();
                for (Map map : list) {
                    if (map.get("name").equals(name)) {
                        String password = map.get("password").toString().trim();
                        et_loginName.setText(name);
                        et_loginPassword.setText(password);
                        if (TextUtils.isEmpty(password)) {
                            isRemind = false;
                            chk_loginRemind.setChecked(false);
                        } else {
                            isRemind = true;
                            chk_loginRemind.setChecked(true);
                        }
                        break;
                    }
                }
            }

            @Override
            public void onDeleteClicked(int position) {
                if (Utils.deleteFile(LoginActivity.this, list.get(position).get("file").toString())) {
                    Log.i("info", "deleteing...");
                    list = getData();
                }
            }
        });
        rv.setAdapter(adapter);

        popupWindow.showAsDropDown(et_loginName);
    }

    public List<Map<String, String>> getData() {
        list.clear();
        String jsonArray = Utils.getStoredUsers(this);
        try {
            JSONArray ja = new JSONArray(jsonArray);
            JSONObject jsonObject = null;
            for (int i = 0; i < ja.length(); i++) {
                jsonObject = ja.getJSONObject(i);
                Map<String, String> map = new HashMap<>();
                map.put("name", jsonObject.getString("name"));
                map.put("password", jsonObject.getString("password"));
                map.put("file", jsonObject.getString("file"));
                list.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
