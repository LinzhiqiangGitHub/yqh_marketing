package com.yqh.marketing.service.expert;

import com.yqh.marketing.base.LoginUserConstant;
import com.yqh.marketing.basedevss.base.conf.Configuration;
import com.yqh.marketing.basedevss.base.conf.GlobalConfig;
import com.yqh.marketing.basedevss.base.context.ClientContext;
import com.yqh.marketing.basedevss.base.data.Record;
import com.yqh.marketing.basedevss.base.data.RecordSet;
import com.yqh.marketing.basedevss.base.sfs.StaticFileStorage;
import com.yqh.marketing.basedevss.base.util.RandomUtils;
import com.yqh.marketing.basedevss.base.web.QueryParams;
import com.yqh.marketing.basedevss.base.web.webmethod.WebMethod;
import com.yqh.marketing.basedevss.base.web.webmethod.WebMethodServlet;
import com.yqh.marketing.common.Constants;
import com.yqh.marketing.common.GlobalLogics;
import com.yqh.marketing.common.PortalContext;
import com.yqh.marketing.utils.RegexUtils;
import org.apache.commons.fileupload.FileItem;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.logging.Logger;


public class ExpertServlet extends WebMethodServlet {

    private static final Logger logger = Logger.getLogger("ExpertServlet.class");
    private StaticFileStorage imageStorage;
    private String imagePattern;
    private String imageOSSPattern;


    @Override
    public void init() throws ServletException {
        Configuration conf = GlobalConfig.get();
        super.init();
        //imageStorage = (StaticFileStorage) ClassUtils2.newInstance(conf.getString("service.servlet.userImgStorage", ""));
        //imageStorage.init();
        //imagePattern = conf.getString("service.expert.UrlPattern", "http://localhost/marketing/userImgStorage/%s");
        //imageOSSPattern = conf.getString("service.expert.oss.UrlPattern", "https://qqyqh-video.oss-cn-beijing.aliyuncs.com/youdou/images/%s");

    }

    @WebMethod("expert/create_expert")
    public Record create_expert(HttpServletRequest req, QueryParams qp){

        Record out_rec = new Record();
        //获取用户ID
        String user_id = qp.checkGetString("user_id");

        //生成达人ID
        String expert_id = String.valueOf(RandomUtils.generateId());

        //头像
        String logo = qp.getString("LOGO","");

        //昵称
        String nick_name = qp.checkGetString("NICK_NAME");
        if (nick_name.isEmpty()){
            out_rec.put("status", 0);
            out_rec.put("message", "昵称不能为空");
            return out_rec;
        }
        //性别
        String gender = qp.checkGetString("GENDER");
        if (gender.isEmpty()){
            out_rec.put("status", 0);
            out_rec.put("message", "性别不能为空");
            return out_rec;
        }

        //达人类型
        int expert_type = (int) qp.checkGetInt("EXPERT_TYPE");

        //真实姓名
        String admin_name = qp.checkGetString("ADMIN_NAME");
        if (admin_name.isEmpty()){
            out_rec.put("status", 0);
            out_rec.put("message", "真实姓名不能为空");
            return out_rec;
        }

        //身份证号
        String v_id = qp.checkGetString("V_ID");
        if (v_id.isEmpty()){
            out_rec.put("status", 0);
            out_rec.put("message", "身份证号不能为空");
            return out_rec;
        }
        if (!RegexUtils.identity_card(v_id)){
            out_rec.put("status", 0);
            out_rec.put("message", "身份证号格式不正确");
            return out_rec;
        }

        //个人简介
        String introduction = qp.getString("INTRODUCTION","");
        //联系邮箱
        String email = qp.checkGetString("EMAIL");
        if (email.isEmpty()){
            out_rec.put("status", 0);
            out_rec.put("message", "邮箱不能为空");
            return out_rec;
        }
        if (!RegexUtils.checkMail(email)){
            out_rec.put("status", 0);
            out_rec.put("message", "邮箱格式不正确");
            return out_rec;
        }

        //联系手机
        String phone = qp.checkGetString("PHONE");
        if (phone.isEmpty()){
            out_rec.put("status", 0);
            out_rec.put("message", "手机号码不能为空");
            return out_rec;
        }
        if (!RegexUtils.checkMobile(phone)){

            out_rec.put("status", 0);
            out_rec.put("message", "手机号码格式不正确");
            return out_rec;
        }

        //微信号
        String microsignal = qp.checkGetString("MICROSIGNAL");
        if (microsignal.isEmpty()){
            out_rec.put("status", 0);
            out_rec.put("message", "微信号不能为空");
            return out_rec;
        }

        //银行卡号
        String bank_card_number = qp.getString("BANK_CARD_NUMBER","");

        //创建时间
        Date create_time = new Date();

        //图片ID
        String img_id = String.valueOf(RandomUtils.generateId());
        //图片地址
        String img_url = qp.getString("IMG_URL", "");
        //图片类型(达人)
        String img_type = LoginUserConstant.User_Image.UP_TO_THE_PICTURES;

        GlobalLogics.getExpertLogic().create_expertLogic_information(
                img_id, img_type, user_id,expert_id,logo,nick_name,gender,expert_type,admin_name,v_id,introduction,email,phone,microsignal,bank_card_number,create_time);
        GlobalLogics.getExpertLogic().create_Image(img_id, user_id, img_url, img_type, create_time);
        out_rec.put("status",1);
        out_rec.put("message", "填写成功");
        out_rec.put("record",expert_id);
        return out_rec;
    }

