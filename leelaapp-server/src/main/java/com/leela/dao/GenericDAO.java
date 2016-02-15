package com.leela.dao;

import java.util.List;

import com.leela.entity.BaseEntity;

public interface GenericDAO {
    public void create(BaseEntity obj);

    public void load(BaseEntity obj);

    public void update(BaseEntity obj);

    public void delete(BaseEntity obj);

    public List findByNamedQuery(String queryName, QueryOptions options);

    public int findByNamedExecuteQuery(final String queryName,
            final QueryOptions options);

    public List findByNativeQuery(String queryName, QueryOptions options);

    public int findByNativeExecuteQuery(String queryName, QueryOptions options);

}