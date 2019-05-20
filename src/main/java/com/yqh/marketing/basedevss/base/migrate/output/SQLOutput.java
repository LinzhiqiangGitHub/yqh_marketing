package com.yqh.marketing.basedevss.base.migrate.output;


import com.yqh.marketing.basedevss.ServerException;
import com.yqh.marketing.basedevss.base.BaseErrors;
import com.yqh.marketing.basedevss.base.data.Record;
import com.yqh.marketing.basedevss.base.migrate.RecordOutput;
import com.yqh.marketing.basedevss.base.sql.ConnectionFactory;
import org.apache.commons.lang.Validate;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class SQLOutput implements RecordOutput {
    private ConnectionFactory connectionFactory;
    private String db;
    private Statement statement;

    public SQLOutput(ConnectionFactory connectionFactory, String db) {
        Validate.notNull(connectionFactory);
        Validate.notNull(db);
        this.connectionFactory = connectionFactory;
        this.db = db;
    }

    public ConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    public String getDb() {
        return db;
    }

    protected abstract String makeSql(Record rec);

    @Override
    public void output(Record rec) {
        if (rec == null || rec.isEmpty())
            return;

        try {
            String sql = makeSql(rec);
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new ServerException(BaseErrors.PLATFORM_SQL_FOR_MIGRATE_ERROR, e);
        }
    }

    @Override
    public void init() {
        try {
            Connection conn = connectionFactory.getConnection(db);
            statement = conn.createStatement();
        } catch (SQLException e) {
            throw new ServerException(BaseErrors.PLATFORM_SQL_FOR_MIGRATE_ERROR, e);
        }
    }

    @Override
    public void destroy() {
        try {
            Connection conn = null;
            if (statement != null) {
                conn = statement.getConnection();
                statement.close();
                statement = null;
            }

            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            throw new ServerException(BaseErrors.PLATFORM_SQL_FOR_MIGRATE_ERROR, e);
        }
    }
}
