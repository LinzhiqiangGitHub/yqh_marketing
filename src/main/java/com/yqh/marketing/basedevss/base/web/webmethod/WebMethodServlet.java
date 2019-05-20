package com.yqh.marketing.basedevss.base.web.webmethod;


import com.yqh.marketing.basedevss.ServerException;
import com.yqh.marketing.basedevss.base.BaseErrors;
import com.yqh.marketing.basedevss.base.ResponseError;
import com.yqh.marketing.basedevss.base.conf.Configuration;
import com.yqh.marketing.basedevss.base.conf.GlobalConfig;
import com.yqh.marketing.basedevss.base.data.Null;
import com.yqh.marketing.basedevss.base.data.Privacy;
import com.yqh.marketing.basedevss.base.io.Charsets;
import com.yqh.marketing.basedevss.base.util.ErrorUtils;
import com.yqh.marketing.basedevss.base.util.StringUtils2;
import com.yqh.marketing.basedevss.base.util.json.JsonGenerateHandler;
import com.yqh.marketing.basedevss.base.util.json.JsonUtils;
import com.yqh.marketing.basedevss.base.web.Cookies;
import com.yqh.marketing.basedevss.base.web.OutputServlet;
import com.yqh.marketing.basedevss.base.web.QueryParams;
import org.apache.avro.AvroRuntimeException;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class WebMethodServlet extends OutputServlet {
    private static final Logger L = LoggerFactory.getLogger(WebMethodServlet.class);
    public static String loginCookieName= GlobalConfig.get().getString("cookie.name","artistcookie");
    private final Map<String, Invoker> invokers = new HashMap<String, Invoker>();
    private volatile Configuration configuration;
    private boolean printErrorDetails = false;

    public WebMethodServlet() {
        initMethods();
    }

    private void initMethods() {
        Class clazz = getClass();
        for (Method method : clazz.getMethods()) {
            if (!Modifier.isPublic(method.getModifiers()))
                continue;

            WebMethod ann = method.getAnnotation(WebMethod.class);
            if (ann == null)
                continue;

            String url = ann.value();
            if (invokers.containsKey(url))
                throw new ServerException(BaseErrors.PLATFORM_WEBMETHOD_INIT_ERROR, "Repetitive url '%s'", url);

            for (Class pt : method.getParameterTypes()) {
                if (!HttpServletRequest.class.isAssignableFrom(pt) &&
                        !HttpServletResponse.class.isAssignableFrom(pt) &&
                        !QueryParams.class.isAssignableFrom(QueryParams.class))
                    throw new ServerException(BaseErrors.PLATFORM_WEBMETHOD_INIT_ERROR, "Invalid parameter type '%s'", pt.getName());
            }

            invokers.put(url, new Invoker(this, method));
        }
    }


    public Configuration getConfiguration() {
//        if (configuration == null) {
//            Configuration conf = new Configuration();
//            Enumeration e = getInitParameterNames();
//            while (e.hasMoreElements()) {
//                String key = e.nextElement().toString();
//                String val = getInitParameter(key);
//                conf.put(key, val);
//            }
//            configuration = conf;
//        }
//        return configuration;

        return GlobalConfig.get();
    }

    @Override
    public void init() throws ServletException {
        super.init();
        Configuration conf = getConfiguration();
        printErrorDetails = conf.getBoolean("webMethod.printErrorDetails", true);  // TODO: default should false
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    protected String getXmlDocument() {
        return null;
    }

    protected String getXmlDocumentPath() {
        return null;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doProcess(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doProcess(req, resp);
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type,Authorization,Accept,X-Requested-With,User-Agent,location");
        resp.setHeader("Access-Control-Allow-Credentials", "false");
        resp.setHeader("Access-Control-Max-Age", "60");
    }

    protected boolean before(HttpServletRequest req, HttpServletResponse resp) {
//        L.trace("begin before");
    	resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setCharacterEncoding(Charsets.DEFAULT);
        String ua = req.getHeader("User-Agent");
        req.setAttribute("beginStamp",String.valueOf((new Date()).getTime()));
        String p=req.getRequestURI();
//        L.trace("----" + StringUtils.trimToEmpty(ua));
        return !StringUtils.isNotBlank(ua) || !StringUtils.containsIgnoreCase(ua, "Sogou web spider");
    }

    protected void after(HttpServletRequest req, HttpServletResponse resp) {
        //L.debug("after");
        try {
            String url = req.getRequestURI();
            if(!url.contains("/weixinapi/index")) {
                String params=JsonUtils.toJson(req.getParameterMap(),false);
                String beginTime = req.getAttribute("beginStamp").toString();
                long diffMillisSecond = new Date().getTime() - Long.parseLong(beginTime);
                String user_salt_val;
                user_salt_val = req.getParameter("ticket");
                if (user_salt_val == null || user_salt_val.isEmpty()) {
                    Cookie cookie = Cookies.getCookie(req, loginCookieName);
                    if (cookie != null) {
                        user_salt_val = cookie.getValue();
                    }
                }
                //这里写日志的

            }
        }catch (Exception e){

        }

    }

    protected void process(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        L.trace("begin process");
    	String url = StringUtils.removeStart(StringUtils2.joinIgnoreNull(req.getServletPath(), req.getPathInfo()), "/");
        if (processDocument(url, req, resp))
            return;

        Invoker invoker = invokers.get(url);
        if (invoker != null) {
            invoker.invoke(req, resp);
        } else {
            resp.setStatus(404); // Not Found
        }
//        L.trace("end process");
    }

    protected Set<String> getInvokerApi(){
        return invokers.keySet();
    }

    private boolean processDocument(String url, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String doc = getXmlDocument();
        if (doc != null && StringUtils.equals(getXmlDocumentPath(), url)) {
            output(null, req, resp, doc, 200, "text/xml");
            return true;
        }
        return false;
    }

    private void doProcess(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String qs = "";
        if ("GET".equalsIgnoreCase(req.getMethod())) {
            qs = ObjectUtils.toString(req.getQueryString());
        }
//        if (L.isTraceEnabled()) {
//            if (qs.isEmpty())
//                L.trace("{} {}", req.getMethod(), req.getRequestURI());
//            else
//                L.trace("{} {}?{}", new Object[]{req.getMethod(), req.getRequestURI(), qs});
//        }

        try {
            boolean b = before(req, resp);
            if (b) {
                process(req, resp);
            }
        } finally {
            after(req, resp);
        }
    }


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String reqMethod = req.getMethod();
        if ("GET".equals(reqMethod) || "POST".equals(reqMethod) || "OPTIONS".equals(reqMethod)) {
            super.service(req, resp);
        } else {
            resp.setStatus(405); // Method Not Allowed
        }
    }

    private class Invoker {
        final Object obj;
        final Method method;

        private Invoker(Object obj, Method method) {
            this.obj = obj;
            this.method = method;
        }


        public void invoke(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//            L.trace("begin invoke");
            QueryParams qp = null;
//        	L.trace("end query params create");
            try {
                Class[] pts = method.getParameterTypes();
                Object[] ps = new Object[pts.length];
                for (int i = 0; i < pts.length; i++) {
                    Class pt = pts[i];
                    if (QueryParams.class.isAssignableFrom(pt)) {
                        ps[i] =  (qp = QueryParams.create(req));
                    } else if (HttpServletRequest.class.isAssignableFrom(pt)) {
                        ps[i] = req;
                    } else if (HttpServletResponse.class.isAssignableFrom(pt)) {
                        ps[i] = resp;
                    }
                }

                Throwable err = null;
                Object r = null;
                try {
                    method.setAccessible(true);
//                    L.trace("begin invoke method");
                	r = method.invoke(obj, ps);


//                    L.trace("end invoke method");
                } catch (InvocationTargetException e) {
                    err = e.getTargetException();
                } catch (Throwable t) {
                    err = t;
                }


                if(!invokerAfter(r,err, req,resp))
                    return;

                if (err != null) {
                    writeError(err, qp, req, resp, printErrorDetails);
                } else {
                    if (r instanceof NoResponse) {
                        // do nothing
                    } else if (r instanceof DirectResponse) {
                        DirectResponse dr = (DirectResponse) r;
                        output(qp, req, resp, dr.content, 200, dr.contentType);
                    } else {
                        String json = JsonUtils.toJson(wrapSingleton(r), true);
//                        output(resp, json, 200, "application/json");
                        output(qp, req, resp, json, 200, "text/plain");
                    }
                }
            } finally {
                if (qp != null)
                    qp.close();
            }
//            L.trace("end invoke");
        }
    }

    protected boolean invokerAfter(Object result,Throwable t,HttpServletRequest req,HttpServletResponse res){
        return true;
    }

    private static Object wrapSingleton(Object o) {
        JsonNodeFactory jnf = JsonNodeFactory.instance;
        Object r;
        if (o == null || o instanceof Null) {
            r = makeResultObjectNode(jnf, jnf.nullNode());
        } else if (o instanceof Privacy || o instanceof CharSequence || o instanceof Character) {
            r = makeResultObjectNode(jnf, jnf.textNode(o.toString()));
        } else if (o instanceof Boolean) {
            r = makeResultObjectNode(jnf, jnf.booleanNode((Boolean) o));
        } else if (o instanceof Byte || o instanceof Short || o instanceof Integer || o instanceof Long) {
            r = makeResultObjectNode(jnf, jnf.numberNode(((Number) o).longValue()));
        } else if (o instanceof Float || o instanceof Double) {
            r = makeResultObjectNode(jnf, jnf.numberNode(((Number) o).doubleValue()));
        } else {
            r = o;
        }
        return r;
    }

    private static JsonNode makeResultObjectNode(JsonNodeFactory jnf, JsonNode jn) {
        ObjectNode r = jnf.objectNode();
        r.put("result", jn);
        return r;
    }


    private static void writeError(final Throwable err, QueryParams qp, HttpServletRequest req, HttpServletResponse resp, boolean printErrorDetails) throws IOException {
        if(err != null)
            err.printStackTrace();

        if (err instanceof ResponseError || err instanceof ServerException || err instanceof AvroRuntimeException) {
            final ServerException respErr = ErrorUtils.wrapResponseError(err);
            String json = JsonUtils.toJson(new JsonGenerateHandler() {
                @Override
                public void generate(JsonGenerator jg) throws IOException {
                    jg.writeStartObject();
                    jg.writeNumberField("error_code", respErr.code);
                    jg.writeStringField("error_msg", ObjectUtils.toString(respErr.getMessage(), ""));
                    jg.writeEndObject();
                }
            }, true);
//            output(resp, json, 200, "application/json");
            output(qp, req, resp, json, 200, "text/plain");
        } else {
            if (printErrorDetails) {
                StringWriter out = new StringWriter();
                out.append("Call stack:\n");
                err.printStackTrace(new PrintWriter(out));
                String errorStack = out.toString();
                output(qp, req, resp, errorStack, 500, "text/plain");
            } else {
                String json = JsonUtils.toJson(new JsonGenerateHandler() {
                    @Override
                    public void generate(JsonGenerator jg) throws IOException {
                        jg.writeStartObject();
                        jg.writeNumberField("error_code", BaseErrors.PLATFORM_UNKNOWN_ERROR);
                        jg.writeStringField("error_msg", ObjectUtils.toString(err.getMessage(), ""));
                        jg.writeEndObject();
                    }
                }, true);
//                output(resp, json, 500, "application/json");
                output(qp, req, resp, json, 500, "text/plain");
            }
        }


    }
}
