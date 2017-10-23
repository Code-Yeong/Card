package com.vector.com.card.dao;

import java.util.List;

/**
 * Created by Administrator on 2017/8/18.
 */
public interface BaseDao<T> {

    /*插入操作*/
    long insert(T t);

    /*删除操作*/
    int delete(long id);

    /*更新操作*/
    int update(T t);

    /*查询操作*/
    T queryById(long id);
}
