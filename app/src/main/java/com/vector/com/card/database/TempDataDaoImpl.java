package com.vector.com.card.database;

import android.content.Context;

import com.vector.com.card.domian.TempData;
import com.vector.com.card.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/9/24.
 */
public class TempDataDaoImpl implements TempDataDao {
    @Override
    public void saveLoginInfo(Context context, TempData tempData) {
        String json = "{}";
        TempData t = Utils.readFromFile(context);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", String.valueOf(tempData.getId()));
            jsonObject.put("name", tempData.getName());
            jsonObject.put("password", tempData.getPassword());
            jsonObject.put("vibrate", tempData.getVibrate());
            jsonObject.put("audio", tempData.getAudio());
            jsonObject.put("aNotice", tempData.getaNotice());
            jsonObject.put("bNotice", tempData.getbNotice());
            jsonObject.put("cNotice", tempData.getcNotice());
            jsonObject.put("dNotice", tempData.getdNotice());
            jsonObject.put("eNotice", tempData.geteNotice());
            jsonObject.put("sNotice", tempData.getsNotice());
            json = jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Utils.writeToFile(context, json);
    }

    @Override
    public TempData readConfig(Context context) {
        return null;
    }
}
