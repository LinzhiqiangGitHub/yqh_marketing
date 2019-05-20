package com.yqh.marketing.service.accessory;

import com.yqh.marketing.basedevss.base.data.RecordSet;
import com.yqh.marketing.basedevss.base.log.Logger;
import com.yqh.marketing.basedevss.base.web.QueryParams;
import com.yqh.marketing.basedevss.base.web.webmethod.WebMethod;
import com.yqh.marketing.basedevss.base.web.webmethod.WebMethodServlet;
import com.yqh.marketing.common.GlobalLogics;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

public class AccessoryServlet extends WebMethodServlet {

    private static final Logger L = Logger.getLogger(AccessoryServlet.class);

    ExecutorService pool;

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @WebMethod("/accessory/get_all_accessory")
    public RecordSet get_all_category(HttpServletRequest req, QueryParams qp) {
        RecordSet b = GlobalLogics.getAccessoryLogic().getAllAccessory();
        return b;
    }

    @WebMethod("accessory/get_insert_accessory")
    public void insert_accessory(HttpServletRequest request, QueryParams qp) {

        /*String jop_id = qp.checkGetString("JOB_ID");
        String advertiser_id = qp.getString("ADVERTISER_ID");
        String logo = qp.getString("LOGO");
        String job_name = qp.getString("JOB_NAME");
        int jobperiod_start = (int) qp.getInt("JOBPERIOD_START", 0);

        String jobperiod_end = qp.getString("JOBPERIOD_END");
        String jobperiod_count = qp.getString("JOBPERIOD_COUNT");
        String job_titly = qp.getString("JOB_TITLY");
        String job_terrace = qp.getString("JOB_TERRACE");
        String job_commission = qp.getString("JOB_COMMISSION");
        String create_time = qp.getString("CREATE_TIME");
        String update_time = qp.getString("UPDATE_TIME");*/

        String jop_id = UUID.randomUUID().toString();
        String advertiser_id = UUID.randomUUID().toString();
        String logo = "url://" + UUID.randomUUID().toString();
        String jop_name = "LIN";
        Date jobperiod_start = new Date();
        Date jobperiod_end = new Date();
        long jobperiod_count = 111;
        String job_titly = "Hello";
        int job_terrace = 0;
        Random random = new Random();
        double v = random.nextDouble();
        double job_commission = v;
        Date create_time = new Date();
        Date update_time = new Date();

        /*GlobalLogics.getAccessoryLogic().Save_Accessory(
                jop_id,
                advertiser_id,
                logo,
                jop_name,
                jobperiod_start,
                jobperiod_end,
                jobperiod_count,
                job_titly,
                job_terrace,
                job_commission,
                create_time,
                update_time);
    }*/
    }
    @WebMethod("accessory/get_insert_accessory_test")
    public boolean insert_accessory_test(HttpServletRequest request, QueryParams qp) {

        long accessory_id = 1111;
        String advertiser_id = "123";
        String user_id = "123";
        String jop_id = "123";
        String complete_id = "123" ;

        int accessory_Type = 1;
        String accessory_Url = "url://" + UUID.randomUUID().toString();
        int accessory_status = 2;

        Date create_time = new Date();
        Date update_time = new Date();

        /*tYqhEnterpriseAccessory.setAccessoryId(accessory_id);
        tYqhEnterpriseAccessory.setUserId(user_id);
        tYqhEnterpriseAccessory.setAdvertiserId(advertiser_id);
        tYqhEnterpriseAccessory.setJobId(jop_id);
        tYqhEnterpriseAccessory.setCompleteId(complete_id);
        tYqhEnterpriseAccessory.setAccessoryType(accessory_Type);
        tYqhEnterpriseAccessory.setAccessoryUrl(accessory_Url);
        tYqhEnterpriseAccessory.setAccessoryStatus(accessory_status);
        tYqhEnterpriseAccessory.setCreateTime(create_time);
        tYqhEnterpriseAccessory.setUpdateTime(update_time);

        boolean accessory = GlobalLogics.getAccessoryLogic().save_Accessory(tYqhEnterpriseAccessory);

        return accessory;*/
        return false;
    }
    @WebMethod("accessory/get_insert_accessory_test1")
    public boolean insert_accessory_test1(HttpServletRequest request, QueryParams qp) {

        long accessory_id = 1111;
        String user_id ="1111";
        String advertiser_id ="22222";
        String job_id = "33333";
        String complete_id = "44444" ;

        int accessory_Type = 1;
        String accessory_Url = "url://" + UUID.randomUUID().toString();
        int accessory_status = 2;

        Date create_time = new Date();
        Date update_time = new Date();

        boolean accessory = GlobalLogics.getAccessoryLogic().save_Accessory1(accessory_id,user_id,advertiser_id,job_id,complete_id,accessory_Type,accessory_Url,accessory_status,create_time,update_time);

        return accessory;
    }


}
