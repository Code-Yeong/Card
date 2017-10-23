package com.vector.com.card.view;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.vector.com.card.R;
import com.vector.com.card.dao.DetailDao;
import com.vector.com.card.dao.MemoDao;
import com.vector.com.card.dao.NoticeDao;
import com.vector.com.card.dao.ScoreDao;
import com.vector.com.card.dao.SignDao;
import com.vector.com.card.dao.TaskDao;
import com.vector.com.card.dao.UserDao;
import com.vector.com.card.domian.Detail;
import com.vector.com.card.domian.Notice;
import com.vector.com.card.domian.Score;
import com.vector.com.card.domian.User;
import com.vector.com.card.utils.Utils;

import java.util.List;

public class PersonalActivity extends BaseActivity {

    private int finishedCount = 0;
    private long userid;
    private String username, mark;
    boolean isSignedFinished = false;
    boolean isSignedBonusGot = false;
    boolean isTaskFinished = false;
    boolean isTaskBonusGot = false;
    private UserDao userDao;
    private TaskDao taskDao;
    private MemoDao memoDao;
    private ScoreDao scoreDao;
    private DetailDao detailDao;
    private TextView tv_task_status;
    private TextView tv_name, tv_content;
    private TextView tv_score, tv_task, tv_memo;
    private TextView tv_sign_score, tv_task_score;
    private ImageView iv_sign_submit, iv_task_submit;
    private ImageView iv_edit;
    private LinearLayout ll_socre, ll_task, ll_memo;

    private Animation anim_sign, anim_task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addActivity(this);
        setContentView(R.layout.activity_personal);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userid = Utils.getUserId(this);
        userDao = new UserDao(this);
        taskDao = new TaskDao(this);
        memoDao = new MemoDao(this);
        scoreDao = new ScoreDao(this);
        detailDao = new DetailDao(this);

        anim_sign = AnimationUtils.loadAnimation(this, R.anim.bonus_button);
        anim_task = AnimationUtils.loadAnimation(this, R.anim.bonus_button);

        tv_task_status = (TextView) findViewById(R.id.personal_task_status);

        tv_name = (TextView) findViewById(R.id.home_personal_user_name);
        tv_content = (TextView) findViewById(R.id.home_personal_user_word);

        tv_score = (TextView) findViewById(R.id.person_count_score);
        tv_task = (TextView) findViewById(R.id.person_count_tasks);
        tv_memo = (TextView) findViewById(R.id.person_count_memo);

        tv_sign_score = (TextView) findViewById(R.id.personal_sign_score);
        tv_task_score = (TextView) findViewById(R.id.personal_task_score);

        iv_edit = (ImageView) findViewById(R.id.home_personal_user_edit);
        iv_sign_submit = (ImageView) findViewById(R.id.personal_sign_submit);
        iv_task_submit = (ImageView) findViewById(R.id.personal_task_submit);

        ll_socre = (LinearLayout) findViewById(R.id.home_personal_user_socre);
        ll_memo = (LinearLayout) findViewById(R.id.home_personal_user_memo);
        ll_task = (LinearLayout) findViewById(R.id.home_personal_user_task);

