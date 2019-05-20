package com.yqh.marketing.service.job;

import com.yqh.marketing.basedevss.base.data.Record;
import com.yqh.marketing.basedevss.base.data.RecordSet;

import java.util.Date;

public interface JobLogic {
    //发布任务
    boolean create_job(String job_id, String advertiser_id, String logo, String job_name, Date jobperiod_start, Date jobperiod_end, long jobperiod_count, String job_titly, int job_terrace, float  job_commission, Date create_time);

    //发布任务状态
    boolean create_job_apply(String jobapplyfor_id, String job_id, String advertiser_id, int apply_for_status, Date create_time);

    //回显用户所有任务
    RecordSet get_all_jobByUserId(String user_id);

    //回显任务所有信息
    Record jopById(String job_id);

    //更新任务
    boolean update_job(String job_id, String logo, String job_name, Date jobperiod_start, Date jobperiod_end, long jobperiod_count, String job_titly, int job_terrace, float job_commission, Date update_time);

    //更新任务状态
    boolean update_job_apply(String job_id, int apply_for_status, Date update_time);

    //展示所有任务
    RecordSet get_all_job(int approved);

    //审核发布任务状态
    boolean updateByjobId(String job_id, int job_status,Date update_time);
    //任务新建图片
    boolean create_Image(String img_id, String job_id, String logo, String img_type, Date create_time);
    //任务更新图片
    boolean update_Image(String job_id, String logo, String img_type, Date update_time);
    //接取任务
    boolean saveCommit(String complete_id, String user_id, String number_id, String job_id, int job_complete_status, Date git_time);

    //任务平台
    int JobByjobId(String job_id);

    //账户平台
    RecordSet NumberByUserid(String user_id);


}
