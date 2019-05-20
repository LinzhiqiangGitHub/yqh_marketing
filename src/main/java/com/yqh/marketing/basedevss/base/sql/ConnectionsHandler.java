package com.yqh.marketing.basedevss.base.sql;


import java.sql.Connection;

public interface ConnectionsHandler<T> {
    T handle(Connection[] conns);
}
