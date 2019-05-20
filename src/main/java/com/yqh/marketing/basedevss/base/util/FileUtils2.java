package com.yqh.marketing.basedevss.base.util;


import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileUtils2 {
    public static String expandPath(String path) {
        if (path.startsWith("~")) {
            return FileUtils.getUserDirectoryPath() + StringUtils.removeStart(path, "~");
        } else {
            return path;
        }
    }

    /**
     * 得到目录下的文件
     *
     * @param filepath   目录
     * @param extendName 扩展名，所有文件用*.*  html文件用*.html,多个扩展名用逗号(,)分隔
     * @return
     */
    public static List<String> getAllFiles(String filepath, String extendName) {
        List<String> files = new ArrayList<String>();
        String[] extend = extendName.split(",");
        for (int i = 0; i < extend.length; i++) {
            extend[i] = extend[i].substring(extend[i].lastIndexOf(".")).toLowerCase();
            if (extend[i].isEmpty() || extend[i].equals(".*")) {
                extend = null;
                break;
            }
        }
        if (extend != null) {
            Arrays.sort(extend);
        }
        getAllFiles(filepath, true, files, extend);
        return files;
    }

    public static boolean getAllFiles(String filepath, boolean isFirst, List<String> files, String[] extend) {

        //try {
        File file = new File(filepath);
        if (!file.isDirectory()) {
            if (extend != null) {
                String ex = file.getPath().substring(file.getPath().lastIndexOf(".")).toLowerCase();
                if (Arrays.binarySearch(extend, ex) > -1) {
                    files.add(file.getPath());
                }
            } else {
                files.add(file.getPath());
            }
        } else if (file.isDirectory()) {
            String[] filelist = file.list();
            for (int i = 0; i < filelist.length; i++) {
                File readfile = new File(filepath + "/" + filelist[i]);
                if (!readfile.isDirectory()) {
                    if (extend != null) {
                        int start = readfile.getPath().lastIndexOf(".");
                        String ex = readfile.getPath().substring(start >= 0 ? start : 0).toLowerCase();
                        if (Arrays.binarySearch(extend, ex) > -1) {
                            files.add(readfile.getPath());
                        }
                    } else {
                        files.add(readfile.getPath());
                    }
                } else if (readfile.isDirectory()) {
                    getAllFiles(filepath + "/" + filelist[i], false, files, extend);
                }
            }

        }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }catch (IOException e){
//            e.printStackTrace();
//        }
        return true;
    }

    /**
     * 复制一个目录及其子目录、文件到另外一个目录
     *
     * @param src
     * @param dest
     * @throws IOException
     */
    public static void copyFolder(File src, File dest) throws IOException {
        if (src.isDirectory()) {
            if (!dest.exists()) {
                dest.mkdir();
            }
            String files[] = src.list();
            for (String file : files) {
                File srcFile = new File(src, file);
                File destFile = new File(dest, file);
                // 递归复制
                copyFolder(srcFile, destFile);
            }
        } else {
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dest);

            byte[] buffer = new byte[1024];

            int length;

            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            in.close();
            out.close();
        }
    }

    public static void isExistPath(String path) {
        File file = new File(path);
        //判断文件夹是否存在,如果不存在则创建文件夹
        if (!file.exists()) {
            file.mkdirs();
        }
        file=null;
    }

    public static List<String> getDirs(String strPath) {
        List<String> dirs = new ArrayList<String>();

        File f = new File(strPath);
        if (f.isDirectory()) {
            dirs.add(f.getPath());
            File[] fList = f.listFiles();
            for (int j = 0; j < fList.length; j++) {
                if (fList[j].isDirectory())
                    dirs.addAll(getDirs(fList[j].getPath()));
            }
        }
        return dirs;
    }
}
