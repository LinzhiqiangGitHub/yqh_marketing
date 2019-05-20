package com.yqh.marketing;
import com.yqh.marketing.basedevss.base.web.JettyServer;

import java.io.File;
import java.io.IOException;

public class ServerStart {

    public static void main(String[] args) throws IOException {
        String prop_file = "";

        for (String arg : args){
            if (arg.contains("properties"))  {
                prop_file = arg;
                break;
            }
        }

        File file0 = new File(prop_file);
        if (!file0.exists()) {
            prop_file = "yqh.properties";
        }

        JettyServer.main(new String[]{"-c", "file://" + prop_file});
    }
}
