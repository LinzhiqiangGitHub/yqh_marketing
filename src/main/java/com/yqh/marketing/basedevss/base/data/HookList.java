package com.yqh.marketing.basedevss.base.data;


import com.yqh.marketing.basedevss.base.context.Context;
import com.yqh.marketing.basedevss.base.util.ClassUtils2;
import com.yqh.marketing.basedevss.base.util.StringUtils2;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collection;

public class HookList extends ArrayList<Hook> {
    public HookList(int initialCapacity) {
        super(initialCapacity);
    }

    public HookList() {
    }

    public HookList(Collection<? extends Hook> c) {
        super(c);
    }

    public void before(Context ctx, RecordSet recs) {
        for (Hook hook : this) {
            if (hook != null)
                hook.before(ctx, recs);
        }
    }

    public void after(Context ctx, RecordSet recs) {
        for (Hook hook : this) {
            if (hook != null)
                hook.after(ctx, recs);
        }
    }

    public void before(Context ctx, Record rec) {
        before(ctx, new RecordSet(rec));
    }

    public void after(Context ctx, Record rec) {
        after(ctx, new RecordSet(rec));
    }

    public void load(String classNames) {
        if (StringUtils.isNotEmpty(classNames)) {
            for (String className : StringUtils2.splitArray(classNames, ",", true)) {
                add((Hook) ClassUtils2.newInstance(className));
            }
        }
    }

    public static HookList loadNew(String classNames) {
        HookList l = new HookList();
        l.load(classNames);
        return l;
    }
}
