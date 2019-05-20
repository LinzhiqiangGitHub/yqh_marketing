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

public class JobServlet extends WebMethodServlet {

    private static final Logger logger = Logger.getLogger("ExpertServlet.class");
    private StaticFileStorage fileStorage;
    private String FilePattern;
    private String FileOSSPattern;

    @Override
    public void init() throws ServletException {
        Configuration conf = GlobalConfig.get();
        super.init();
        //fileStorage = (StaticFileStorage) ClassUtils2.newInstance(conf.getString("service.servlet.fileStorage", ""));
        //fileStorage.init();
        //FilePattern = conf.getString("service.file.UrlPattern", " http://localhost/marketing/fileImgStorage/%s");
        //FileOSSPattern = conf.getString("service.file.oss.UrlPattern", "https://qqyqh-video.oss-cn-beijing.aliyuncs.com/youdou/file/%s");

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
        String logo = qp.getString("LOGO", "");

        //任务名称
        String job_name = qp.checkGetString("JOB_NAME");
        if (job_name.isEmpty()){
            out_rec.put("status",0);
            out_rec.put("message","任务名称不能为空");
            return out_rec;
        }
        //String 转 DATE
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //任务开始时间
        String start_time = qp.checkGetString("JOBPERIOD_START");
        Date jobperiod_start = sdf.parse(start_time);
        if (start_time.isEmpty()){
            out_rec.put("status",0);
            out_rec.put("message","任务开始日期不能为空");
            return out_rec;
        }
        //任务结束时间
        String end_time = qp.checkGetString("JOBPERIOD_END");
        Date jobperiod_end = sdf.parse(start_time);
        if (end_time.isEmpty()){
            out_rec.put("status",0);
            out_rec.put("message","任务结束日期不能为空");
            return out_rec;
        }


        //任务次数
        long jobperiod_count = qp.getInt("JOBPERIOD_COUNT",1);
        if (jobperiod_count == 0){
            out_rec.put("status",0);
            out_rec.put("message","任务次数不能为空");
            return out_rec;
        }
        //任务内容
        String job_titly = qp.getString("JOB_TITLY","");

        //任务平台
        int job_terrace = (int) qp.checkGetInt("JOB_TERRACE");

        //任务佣金
        //浮点损失精度使用long
        float job_commission = qp.checkGetInt("JOB_COMMISSION");
        if (job_commission == 0L){
            out_rec.put("status",0);
            out_rec.put("message","佣金不能为空");
            return out_rec;
        }

        //任务创建时间
        Date create_time = new Date();
        boolean recort = GlobalLogics.getJobLogic().create_job(job_id,advertiser_id,logo,job_name,jobperiod_start,jobperiod_end,
                jobperiod_count,job_titly,job_terrace,job_commission,create_time
        );
        //图片ID
        String img_id = String.valueOf(RandomUtils.generateId());
        //图片类型(广告客户)
        String img_type = LoginUserConstant.User_Image.TASK_LOGO;
        boolean image = GlobalLogics.getJobLogic().create_Image(img_id, job_id, logo, img_type, create_time);

        out_rec.put("status",1);
        out_rec.put("message","发布任务");
        out_rec.put("record",recort);
        return out_rec;
    }

    //用户所有任务回显
    @WebMethod("job/jobByUser_Id")
    public RecordSet jobByUser_Id(HttpServletRequest req, QueryParams qp){

        String User_id =  qp.checkGetString("User_id");
        RecordSet out_rec = GlobalLogics.getJobLogic().get_all_jobByUserId(User_id);

        return out_rec;
    }

    //指定用户任务回显
    @WebMethod("job/jobById")
    public Record jobById(HttpServletRequest req, QueryParams qp) {
        String job_id = qp.checkGetString("JOB_ID");
        Record out_rec = GlobalLogics.getJobLogic().jopById(job_id);
        return out_rec;
    }

    //用户更新任务
    @WebMethod("job/update_job")
    public Record update_job(HttpServletRequest req, QueryParams qp) throws ParseException {

        Record out_rec = new Record();

        //用户ID
        String job_id = qp.checkGetString("JOB_ID");

        //图片地址
        String logo = qp.getString("LOGO", "");

        //任务名称
        String job_name = qp.checkGetString("JOB_NAME");
        if (job_name.isEmpty()){
            out_rec.put("status",0);
            out_rec.put("message","任务名称不能为空");
            return out_rec;
        }

        //String 转 DATE
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //任务开始时间
        String start_time = qp.checkGetString("JOBPERIOD_START");
        Date jobperiod_start = sdf.parse(start_time);
        if (start_time.isEmpty()){
            out_rec.put("status",0);
            out_rec.put("message","任务开始日期不能为空");
            return out_rec;
        }
        //任务结束时间
        String end_time = qp.checkGetString("JOBPERIOD_END");
        Date jobperiod_end = sdf.parse(start_time);
        if (end_time.isEmpty()){
            out_rec.put("status",0);
            out_rec.put("message","任务结束日期不能为空");
            return out_rec;
        }


        //任务次数
        long jobperiod_count = qp.getInt("JOBPERIOD_COUNT",1);
        if (jobperiod_count == 0){
            out_rec.put("status",0);
            out_rec.put("message","任务次数不能为空");
            return out_rec;
        }

        //任务内容
        String job_titly = qp.getString("JOB_TITLY","");

        //任务平台
        int job_terrace = (int) qp.checkGetInt("JOB_TERRACE");

        //任务佣金
        //浮点损失精度使用long
        float job_commission = qp.checkGetInt("JOB_COMMISSION");
        if (job_commission == 0L){
            out_rec.put("status",0);
            out_rec.put("message","佣金不能为空");
            return out_rec;
        }

        //任务更新时间
        Date update_time = new Date();

        //任务更新
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

    //用户回显所有任务
    @WebMethod("job/get_all_jobByjobId")
    public boolean get_all_jobByjobId(HttpServletRequest req, QueryParams qp) throws ParseException {

        logger.info("任务展示");

        String job_id = qp.checkGetString("JOB_ID");
        int job_status = (int) qp.checkGetInt("JOB_STATUS");
        Date update_time = new Date();
        //审核通过后可以
        boolean record = GlobalLogics.getJobLogic().updateByjobId(job_id,job_status,update_time);

        return record;

    }

    //所有已发布任务展示
    @WebMethod("job/get_all_job")
    public RecordSet get_all_job(HttpServletRequest req, QueryParams qp) {

        logger.info("任务展示");
        //审核通过展示任务列表
        RecordSet records = GlobalLogics.getJobLogic().get_all_job(JobConstant.AUDIT_STATUS.APPROVED);
        return records;
    }

    //任务详情
    @WebMethod("job/jobImplByjobId")

    public Record jobImplByjobId(HttpServletRequest req, QueryParams qp) throws ParseException {

        logger.info("任务展示");

        String job_id = qp.checkGetString("JOB_ID");
        //获取任务详情
        Record record = GlobalLogics.getJobLogic().jopById(job_id);

        return record;

    }


    //任务接取
    @WebMethod("job/receive_job")
    public RecordSet receive_job(HttpServletRequest req, QueryParams qp) {
        logger.info("任务接取");
        //审核通过展示任务列表
        //RecordSet records = GlobalLogics.getJobLogic().receive_job(JobConstant.JOB_AUDIT_STATUS.PICK_UP_THE_TASK);
        final RecordSet records = new RecordSet();
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
            full_path = String.format(FileOSSPattern, ATTACHMENTS_ADDR);
        }
        Record rec = new Record();
        rec.put("full_path",full_path);
        rec.put("file_size",file_item.getSize());
        return rec;
    }

}
