package com.yqh.marketing.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;

/**
 * Created by chenguo on 2016/6/4.
 */
public class LocalUtils {
    protected static Logger log = LoggerFactory.getLogger(LocalUtils.class);

    public static String getLocalIp(){
        InetAddress ia=null;
        String localip="";
        try {
            ia=ia.getLocalHost();

            String localname=ia.getHostName();
            localip=ia.getHostAddress();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return localip;
    }
}
