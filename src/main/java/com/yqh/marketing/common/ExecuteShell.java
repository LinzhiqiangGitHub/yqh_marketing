package com.yqh.marketing.common;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStreamReader;
import java.io.LineNumberReader;

/**
 * Created by chenguo on 2016/8/2.
 */
public class ExecuteShell {
    protected static final Logger log = LoggerFactory.getLogger(ExecuteShell.class);
    /**
     * 执行shell命令
     *String[] cmd = { "sh", "-c", "lsmod |grep linuxVmux" }或者
     *String[] cmd = { "sh", "-c", "./load_driver.sh" }
     *int tp = 1 返回执行结果  非1 返回命令执行后的输出
     */
    public static String runCommand(String[] cmd,int tp){
        log.debug("开始执行shell。。。");
        StringBuffer buf = new StringBuffer(1000);
        String rt="-1";
        try {
            log.debug("开始执行shell。。。");
            Process pos = Runtime.getRuntime().exec(cmd);
            log.debug("结束执行shell。。。");

            pos.waitFor();
            log.debug("watiFor。。。");
            if(tp==1){
                if(pos.exitValue()==0){
                    rt="1";
                }
            }else{
                InputStreamReader ir = new InputStreamReader(pos.getInputStream());
                LineNumberReader input = new LineNumberReader(ir);
                String ln="";
                log.debug("watiFor11。。。");
                while ((ln =input.readLine()) != null) {
                    buf.append(ln+"\r\n");
                }
                log.debug("buf"+buf.toString());
                rt = buf.toString();
                input.close();
                ir.close();
            }
        } catch (java.io.IOException e) {
            rt=e.toString();
        }catch (Exception e) {
            rt=e.toString();
        }
        return rt;
    }
    /**
     * 执行shell命令
     *String[] cmd = { "sh", "-c", "lsmod |grep linuxVmux" }或者
     *String[] cmd = { "sh", "-c", "./load_driver.sh" }
     *int tp = 1 返回执行结果  非1 返回命令执行后的输出
     */
    public static String runCommand(String[] cmd){
        log.debug("开始执行shell。。。");
        StringBuffer buf = new StringBuffer(1000);
        String rt="-1";
        try {
            log.debug("开始执行shell。。。");
            Process pos = Runtime.getRuntime().exec(cmd);
            log.debug("结束执行shell。。。");
            StreamGobbler errorGobbler = new StreamGobbler(pos.getErrorStream(), "ERROR");

            // kick off stderr
            errorGobbler.start();

            StreamGobbler outGobbler = new StreamGobbler(pos.getInputStream(), "STDOUT");
            // kick off stdout
            outGobbler.start();
            pos.waitFor();
            log.debug("watiFor。。。");
            int tp=0;
            if(tp==1){
                if(pos.exitValue()==0){
                    rt="1";
                }
            }else{
                InputStreamReader ir = new InputStreamReader(pos.getInputStream());
                LineNumberReader input = new LineNumberReader(ir);
                String ln="";
                log.debug("watiFor11。。。");
                while ((ln =input.readLine()) != null) {
                    buf.append(ln+"\r\n");
                }
                log.debug("buf"+buf.toString());
                rt = buf.toString();
                input.close();
                ir.close();
            }
        } catch (java.io.IOException e) {
            rt=e.toString();
        }catch (Exception e) {
            rt=e.toString();
        }
        return rt;
    }
    /**
     * 执行简单命令
     * String cmd="ls"
     *int tp = 1 返回执行结果  非1 返回命令执行后的输出
     */
    public static String runCommand(String cmd,int tp){
        StringBuffer buf = new StringBuffer(1000);
        String rt="-1";
        try {
            Process pos = Runtime.getRuntime().exec(cmd);
            pos.waitFor();
            if(tp==1){
                if(pos.exitValue()==0){
                    rt="1";
                }
            }else{
                InputStreamReader ir = new InputStreamReader(pos.getInputStream());
                LineNumberReader input = new LineNumberReader(ir);
                String ln="";
                while ((ln =input.readLine()) != null) {
                    buf.append(ln+"\r\n");
                }
                rt = buf.toString();
                input.close();
                ir.close();
            }
        } catch (java.io.IOException e) {
            rt=e.toString();
        }catch (Exception e) {
            rt=e.toString();
        }
        return rt;
    }
}
