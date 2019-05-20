package com.yqh.marketing.basedevss.base.migrate.output;


import com.yqh.marketing.basedevss.base.data.Record;
import com.yqh.marketing.basedevss.base.migrate.RecordOutput;

public class NullOutput implements RecordOutput {
    @Override
    public void output(Record rec) {
    }

    @Override
    public void init() {
    }

    @Override
    public void destroy() {
    }
}
