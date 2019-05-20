package com.yqh.marketing.utils;

import java.io.UnsupportedEncodingException;
import org.apache.commons.httpclient.Header;  
import org.apache.commons.httpclient.HttpClient;  
import org.apache.commons.httpclient.NameValuePair;  
import org.apache.commons.httpclient.methods.PostMethod;  
  
public class SendMsg_webchinese {  
  
public static void main(String[] args)throws Exception {
  
        HttpClient client = new HttpClient();
        PostMethod post = new PostMethod("https://wechat.qqyqh.com/api/sms/send_sms");

        //头文件中转码
        post.addRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=gbk");
        NameValuePair[] data ={

        new NameValuePair("Uid", "本站用户名"),
        new NameValuePair("Key", "接口秘钥"),
        new NameValuePair("smsMob","手机号码"),
        new NameValuePair("smsText","验证码：8888")};

        post.setRequestBody(data);

        client.executeMethod(post);

        Header[] headers = post.getResponseHeaders();
        int statusCode = post.getStatusCode();
        System.out.println("statusCode:"+statusCode);
        for(Header h : headers)
        {
        System.out.println(h.toString());
        }
        String result = new String(post.getResponseBodyAsString().getBytes("gbk"));
        System.out.println(result); //打印返回消息状态


        post.releaseConnection();
  
    }
}  