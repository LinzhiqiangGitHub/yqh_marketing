package com.yqh.marketing.basedevss.base.util;


import java.util.LinkedList;

public abstract class InitializersHandler {
    public abstract void handle(Initializable[] objs);


    public static void initAndDestroy(Initializable[] objs, InitializersHandler handler) {
        LinkedList<Initializable> inited = new LinkedList<Initializable>();
        try {
            for (Initializable obj : objs) {
                obj.init();
                inited.addFirst(obj);
            }
            handler.handle(objs);
        } finally {
            for (Initializable obj : inited) {
                try {
                    obj.destroy();
                } catch (Throwable ignored) {
                }
            }
        }
    }
}
