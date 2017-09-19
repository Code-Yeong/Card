package com.vector.com.card.domian;

import com.vector.com.card.utils.Utils;

/**
 * Created by Administrator on 2017/8/18.
 */
public class User {
    private long id;
    private String name;
    private String time;
    private String password;
    private String mark;
    private String score;
    private String keepLogin;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
        this.keepLogin = "0";
        this.score = "0";
        this.time = Utils.getCurrentTime();
    }

    public User(long id, String score) {
        this.id = id;
        this.score = score;
    }

    public User(long id) {
        this.id = id;
    }

    public User(long id, String name, String time, String password, String mark, String keepLogin, String score) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.password = password;
        this.mark = mark;
        this.keepLogin = keepLogin;
        this.score = score;
    }

    public User(long id, String name, String time, String password, String mark, String score) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.password = password;
        this.mark = mark;
        this.score = score;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getKeepLogin() {
        return keepLogin;
    }

    public void setKeepLogin(String keepLogin) {
        this.keepLogin = keepLogin;
    }

    @Override
    public String toString() {
        return "{\"id\":\"" + id + "\",\"name\":\"" + name + "\",\"time\":\"" + time + "\",\"password\":\"" + password
                + "\",\"mark\":\"" + mark + "\",\"keepLogin\":\"" + keepLogin + "\",\"score\":\"" + score + "\"}";
    }
}