package com.vector.com.card.database;

import android.content.Context;

import com.vector.com.card.domian.TempData;

/**
 * Created by Administrator on 2017/9/24.
 */
public interface TempDataDao {
    void saveLoginInfo(Context context, TempData tempData);

    TempData readConfig(Context context);
}
