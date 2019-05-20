package com.yqh.marketing.utils;

import org.apache.http.HttpEntity;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
/*
appKey
供应商标识
appSecret
供应商的密钥(请勿外泄且不参与传输)
method
接口方法名
sign
加密签名，具体加密算法请见Sign生成。
timestamp
进行接口调用时的时间戳，即当前时间戳 （时间戳：当前距离Epoch（1970年1月1日） 以秒计算的时间，即unix-timestamp）优俏货在各个接口处校验timestamp的时效性，五分钟之内有效
*/

public class SmsUtils {

    public CloseableHttpResponse sign(String login_mobile, String verification_code, String login) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://wechat.qqyqh.com/api/sms/send_sms");
        CloseableHttpResponse response1 = httpclient.execute(httpGet);
        String http_url = "https://wechat.qqyqh.com/api/sms/send_sms";
        String start = "您的验证码：" ;
        String end = "，有效时间为15分钟。";
        StringBuilder sub = new StringBuilder();
        BufferedReader br;
        URL url;
        HttpURLConnection con;
        String line;

        String sendContent =start + verification_code + end;
        url = new URL(http_url+""+"?MSISDNS="+login_mobile+""+"&CONTENT="+sendContent+""+"&SMS_TYPE="+login+"");
        con = (HttpURLConnection) url.openConnection();
        br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
        while ((line = br.readLine()) != null) {
            sub.append(line + "");
        }
        br.close();

        // The underlying HTTP connection is still held by the response object
        // to allow the response content to be streamed directly from the network socket.
        // In order to ensure correct deallocation of system resources
        // the user MUST call CloseableHttpResponse#close() from a finally clause.
        // Please note that if response content is not fully consumed the underlying
        // connection cannot be safely re-used and will be shut down and discarded
        // by the connection manager.
        try {
            System.out.println(response1.getStatusLine());
            HttpEntity entity1 = response1.getEntity();
            // do something useful with the response body
            // and ensure it is fully consumed
            EntityUtils.consume(entity1);
        } finally {
            response1.close();
        }

        HttpPost httpPost = new HttpPost("https://wechat.qqyqh.com/api/sms/send_sms");
        //List <NameValuePair> nvps = new ArrayList <NameValuePair>();
        //nvps.add(new BasicNameValuePair("MSISDNS", login_mobile));
//        String start = "您的验证码：" ;
//        String end = "，有效时间为15分钟。";
       // nvps.add(new BasicNameValuePair("CONTENT",start + verification_code + end));
       // nvps.add(new BasicNameValuePair("SMS_TYPE", login));
//        httpPost.setEntity(new StringEntity(nvps,"utf-8"));
        CloseableHttpResponse response2 = httpclient.execute(httpPost);

        try {
            System.out.println(response2.getStatusLine());
            HttpEntity entity2 = response2.getEntity();
            // do something useful with the response body
            // and ensure it is fully consumed
            EntityUtils.toString(entity2,"UTF-8");
            EntityUtils.consume(entity2);
            return(response2);
        } finally {
            response2.close();
        }
    }
}
