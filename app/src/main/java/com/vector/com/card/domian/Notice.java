package com.vector.com.card.domian;

import com.vector.com.card.utils.Utils;

/**
 * Created by Administrator on 2017/9/24.
 */
public class Notice {

    private long id;
    private String user;
    private String content;
    private String ntype;
    private String status;
    private String time;

    public Notice(long id, String status) {
        this.id = id;
        this.status = status;
    }

    public Notice(String user, String content, String ntype) {
        this.user = user;
        this.content = content;
        this.ntype = ntype;
        this.status = "0";
        this.time = Utils.getCurrentTime();
    }

    public Notice(long id, String user, String content, String ntype, String status, String time) {
        this.id = id;
        this.user = user;
        this.content = content;
        this.ntype = ntype;
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

    public String getNtype() {
        return ntype;
    }

    public void setNtype(String ntype) {
        this.ntype = ntype;
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