    //查询所有达人用户数据
    @WebMethod("expert/get_all_expert_page")
    public Record get_all_expert_page(HttpServletRequest req, QueryParams qp) {
        ClientContext ctx = PortalContext.getClientContext(req, qp, true, true);
        String NICK_NAME = qp.getString("NICK_NAME","");
        String START_TIME = qp.getString("START_TIME","");
        String END_TIME = qp.getString("END_TIME","");
        int STATE = (int)qp.getInt("STATE",9);
        int page = (int)qp.getInt("PAGE",0);
        int count = (int)qp.getInt("COUNT",0);
        Record records = GlobalLogics.getExpertLogic().get_all_expert_page(NICK_NAME,START_TIME, END_TIME,NICK_NAME, STATE, page, count);
        return records;
    }

    //更新达人用户信息
    @WebMethod("expert/update_expert")
    public Record update_expert(HttpServletRequest req, QueryParams qp){

        Record out_rec = new Record();
        //获取用户ID
        String user_id = qp.checkGetString("user_id");

        //logo图片地址
        String img_url = qp.getString("https://qqyqh-video.oss-cn-beijing.aliyuncs.com/imgStorage/3268025749733575694.jpg", "");

        //昵称
        String nick_name = qp.checkGetString("NICK_NAME");
        if (nick_name.isEmpty()){
            out_rec.put("status", 0);
            out_rec.put("message", "昵称不能为空");
            return out_rec;
        }
        //性别
        String gender = qp.checkGetString("GENDER");
        if (gender.isEmpty()){
            out_rec.put("status", 0);
            out_rec.put("message", "性别不能为空");
            return out_rec;
        }

        //达人类型
        int expert_type = (int) qp.checkGetInt("EXPERT_TYPE");

        //真实姓名
        String admin_name = qp.checkGetString("ADMIN_NAME");
        if (admin_name.isEmpty()){
            out_rec.put("status", 0);
            out_rec.put("message", "真实姓名不能为空");
            return out_rec;
        }

        //身份证号
        String v_id = qp.checkGetString("V_ID");
        if (v_id.isEmpty()){
            out_rec.put("status", 0);
            out_rec.put("message", "身份证号不能为空");
            return out_rec;
        }
        if (!RegexUtils.identity_card(v_id)){
            out_rec.put("status", 0);
            out_rec.put("message", "身份证号格式不正确");
            return out_rec;
        }

        //个人简介
        String introduction = qp.getString("INTRODUCTION","");

        //联系邮箱
        String email = qp.checkGetString("EMAIL");
        if (email.isEmpty()){
            out_rec.put("status", 0);
            out_rec.put("message", "邮箱不能为空");
            return out_rec;
        }
        if (!RegexUtils.checkMail(email)){
            out_rec.put("status", 0);
            out_rec.put("message", "邮箱格式不正确");
            return out_rec;
        }

        //联系手机
        String phone = qp.checkGetString("PHONE");
        if (phone.isEmpty()){
            out_rec.put("status", 0);
            out_rec.put("message", "手机号码不能为空");
            return out_rec;
        }
        if (!RegexUtils.checkMobile(phone)){

            out_rec.put("status", 0);
            out_rec.put("message", "手机号码格式不正确");
            return out_rec;
        }

        //微信号
        String microsignal = qp.checkGetString("MICROSIGNAL");
        if (microsignal.isEmpty()){
            out_rec.put("status", 0);
            out_rec.put("message", "微信号不能为空");
            return out_rec;
        }

        //银行卡号
        String bank_card_number = qp.getString("BANK_CARD_NUMBER","");
        if (!bank_card_number.isEmpty()){

        }

        //创建时间
        Date update_time = new Date();


        GlobalLogics.getExpertLogic().update_expertLogic_information(
                user_id,img_url,nick_name,gender,expert_type,admin_name,v_id,introduction,email,
                phone,microsignal,bank_card_number,update_time);

        //GlobalLogics.getExpertLogic().update_Image(user_id, img_url, update_time);



        out_rec.put("status",1);
        out_rec.put("message", "填写成功");
        out_rec.put("record",true);
        return out_rec;
    }

