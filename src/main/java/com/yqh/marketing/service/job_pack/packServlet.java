package com.yqh.marketing.service.job_pack;

import com.yqh.marketing.basedevss.base.data.RecordSet;
import com.yqh.marketing.basedevss.base.log.Logger;
import com.yqh.marketing.basedevss.base.web.QueryParams;
import com.yqh.marketing.basedevss.base.web.webmethod.WebMethod;
import com.yqh.marketing.basedevss.base.web.webmethod.WebMethodServlet;
import com.yqh.marketing.common.GlobalLogics;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class packServlet extends WebMethodServlet {

    private static final Logger LOGGER = Logger.getLogger(packServlet.class);

    @Override
    public void init() throws ServletException {
        super.init();
    }

    //PACK_ID,EXPERT_ID,JOB_ID,CHECKED,CREATED_TIME,UPDATED_TIME
    @WebMethod("jop_pack/insert_all_pack")
    public boolean insert_pack(HttpServletRequest request, QueryParams qp) {

        String pack_id = "1232141";
        String expert_id = "12412412";
        String job_id = "121243";
        byte checked = 1;

        Date created_time = new Date();
        Date update_time = new Date();

        boolean b = GlobalLogics.getPackLogic().Save_job_pack(pack_id,expert_id,job_id,checked,created_time,update_time);

        return b;

    }

    @WebMethod("jop_pack/get_all_pack")
    public RecordSet getallpack(HttpServletRequest req, QueryParams qp) {
        RecordSet b = GlobalLogics.getPackLogic().getallpack();
        return b;
    }
}
