package com.yqh.marketing.basedevss.base.util;


import com.yqh.marketing.basedevss.ServerException;
import com.yqh.marketing.basedevss.base.BaseErrors;

public class ErrorUtils {
    public static ServerException wrapResponseError(Throwable t) {
        if (t instanceof IllegalArgumentException) {
            IllegalArgumentException e = (IllegalArgumentException)t;
            return new ServerException(BaseErrors.PLATFORM_ILLEGAL_PARAM, e.getMessage());
        } else if (t instanceof ServerException)  {
            ServerException e = (ServerException)t;
            return new ServerException(e.code, e.getMessage());
        } else {
            return new ServerException(BaseErrors.PLATFORM_UNKNOWN_ERROR, t.getMessage());
        }
    }
}
