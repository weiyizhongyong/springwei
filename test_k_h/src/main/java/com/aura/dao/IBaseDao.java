package com.aura.dao;


import java.util.List;

public interface IBaseDao<T> {
    void insert(T entity);
    void insertBatch(List<T> list);
    List<T> querty();

}
