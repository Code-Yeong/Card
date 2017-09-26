package com.vector.com.card.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.vector.com.card.R;
import com.vector.com.card.domian.TempData;
import com.vector.com.card.domian.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2017/8/3.
 */
public class Utils {
    private static final String FILENAME = ".dat";
    public static final int COIN_ALARM = 0;
    public static final int NOTICE_ALARM = 1;

    public static void showMsg(View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).setAction("Action", null).show();
    }

    public static void vibrate(Context context) {
        TempData tempData = readFromFile(context);
        if (!tempData.getVibrate().equals("on")) {
            return;
        }
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(20);
    }

    public static void alarm(Context context, int type) {
        TempData tempData = Utils.readFromFile(context);
        int audioResource = 0;
        if (tempData.getAudio().equals("on")) {
            if (type == COIN_ALARM) {
                audioResource = R.raw.con_in;
            } else if (type == NOTICE_ALARM) {
                audioResource = R.raw.notice;
            }
            MediaPlayer mediaPlayer = MediaPlayer.create(context, audioResource);
            mediaPlayer.start();
        }
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

    public static boolean deleteFile(Context cxt, String fileName) {
        File file = new File(cxt.getFilesDir().getAbsolutePath() + "/" + fileName);
        return file.delete();
    }

    public static void writeToFile(Context context, String json) {

        Log.i("info", "Write:" + json);
        try {
            File file = new File(context.getFilesDir().getAbsolutePath() + "/" + ((UserApplication) (UserApplication.getInstance())).getUserId() + FILENAME);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file, false);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            BufferedWriter bw = new BufferedWriter(outputStreamWriter);
            if (json.length() != 0) {
                bw.write(json);
            } else {
                bw.write("{}");
            }
            bw.close();
            outputStreamWriter.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static TempData readFromFile(@Nullable Context context) {
        String data = "";
        TempData tempData;
        File file = new File(context.getFilesDir().getAbsolutePath() + "/" + ((UserApplication) UserApplication.getInstance()).getUserId() + FILENAME);
        try {
            if (!file.exists()) {
                file.createNewFile();
                return null;
            }
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader br = new BufferedReader(inputStreamReader);
            String chunk;
            while ((chunk = br.readLine()) != null) {
                data += chunk;
            }

            JSONObject o = new JSONObject(data);

            tempData = new TempData(Long.parseLong(o.getString("id")), o.getString("name"), o.getString("password"),
                    o.getString("vibrate"), o.getString("audio"), o.getString("aNotice"), o.getString("bNotice"),
                    o.getString("cNotice"), o.getString("dNotice"), o.getString("eNotice"), o.getString("sNotice"));

        } catch (Exception e) {
            tempData = null;
            Log.i("info", "read File error...........");
            e.printStackTrace();
        }
        return tempData;
    }

    public static String getStoredUsers(@NonNull Context context) {
        JSONArray jsonArray = new JSONArray();
        try {
            File[] fs = new File(context.getFilesDir().getAbsolutePath()).listFiles();
            String data = "";
            JSONObject jsonObject;
            FileInputStream fileInputStream;
            InputStreamReader inputStreamReader;
            BufferedReader br;
            for (File ff : fs) {
                if (ff.getName().matches("^[0-9]{1,8}.dat$")) {
                    Log.i("info", "Filename:" + ff.getName());
                    fileInputStream = new FileInputStream(ff);
                    inputStreamReader = new InputStreamReader(fileInputStream);
                    br = new BufferedReader(inputStreamReader);
                    String chunk;
                    while ((chunk = br.readLine()) != null) {
                        data += chunk;
                    }
                    br.close();
                    inputStreamReader.close();
                    fileInputStream.close();
                    if (data.trim().length() == 0) {
                        continue;
                    }
                    jsonObject = new JSONObject(data);
                    jsonObject.put("file", ff.getName());
                    data = "";
                    jsonArray.put(jsonObject);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray.toString();
    }


    public static long getUserId(Context context) {
        long userid = Long.parseLong(((UserApplication) UserApplication.getInstance()).getUserId());
        return userid;
    }

    public static int dp2pix(Context context, float dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale);
    }

    public static int getMaxdaysOfThisMonth() {
        Calendar calendar = Calendar.getInstance();
        int max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        return max;
    }

    public static float getMaxValue(float[] data) {
        Arrays.sort(data);
        return data[data.length - 1];
    }

    public static String getVersionName(Context context) {
        String version = "未知";
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            version = packInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return version;
    }

    public static String getFormatedTime(String time) {
        long oneMinute = 1 * 60 * 1000;
        long oneHour = 60 * oneMinute;
        long oneDay = 24 * oneHour;
        long oneWeek = 7 * oneDay;
        String result = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Calendar calendar = Calendar.getInstance();
            long currentTime = calendar.getTimeInMillis();
            calendar.setTime(sdf.parse(time));
            long pastTime = calendar.getTimeInMillis();
            long loss = currentTime - pastTime;
//            Log.i("info", "c:" + currentTime + "--p:" + pastTime + "--l:" + loss);
            if (loss < oneMinute) {
                result = "刚刚";
            } else if (loss < oneHour) {
                result = loss / oneMinute + "分钟前";
            } else if (loss < oneDay) {
                result = loss / oneHour + "小时前";
            } else if (loss < oneWeek) {
                result = loss / oneDay + "天前";
            } else {
                result = time.substring(0, 10);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            result = time.substring(0, 10);
        }
        return result;
    }
}
