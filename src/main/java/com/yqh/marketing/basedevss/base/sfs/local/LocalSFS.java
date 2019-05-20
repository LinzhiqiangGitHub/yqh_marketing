package com.yqh.marketing.basedevss.base.sfs.local;


import com.yqh.marketing.basedevss.ServerException;
import com.yqh.marketing.basedevss.base.BaseErrors;
import com.yqh.marketing.basedevss.base.conf.Configuration;
import com.yqh.marketing.basedevss.base.sfs.AbstractSFS;
import com.yqh.marketing.basedevss.base.util.FileUtils2;
import com.yqh.marketing.basedevss.base.util.StringUtils2;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.Validate;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class LocalSFS extends AbstractSFS {
    public String directory;

    public LocalSFS(Configuration conf) {
        this(conf.checkGetString("dir"));
    }

    public LocalSFS(String dir) {
        Validate.notNull(dir);
        dir = FileUtils2.expandPath(dir.trim());
        if (!dir.endsWith("/"))
            dir += "/";

        try {
            FileUtils.forceMkdir(new File(dir));
        } catch (IOException e) {
            throw new ServerException(BaseErrors.PLATFORM_SFS_IO_ERROR, e);
        }
        this.directory = dir;
    }

    private String joinPath(String path) {
        return StringUtils2.joinIgnoreNull(directory, calculateRelativePath(path));
    }

    protected String calculateRelativePath(String file) {
        return file;
    }

    @Override
    public OutputStream create(String file) {
        Validate.notNull(file);
        checkFile(file);

        String path = joinPath(file);
        File f = new File(path);
        if (f.exists())  {
            f.delete();
        }
        try {
            String dir = FilenameUtils.getFullPathNoEndSeparator(path);
            FileUtils.forceMkdir(new File(dir));
            return new FileOutputStream(f, false);
        } catch (IOException e) {
            throw new ServerException(BaseErrors.PLATFORM_SFS_IO_ERROR, e);
        }
    }

    @Override
    public InputStream read(String file) {
        Validate.notNull(file);

        String path = joinPath(file);
        File f = new File(path);
        if (!f.exists() || !f.isFile())
            throw new ServerException(BaseErrors.PLATFORM_SFS_IO_ERROR, "Open file error (%s)", file);

        try {
            return new FileInputStream(f);
        } catch (IOException e) {
            throw new ServerException(BaseErrors.PLATFORM_SFS_IO_ERROR, e);
        }
    }

    @Override
    public InputStream read(String file, HttpServletResponse resp) {
        return read(file);
    }

    @Override
    public boolean delete(String file) {
        Validate.notNull(file);
        return new File(joinPath(file)).delete();
    }

    @Override
    public boolean exists(String file) {
        Validate.notNull(file);

        String path = joinPath(file);
        File f = new File(path);
        return f.exists() && f.isFile();
    }

    @Override
    public void init() {

    }

    @Override
    public void destroy() {

    }
}
