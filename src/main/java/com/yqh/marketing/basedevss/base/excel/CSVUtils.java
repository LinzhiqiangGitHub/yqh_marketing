package com.yqh.marketing.basedevss.base.excel;


import com.csvreader.CsvReader;
import org.apache.commons.fileupload.FileItem;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * CSV操作(导出和导入)
 *
 */
public class CSVUtils {
    /**
     * 导入
     *
     */
    public static List<String> importCsv(FileItem fileItem) throws IOException {
        List<String> dataList=new ArrayList<String>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileItem.getInputStream(),"GBK"));
            CsvReader creader = new CsvReader(reader, ',');
            while(creader.readRecord()){
                dataList.add(creader.getRawRecord());
            }
            creader.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }

        return dataList;
    }
}