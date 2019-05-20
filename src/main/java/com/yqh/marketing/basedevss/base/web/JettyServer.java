package com.yqh.marketing.basedevss.base.web;


import com.yqh.marketing.basedevss.ServerException;
import com.yqh.marketing.basedevss.base.BaseErrors;
import com.yqh.marketing.basedevss.base.conf.Configuration;
import com.yqh.marketing.basedevss.base.conf.GlobalConfig;
import com.yqh.marketing.basedevss.base.net.HostAndPort;
import com.yqh.marketing.basedevss.base.util.ClassUtils2;
import com.yqh.marketing.basedevss.base.util.ProcessUtils;
import com.yqh.marketing.basedevss.base.util.StringUtils2;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.server.ssl.SslSocketConnector;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.Servlet;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//import com.yqh.platform.basedevss.base.mq.MQCollection;
//import org.eclipse.jetty.webapp.WebAppContext;


public class JettyServer {
    private static final List<LifeCycle> lifeCycles = new ArrayList<LifeCycle>();

    public static void addLifeCycle(LifeCycle lc) {
        lifeCycles.add(lc);
    }

    public static void removeLifeCycle(LifeCycle lc) {
        lifeCycles.remove(lc);
    }

    private static void configureServer(Server server) {
        Configuration conf = GlobalConfig.get();
        String serverPort = conf.getString("server.port","8089");
        String bindAddrs = conf.getString("server.address", "*:"+serverPort+"");
        String host=conf.getString("server.host","localhost");
        // Configure host and port
        for (String s : StringUtils.split(bindAddrs, ",")) {
            HostAndPort bindAddr = HostAndPort.parse(s);
            Connector connector = new SelectChannelConnector();

            if (bindAddr.host != null)
                connector.setHost(bindAddr.host);
            if(!host.isEmpty())
            {
                connector.setHost(host);
            }
            connector.setPort(bindAddr.port);

            server.addConnector(connector);
        }

        initServlet(server, conf);
//        initWarWebApp(server, conf);
//        initDirWebApp(server, conf);
    }
    private static void configureServerHttps(Server server) {
        Configuration conf = GlobalConfig.get();
        String serverPort = conf.getString("server.port","8089");
        String bindAddrs = conf.getString("server.address", "*:"+serverPort+"");
        String host=conf.getString("server.host","localhost");
        // Configure host and port
        for (String s : StringUtils.split(bindAddrs, ",")) {
            HostAndPort bindAddr = HostAndPort.parse(s);
            SslSocketConnector connector = new SslSocketConnector();

            if (bindAddr.host != null)
                connector.setHost(bindAddr.host);
            if(!host.isEmpty())
            {
                connector.setHost(host);
            }
            connector.setPort(bindAddr.port);

            server.addConnector(connector);

            //region liuhongjia 为了兼容websocket 升级jetty版本注释
            //SslContextFactory cf = connector.getSslContextFactory();
//            cf.setKeyStore("src/test/java/jetty/epayService.keystore");
//            cf.setKeyStorePassword("123456");
//            cf.setKeyManagerPassword("123456");
            //endregion
            server.addConnector(connector);
        }

        initServlet(server, conf);
//        initWarWebApp(server, conf);
//        initDirWebApp(server, conf);
    }
    private static void initServlet(Server server, Configuration conf) {
        ServletContextHandler servletContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContext.setContextPath("/");
        server.setHandler(servletContext);
        for (Map.Entry<String, Object> e : conf.entrySet()) {
            String k = e.getKey();
            String v = ObjectUtils.toString(e.getValue(), "");
            if (!k.matches("^servlet\\.\\w+\\.class$"))
                continue;

            String servletName = StringUtils.split(k, '.')[1];
            String pathSpecs = conf.getString("servlet." + servletName + ".path", "/*");

            Servlet servlet = (Servlet) ClassUtils2.newInstance(v.trim());
            ServletHolder holder = new ServletHolder(servlet);
            holder.setName(servletName);
            holder.setInitParameters(conf.toStrStr());
            for (String pathSpec : StringUtils2.splitList(pathSpecs, ",", true)) {
                servletContext.addServlet(holder, pathSpec);
            }
        }
        File rootDir = new File(conf.getString("webRoot", ""));
        ServletHolder rootHolder = new ServletHolder(new DefaultServlet());
        String rootDirStr = rootDir.isAbsolute() ? rootDir.getAbsolutePath() : FilenameUtils.concat(new File(".").getAbsolutePath(), rootDir.getPath());
        rootHolder.setInitParameter("resourceBase", rootDirStr);
        servletContext.addServlet(rootHolder, "/*");

    }

//    private static void initWarWebApp(Server server, Configuration conf) {
//        for (Map.Entry<String, Object> e : conf.entrySet()) {
//            String k = e.getKey();
//            String v = ObjectUtils.toString(e.getValue(), "");
//            if (!k.matches("^webapp\\.\\w+\\.war$"))
//                continue;
//
//            String webAppName = StringUtils.split(k, ".")[1];
//            String pathSpecs = conf.checkGetString("webapp." + webAppName + ".path");
//
//            WebAppContext webAppContext = new WebAppContext();
//            webAppContext.setContextPath(pathSpecs);
//            webAppContext.setWar(v);
//            server.setHandler(webAppContext);
//        }
//    }
//
//    @SuppressWarnings("unchecked")
//    private static void initDirWebApp(Server server, Configuration conf) {
//        for (Map.Entry<String, Object> e : conf.entrySet()) {
//            String k = e.getKey();
//            String v = ObjectUtils.toString(e.getValue(), "");
//            if (!k.matches("^webapp\\.\\w+\\.dir"))
//                continue;
//
//            String webAppName = StringUtils.split(k, ".")[1];
//            LinkedHashMap<String, String> dirs = (LinkedHashMap<String, String>) JsonUtils.fromJson(v, LinkedHashMap.class);
//            String pathSpecs = conf.checkGetString("webapp." + webAppName + ".path");
//
//
//            WebAppContext webAppContext = new WebAppContext();
//            webAppContext.setDescriptor(dirs.get("web.xml"));
//            webAppContext.setResourceBase(dirs.get("resource"));
//            webAppContext.setContextPath(pathSpecs);
//
//            server.setHandler(webAppContext);
//        }
//    }

