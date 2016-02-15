package com.leela.dao;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import com.leela.entity.BaseEntity;

public class HibernateDAO implements GenericDAO {

    private final SessionFactory sessionFactory;

    @Inject
    public HibernateDAO(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(final BaseEntity obj) {
        sessionFactory.getCurrentSession().save(obj);
    }

    @Override
    public void load(final BaseEntity obj) {
        sessionFactory.getCurrentSession().load(obj, obj.getId());
    }

    @Override
    public void update(final BaseEntity obj) {
        sessionFactory.getCurrentSession().saveOrUpdate(obj);
    }

    @Override
    public void delete(final BaseEntity obj) {
        sessionFactory.getCurrentSession().delete(obj);
    }

    @Override
    public List findByNamedQuery(final String queryName,
            final QueryOptions options) {
        final Query query = sessionFactory.getCurrentSession()
                .getNamedQuery(queryName);

        for (final String paramName : query.getNamedParameters()) {
            final Object value = options.getParams().get(paramName);

            if (value == null) {
                throw new HibernateException("Value for query parameter '"
                        + paramName + "' is null.");
            }
            if (value instanceof Collection) {
                query.setParameterList(paramName, (Collection) value);
            } else {
                query.setParameter(paramName, value);
            }
        }

        if (options.getMaxResults() != 0) {
            query.setMaxResults(options.getMaxResults());
        }

        if (options.getFirstResults() != -1) {
            query.setFirstResult(options.getFirstResults());
        }

        return query.list();
    }

    @Override
    public int findByNamedExecuteQuery(final String queryName,
            final QueryOptions options) {
        final Query query = sessionFactory.getCurrentSession()
                .getNamedQuery(queryName);
        for (final String paramName : query.getNamedParameters()) {
            final Object value = options.getParams().get(paramName);
            if (value != null) {
                query.setParameter(paramName, value);
            }
        }

        if (options.getMaxResults() != 0) {
            query.setMaxResults(options.getMaxResults());
        }

        if (options.getFirstResults() != 0) {
            query.setFirstResult(options.getFirstResults());
        }

        final int results = query.executeUpdate();
        return results;
    }

    @Override
    public List findByNativeQuery(final String queryName,
            final QueryOptions options) {
        final Query query = sessionFactory.getCurrentSession()
                .createSQLQuery(queryName);
        for (final String paramName : query.getNamedParameters()) {
            final Object value = options.getParams().get(paramName);
            query.setParameter(paramName, value);
        }

        if (options.getMaxResults() != 0) {
            query.setMaxResults(options.getMaxResults());
        }
        return query.list();
    }

    @Override
    public int findByNativeExecuteQuery(final String queryName,
            final QueryOptions options) {
        final Query query = sessionFactory.getCurrentSession()
                .createSQLQuery(queryName);

        for (final String paramName : query.getNamedParameters()) {
            final Object value = options.getParams().get(paramName);
            query.setParameter(paramName, value);
        }

        if (options.getMaxResults() != 0) {
            query.setMaxResults(options.getMaxResults());
        }

        final int results = query.executeUpdate();
        return results;
    }
}