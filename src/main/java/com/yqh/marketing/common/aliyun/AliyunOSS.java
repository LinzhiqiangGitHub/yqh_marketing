package com.yqh.marketing.common.aliyun;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.yqh.marketing.basedevss.base.conf.GlobalConfig;
import org.apache.commons.fileupload.FileItem;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AliyunOSS {
    private static final String endpoint = GlobalConfig.get().getString("aliyun.oss.endpoint","http://oss-cn-beijing.aliyuncs.com");
    private static final String accessKeyId = GlobalConfig.get().getString("aliyun.oss.accessKeyId","LTAISJosTtS9oHDu");
    private static final String accessKeySecret = GlobalConfig.get().getString("aliyun.oss.accessKeySecret","A5i4E6P3eip80QKRdiUsNXKjXGPT7C");
    private static final String bucketName = GlobalConfig.get().getString("aliyun.oss.bucketName","qqyqh-video");

    public static void uploadFile(String key, InputStream is){
        // 创建OSSClient实例
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        // 上传文件
        ossClient.putObject(bucketName, key, is);
        // 关闭client
        ossClient.shutdown();
    }

    public static void uploadFile(String key, FileItem file_item){
        try {
            uploadFile(key,file_item.getInputStream());
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public static Map<String,Object> getFile(String key){
        Map<String,Object> fileRecord = new HashMap<String,Object>();
        try {
            // 创建OSSClient实例
            OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

            OSSObject ossObject = ossClient.getObject(bucketName, key);
            fileRecord.put("ContentType",ossObject.getObjectMetadata().getContentType());
            fileRecord.put("ContentLength",ossObject.getObjectMetadata().getContentLength());

            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            byte[] buf = new byte[8 * 1024];
            int rc = 0;
            InputStream in = ossObject.getObjectContent();
            while ((rc = in.read(buf, 0, buf.length)) > 0) {
                swapStream.write(buf, 0, rc);
            }
            byte[] fileBytes = swapStream.toByteArray();
            fileRecord.put("FileBytes",fileBytes);
            in.close();
            ossObject.getObjectContent().close();
            ossClient.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileRecord;
    }

}