    public static void run() {
        Server server = new Server();

//        Global.get().setServer(server);

        //String proxy=GlobalConfig.get().getString("server.proxy","http");
        //if(proxy.equals("https")){
        //    configureServerHttps(server);
        //}else{
            configureServer(server);
        //}

        // TelnetAppenderService telnetLog = TelnetAppenderService.getInstance();

        try {
            for (LifeCycle lc : lifeCycles) {
                try {
                lc.before();
                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
            }
//            MQCollection.initMQs();
        //    if (conf.getBoolean("log.telnet", false))
        //        telnetLog.start();
            server.start();

//            Global.get().serverStartAfter();

            System.out.println("server start...");

            server.join();
        } catch (Exception e) {
            throw new ServerException(BaseErrors.PLATFORM_RUN_SERVER_ERROR, e, "Run Jetty basedevss error");
        } finally {
        //    if (telnetLog.isStarted())
        //        telnetLog.stop();
            server.destroy();

            for (LifeCycle lc : lifeCycles) {
                try {
                    lc.after();
                } catch (Exception ignored) {
                }
            }

            //MQCollection.destroyMQs();
        }
    }


    private static void printHelp() {
        //System.out.printf("%s -c configPath1 [-c configPath2 ...]\n", JettyServer.class.getName());
    }

    public static void main(String[] args) throws IOException {
        if (ArrayUtils.contains(args, "-h")) {
            printHelp();
            return;
        }

        GlobalConfig.loadArgs(args);
        System.out.println("server start loadArgs ...");
        Configuration config = GlobalConfig.get();

        String[] lifeClasses = StringUtils2.splitArray(config.getString("globals", ""), ",", true);
        for (String lifeClass : lifeClasses) {
            lifeCycles.add((LifeCycle) ClassUtils2.newInstance(lifeClass));
        }

        //pid
        String pidDirStr = FileUtils.getUserDirectoryPath() + "/.bpid";
        File pidDir = new File(pidDirStr);
        if (!pidDir.exists()) {
            FileUtils.forceMkdir(pidDir);
        }
        ProcessUtils.writeProcessId(pidDirStr + "/"+ config.getString("server.pid.fileName","yqh_server.pid"));
        System.out.println("server start begin...");
        run();

    }



    public static interface LifeCycle {
        void before() throws Exception;
        void after() throws Exception;
    }
}
