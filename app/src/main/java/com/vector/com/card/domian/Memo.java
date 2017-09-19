package com.vector.com.card.domian;

import com.vector.com.card.utils.Utils;

/**
 * Created by Administrator on 2017/8/26.
 */
public class Memo {
    private long id;
    private String user;
    private String content;
    private String flag;
    private String time;

    public Memo(String user, String content, String flag) {
        this.user = user;
        this.content = content;
        this.flag = flag;
        this.time = Utils.getCurrentTime();
    }

    public Memo(long id, String flag) {
        this.id = id;
        this.flag = flag;
    }

    public Memo(long id, String user, String content, String flag, String time) {
        this.id = id;
        this.user = user;
        this.content = content;
        this.flag = flag;
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

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
