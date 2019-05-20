package com.yqh.marketing.basedevss.base.sfs;

import com.yqh.marketing.basedevss.ServerException;
import com.yqh.marketing.basedevss.base.BaseErrors;
import com.yqh.marketing.basedevss.base.data.Record;
import com.yqh.marketing.basedevss.base.image.ImageMagickHelper;
import com.yqh.marketing.basedevss.base.io.Charsets;
import com.yqh.marketing.basedevss.base.util.DateUtils;
import com.yqh.marketing.basedevss.base.util.SystemHelper;
import com.yqh.marketing.basedevss.base.util.image.ImageUtils;
import com.yqh.marketing.basedevss.base.web.WebUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import javax.servlet.http.HttpServletResponse;
import java.io.*;


public class SFSUtils {
    public static final String TEMP_PHOTO_IMAGE_DIR = SystemHelper.getPathInTempDir("temp_photo_image");

    public static void saveUpload(FileItem fileItem, StaticFileStorage sfs, String file) {
        Validate.notNull(sfs);
        Validate.notNull(fileItem);

        OutputStream out = null;
        try {
            out = new BufferedOutputStream(sfs.create(file), 1024 * 50);
            IOUtils.copy(fileItem.getInputStream(), out);
            out.flush();
        } catch (IOException e) {
            throw new ServerException(BaseErrors.PLATFORM_SFS_IO_ERROR, e);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    public static void saveUploadTempFile(InputStream in, StaticFileStorage sfs, String file) {
        OutputStream out = null;
        try {
            out = new BufferedOutputStream(sfs.create(file), 1024 * 50);
            IOUtils.copy(in, out);
            out.flush();
        } catch (IOException e) {
            throw new ServerException(BaseErrors.PLATFORM_SFS_IO_ERROR, e);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    public static void saveUploadTempPic(InputStream in, StaticFileStorage sfs, String file) {
        OutputStream out = null;
        try {
            out = new BufferedOutputStream(sfs.create(file), 1024 * 50);
            IOUtils.copy(in, out);
            out.flush();
        } catch (IOException e) {
            throw new ServerException(BaseErrors.PLATFORM_SFS_IO_ERROR, e);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    public static void writeResponse(HttpServletResponse resp, StaticFileStorage sfs, String file, String contentType) {
        Validate.notNull(resp);
        Validate.notNull(sfs);
        InputStream in = null;
        try {
            if (StringUtils.isBlank(contentType)) {
                contentType = WebUtils.getMimeTypeByFileName(file);
            }
            resp.setContentType(contentType);
            resp.addHeader("Content-Disposition", "attachment; filename=\"" + file + "\"");

            in = sfs.read(file, resp);
            int length = IOUtils.copy(in, resp.getOutputStream());
            resp.setContentLength(length);
        } catch (IOException e) {
            throw new ServerException(BaseErrors.PLATFORM_SFS_IO_ERROR, e);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    public static void writeResponse(HttpServletResponse resp, StaticFileStorage sfs, String file) {
        writeResponse(resp, sfs, file, null);
    }

    public static void saveScaledUploadImage(File fileItem, StaticFileStorage sfs, String file, String w, String h, String format) {
        Validate.notNull(sfs);
        Validate.notNull(fileItem);
        InputStream input = null;
        OutputStream out = null;

        try {
            input = new FileInputStream(fileItem);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            out = new BufferedOutputStream(sfs.create(file), 1024 * 50);
            ImageUtils.scale(input, out, w, h, format);
            input.close();
        } catch (IOException e) {
            throw new ServerException(BaseErrors.PLATFORM_SFS_IO_ERROR, e);
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                    IOUtils.closeQuietly(out);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    public static void saveScaledUploadImage(FileItem fileItem, StaticFileStorage sfs, String file, String w, String h, String format) {
        Validate.notNull(sfs);
        Validate.notNull(fileItem);
        OutputStream out = null;
        try {
            out = new BufferedOutputStream(sfs.create(file), 1024 * 50);
            ImageUtils.scale(fileItem.getInputStream(), out, w, h, format);
        } catch (IOException e) {
            throw new ServerException(BaseErrors.PLATFORM_SFS_IO_ERROR, e);
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                    IOUtils.closeQuietly(out);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveScaledUploadImageNewCut(FileItem fileItem, StaticFileStorage sfs, String file,int beginX,int beginY, String w, String h, String format) {
        Validate.notNull(sfs);
        Validate.notNull(fileItem);
        OutputStream out = null;
        try {
            out = new BufferedOutputStream(sfs.create(file), 1024 * 50);
            ImageUtils.scaleNewCut(fileItem.getInputStream(), out,beginX,beginY, w, h, format);
        } catch (IOException e) {
            throw new ServerException(BaseErrors.PLATFORM_SFS_IO_ERROR, e);
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                    IOUtils.closeQuietly(out);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String revertPhoto(FileItem fileItem, String expName, Record record) throws IOException {

        long date = DateUtils.nowMillis();
                String tmp = TEMP_PHOTO_IMAGE_DIR + File.separator ;
                String tmpFile = tmp +date + "."+expName;
                File f0 = new File(tmp);
                if(!f0.exists()){
                    f0.mkdirs();
                }
                File f = new File(tmpFile);
                try {
                    fileItem.write(f);

                } catch (Exception e) {
                    e.printStackTrace();
                }


                int orientation = getOrientation(record.getString("orientation"));
                String file = tmpFile;
                switch (orientation) {
                    case 90: {
                        String _90 = date+"_90."+expName;
                        String file_90 = tmp+_90;
                        ImageMagickHelper.rotate(tmpFile, _90, "90");
                        file = file_90;
                        break;
                    }
                    case 180: {
                        String _180 = date+"_180."+expName;
                        String file_180 = tmp+_180 ;
                        ImageMagickHelper.rotate(tmpFile, _180, "180");
                        file = file_180;
                        break;
                    }
                    case 270: {
                        String _270 = date + "_270."+expName;
                        String file_270 = tmp+_270;
                        ImageMagickHelper.rotate(tmpFile, _270, "270");
                        file = file_270;
                        break;
                    }
                    default: {
                        String _default = date +"_0."+expName;
                        String file_0 = tmp + _default;
                        ImageMagickHelper.rotate(tmpFile, _default, "0");
                        file = file_0;
                        break;
                    }

                }

        f.delete();

        return file;
    }



    private static int getOrientation(String orientation) {
        if ("Bottom, right side (Rotate 180)".equals(orientation))
            return 180;
        else if ("Right side, top (Rotate 90 CW)".equals(orientation))
            return 90;
        else if ("Left side, bottom (Rotate 270 CW)".equals(orientation))
            return 270;
        else
            return 0;
    }

    public static void saveScaledImage(InputStream input, StaticFileStorage sfs, String file, String w, String h, String format) {
        if (w.equals("0") || h.equals("0")) {
            w = "";
            h = "";
        }

        Validate.notNull(sfs);
        Validate.notNull(input);
        OutputStream out = null;
        try {
            out = new BufferedOutputStream(sfs.create(file), 1024 * 50);
            if (w.length() == 0 || h.length() == 0) {
                //save to local directly
                int len = 0;
                byte[] buf = new byte[8 * 1024];
                while ((len = input.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } else {
                ImageUtils.scale(input, out, w, h, format);
            }
        } catch (IOException e) {
            throw new ServerException(BaseErrors.PLATFORM_SFS_IO_ERROR, e);
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                    IOUtils.closeQuietly(out);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveText(String text, StaticFileStorage sfs, String file) {
        Validate.notNull(text);
        Validate.notNull(sfs);
        Validate.notNull(file);
        OutputStream out = null;
        try {
            out = sfs.create(file);
            IOUtils.write(text, out, Charsets.DEFAULT);
        } catch (IOException e) {
            throw new ServerException(BaseErrors.PLATFORM_SFS_IO_ERROR, e);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    public static void saveBytes(byte[] bytes, StaticFileStorage sfs, String file) {
        Validate.notNull(bytes);
        Validate.notNull(sfs);
        Validate.notNull(file);

        OutputStream out = null;
        try {
            out = sfs.create(file);
            out.write(bytes);
        } catch (IOException e) {
            throw new ServerException(BaseErrors.PLATFORM_SFS_IO_ERROR, e);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

}

