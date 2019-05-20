package com.yqh.marketing.common;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.IOException;

public class FileMD5Utils {

    public static String getMd5ByFile(String fileName)  {

        String md5 = null;
        FileInputStream fis=null;
        try {
            fis= new FileInputStream(fileName);
            md5 = DigestUtils.md5Hex(IOUtils.toByteArray(fis));
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fis!=null)
                IOUtils.closeQuietly(fis);
        }

        return md5;
//        String value = null;
//        FileInputStream in = null;
//        try {
//            File file=new File(fileName);
//            in=new FileInputStream(file);
//            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
//            MessageDigest md5 = MessageDigest.getInstance("MD5");
//            md5.update(byteBuffer);
//            BigInteger bi = new BigInteger(1, md5.digest());
//            value = bi.toString(16);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if(null != in) {
//                try {
//                    in.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return value;
    }

    public static void main(String[] args) throws IOException {

        String path="E:\\commons-codec-1.9-bin.zip";

        String v = getMd5ByFile(path);
        System.out.println("MD5:"+v.toUpperCase());

        FileInputStream fis= new FileInputStream(path);
        String md5 = DigestUtils.md5Hex(IOUtils.toByteArray(fis));
        IOUtils.closeQuietly(fis);
        System.out.println("MD5:"+md5);

        //System.out.println("MD5:"+DigestUtils.md5Hex("WANGQIUYUN"));
    }

}