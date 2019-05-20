package com.yqh.marketing.basedevss.base.migrate;


import com.yqh.marketing.basedevss.base.data.Record;

public interface RecordMigrateHandler {
    void handle(Record in, Record[] out) throws MigrateStopException;
}
