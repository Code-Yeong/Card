package com.vector.com.card.daoimpl;

import com.vector.com.card.dao.BaseDao;

/**
 * Created by Administrator on 2017/9/28.
 */
public class SignDaoImpl<T> implements BaseDao<T>{
    @Override
    public long insert(T t) {
        return 0;
    }

    @Override
    public int delete(long id) {
        return 0;
    }

    @Override
    public int update(T t) {
        return 0;
    }

    @Override
    public T queryById(long id) {
        return null;
    }
}
