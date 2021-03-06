package com.yqh.marketing.basedevss.base.rpc;


import com.yqh.marketing.basedevss.base.util.Initializable;
import org.apache.commons.lang.ObjectUtils;

public abstract class RPCService implements Initializable {
    public abstract Class getInterface();
    public abstract Object getImplement();

    protected String toStr(Object o) {
        return ObjectUtils.toString(o, "");
    }
}
