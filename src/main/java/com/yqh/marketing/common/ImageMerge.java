package com.yqh.marketing.common;

import com.yqh.marketing.basedevss.base.conf.GlobalConfig;

import javax.imageio.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * 图片合并 图片压缩
 *
 * @author liuhongjia
 * @date 2016/8/16 18:59
 */
public class ImageMerge {




    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
                || (codePoint == 0xD)
                || ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
                || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
                || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }

    /**
     * 压缩图片
     *
     * @param srcFilePath
     * @param descFilePath
     * @return
     */
    public static boolean compressPic(String srcFilePath, String descFilePath) {
        File file = null;
        BufferedImage src = null;
        FileOutputStream out = null;
        ImageWriter imgWrier;
        ImageWriteParam imgWriteParams;

        // 指定写图片的方式为 jpg
        imgWrier = ImageIO.getImageWritersByFormatName("jpg").next();
        imgWriteParams = new javax.imageio.plugins.jpeg.JPEGImageWriteParam(null);
        // 要使用压缩，必须指定压缩方式为MODE_EXPLICIT
        imgWriteParams.setCompressionMode(imgWriteParams.MODE_EXPLICIT);
        // 这里指定压缩的程度，参数qality是取值0~1范围内，
        imgWriteParams.setCompressionQuality((float) 0.7);
        imgWriteParams.setProgressiveMode(imgWriteParams.MODE_DISABLED);
        ColorModel colorModel = ColorModel.getRGBdefault();
        // 指定压缩时使用的色彩模式
        imgWriteParams.setDestinationType(new ImageTypeSpecifier(colorModel, colorModel
                .createCompatibleSampleModel(16, 16)));

        try {
            file = new File(srcFilePath);
            src = ImageIO.read(file);
            out = new FileOutputStream(descFilePath);

            imgWrier.reset();
            // 必须先指定 out值，才能调用write方法, ImageOutputStream可以通过任何 OutputStream构造
            imgWrier.setOutput(ImageIO.createImageOutputStream(out));
            // 调用write方法，就可以向输入流写图片
            imgWrier.write(null, new IIOImage(src, null, null), imgWriteParams);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 缩小Image，此方法返回源图像按给定宽度、高度限制下缩放后的图像
     *
     * @param inputImage
     * @param maxWidth：压缩后宽度
     * @param maxHeight：压缩后高度
     * @throws IOException return
     */
    public static BufferedImage scaleByPercentage(BufferedImage inputImage, int newWidth, int newHeight)  {
        //获取原始图像透明度类型
        int type = inputImage.getColorModel().getTransparency();
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        //开启抗锯齿
        RenderingHints renderingHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //使用高质量压缩
        renderingHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        BufferedImage img = new BufferedImage(newWidth, newHeight, type);
        Graphics2D graphics2d = img.createGraphics();
        graphics2d.setRenderingHints(renderingHints);
        graphics2d.drawImage(inputImage, 0, 0, newWidth, newHeight, 0, 0, width, height, null);
        graphics2d.dispose();
        return img;
    }


    /**
     * 通过网络获取图片
     *
     * @param url
     * @return
     */
    public static BufferedImage getUrlByBufferedImage(String url) {
        try {
            URL urlObj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
            // 连接超时
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setConnectTimeout(25000);
            // 读取超时 --服务器响应比较慢,增大时间
            conn.setReadTimeout(25000);
            conn.setRequestMethod("GET");
            conn.addRequestProperty("Accept-Language", "zh-cn");
            conn.addRequestProperty("Content-type", "image/jpeg");
            conn.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; .NET CLR 2.0.50727)");
            conn.connect();
            BufferedImage bufImg = ImageIO.read(conn.getInputStream());
            conn.disconnect();
            return bufImg;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 传入的图像必须是正方形的 才会 圆形  如果是长方形的比例则会变成椭圆的
     *
     * @param url 用户头像地址
     * @return
     * @throws IOException
     */
    public static BufferedImage convertCircular(String url) throws IOException {
        BufferedImage bi1 = ImageIO.read(new File(url));
        //这种是黑色底的
        BufferedImage bi2 = new BufferedImage(bi1.getWidth(), bi1.getHeight(), BufferedImage.TYPE_INT_RGB);

        //透明底的图片
        bi2 = new BufferedImage(bi1.getWidth(), bi1.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        Ellipse2D.Double shape = new Ellipse2D.Double(0, 0, bi1.getWidth(), bi1.getHeight());
        Graphics2D g2 = bi2.createGraphics();
        g2.setClip(shape);
        // 使用 setRenderingHint 设置抗锯齿
        g2.drawImage(bi1, 0, 0, null);
        //设置颜色
        g2.setBackground(Color.green);
        g2.dispose();
        return bi2;
    }

    public static void getSmartPic(String imgUrl) {
        try {
            //http://avatar.csdn.net/3/1/7/1_qq_27292113.jpg?1488183229974  是头像地址
            //获取图片的流
            BufferedImage url = getUrlByBufferedImage(imgUrl);
//            Image src = ImageIO.read(new File("C:/Users/Administrator/Desktop/Imag.png"));
//            BufferedImage url = (BufferedImage) src;
            //处理图片将其压缩成正方形的小图
            BufferedImage  convertImage= scaleByPercentage(url, 100,100);
            //裁剪成圆形 （传入的图像必须是正方形的 才会 圆形 如果是长方形的比例则会变成椭圆的）
            convertImage = convertCircular(imgUrl);
            //生成的图片位置
            String imagePath= "C:/Users/Administrator/Desktop/Imag.png";
            ImageIO.write(convertImage, imagePath.substring(imagePath.lastIndexOf(".") + 1), new File(imagePath));
            System.out.println("ok");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 随机选取资源服务器URL
     *
     * @return
     */
    public static String getImageResoure() {
        try {
            String temp = GlobalConfig.get().getString("lechun.resource.url", "http://resource.lechun.cc/");
            String[] arr = temp.split(",");
//        int result=Math.abs(RandomUtils.generateStrId().hashCode())%arr.length;// 返回[0,10)集合中的整数，注意不包括10
            return arr[0];
        } catch (Exception e) {
            return "http://resource.lechun.cc/";
        }
    }

    /**
     * 随机选取资源服务器URL
     *
     * @param path
     * @return
     */
    public static String getImageResoure(String path) {
        try {
            String temp = GlobalConfig.get().getString("lechun.resource.url", "http://resource.lechun.cc/");
            String[] arr = temp.split(",");
            int result = Math.abs(path.hashCode()) % arr.length;// 返回[0,10)集合中的整数，注意不包括10
            String url = arr[result];
            if (url.endsWith("/") && path.startsWith("/")) {
                url = url.substring(0, url.length() - 1) + path;
            } else if (!url.endsWith("/") && !path.startsWith("/")) {
                url = url + "/" + path;
            } else {
                url = url + path;
            }

            return url;
        } catch (Exception e) {
            return "http://resource.lechun.cc/" + path;
        }
    }

    public static String getProductImageServerPath(String url) {
        url = GlobalConfig.get().getString("service.proImgPattern", "").replace("%s", url);
        return ImageMerge.getImageResoure(url);
    }
}
