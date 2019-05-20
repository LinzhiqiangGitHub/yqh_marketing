package com.yqh.marketing.common;

import com.yqh.marketing.basedevss.base.data.Record;
import com.yqh.marketing.basedevss.base.data.RecordSet;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.collections.map.HashedMap;
import org.eclipse.jetty.util.log.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/13 0013.
 */
public class RequestPost extends SQLExecutorBase {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    /**
     * 获得authToken参数
     * @param projectKeyId
     * @param secretKey
     * @param keyArray
     * @return
     * @throws Exception
     */

    public String authToken(String projectKeyId, String secretKey, String keyArray) throws Exception {
        String message = "ai="+projectKeyId+"&cs="+keyArray;
        Mac hmac = Mac.getInstance("HmacSHA256");
        hmac.init(new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256"));
        byte[] signature = hmac.doFinal(message.getBytes("UTF-8"));
        return Hex.encodeHexString(signature);
    }

    /**
     * 判断是否有一次发送
     * @param startPage
     * @return
     */
    public boolean  next(String startPage){
        boolean next_id=false;

        String sqll="select count(*) as counts from t_mall_order_main";
        RecordSet recordSet=getSqlExecutor_Read().executeRecordSet(sqll, null);
      //  System.out.println("recordSet=========:"+recordSet);
        String rss=recordSet.get(0).getString("counts");

      //  System.out.println("====得到总字段数：===="+rss);
        //获得总页码数
        int pageCount=Integer.valueOf(rss)/100;
      //  System.out.println("获得总页码数"+pageCount);
        //获得当前页码数
        int currentPage=(Integer.valueOf(startPage)+100-1)/100;
      //  System.out.println("当前页码数======CurrentPage:"+currentPage);

        if (currentPage<=pageCount){
            next_id=true;
        }
        return next_id;
    }

    /**
     * 写入起始页码数据
     */
    public static void WriteDate(String endPage) {
        //C:\Users\Administrator\Desktop\lechun\startPage.txt
        try{
            File file = new File("startPage.txt");
            if (file.exists()) {
                file.delete();
            }

            file.createNewFile();
            BufferedWriter output = new BufferedWriter(new FileWriter(file));

            ArrayList ResolveList = new ArrayList();

            output.write(endPage);

            output.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    /**
     * 神策写入数据,message ,mkDir 文件夹名，fileName 文件名
     */
    public static void WriteSensorsDate(String message,String mkDir,String fileName) {
        byte[] buff = new byte[]{};
        try{
           File file = new File(mkDir);
            if (!file .exists()  && !file .isDirectory())
            {
                file .mkdir();
            }

           buff= message.getBytes();

            FileOutputStream out = new FileOutputStream(mkDir+"/SensorsLog_"+fileName+".txt", true);

            out.write(buff);
            out.close();
        } catch (Exception ex) {
            Log.debug("神策写入数据失败： "+fileName);
        }
    }

    /**
     *读取起始页
     * @return
     */
    public static String getPageStart(){
        String str2="";
        //C:\Users\Administrator\Desktop\lechun\startPage.txt
                try {
                    FileInputStream fis=new FileInputStream(new File("startPage.txt"));//新建一个FileInputStream对象
                     try {
                             byte[] b=new byte[fis.available()];//新建一个字节数组
                             fis.read(b);//将文件中的内容读取到字节数组中
                             fis.close();
                         str2=new String(b);//再将字节数组中的内容转化成字符串形式输出
                             System.out.println("startPage文件中得到的内容=============："+str2);
                         } catch (IOException e) {
                             e.printStackTrace();
                         }
                 } catch (FileNotFoundException e) {
                     e.printStackTrace();
                 }
        return str2;
    }
    /**
     * 获得所有需要上传的数据
     * @return
     */
    public String getAllJson(String pageStart){
       // RequestPost requestpost=new RequestPost();
        //获得起始页
      //  String pageStart=requestpost.getPageStart();

        String sql="SELECT sign,nick_name FROM t_mall_customer LIMIT "+pageStart+",100";
        RecordSet sign=getSqlExecutor_Read().executeRecordSet(sql,null);
        RecordSet recordSet =new RecordSet();

        if(sign!=null) {
/*            recordSet.add(Record.of("c1",sign.get(0).getString("sign")));
            recordSet.add(Record.of("c1",sign.get(1).getString("sign")));*/
            for (int i = 0; i < sign.size(); i++) {
                Record rd=new Record();
                    //decode
                    String cookie_Id = sign.get(i).getString("sign");
                    String nick_name =sign.get(i).getString("nick_name");

                    rd.put("cs1","user_id:"+cookie_Id);
                    rd.put("cs2","nick_name:"+nick_name);

                    recordSet.add(rd);
            }
        }
        return recordSet.toString();
    }

    /**
     * 得到getArray
     * @获得所有的cookies，并转化为json类型数据参数,从json数据获得KeyArray
     * @param
     * @return
     */
    public  String getKeyArray(String startPage){
        String sql="SELECT sign FROM t_mall_customer LIMIT "+startPage+",100";
        RecordSet sign=getSqlExecutor_Read().executeRecordSet(sql,null);
       // System.out.println("keyArray里面的RecordSet的value值："+sign.get(0).getString("sign"));
        Map keymap=new HashedMap();
        String p=",";
        String value="";
        if(sign!=null){
            for(int i=0;i<sign.size();i++){
                //decode
                String v= sign.get(i).getString("sign");
                //得到map类型的cookie
                keymap.put("cs"+(i+1),"user_id:"+v);
            }
            //将map强转成json
            JSONObject  cookieJson = JSONObject.fromObject(keymap);
            for(int i=0;i<sign.size();i++){
               value=value+cookieJson.get("cs"+(i+1));
                if(i<sign.size()-1){
                    value=value+p;
                }
            }
        }
        return  value;
    }


}
