package com.yqh.marketing.basedevss.base.sql;

import java.sql.PreparedStatement;

/**
 * Created by chenguo on 2017/5/23.
 */
public interface SQLPrepareStatementHandler {
    void handle(PreparedStatement stmt) throws java.sql.SQLException;
}