package com.yqh.marketing.common;

import com.yqh.marketing.basedevss.base.log.Logger;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public class HttpRequest {
    private static final Logger L = Logger.getLogger(HttpRequest.class);
    private static final int connTimeout=10000;
    private static final int readTimeout=10000;
    private static final String charset="UTF-8";
    private static HttpClient client = null;

    static {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(128);
        cm.setDefaultMaxPerRoute(128);
        client = HttpClients.custom().setConnectionManager(cm).build();
    }

    public static String httpsGet(String url,String param,Map<String, String> headers)
    {
        HttpClient client = null;
        HttpGet get = new HttpGet(url+(param.isEmpty()?"":"?"+param));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            get.addHeader(e.getKey(), e.getValue());
        }
        String result = "";
        try {
            // 设置参数
            RequestConfig.Builder customReqConf = RequestConfig.custom();

            customReqConf.setConnectTimeout(connTimeout);
            customReqConf.setSocketTimeout(readTimeout);

            get.setConfig(customReqConf.build());

            HttpResponse res = null;

            if (url.startsWith("https")) {
                // 执行 Https 请求.
                try {
                    client = createSSLInsecureClient();
                    res = client.execute(get);
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                }catch (ClientProtocolException e){
                    e.printStackTrace();
                } catch (IOException e){
                    e.printStackTrace();
                }

            } else {
                // 执行 Http 请求.
                client = HttpRequest.client;
                try {
                    res = client.execute(get);
                } catch (ClientProtocolException e){
                    e.printStackTrace();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }

            try {
                result = IOUtils.toString(res.getEntity().getContent(), charset);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            get.releaseConnection();
            if (url.startsWith("https") && client != null && client instanceof CloseableHttpClient) {

                try {
                    ((CloseableHttpClient) client).close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String get(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString =param.isEmpty()?url:(url + "?" + param);
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setRequestProperty("charset","UTF-8");
            connection.setConnectTimeout(1000);
            connection.setReadTimeout(1000);

            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String post(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }

    public static String postErp(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "lechun/postjson");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }
    public static String httpsPost(String url,String param){
        HttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = "";
        String charset="utf-8";
        try{
            httpClient = new SSLClient();
            httpPost = new HttpPost(url);
            //设置参数
            httpPost.setEntity(new StringEntity(param, "UTF-8"));
            HttpResponse response = httpClient.execute(httpPost);
            if(response != null){
                HttpEntity resEntity = response.getEntity();
                if(resEntity != null){
                    result = EntityUtils.toString(resEntity,charset);
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }finally {
            httpClient = null;
            httpPost = null;
        }
        return result;
    }

    /**
     * GrowingIO的post发送
     * @param url
     * @param params
     * @return
     */
    public static String httpsGrowingIOPost(String url,String params){

        HttpPost post=null;
        HttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = "";
        String charset="utf-8";
        try{
            httpClient = new SSLClient();
            httpPost = new HttpPost(url);
            //设置参数
            httpPost.setEntity(new StringEntity(params, ContentType.APPLICATION_JSON));
            //httpPost.setHeader("Content-Type","application/json");
            httpPost.setHeader("Access-Token","QYzrRM9xtTbXdA6GtwFvqBKe7H8Z4pnf");  //publicKey=QYzrRM9xtTbXdA6GtwFvqBKe7H8Z4pnf
            HttpResponse response = httpClient.execute(httpPost);
            if(response != null){
                HttpEntity resEntity = response.getEntity();
                if(resEntity != null){
                    result = EntityUtils.toString(resEntity,charset);
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }finally {
            httpClient = null;
            httpPost = null;
        }
        return result;
    }



    public static String getBodyString(BufferedReader br) {
        String inputLine;

        StringBuffer sb = new StringBuffer();
        try {
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            br.close();
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
        return sb.toString();
    }

    /**
     * 发送一个 GET 请求
     *
     * @param url
     * @return
     * @throws ConnectTimeoutException   建立链接超时
     * @throws SocketTimeoutException   响应超时
     * @throws Exception
     */
    public static String httpsGet(String url,String param)
    {

        HttpClient client = null;
        HttpGet get = new HttpGet(url+(param.isEmpty()?"":"?"+param));
        String result = "";
        try {
            // 设置参数
            RequestConfig.Builder customReqConf = RequestConfig.custom();

            customReqConf.setConnectTimeout(connTimeout);
            customReqConf.setSocketTimeout(readTimeout);

            get.setConfig(customReqConf.build());

            HttpResponse res = null;

            if (url.startsWith("https")) {
                // 执行 Https 请求.
                try {
                    client = createSSLInsecureClient();
                    res = client.execute(get);
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                }catch (ClientProtocolException e){
                    e.printStackTrace();
                } catch (IOException e){
                    e.printStackTrace();
                }

            } else {
                // 执行 Http 请求.
                client = HttpRequest.client;
                try {
                    res = client.execute(get);
                } catch (ClientProtocolException e){
                    e.printStackTrace();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }

            try {
                result = IOUtils.toString(res.getEntity().getContent(), charset);
//                L.debug(null,"httpsGet result:"+result.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            get.releaseConnection();
            if (url.startsWith("https") && client != null && client instanceof CloseableHttpClient) {

                try {
                    ((CloseableHttpClient) client).close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
    /**
     * 创建 SSL连接
     * @return
     * @throws GeneralSecurityException
     */
    private static CloseableHttpClient createSSLInsecureClient() throws GeneralSecurityException {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                public boolean isTrusted(X509Certificate[] chain,String authType) throws CertificateException {
                    return true;
                }
            }).build();

            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new X509HostnameVerifier() {

                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }

                @Override
                public void verify(String host, SSLSocket ssl)
                        throws IOException {
                }

                @Override
                public void verify(String host, X509Certificate cert)
                        throws SSLException {
                }

                @Override
                public void verify(String host, String[] cns,
                                   String[] subjectAlts) throws SSLException {
                }

            });

            return HttpClients.custom().setSSLSocketFactory(sslsf).build();

        } catch (GeneralSecurityException e) {
            throw e;
        }
    }
    public static String getRemoteHost(HttpServletRequest request){
        String ipAddress = null;
        //ipAddress = this.getRequest().getRemoteAddr();
        ipAddress = request.getHeader("x-forwarded-for");
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if(ipAddress.equals("127.0.0.1")){
                //根据网卡取本机配置的IP
                InetAddress inet=null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress= inet.getHostAddress();
            }

        }

        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if(ipAddress!=null && ipAddress.length()>15){ //"***.***.***.***".length() = 15
            if(ipAddress.indexOf(",")>0){
                ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }
    public static String getUserBrowser(HttpServletRequest request){
        String Agent = request.getHeader("User-Agent");
        StringTokenizer st = new StringTokenizer(Agent,";");
        st.nextToken();
        //得到用户的浏览器名
        String userbrowser = st.nextToken();
        st=null;
        return userbrowser;

    }
    public static String getUserOs(HttpServletRequest request){
        try {
            String Agent = request.getHeader("User-Agent");
            StringTokenizer st = new StringTokenizer(Agent, ";");
            st.nextToken();
            st.nextToken();
            String useros = st.nextToken();
            st = null;
            return useros;
        }catch (NoSuchElementException e){
            e.printStackTrace();
        }
        return "";
    }

}
