package com.yqh.marketing.service.job;

import com.yqh.marketing.base.LoginUserConstant;
import com.yqh.marketing.basedevss.base.conf.Configuration;
import com.yqh.marketing.basedevss.base.conf.GlobalConfig;
import com.yqh.marketing.basedevss.base.data.Record;
import com.yqh.marketing.basedevss.base.data.RecordSet;
import com.yqh.marketing.basedevss.base.sfs.StaticFileStorage;
import com.yqh.marketing.basedevss.base.util.ClassUtils2;
import com.yqh.marketing.basedevss.base.util.RandomUtils;
import com.yqh.marketing.basedevss.base.web.QueryParams;
import com.yqh.marketing.basedevss.base.web.webmethod.WebMethod;
import com.yqh.marketing.basedevss.base.web.webmethod.WebMethodServlet;
import com.yqh.marketing.common.Constants;
import com.yqh.marketing.common.GlobalLogics;
import org.apache.commons.fileupload.FileItem;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

public class JobTest extends WebMethodServlet {

    private static final Logger logger = Logger.getLogger("JobTest.class");
    private StaticFileStorage fileStorage;
    private String FilePattern;
    private String FileOSSPattern;

    @Override
    public void init() throws ServletException {
        Configuration conf = GlobalConfig.get();
        super.init();
        //fileStorage = (StaticFileStorage) ClassUtils2.newInstance(conf.getString("service.servlet.fileStorage", ""));
        //fileStorage.init();
        FilePattern = conf.getString("service.file.UrlPattern", " http://localhost/marketing/fileImgStorage/%s");
        FileOSSPattern = conf.getString("service.file.oss.UrlPattern", "https://qqyqh-video.oss-cn-beijing.aliyuncs.com/youdou/file/%s");

    }
    //JOB_ID,ADVERTISER_ID,LOGO,JOB_NMAE,JOBPERIOD_START,JOBPERIOD_END,JOBPERIOD_COUNT,JOB_TITLY,JOB_TERRACE,JOB_COMMISSION,CREATE_TIME,UPDATE_TIME

    //发布任务
    @WebMethod("job/save_job")
    public Record save_job(HttpServletRequest req, QueryParams qp) throws ParseException {

        Record out_rec = new Record();
        //任务ID
        String job_id = String.valueOf(RandomUtils.generateId());
        //用户ID
        String advertiser_id = String.valueOf(RandomUtils.generateId());

        //图片地址
        String logo = "wwwwww";

        //任务名称
        String job_name = "快点来吧";
        if (job_name.isEmpty()){
            out_rec.put("status",0);
            out_rec.put("message","任务名称不能为空");
            return out_rec;
        }
        //String 转 DATE
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //任务开始时间
        String start_time = "2008-08-08 12:10:12";
        Date jobperiod_start = sdf.parse(start_time);
        if (start_time.isEmpty()){
            out_rec.put("status",0);
            out_rec.put("message","任务开始日期不能为空");
            return out_rec;
        }
        //任务结束时间
        String end_time = "2019-08-09 12:10:12";
        Date jobperiod_end = sdf.parse(start_time);
        if (end_time.isEmpty()){
            out_rec.put("status",0);
            out_rec.put("message","任务结束日期不能为空");
            return out_rec;
        }
        //任务次数
        long jobperiod_count = 200;
        if (jobperiod_count == 0){
            out_rec.put("status",0);
            out_rec.put("message","任务次数不能为空");
            return out_rec;
        }
        //任务内容
        String job_titly = "JOB_TITLY";

        //任务平台
        int job_terrace = 1;

        //任务佣金
        float job_commission = 1075;
        if (job_commission == 0.00){
            out_rec.put("status",0);
            out_rec.put("message","佣金不能为空");
            return out_rec;
        }

        //任务创建时间
        Date create_time = new Date();

        boolean recort = GlobalLogics.getJobLogic().create_job(job_id,advertiser_id,logo,job_name,jobperiod_start,jobperiod_end,
                jobperiod_count,job_titly,job_terrace,job_commission,create_time
        );

        //JOBAPPLYFOR_ID,JOB_ID,ADVERTISER_ID,ADVERTISER_NAME,APPLY_FOR_STATUS,APPLY_FOR_TIME,UPDATE_TIME
        //任务状态ID
        String jobapplyfor_id = RandomUtils.generateStrId();
        //任务状态
        int apply_for_status = JobConstant.AUDIT_STATUS.CHECK_PENDING;

        boolean job_apply = GlobalLogics.getJobLogic().create_job_apply(jobapplyfor_id, job_id, advertiser_id, apply_for_status, create_time);

        //图片ID
        String img_id = String.valueOf(RandomUtils.generateId());
        //图片类型(广告客户)
        String img_type = LoginUserConstant.User_Image.TASK_LOGO;
        boolean image = GlobalLogics.getJobLogic().create_Image(img_id, job_id, logo, img_type, create_time);

        out_rec.put("status",1);
        out_rec.put("message","发布任务");
        out_rec.put("record",recort);
        out_rec.put("record2",job_apply);
        out_rec.put("record3",image);
        return out_rec;
    }

