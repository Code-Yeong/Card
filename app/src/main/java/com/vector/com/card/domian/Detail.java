package com.vector.com.card.domian;

import com.vector.com.card.utils.Utils;

/**
 * Created by Administrator on 2017/8/18.
 */
public class Detail {
    private long id;
    private String task;
    private String content;
    private String time;
    private boolean isCompleted;

    public Detail(String task) {
        this.task = task;
        this.time = Utils.getCurrentDate();
    }

    public Detail(long id, String task, String content, String time) {
        this.id = id;
        this.task = task;
        this.content = content;
        this.time = time;
    }

    public Detail(long id, String task, String time) {
        this.id = id;
        this.task = task;
        this.time = time;
    }

    public Detail(long id, String task, String time, boolean isCompleted) {
        this.id = id;
        this.task = task;
        this.time = time;
        this.isCompleted = isCompleted;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