    //回显数据
    @WebMethod("expert/expertByUserId")
    public Record expertByUserId(HttpServletRequest req, QueryParams qp) {
        //获取user_id
        String user_id = qp.checkGetString("USER_ID");
        //数据回显
        Record record = GlobalLogics.getExpertLogic().expertByUserId(user_id);
        return record;

    }

    //创建账户管理
    @WebMethod("expert/create_expert_kol_number")
    public Record create_expert_kol_number(HttpServletRequest req, QueryParams qp) {
        //NUMBER_ID,EXPERT_ID,PLATEFORM_TYPE,NUMBER_NAME,FANS_COUNT,NUMBER_TITLY,NUMBER_STATUS,CREATE_TIME,UPDATE_TIME
        Record out_rec = new Record();
        //账户id
        String number_id = String.valueOf(RandomUtils.generateId());
        //获取达人的user_id
        String user_id = qp.checkGetString("EXPERT_ID");
        //获取平台类型
        int plateform_type = (int) qp.checkGetInt("PLATEFORM_TYPE");
        if (plateform_type == 0){
            out_rec.put("status", 0);
            out_rec.put("message", "平台类型不能为空");
        }
        //获取账户名称
        String number_name = qp.checkGetString("NUMBER_NAME");
        if (number_name.isEmpty()){
            out_rec.put("status", 0);
            out_rec.put("message", "账户名称不能为空");
        }
        //粉丝数量
        String fans_count = qp.checkGetString("FANS_COUNT");
        if (fans_count.isEmpty()){
            out_rec.put("status", 0);
            out_rec.put("message", "粉丝数量不能为空");
        }
        //账户的内容类型
        String number_titly = qp.checkGetString("NUMBER_TITLY");
        if (number_titly.isEmpty()){
            out_rec.put("status", 0);
            out_rec.put("message", "账户的内容类型不能为空");
        }
        if (number_titly.length() < 200){
            out_rec.put("status", 0);
            out_rec.put("message", "账户的内容类型描述不能低于200字");
        }

        //账户审核状态
        int number_status = ExpertUserConstant.AUDIT_STATUS.CHECK_PENDING;

        //创建日期
        Date create_time = new Date();

        boolean record = GlobalLogics.getExpertLogic().create_expert_number(number_id,user_id,plateform_type,number_name,fans_count,number_titly,number_status,create_time);

        out_rec.put("status", 1);
        out_rec.put("message", "账户创建成功");
        out_rec.put("record",record);
        return out_rec;

    }

    //回显账户数据
    @WebMethod("expert/expertnumberByUserId")
    public Record expertnumberByUserId(HttpServletRequest req, QueryParams qp) {
        Record out_rec = new Record();
        //获取user_id
        String user_id = qp.checkGetString("USER_ID");
        //数据回显
        RecordSet recordSet = GlobalLogics.getExpertLogic().expertnumberByUserId(user_id);
        out_rec.put("status", 1);
        out_rec.put("message", "回显成功");
        out_rec.put("record",recordSet);
        return out_rec;

    }

    //查询所有用户账户数据(管理页面)
    @WebMethod("expert/get_all_expertnumber_page")
    public Record get_all_expertnumber_page(HttpServletRequest req, QueryParams qp) {
        ClientContext ctx = PortalContext.getClientContext(req, qp, true, true);
        String NICK_NAME = qp.getString("NICK_NAME","");
        String START_TIME = qp.getString("START_TIME","");
        String END_TIME = qp.getString("END_TIME","");
        int STATE = (int)qp.getInt("STATE",9);
        int page = (int)qp.getInt("PAGE",0);
        int count = (int)qp.getInt("COUNT",0);

        //数据回显
        Record record = GlobalLogics.getExpertLogic().get_all_expertnumber_page(NICK_NAME,START_TIME, END_TIME,NICK_NAME, STATE, page, count);

        return record;

    }