    //用户所有任务回显
    @WebMethod("job/jobByUser_Id")
    public RecordSet jobByUser_Id(HttpServletRequest req, QueryParams qp){
        String User_id = "3266854790198187797";
        RecordSet out_rec = GlobalLogics.getJobLogic().get_all_jobByUserId(User_id);
        return out_rec;
    }

    //指定用户任务回显
    @WebMethod("job/jobById")
    public Record jobById(HttpServletRequest req, QueryParams qp) {
        String job_id = "3266858064633179204";
        Record out_rec = GlobalLogics.getJobLogic().jopById(job_id);
        return out_rec;
    }

    //更新任务
    @WebMethod("job/update_job")
    public Record update_job(HttpServletRequest req, QueryParams qp) throws ParseException {

        Record out_rec = new Record();

        //用户ID
        String job_id = "3267165490203306537";

        //图片地址
        String logo = "qiniiig";

        //任务名称
        String job_name = "JOB_NAME";
        if (job_name.isEmpty()){
            out_rec.put("status",0);
            out_rec.put("message","任务名称不能为空");
            return out_rec;
        }
        //String 转 DATE
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //任务开始时间
        String start_time = "2008-08-08 12:10:12";
        Date jobperiod_start = sdf.parse(start_time);
        if (start_time.isEmpty()){
            out_rec.put("status",0);
            out_rec.put("message","任务开始日期不能为空");
            return out_rec;
        }
        //任务结束时间
        String end_time = "2019-08-08 12:10:12";

        Date jobperiod_end = sdf.parse(start_time);
        if (end_time.isEmpty()){
            out_rec.put("status",0);
            out_rec.put("message","任务结束日期不能为空");
            return out_rec;
        }
        //任务次数
        long jobperiod_count = 200;
        if (jobperiod_count == 0){
            out_rec.put("status",0);
            out_rec.put("message","任务次数不能为空");
            return out_rec;
        }
        //任务内容
        String job_titly = "JOB_TITLY";

        //任务平台
        int job_terrace = 1;

        //任务佣金
        float job_commission = 1075;
        if (job_commission == 0L){
            out_rec.put("status",0);
            out_rec.put("message","佣金不能为空");
            return out_rec;
        }

        //任务更新时间
        Date update_time = new Date();
        boolean recort = GlobalLogics.getJobLogic().update_job(job_id,logo,job_name,jobperiod_start,jobperiod_end,
                jobperiod_count,job_titly,job_terrace,job_commission,update_time
        );
        //任务状态
        int apply_for_status = JobConstant.AUDIT_STATUS.CHECK_PENDING;
        //任务状态更新
        boolean job_apply = GlobalLogics.getJobLogic().update_job_apply(job_id, apply_for_status, update_time);

        //图片类型(广告客户)
        String img_type = LoginUserConstant.User_Image.TASK_LOGO;
        boolean image = GlobalLogics.getJobLogic().update_Image(job_id, logo, img_type, update_time);

        out_rec.put("status",1);
        out_rec.put("message","更新任务");
        out_rec.put("record",recort);
        out_rec.put("record2",job_apply);
        out_rec.put("record3",image);
        return out_rec;
    }

