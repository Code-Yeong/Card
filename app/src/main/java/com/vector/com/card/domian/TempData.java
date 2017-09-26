package com.vector.com.card.domian;

/**
 * Created by Administrator on 2017/9/24.
 */
public class TempData {
    private long id;
    private String name;
    private String password;
    private String vibrate;
    private String audio;
    private String aNotice;
    private String bNotice;
    private String cNotice;
    private String dNotice;
    private String eNotice;
    private String sNotice;


    public TempData(long id, String name, String password, String vibrate, String audio, String aNotice, String bNotice, String cNotice, String dNotice, String eNotice, String sNotice) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.vibrate = vibrate;
        this.audio = audio;
        this.aNotice = aNotice;
        this.bNotice = bNotice;
        this.cNotice = cNotice;
        this.dNotice = dNotice;
        this.eNotice = eNotice;
        this.sNotice = sNotice;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVibrate() {
        return vibrate;
    }

    public void setVibrate(String vibrate) {
        this.vibrate = vibrate;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getaNotice() {
        return aNotice;
    }

    public void setaNotice(String aNotice) {
        this.aNotice = aNotice;
    }

    public String getbNotice() {
        return bNotice;
    }

    public void setbNotice(String bNotice) {
        this.bNotice = bNotice;
    }

    public String getcNotice() {
        return cNotice;
    }

    public void setcNotice(String cNotice) {
        this.cNotice = cNotice;
    }

    public String getdNotice() {
        return dNotice;
    }

    public void setdNotice(String dNotice) {
        this.dNotice = dNotice;
    }

    public String geteNotice() {
        return eNotice;
    }

    public void seteNotice(String eNotice) {
        this.eNotice = eNotice;
    }

    public String getsNotice() {
        return sNotice;
    }

    public void setsNotice(String sNotice) {
        this.sNotice = sNotice;
    }
}