    //查询所有用户账户数据(管理页面)
    @WebMethod("expert/get_all_expertnumber")
    public RecordSet get_all_expertnumber(HttpServletRequest req, QueryParams qp) {
        //ClientContext ctx = PortalContext.getClientContext(req, qp, true, true);
        logger.info("查询达人用户账号信息");
        //数据回显
        RecordSet record = GlobalLogics.getExpertLogic().get_all_expertnumber();
        return record;
    }


    //账户管理（更新）
    @WebMethod("expert/update_expert_kol_number")
    public Record update_expert_kol_number(HttpServletRequest req, QueryParams qp) {
        //NUMBER_ID,EXPERT_ID,PLATEFORM_TYPE,NUMBER_NAME,FANS_COUNT,NUMBER_TITLY,NUMBER_STATUS,CREATE_TIME,UPDATE_TIME
        Record out_rec = new Record();

        //获取达人的user_id
        String user_id = qp.checkGetString("EXPERT_ID");
        //获取平台类型
        int plateform_type = (int) qp.checkGetInt("PLATEFORM_TYPE");
        if (plateform_type == 0){
            out_rec.put("status", 0);
            out_rec.put("message", "平台类型不能为空");
        }
        //获取账户名称
        String number_name = qp.checkGetString("NUMBER_NAME");
        if (number_name.isEmpty()){
            out_rec.put("status", 0);
            out_rec.put("message", "账户名称不能为空");
        }
        //粉丝数量
        String fans_count = qp.checkGetString("FANS_COUNT");
        if (fans_count.isEmpty()){
            out_rec.put("status", 0);
            out_rec.put("message", "粉丝数量不能为空");
        }
        //账户的内容类型
        String number_titly = qp.checkGetString("NUMBER_TITLY");
        if (number_titly.isEmpty()){
            out_rec.put("status", 0);
            out_rec.put("message", "账户的内容类型不能为空");
        }
        if (number_titly.length() < 200){
            out_rec.put("status", 0);
            out_rec.put("message", "账户的内容类型描述不能低于200字");
        }

        //账户审核状态
        int number_status = ExpertUserConstant.AUDIT_STATUS.CHECK_PENDING;

        //创建日期
        Date update_time = new Date();

        boolean record = GlobalLogics.getExpertLogic().update_expert_number(user_id,plateform_type,number_name,fans_count,number_titly,number_status,update_time);

        out_rec.put("status", 1);
        out_rec.put("message", "账户更新成功");
        out_rec.put("record",record);
        return out_rec;
    }


    //达人账户审核
    @WebMethod("expert/update_expert_kol_numberByStatus")
    public Record expertnumberByStatus(HttpServletRequest req, QueryParams qp) {

        Record out_rec = new Record();

        //获取用户账号id
        String number_id = qp.checkGetString("NUMBER_ID");
        //获取用户状态
        int user_status = (int) qp.checkGetInt("NUMBER_STATUS");
        //当前时间
        Date update_time = new Date();
        //数据审核
        boolean status = GlobalLogics.getExpertLogic().expertnumberByStatus(number_id,user_status,update_time);

        if (user_status == ExpertUserConstant.AUDIT_STATUS.CHECK_PENDING) {
            out_rec.put("status", 0);
            out_rec.put("message", "待审核");
            out_rec.put("record", status);
            return out_rec;
        }
        if (user_status == ExpertUserConstant.AUDIT_STATUS.REJECTED){
            out_rec.put("status", 0);
            out_rec.put("message", "审核失败");
            out_rec.put("record",status);
            return out_rec;
        }
        out_rec.put("status", 1);
        out_rec.put("message", "审核成功");
        out_rec.put("record",status);
        return out_rec;
    }

    @WebMethod("images/upload_images")
    public Record upload_video(HttpServletRequest req, QueryParams qp) {
        FileItem file_item = qp.getFile("IMAGESdate");
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
            String ORID =    Long.toString(RandomUtils.generateId());
            ATTACHMENTS_ADDR = ORID + "." + expName;
            b = Constants.uploadImgOSS(file_item, ATTACHMENTS_ADDR);
        }
        //https://qqyqh-video.oss-cn-beijing.aliyuncs.com/videoStorage/3240561902876867079.mp4
        if (b) {
            full_path = String.format(imageOSSPattern, ATTACHMENTS_ADDR);
        }
        Record rec = new Record();
        rec.put("full_path",full_path);
        rec.put("video_size",file_item.getSize());
        return rec;
    }

}