    //用户审核发布状态
    @WebMethod("job/updateByjobId")
    public boolean updateByjobId(HttpServletRequest req, QueryParams qp) throws ParseException {

        logger.info("任务展示");

        String job_id = "3266828576409862762";
        int job_status = JobConstant.AUDIT_STATUS.APPROVED;
        Date update_time = new Date();
        //审核通过后可以
        boolean record_job = GlobalLogics.getJobLogic().updateByjobId(job_id,job_status,update_time);

        return record_job;

    }



    //用户接取任务
    @WebMethod("job/saveCommit")
    public Record saveCommit(HttpServletRequest req, QueryParams qp) throws ParseException {

        logger.info("任务领取");
        Record out_rec = new Record();
        //COMPLETE_ID,JOB_ID,USER_ID,RECEIVER_NAME,JOB_COMPLETE_STATUS,GIT_TIME,UPDATE_TIME
        String complete_id = RandomUtils.generateStrId();

        String number_id = "";

        String job_id = "3266854790197568370";

        String user_id = "3266032270399972252";
        //获取任务平台
        int job_terrace = GlobalLogics.getJobLogic().JobByjobId(job_id);
        //获取账户平台
        RecordSet plateform = GlobalLogics.getJobLogic().NumberByUserid(user_id);
        for (Record record : plateform) {
            if (record.getInt("PLATEFORM_TYPE") != job_terrace){
                out_rec.put("status",0);
                out_rec.put("message","领取失败，无账户符合要求。");
                out_rec.put("record",record.getInt("PLATEFORM_TYPE"));
                out_rec.put("record2",job_terrace);
                return out_rec;
            }
            number_id = record.getString("NUMBER_ID");
            //当前时间
            Date git_time = new Date();
            //状态
            int job_complete_status = JobConstant.COMMIT_JOB_AUDIT_STATUS.SUBMIT_JOB_CHECK_PENDING;
            //审核通过后可以
            GlobalLogics.getJobLogic().saveCommit(complete_id, user_id, number_id, job_id, job_complete_status, git_time);

        }

        out_rec.put("status",1);
        out_rec.put("message", "领取成功，请查看");
        return out_rec;

    }

    //用户回显所有任务
    @WebMethod("job/get_all_job")
    public RecordSet get_all_job(HttpServletRequest req, QueryParams qp) throws ParseException {

        logger.info("任务展示");
        //审核通过展示任务列表
        RecordSet records = GlobalLogics.getJobLogic().get_all_job(JobConstant.AUDIT_STATUS.APPROVED);
        return records;
    }



    @WebMethod("file/upload_file")
    public Record upload_file(HttpServletRequest req, QueryParams qp) {
        FileItem file_item = qp.getFile("Filedata");
        if (file_item.getSize()<0)
            return new Record();
        String full_path = "";
        boolean b = false;
        String ATTACHMENTS_ADDR = "";
        if (file_item != null && file_item.getSize() > 0) {
            String ATTACHMENTS_NAME = file_item.getName().substring(file_item.getName().lastIndexOf("\\") + 1);
            String expName = "";
            if (ATTACHMENTS_NAME.contains(".")) {
                expName = ATTACHMENTS_NAME.substring(ATTACHMENTS_NAME.lastIndexOf(".") + 1);
            }
            String ORID =  Long.toString(RandomUtils.generateId());
            ATTACHMENTS_ADDR = ORID + "." + expName;
            b = Constants.uploadFileOSS(file_item, ATTACHMENTS_ADDR);
        }
        //https://qqyqh-video.oss-cn-beijing.aliyuncs.com/videoStorage/3240561902876867079.mp4
        if (b) {
            //文件路径
            full_path = String.format(FileOSSPattern, ATTACHMENTS_ADDR);
        }
        Record rec = new Record();
        rec.put("full_path",full_path);
        rec.put("file_size",file_item.getSize());
        return rec;
    }
}
