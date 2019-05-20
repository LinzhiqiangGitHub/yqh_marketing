package com.yqh.marketing.basedevss.base.web;

import com.yqh.marketing.basedevss.base.io.Charsets;
import com.yqh.marketing.basedevss.base.util.json.JsonUtils;
import com.yqh.marketing.common.Constants;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.zip.GZIPOutputStream;

public abstract class OutputServlet extends HttpServlet {
    private static final Logger L = LoggerFactory.getLogger(OutputServlet.class);

    /*
    * 取得一个压缩的PrintWriter
    */
    private static PrintWriter getWriter(HttpServletResponse response) throws IOException {
        ServletOutputStream sos = response.getOutputStream();
        GZIPOutputStream gzipos = new GZIPOutputStream(sos);

        return new PrintWriter(gzipos);
    }

    protected static void output(QueryParams qp, HttpServletRequest req, HttpServletResponse resp, String text, int statusCode, String contentType) throws IOException {
        resp.setCharacterEncoding(Charsets.DEFAULT);
        resp.setStatus(statusCode);
        resp.setContentType(contentType);
        resp.setHeader("Cache-Control", "maxage=1");
        resp.setHeader("Pragma", "public");
        resp.setDateHeader("Date", 0);
        resp.setDateHeader("Expires", 1);
        String address="";
        try {
            InetAddress ad = InetAddress.getLocalHost();
            address=Constants.getServerIpName(ad.getHostAddress());
//            String[] adArr = ad.getHostAddress().split("\\.");
//            if(adArr.length>0) {
//                address = adArr[adArr.length - 1];
//            }
        }
        catch (UnknownHostException e){

        }catch (Exception e){

        }
        resp.setHeader("Servers",address);
        String encoding=req.getHeader("Accept-Encoding");

        PrintWriter writer;
        if (encoding!=null && encoding.indexOf("gzip")>=0) {
            resp.setHeader("Content-Encoding", "gzip");
            writer = getWriter(resp);
        }
        else
        {
            writer=resp.getWriter();
        }

        String callback = qp != null ? qp.getString("callback", "") : req.getParameter("callback");
        if (StringUtils.isBlank(callback)) {
            writer.write(text);
        } else {
            writer.write(callback);
            if (JsonUtils.isValidate(text)) {
                writer.write("(");
                writer.write(text);
                writer.write(")");
            } else {
                writer.write("('");
                writer.write(StringEscapeUtils.escapeJavaScript(text));
                writer.write("')");
            }
        }
        writer.flush();
        writer.close();
//        }
//        if (L.isTraceEnabled())
//            L.trace(text);
//        L.debug(text);
    }
}
