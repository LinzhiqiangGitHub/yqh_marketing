package com.yqh.marketing.basedevss.base.data;


import com.yqh.marketing.basedevss.base.context.Context;

public interface Expansion {
    void expand(Context ctx, RecordSet recs, String[] cols);
}
