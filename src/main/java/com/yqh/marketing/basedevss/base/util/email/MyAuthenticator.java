package com.yqh.marketing.basedevss.base.util.email;

import javax.mail.*;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-8-28
 * Time: 下午4:03
 * To change this template use File | Settings | File Templates.
 */
public class MyAuthenticator extends Authenticator {

    String userName = null;
    String password = null;

    public MyAuthenticator() {
    }

    public MyAuthenticator(String username, String password) {
        this.userName = username;
        this.password = password;
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(userName, password);
    }
}
