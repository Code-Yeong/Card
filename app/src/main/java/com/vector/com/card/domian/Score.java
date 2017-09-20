package com.vector.com.card.domian;

import com.vector.com.card.utils.Utils;

/**
 * Created by Administrator on 2017/9/19.
 */
public class Score {
    private long id;
    private String userid;
    private String content;
    private String score;
    private String time;

    public Score(String userid, String content, String score) {
        this.userid = userid;
        this.content = content;
        this.score = score;
        this.time = Utils.getCurrentTime();
    }

    public Score(long id, String content, String score, String time) {
        this.id = id;
        this.content = content;
        this.score = score;
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
