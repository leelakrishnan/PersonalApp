package com.leela.dao;

import java.util.HashMap;
import java.util.Map;

public class QueryOptions {
    private Map<String, Object> params = new HashMap<String, Object>();

    private int maxResults = 0;
    private int firstResults = -1;

    public QueryOptions() {
    }

    public QueryOptions(final Map<String, Object> params) {
        this.params = params;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(final int maxResults) {
        this.maxResults = maxResults;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(final Map<String, Object> params) {
        this.params = params;
    }

    public int getFirstResults() {
        return firstResults;
    }

    public void setFirstResults(final int firstResults) {
        this.firstResults = firstResults;
    }
}
