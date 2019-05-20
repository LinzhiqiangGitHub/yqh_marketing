package com.yqh.marketing.basedevss.base.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenguo on 2016/2/3.
 */
public class Cookies {
    /**
     * 设置cookie
     * @param response
     * @param name  cookie名字
     * @param value cookie值
     * @param maxAge cookie生命周期  以秒为单位
     */
    public static void addCookie(HttpServletResponse response,String domain, String name, String value, int maxAge){
        Cookie cookie = new Cookie(name,value);
        cookie.setPath("/");
        cookie.setDomain(domain);
        if(maxAge>0)  cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }
    /**
     * 根据名字获取cookie
     * @param request
     * @param name cookie名字
     * @return
     */
    public static Cookie getCookie(HttpServletRequest request, String name){
        Map<String,Cookie> cookieMap = ReadCookieMap(request);
        if(cookieMap.containsKey(name)){
            Cookie cookie = (Cookie)cookieMap.get(name);
            return cookie;
        }else{
            return null;
        }
    }

    /**
     * 修改cookie
     * @param request
     * @param response
     * @param name
     * @param value
     * 注意一、修改、删除Cookie时，新建的Cookie除value、maxAge之外的所有属性，例如name、path、domain等，都要与原Cookie完全一样。否则，浏览器将视为两个不同的Cookie不予覆盖，导致修改、删除失败。
     */
    public static void editCookie(HttpServletRequest request,HttpServletResponse response,String domain,String name,String value,int maxAge){
        Cookie[] cookies = request.getCookies();
        if (null==cookies) {
            System.out.println("没有cookie==============");
        } else {
            for(Cookie cookie : cookies){
                if(cookie.getName().equals(name)){
                    cookie.setValue(value);
                    cookie.setPath("/");
                    cookie.setDomain(domain);
                    cookie.setMaxAge(maxAge);// 设置为30min
                    response.addCookie(cookie);
                    break;
                }
            }
        }

    }
    /**
     * 删除cookie
     * @param request
     * @param response
     * @param name
     */
    public static void delCookie(HttpServletRequest request,HttpServletResponse response,String doMain,String name){
        Cookie[] cookies = request.getCookies();
        if (null==cookies) {
        } else {
            for(Cookie cookie : cookies){
                if(cookie.getName().equals(name)){
                    cookie.setValue(null);
                    cookie.setMaxAge(0);// 立即销毁cookie
                    cookie.setPath("/");
                    cookie.setDomain(doMain);
                    response.addCookie(cookie);
                    break;
                }
            }
        }
    }
    /**
     * 将cookie封装到Map里面
     * @param request
     * @return
     */
    public static Map<String,Cookie> ReadCookieMap(HttpServletRequest request){
        Map<String,Cookie> cookieMap = new HashMap<String,Cookie>();
        Cookie[] cookies = request.getCookies();
        if(null!=cookies){
            for(Cookie cookie : cookies){
                cookieMap.put(cookie.getName(), cookie);
            }
        }
        return cookieMap;
    }
}
