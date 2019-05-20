package com.yqh.marketing.common;

import com.yqh.marketing.basedevss.ServerException;
import com.yqh.marketing.basedevss.base.auth.WebSignatures;

import com.yqh.marketing.basedevss.base.context.AdminContext;
import com.yqh.marketing.basedevss.base.context.ClientContext;

import com.yqh.marketing.basedevss.base.data.Record;
import com.yqh.marketing.basedevss.base.log.Logger;
import com.yqh.marketing.basedevss.base.util.Encoders;
import com.yqh.marketing.basedevss.base.web.QueryParams;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;


public class PortalContext {
    private static final Logger L = Logger.getLogger(PortalContext.class);


    /*public static AdminContext getAdminContext(HttpServletRequest req, QueryParams qp, boolean needTicket,boolean needSign) {
        AdminContext ctx = new AdminContext();
        L.debug(null,qp.toString());

        //需要ticket
        String ticket = qp.getString("ticket");
        if(ticket.isEmpty())
            ticket = Constants.getTicket(req);
        if (needTicket) {
            if (ticket.isEmpty() || "null".equals(ticket) || "undefined".equals(ticket))
                throw new ServerException(ErrorCodes.AUTH_NEED_TOKEN, "need ticket");
        }


        long user_type = 0;
        Record ut;
        //如果上传了ticket，不管是不是合法，都需要验证
        if (!ticket.isEmpty() && !"null".equals(ticket) && !"undefined".equals(ticket)) {
            ut = GlobalLogics.getUser().getUserIdByTicket(null, ticket);
            if (ut.isEmpty()) {
                throw new ServerException(ErrorCodes.AUTH_TICKET_INVALID, "ticket not exists");
            } else {
                user_type = ut.getInt("USER_TYPE");
                ctx.setUser_id(ut.getString("USER_ID"));
                Record users = GlobalLogics.getUser().getUserById(ut.getString("USER_ID"));
                ctx.setUserDeptId(users.getString("DEPARTMENT_ID"));
                ctx.setDisplay_name(users.getString("DISPLAY_NAME"));
                if(users!=null){
                    ctx.setUserName(users.getString("USER_NAME"));
                }
                ctx.setUser_type(String.valueOf(user_type));
                String rurl=req.getHeader("referer");
//                if(!GlobalLogics.getUserMod().existsUserModByName(ut.getString("USER_ID"),rurl)){
//                    throw new ServerException(ErrorCodes.AUTH_OUT_OF_LIMIT, "no verify by permission");
//                }
            }
        }

        if (needSign) {
            //再检查签名是不是正确   q
            String sign = qp.checkGetString("sign");
            String expectantSign = WebSignatures.md5Sign(qp);
            if (!StringUtils.equals(sign, expectantSign)) {
                sign = StringUtils.replace(sign, " ", "+");
                if (!StringUtils.equals(sign, expectantSign)){
                    throw new ServerException(ErrorCodes.AUTH_SIGNATURE_ERROR, "Invalid md5 signatures");
                }
            }
        }

        String IP = req.getRemoteAddr() ;
        String URI = req.getRequestURI();
        String URL = ctx.getUrl();
        String QSTR =req.getQueryString();
        ctx.setApp_type(qp.getString("app_type", "0"));
        ctx.setDevice_id(qp.getString("device_id", ""));
        ctx.setLanguage(qp.getString("language", ""));
        ctx.setTicket(qp.getString("ticket", ""));
        ctx.setUser_agent(qp.getString("user_agent", ""));
        ctx.setApp_platform(qp.getString("app_platform", ""));
        ctx.setCall_id(qp.getString("call_id", "0"));
        ctx.setLocation(qp.getString("location", ""));
        ctx.setVersionCode(qp.getString("version_code", ""));
        ctx.setIp_addr(req.getRemoteAddr());
        //add by wanghanxiao 添加用户VIP级别，如果非会员，则依旧为null

        if (!qp.getString("url", "").equals("")){
            byte[] a = Encoders.fromBase64(qp.getString("url", ""));
            String urlReally =  new String(a);
            ctx.setUrl(URLDecoder.decode(urlReally));
        } else {
            ctx.setUrl("");
        }
        if (!qp.getString("frompage", "").equals("")){
            byte[] a = Encoders.fromBase64(qp.getString("frompage", ""));
            String urlReally =  new String(a);
            ctx.setFrom_page(URLDecoder.decode(urlReally));
        } else {
            ctx.setFrom_page("");
        }

        ctx.setChannel_id(qp.getString("channel_id", ""));
        String deviceId = qp.getString("device_id","");
        String UAU =  ctx.getUser_agent().toUpperCase();
        if (UAU.contains("ANDROID")
                || UAU.contains("IPHONE")
                || UAU.contains("IPAD")) {
            deviceId = "mobile";
        }
        return ctx;
    }
*/

    public static ClientContext getClientContext(HttpServletRequest req, QueryParams qp, boolean needTicket,boolean needSign) {

        ClientContext ctx = new ClientContext();
        L.debug(null, qp.toString());
        //需要ticket
        String ticket = qp.getString("ticket");


        if (ticket.isEmpty())
            ticket = Constants.getTicket(req);
        if (needTicket) {
            if (ticket.isEmpty() || "null".equals(ticket) || "undefined".equals(ticket))
                throw new ServerException(ErrorCodes.AUTH_NEED_TOKEN, "need ticket");
        }

        //如果上传了ticket，不管是不是合法，都需要验证
        if (!ticket.isEmpty() && !"null".equals(ticket) && !"undefined".equals(ticket)) {
            if (needSign) {
                //再检查签名是不是正确
                String sign = qp.checkGetString("sign");
                String app_id = "20181008004";
                //此处去数据库里面查
                String app_key = "12190b45cac111e8b81112163e43242f";
                //移除数据
                String expectantSign = WebSignatures.ClientMd5Sign(qp, app_id, app_key);
                if (!StringUtils.equals(sign, expectantSign)) {
                    sign = StringUtils.replace(sign, " ", "+");
                    if (!StringUtils.equals(sign, expectantSign)) {
                        throw new ServerException(ErrorCodes.AUTH_SIGNATURE_ERROR, "Invalid md5 signatures");
                    }
                }
            }
        }
        return ctx;
    }
}
