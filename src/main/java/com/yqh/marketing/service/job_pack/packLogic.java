package com.yqh.marketing.service.job_pack;

import com.yqh.marketing.basedevss.base.data.RecordSet;

import java.util.Date;

public interface packLogic {

    boolean Save_job_pack(String PACK_ID, String EXPERT_ID, String JOB_ID, byte CHECKED, Date CREATED_TIME, Date UPDATED_TIME);

    RecordSet getallpack();
}
