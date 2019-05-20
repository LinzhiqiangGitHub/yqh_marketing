package com.yqh.marketing.basedevss.base.util;


public class InitUtils {
    public static void init(Object o) {
        try {
            if (o instanceof Initializable)
                ((Initializable) o).init();
        }catch (Throwable t){
            t.printStackTrace();
        }
    }

    public static void destroy(Object o) {
        try {
            if (o instanceof Initializable)
                ((Initializable) o).destroy();
        }catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static void batchInit(Object... objs) {
        for (Object o : objs)
            init(o);
    }

    public static void batchDestroy(Object... objs) {
        for (Object o : objs)
            destroy(o);
    }
}
