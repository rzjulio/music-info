package org.expeditors.mexicoapps.onlinemusicinfo.dao;

import java.util.List;

public interface BaseDao<T> {
    T insert(T object);
    boolean update(T object);
    boolean delete(int id);
    T findById(int id);
    List<T> findAll();
}
