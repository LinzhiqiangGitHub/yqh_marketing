package com.yqh.marketing.common;

import com.yqh.marketing.basedevss.base.conf.Configuration;
import com.yqh.marketing.basedevss.base.conf.GlobalConfig;
import com.yqh.marketing.basedevss.base.data.Record;
import com.yqh.marketing.basedevss.base.util.DateUtils;
import com.yqh.marketing.basedevss.base.util.json.JsonUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.codehaus.jackson.JsonNode;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 导出Excel文档工具类
 * @author 那位先生
 * @date 2014-8-6
 * */
public class ExcelUtil {

    /**
     * 创建excel文档，
     * @param list 数据
     * @param keys list中map的key数组集合
     * @param columnNames excel的列名
     * */
    public static Workbook createWorkBook(List<Map<String, Object>> list,String []keys,String columnNames[]) {
        // 创建excel工作簿
    	Workbook wb = new SXSSFWorkbook(100);//keep 100 rows in memory, exceeding rows will be flushed to disk
        // 创建第一个sheet（页），并命名
        Sheet sheet = wb.createSheet(list.get(0).get("sheetName").toString());
        // 手动设置列宽。第一个参数表示要为第几列设；，第二个参数表示列的宽度，n为列高的像素数。
        for(int i=0;i<keys.length;i++){
            sheet.setColumnWidth(i, (short) (35.7 * 150));
        }

        // 创建第一行
        Row row = sheet.createRow(0);

        // 创建两种单元格格式
        CellStyle cs = wb.createCellStyle();
        CellStyle cs2 = wb.createCellStyle();

        // 创建两种字体
        Font f = wb.createFont();
        Font f2 = wb.createFont();

        // 创建第一种字体样式（用于列名）
        f.setFontHeightInPoints((short) 10);
        f.setColor(IndexedColors.BLACK.getIndex());
        f.setBoldweight(Font.BOLDWEIGHT_BOLD);

        // 创建第二种字体样式（用于值）
        f2.setFontHeightInPoints((short) 10);
        f2.setColor(IndexedColors.BLACK.getIndex());

//        Font f3=wb.createFont();
//        f3.setFontHeightInPoints((short) 10);
//        f3.setColor(IndexedColors.RED.getIndex());

        // 设置第一种单元格的样式（用于列名）
        cs.setFont(f);
        cs.setBorderLeft(CellStyle.BORDER_THIN);
        cs.setBorderRight(CellStyle.BORDER_THIN);
        cs.setBorderTop(CellStyle.BORDER_THIN);
        cs.setBorderBottom(CellStyle.BORDER_THIN);
        cs.setAlignment(CellStyle.ALIGN_CENTER);

        // 设置第二种单元格的样式（用于值）
        cs2.setFont(f2);
        cs2.setBorderLeft(CellStyle.BORDER_THIN);
        cs2.setBorderRight(CellStyle.BORDER_THIN);
        cs2.setBorderTop(CellStyle.BORDER_THIN);
        cs2.setBorderBottom(CellStyle.BORDER_THIN);
        cs2.setAlignment(CellStyle.ALIGN_CENTER);
        //设置列名
        for(int i=0;i<columnNames.length;i++){
            Cell cell = row.createCell(i);
            cell.setCellValue(columnNames[i]);
            cell.setCellStyle(cs);
        }
        //设置每行每列的值
        for (int i = 1; i < list.size(); i++) {
            // Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
            // 创建一行，在页sheet上
            Row row1 = sheet.createRow(i);
            // 在row行上创建一个方格
            for(int j=0;j<keys.length;j++){
                Cell cell = row1.createCell(j);
                cell.setCellValue(list.get(i).get(keys[j]) == null?" ": list.get(i).get(keys[j]).toString());
                cell.setCellStyle(cs2);
            }
        }
        return wb;
    }
    /**
     * 创建excel文档，
     * @param headList list中map的key数组集合
     * @param dataList excel的列名
     * */
    public static Record exportExcel(List<String> headList, List<Record> dataList, String datePattern, String file){
        Configuration conf = GlobalConfig.get();
        String fileName = file + "-" + DateUtils.now().replace("-","").replace(" ","").replace(":","")+headList.hashCode() + ".xlsx";
        try {

            String filePath = conf.getString("service.export.excel.fileStorage", "");
            JsonNode jn = JsonUtils.parse(filePath);
            JsonNode node =  jn.path("args");

            String fileInfoPath = node.get("dir").asText() +"/"+ fileName;
            OutputStream outXls = new FileOutputStream(fileInfoPath);

        String DEFAULT_DATE_PATTERN="yyyy-MM-dd HH:mm:ss";//默认日期格式
        if(datePattern==null) datePattern = DEFAULT_DATE_PATTERN;
        // 声明一个工作薄
        SXSSFWorkbook workbook = new SXSSFWorkbook(1000);//缓存
        workbook.setCompressTempFiles(true);
        // 列头样式
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillPattern(FillPatternType.NO_FILL);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        Font headerFont = workbook.createFont();
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setBold(false);
        headerStyle.setFont(headerFont);
        // 单元格样式
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillPattern(FillPatternType.NO_FILL);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        Font cellFont = workbook.createFont();
        cellFont.setBold(false);
        cellStyle.setFont(cellFont);
            // 单元格样式
            CellStyle floatCellStyle = workbook.createCellStyle();
            floatCellStyle.setFillPattern(FillPatternType.NO_FILL);
            floatCellStyle.setBorderBottom(BorderStyle.THIN);
            floatCellStyle.setBorderLeft(BorderStyle.THIN);
            floatCellStyle.setBorderRight(BorderStyle.THIN);
            floatCellStyle.setBorderTop(BorderStyle.THIN);
            floatCellStyle.setAlignment(HorizontalAlignment.CENTER);
            floatCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            DataFormat df = workbook.createDataFormat();
            floatCellStyle.setDataFormat(df.getFormat("0.00"));
            cellFont.setBold(false);
            cellStyle.setFont(cellFont);
        // 生成一个(带标题)表格
        SXSSFSheet sheet = workbook.createSheet();

        // 遍历集合数据，产生数据行
        SXSSFRow headerRow = sheet.createRow(0); //列头 rowIndex =0
        int headColIndex=0;
        for(String fieldName : headList) {
            headerRow.createCell(headColIndex).setCellValue(fieldName);
            headerRow.getCell(headColIndex).setCellStyle(headerStyle);
            headColIndex +=1;
        }
        int rowIndex = 1;
        for (Record data : dataList) {
            SXSSFRow dataRow = sheet.createRow(rowIndex);
                int colIndex = 0;
                for(String key:data.getColumns()){
                    SXSSFCell newCell = dataRow.createCell(colIndex);
                   Object o =  data.get(key);

                    if(o == null) {
                        String cellValue = "";
                        newCell.setCellType(CellType.STRING);
                        newCell.setCellValue(cellValue);
                        newCell.setCellStyle(cellStyle);
                    }else if(o instanceof Date) {
                        String cellValue = new SimpleDateFormat(datePattern).format(o);
                        newCell.setCellType(CellType.STRING);
                        newCell.setCellValue(cellValue);
                        newCell.setCellStyle(cellStyle);
                    } else if(o instanceof Integer || o instanceof Long) {
                        Integer cellValue = Integer.parseInt(o.toString());
                        newCell.setCellType(CellType.NUMERIC);
                        newCell.setCellValue(cellValue);
                        newCell.setCellStyle(cellStyle);
                    } else if(o instanceof Float || o instanceof Double) {
                        Double cellValue = Double.parseDouble(o.toString());
                        newCell.setCellType(CellType.NUMERIC);
                        newCell.setCellValue(cellValue);
                        newCell.setCellStyle(floatCellStyle);
                    } else {
                        String cellValue = o.toString();
                        newCell.setCellType(CellType.NUMERIC);
                        newCell.setCellValue(cellValue);
                        newCell.setCellStyle(cellStyle);
                    }

                    colIndex += 1;
                }

            rowIndex++;
        }
        // 自动调整宽度
        /*for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }*/
            workbook.write(outXls);
            workbook.close();
            workbook.dispose();
            outXls.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String url0 = String.format(conf.getString("service.export.excelPattern", "/exportFileStorage/%s"), fileName);
        return Record.of("url",url0);
    }
   /* *//**
     * 创建excel文档，
     * @param title 数据
     * @param headMap list中map的key数组集合
     * @param dataList excel的列名
     * *//*
   public static void exportExcel(String title,Map<String, String> headMap,List<Object> dataList,String datePattern,int colWidth,String file) throws FileNotFoundException {
        OutputStream outXls = new FileOutputStream(file);
        String NO_DEFINE = "no_define";//未定义的字段
        String DEFAULT_DATE_PATTERN="yyyy-MM-dd HH:mm:ss";//默认日期格式
        int DEFAULT_COLOUMN_WIDTH = 17;
        if(datePattern==null) datePattern = DEFAULT_DATE_PATTERN;
        // 声明一个工作薄
        SXSSFWorkbook workbook = new SXSSFWorkbook(1000);//缓存
        workbook.setCompressTempFiles(true);
        //表头样式
        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        Font titleFont = workbook.createFont();
        titleFont.setFontHeightInPoints((short) 20);
        titleFont.setBoldweight((short) 700);
        titleStyle.setFont(titleFont);
        // 列头样式
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        Font headerFont = workbook.createFont();
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headerStyle.setFont(headerFont);
        // 单元格样式
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        Font cellFont = workbook.createFont();
        cellFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        cellStyle.setFont(cellFont);
        // 生成一个(带标题)表格
        SXSSFSheet sheet = workbook.createSheet();
        //设置列宽
        int minBytes = colWidth<DEFAULT_COLOUMN_WIDTH?DEFAULT_COLOUMN_WIDTH:colWidth;//至少字节数
        int[] arrColWidth = new int[headMap.size()];
        // 产生表格标题行,以及设置列宽
        String[] properties = new String[headMap.size()];
        String[] headers = new String[headMap.size()];
        int ii = 0;
        for (Iterator<String> it = headMap.keySet().iterator(); it.hasNext();) {
            String fieldName = it.next();

            properties[ii] = fieldName;
            headers[ii] = headMap.get(fieldName);

            int bytes = fieldName.getBytes().length;
            arrColWidth[ii] =  bytes < minBytes ? minBytes : bytes;
            sheet.setColumnWidth(ii,arrColWidth[ii]*256);
            ii++;
        }
        // 遍历集合数据，产生数据行
        int rowIndex = 0;
        for (Object obj : dataList) {
            if(rowIndex == 0){
                SXSSFRow titleRow = sheet.createRow(0);//表头 rowIndex=0
                titleRow.createCell(0).setCellValue(title);
                titleRow.getCell(0).setCellStyle(titleStyle);
                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headMap.size() - 1));

                SXSSFRow headerRow = sheet.createRow(1); //列头 rowIndex =1
                for(int i=0;i<headers.length;i++)
                {
                    headerRow.createCell(i).setCellValue(headers[i]);
                    headerRow.getCell(i).setCellStyle(headerStyle);

                }
                rowIndex = 2;//数据内容从 rowIndex=2开始
            }
            JSONObject jo = (JSONObject) JSONObject.toJSON(obj);
            SXSSFRow dataRow = sheet.createRow(rowIndex);
            for (int i = 0; i < properties.length; i++)
            {
                SXSSFCell newCell = dataRow.createCell(i);

                Object o =  jo.get(properties[i]);
                String cellValue = "";
                if(o==null) cellValue = "";
                else if(o instanceof Date) cellValue = new SimpleDateFormat(datePattern).format(o);
                else if(o instanceof Float || o instanceof Double)
                    cellValue= new BigDecimal(o.toString()).setScale(2,BigDecimal.ROUND_HALF_UP).toString();
                else cellValue = o.toString();

                newCell.setCellValue(cellValue);
                newCell.setCellStyle(cellStyle);
            }
            rowIndex++;
        }
        // 自动调整宽度
        *//**//*for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }*//**//*
        try {
            workbook.write(outXls);
            workbook.close();
            workbook.dispose();
            outXls.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
    public static void main(String[] args) {
        String skuDetail = "12123@1|12124@2";
        String[] sku = skuDetail.split("@");
        System.out.println(sku);

    }
}
