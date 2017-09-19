package com.vector.com.card.utils;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.vector.com.card.domian.User;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2017/8/3.
 */
public class Utils {

    public static void showMsg(View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).setAction("Action", null).show();
    }

    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return sdf.format(date.getTime());
    }

    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return sdf.format(date.getTime());
    }

    public static String getFormatDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM月dd日 EEEE");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(sdf.parse(date));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sdf2.format(calendar.getTime());
    }

    public static void writeToFile(String fileName, User user) {
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file, false);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            BufferedWriter bw = new BufferedWriter(outputStreamWriter);
            if (user != null) {
                bw.write(user.toString());
            } else {
                bw.write("");
            }
            bw.close();
            outputStreamWriter.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static User readFromFile(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            return null;
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader br = new BufferedReader(inputStreamReader);
            String chunk = br.readLine();
            JSONObject jsonObject = new JSONObject(chunk);
            return new User(Long.parseLong(jsonObject.getString("id")), jsonObject.getString("name"), jsonObject.getString("time"),
                    jsonObject.getString("password"), jsonObject.getString("mark"), jsonObject.getString("keepLogin"), jsonObject.getString("score"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
