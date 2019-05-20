package com.yqh.marketing.basedevss.base.poi;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Created by Sandy on 2014/7/9.
 */
public class HssfStyle {
    //设置单元格样式
    public HSSFCellStyle leftStyle(HSSFWorkbook wb){
        HSSFCellStyle curStyle = wb.createCellStyle();
        HSSFFont curFont = wb.createFont();								//设置字体
        //curFont.setFontName("Times New Roman");						//设置英文字体
        curFont.setFontName("宋体");								//设置英文字体
        curFont.setCharSet(HSSFFont.DEFAULT_CHARSET);					//设置中文字体，那必须还要再对单元格进行编码设置

        curFont.setFontHeightInPoints((short)10);						//字体大小
        curStyle.setFont(curFont);


        curStyle.setBorderTop(HSSFCellStyle.BORDER_THICK);				//粗实线
        curStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);			//实线
        curStyle.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);			//比较粗实线
        curStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);				//实线

        curStyle.setWrapText(true);  									//换行
        curStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);				//横向具右对齐
        curStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);	//单元格垂直居中

        return curStyle;
    }
}
