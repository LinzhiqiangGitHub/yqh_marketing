package com.yqh.marketing.basedevss.base.data;


import com.yqh.marketing.basedevss.base.context.Context;

public interface Hook {
    void before(Context ctx, RecordSet recs);
    void after(Context ctx, RecordSet recs);
}