        ll_socre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewActivity(ScoreActivity.class);
            }
        });

        ll_memo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewActivity(PersonalActivity.class);
            }
        });

        ll_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewActivity(TaskActivity.class);
            }
        });

        iv_sign_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSignedFinished) {
                    if (!isSignedBonusGot) {
                        scoreDao = new ScoreDao(PersonalActivity.this);
                        String score_str = userDao.getSignedScore(userid);
                        int score = Integer.parseInt(score_str);
                        score += Integer.parseInt(tv_sign_score.getText().toString());
                        scoreDao.insert(new Score(String.valueOf(userid), "签到奖励", tv_sign_score.getText().toString(), "T1"));
                        userDao.updateScore(new User(userid, String.valueOf(score)));
                        init();
                    }
                }
            }
        });

        iv_task_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTaskFinished) {
                    if (!isTaskBonusGot) {
                        scoreDao = new ScoreDao(PersonalActivity.this);
                        String score_str = userDao.getSignedScore(userid);
                        int score = Integer.parseInt(score_str);
                        score += Integer.parseInt(tv_task_score.getText().toString());
                        scoreDao.insert(new Score(String.valueOf(userid), "任务奖励", tv_task_score.getText().toString(), "T2"));
                        userDao.updateScore(new User(userid, String.valueOf(score)));
                        init();
                    }
                }
            }
        });

        iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.self_edit_user, null);
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                ImageView iv_close = (ImageView) view.findViewById(R.id.self_edit_user_close);
                final Button btn_submit = (Button) view.findViewById(R.id.self_edit_user_submit);
                final TextInputEditText et_mark = (TextInputEditText) view.findViewById(R.id.self_edit_user_mark);
                final EditText et_name = (EditText) view.findViewById(R.id.self_edit_user_name);
                et_name.setText(username);
                et_mark.setText(mark);
                final PopupWindow popupWindow = new PopupWindow(view, (int) Utils.dp2pix(PersonalActivity.this, 250), (int) Utils.dp2pix(PersonalActivity.this, 350));

                popupWindow.setOutsideTouchable(false);
                popupWindow.setFocusable(true);
                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

                iv_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

                btn_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String n = et_name.getText().toString().trim();
                        String m = et_mark.getText().toString().trim();
                        if (!TextUtils.isEmpty(m) && !TextUtils.isEmpty(n)) {
                            if (userDao.update(userid, n, m) > 0) {
                                new NoticeDao(PersonalActivity.this).insert(new Notice(String.valueOf(userid), "您成功修改了个人信息", "D"));
                                popupWindow.dismiss();
                                init();
                            }
                        } else {
                            Utils.showMsg(v, "填写不正确");
                        }
                    }
                });
            }
        });

        init();
    }

    public void init() {
        setUserInfo();
        setTaskCount();
        setMemoCount();
        setTodayTaskInfo();
        tv_task_status.setText(finishedCount + "/2");
    }

    public void setUserInfo() {
        User user = userDao.queryById(userid);
        username = user.getRealName();
        mark = user.getMark();
        tv_name.setText(username);
        tv_content.setText(mark);
        tv_score.setText(user.getScore());
    }

    public void setTaskCount() {
        tv_task.setText(String.valueOf(taskDao.queryByUserId(userid).size()));
    }

    public void setMemoCount() {
        tv_memo.setText(String.valueOf(memoDao.queryBuUserId(userid).size()));
    }

    public void setTodayTaskInfo() {
        finishedCount = 0;
        int finishedTasks = 0;
        boolean isSigned = new SignDao(this).getSignedStatus(userid);
        List<Detail> list = detailDao.queryAllByUserId(userid);
        if (isSigned) {
            iv_sign_submit.setVisibility(View.VISIBLE);
            isSignedFinished = true;
            if (!scoreDao.isGotBonus(userid, "T1", Utils.getCurrentTime())) {
                iv_sign_submit.setAnimation(anim_sign);
                isSignedBonusGot = false;
            } else {
                anim_sign.cancel();
                finishedCount++;
                isSignedBonusGot = true;
                iv_sign_submit.setImageResource(R.drawable.icon_received);
            }
        } else {
            iv_sign_submit.setVisibility(View.GONE);
            isSignedFinished = false;
            iv_sign_submit.setImageResource(R.drawable.icon_receive);
        }

        for (Detail d : list) {
            if (d.getTime().equals("FINISHED")) {
                finishedTasks++;
                if (finishedTasks >= 3) {
                    iv_task_submit.setVisibility(View.VISIBLE);
                    isTaskFinished = true;
                    if (!scoreDao.isGotBonus(userid, "T2", Utils.getCurrentTime())) {
                        iv_task_submit.setAnimation(anim_task);
                        anim_task.start();
                        isTaskBonusGot = false;
                    } else {
                        anim_task.cancel();
                        isTaskBonusGot = true;
                        finishedCount++;
                        iv_task_submit.setImageResource(R.drawable.icon_received);
                    }
                    break;
                } else {
                    iv_task_submit.setVisibility(View.GONE);
                    isTaskFinished = false;
                }
            } else {
                iv_task_submit.setVisibility(View.GONE);
            }
        }
    }
}
