package com.yqh.marketing.common;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

public class ExcelHelpUtil {

   
    public static void excelHelp(HttpServletRequest request,HttpServletResponse response,List<Map<String, Object>> list,String[] keys,String[] columnNames,String fileName) throws Exception {
    	ByteArrayOutputStream os = new ByteArrayOutputStream();
    	//list最大记录32767
        try {
            ExcelUtil.createWorkBook(list,keys,columnNames).write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] content = os.toByteArray();
        InputStream is = new ByteArrayInputStream(content);
        // 设置response参数，可以打开下载页面
        setFileDownloadHeader(request,response,fileName);
//        response.reset();
//        response.setContentType("application/vnd.ms-excel;charset=utf-8");
//        response.setHeader("Content-Disposition", "attachment;filename="+ new String((fileName + ".xls").getBytes(), "iso-8859-1"));
        ServletOutputStream out = response.getOutputStream();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(out);
            byte[] buff = new byte[2048];
            int bytesRead;
            // Simple read/write loop.
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (final IOException e) {
            throw e;
        } finally {
            if (bis != null)
                bis.close();
            if (bos != null)
                bos.close();
        }
    }
    public static void setFileDownloadHeader(HttpServletRequest request, HttpServletResponse response, String fileName) throws Exception{
        final String userAgent = request.getHeader("USER-AGENT");
        try {
            String finalFileName = null;
            if(userAgent != null && userAgent.contains("MSIE")){//IE浏览器
                finalFileName = URLEncoder.encode(fileName,"UTF8");
            }else if(userAgent != null && userAgent.contains("Mozilla")){//google,火狐浏览器
                finalFileName = new String(fileName.getBytes(), "ISO8859-1");
            }else{
                finalFileName = URLEncoder.encode(fileName,"UTF8");//其他浏览器
            }
            response.setHeader("Content-Disposition", "attachment; filename=\"" + finalFileName + ".xlsx\"");//这里设置一下让浏览器弹出下载提示框，而不是直接在浏览器中打开
        } catch (UnsupportedEncodingException e) {
        	throw e;
        }
    }
}
