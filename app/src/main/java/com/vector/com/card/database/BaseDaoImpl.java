package com.vector.com.card.database;

/**
 * Created by Administrator on 2017/9/24.
 */
public class BaseDaoImpl<T> implements BaseDao<T> {
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
