package com.yqh.marketing.basedevss.base.migrate;


import com.yqh.marketing.basedevss.base.data.Record;
import com.yqh.marketing.basedevss.base.util.Initializable;

public interface RecordOutput extends Initializable {
    void output(Record rec);
}
