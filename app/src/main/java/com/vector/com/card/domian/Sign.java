package com.vector.com.card.domian;

/**
 * Created by Administrator on 2017/8/22.
 */
public class Sign {
    private long id;
    private String user;
    private String time;

    public Sign(String user) {
        this.user = user;
    }

    public Sign(String user, String time) {
        this.user = user;
        this.time = time;
    }

    public Sign(long id, String user, String time) {
        this.id = id;
        this.user = user;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
