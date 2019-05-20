package com.yqh.marketing.common;

import com.yqh.marketing.basedevss.base.data.Record;
import com.yqh.marketing.common.cache.SpyMemcachedUtil;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;


/**
 * 验证码生成器
 */
public class ImageValidateCode {

    // 图片的宽度。
    private int width = 160;
    // 图片的高度。
    private int height = 40;
    // 验证码字符个数
    private int codeCount = 5;
    // 验证码干扰线数
    private int lineCount = 150;
    // 验证码
    private static String code = null;
    // 验证码图片Buffer
    private BufferedImage buffImg=null;


    private char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J','K', 'L',
            'M', 'N',  'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W','X', 'Y',
            'Z',  '1', '2', '3', '4', '5', '6', '7', '8', '9' };


    public  ImageValidateCode() {
        this.createCode();
    }


    /**
     *
     * @param width 图片宽
     * @param height 图片高
     */
    public  ImageValidateCode(int width,int height) {
        this.width=width;
        this.height=height;
        this.createCode();
    }

    /**
     *
     * @param width 图片宽
     * @param height 图片高
     * @param codeCount 字符个数
     * @param lineCount 干扰线条数
     */
    public  ImageValidateCode(int width,int height,int codeCount,int lineCount) {
        this.width=width;
        this.height=height;
        this.codeCount=codeCount;
        this.lineCount=lineCount;
        this.createCode();
    }

    public void createCode() {
        int x = 0,fontHeight=0,codeY=0;
        int red = 0, green = 0, blue = 0;

        x = width / (codeCount +2);//每个字符的宽度
        fontHeight = height - 2;//字体的高度
        codeY = height - 4;

// 图像buffer
        buffImg = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
        Graphics2D g = buffImg.createGraphics();
// 生成随机数
        Random random = new Random();
// 将图像填充为白色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
// 创建字体
        ImgFontByte imgFont=new ImgFontByte();
        Font font =imgFont.getFont(fontHeight);
        g.setFont(font);

        for (int i = 0; i < lineCount; i++) {
            int xs = random.nextInt(width);
            int ys = random.nextInt(height);
            int xe = xs+random.nextInt(width/8);
            int ye = ys+random.nextInt(height/8);
            red = random.nextInt(255);
            green = random.nextInt(255);
            blue = random.nextInt(255);
            g.setColor(new Color(red, green, blue));
            g.drawLine(xs, ys, xe, ye);
        }

// randomCode记录随机产生的验证码
        StringBuffer randomCode = new StringBuffer();
// 随机产生codeCount个字符的验证码。
        for (int i = 0; i < codeCount; i++) {
            String strRand = String.valueOf(codeSequence[random.nextInt(codeSequence.length)]);
// 产生随机的颜色值，让输出的每个字符的颜色值都将不同。
            red = random.nextInt(255);
            green = random.nextInt(255);
            blue = random.nextInt(255);
            g.setColor(new Color(red, green, blue));
            g.drawString(strRand, (i + 1) * x, codeY);
// 将产生的四个随机数组合在一起。
            randomCode.append(strRand);
        }
// 将四位数字的验证码保存到Session中。
        code = randomCode.toString();
    }

    public void write(String path) throws IOException {
        OutputStream sos = new FileOutputStream(path);
        this.write(sos);
    }

    public void write(OutputStream sos) throws IOException {
        ImageIO.write(buffImg, "png", sos);
        sos.close();
    }

    public BufferedImage getBuffImg() {
        return buffImg;
    }

    public static String getCode() {
        return code;
    }

    /**
     * 验证
     * @param req HttpServletRequest
     * @param valiteType 验证类型
     * @param noValiteCount 免验证次数
     * @param periodSecond 时效
     * @param inputCode 验证码
     * @return record :status 1表示验证通过，0表示不通过，nextValidate表示下次是否需要验证，1是，0否
     */
    public static Record validate(HttpServletRequest req, ValiteType valiteType, int noValiteCount, int periodSecond, String inputCode){
        Record rtn=Record.of("status",0,"nextValidate",1);
        if(req==null){
            rtn.put("nextValidate",0);
            return rtn;
        }
        String sessionId=req.getSession().getId();
        String key=sessionId+"_counter_"+valiteType.name();
        long l= SpyMemcachedUtil.getInstance().incr(key,1,1,periodSecond);
        if(l>noValiteCount){
            if (inputCode==null||inputCode.trim().isEmpty()) {
                rtn.put("nextValidate",1);
                return rtn;
            }
            String code=SpyMemcachedUtil.getInstance().get(sessionId+"_validateCode");
            if(code==null){
                rtn.put("nextValidate",1);
                return rtn;
            }
            if(inputCode.toLowerCase().equals(code.toLowerCase())){
                rtn.put("nextValidate",1);
                rtn.put("status",1);
                return  rtn;
            }
        }else{
            if(l+1>=noValiteCount){
                rtn.put("nextValidate", 1);
            }else {
                rtn.put("nextValidate", 0);
            }
            rtn.put("status",1);
            SpyMemcachedUtil.getInstance().remove(key);
            return  rtn;
        }
        return rtn;
    }
    public enum ValiteType{
        login,bindMobile,other
    }
    /**
     * 验证
     * @param req HttpServletRequest
     * @param valiteType 验证类型
     * @param noValiteCount 免验证次数
     * @return
     */
    public static boolean isOpenValidate(HttpServletRequest req, ValiteType valiteType, int noValiteCount) {
        if(req==null){
            return false;
        }
        String sessionId=req.getSession().getId();
        String l= SpyMemcachedUtil.getInstance().get(sessionId+"_counter_"+valiteType.name());
        if(l==null||l.isEmpty()){
            return false;
        }
        if(Integer.parseInt(l)>=noValiteCount){
            return true;
        }
        return false;
    }

}