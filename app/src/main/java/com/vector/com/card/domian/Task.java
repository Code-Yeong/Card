package com.vector.com.card.domian;

import com.vector.com.card.utils.Utils;

/**
 * Created by Administrator on 2017/8/18.
 */
public class Task {
    private long id;
    private String user;
    private String content;
    private String status;
    private String time;

    public Task(String user, String content) {
        this.user = user;
        this.content = content;
        this.status = "0";
        this.time = Utils.getCurrentTime();
    }

    public Task(long id, String status) {
        this.id = id;
        this.status = status;
    }

    public Task(long id, String user, String content, String status, String time) {
        this.id = id;
        this.user = user;
        this.content = content;
        this.status = status;
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}