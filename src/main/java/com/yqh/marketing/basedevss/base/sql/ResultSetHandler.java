package com.yqh.marketing.basedevss.base.sql;


import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetHandler {
    void handle(ResultSet rs) throws SQLException;
}
