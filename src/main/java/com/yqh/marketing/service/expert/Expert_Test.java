package com.yqh.marketing.service.expert;

import com.yqh.marketing.base.LoginUserConstant;
import com.yqh.marketing.basedevss.base.data.Record;
import com.yqh.marketing.basedevss.base.data.RecordSet;
import com.yqh.marketing.basedevss.base.util.RandomUtils;
import com.yqh.marketing.basedevss.base.web.QueryParams;
import com.yqh.marketing.basedevss.base.web.webmethod.WebMethod;
import com.yqh.marketing.basedevss.base.web.webmethod.WebMethodServlet;
import com.yqh.marketing.common.GlobalLogics;
import com.yqh.marketing.utils.RegexUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.logging.Logger;

public class Expert_Test extends WebMethodServlet {
    private static final Logger logger = Logger.getLogger("Expert_Test.class");

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @WebMethod("expert/create_expert1")
    public Record creat_expert(HttpServletRequest req, QueryParams qp){

        Record out_rec = new Record();
        //获取用户ID
        String user_id = "3266032270399972252";

        //生成达人ID
        String expert_id = String.valueOf(RandomUtils.generateId());

        //头像
        String logo =  "url：//" + String.valueOf(RandomUtils.generateId());;

        //昵称
        String nick_name = "一只小团团2";
        if (nick_name.isEmpty()){
            out_rec.put("status", 0);
            out_rec.put("message", "昵称不能为空");
            return out_rec;
        }
        //性别
        String gender = ExpertUserConstant.GENDER.MAN;
        if (gender.isEmpty()){
            out_rec.put("status", 0);
            out_rec.put("message", "性别不能为空");
            return out_rec;
        }

        //达人类型
        int expert_type = ExpertUserConstant.EXPERTTYPE.VIDEO_CLIP;
        //真实姓名
        String admin_name = "团tuna2";
        if (admin_name.isEmpty()){
            out_rec.put("status", 0);
            out_rec.put("message", "真实姓名不能为空");
            return out_rec;
        }


        //身份证号
        String v_id = "341181199207180811";
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
        String introduction = "hello,word----";
        //联系邮箱
        String email = "1468633145@11qq.com";
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
        String phone = "13955404378";
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
        String microsignal = "1234567890";
        if (microsignal.isEmpty()){
            out_rec.put("status", 0);
            out_rec.put("message", "微信号不能为空");
            return out_rec;
        }
        if (microsignal.length() < 5 || microsignal.length() > 33){
            out_rec.put("status", 0);
            out_rec.put("message", "微信号长度为6-32之间");
            return out_rec;
        }

        //银行卡号
        String bank_card_number = "6222021001116245702";
        if (bank_card_number.isEmpty()){
            out_rec.put("status", 0);
            out_rec.put("message", "银行卡号不能为空");
            return out_rec;
        }
        if(RegexUtils.identity_card(bank_card_number)){
            out_rec.put("status", 0);
            out_rec.put("message", "银行卡号格式错误");
            return out_rec;
        }

        //图片ID
        String img_id = String.valueOf(RandomUtils.generateId());
        //图片地址
        String img_url = "url://123";
        //图片类型(达人)
        String img_type = LoginUserConstant.User_Image.UP_TO_THE_PICTURES;

        //创建时间
        Date create_time = new Date();

        GlobalLogics.getExpertLogic()
                .create_expertLogic_information(img_id, img_type,
                        user_id,expert_id,logo,nick_name,gender,expert_type,
                        admin_name,v_id,introduction,email,phone,microsignal,
                        bank_card_number,create_time);

        //GlobalLogics.getExpertLogic().create_Image(img_id, user_id, img_url, img_type, create_time);
        out_rec.put("status",1);
        out_rec.put("message", "填写成功");
        out_rec.put("record",expert_id);
        return out_rec;
    }
    @WebMethod("expert/update_expert")
    public Record update_expert(HttpServletRequest req, QueryParams qp){

        Record out_rec = new Record();
        //获取用户ID
        String user_id = "3266032270399972252";

        //图片地址
        String img_url = "http://1234567890";

        //昵称
        String nick_name = "NICK_NAME";
        if (nick_name.isEmpty()){
            out_rec.put("status", 0);
            out_rec.put("message", "昵称不能为空");
            return out_rec;
        }
        //性别
        String gender = "男士";
        if (gender.isEmpty()){
            out_rec.put("status", 0);
            out_rec.put("message", "性别不能为空");
            return out_rec;
        }

        //达人类型
        int expert_type = 1;

        //真实姓名
        String admin_name = "ADMIN_NAME";
        if (admin_name.isEmpty()){
            out_rec.put("status", 0);
            out_rec.put("message", "真实姓名不能为空");
            return out_rec;
        }

        //身份证号
        String v_id = "341181199708170965";
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
        String introduction = "INTRODUCTION";
        //联系邮箱
        String email = "1468633145@qq.com";
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
        String phone = "18755680989";
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
        String microsignal = "MICROSIGNAL";
        if (microsignal.isEmpty()){
            out_rec.put("status", 0);
            out_rec.put("message", "微信号不能为空");
            return out_rec;
        }

        //银行卡号
        String bank_card_number = "BANK_CARD_NUMBER";

        //创建时间
        Date update_time = new Date();


        boolean expert = GlobalLogics.getExpertLogic().update_expertLogic_information(
                user_id, img_url, nick_name, gender, expert_type, admin_name, v_id, introduction, email,
                phone, microsignal, bank_card_number, update_time);

        out_rec.put("status",1);
        out_rec.put("message", "填写成功");
        out_rec.put("record",expert);
        return out_rec;
    }

    //回显数据
    @WebMethod("expert/expertByUserId")
    public Record advertiserByUserId(HttpServletRequest req, QueryParams qp) {

        Record out_rec = new Record();

        //修改日期
        Date date = new Date();
        //获取user_id
        String user_id = "3266032270399972252";
        //数据回显
        Record record = GlobalLogics.getExpertLogic().expertByUserId(user_id);
        //IMG数据回显
        Record recordIMG = GlobalLogics.getExpertLogic().ImageByuserId(user_id);

        out_rec.put("status", 1);
        out_rec.put("message", "回显成功");
        out_rec.put("record",record);
        out_rec.put("record2",recordIMG);
        return out_rec;

    }


    //用户创建账户信息
    @WebMethod("expert/create_expert_kol_number")
    public Record expert_kol_number(HttpServletRequest req, QueryParams qp) {
        //NUMBER_ID,EXPERT_ID,PLATEFORM_TYPE,NUMBER_NAME,FANS_COUNT,NUMBER_TITLY,NUMBER_STATUS,CREATE_TIME,UPDATE_TIME

        Record out_rec = new Record();
        //账户id
        String number_id = String.valueOf(RandomUtils.generateId());

        //获取达人的user_id
        String user_id = "3266032270399972252";
        //获取平台类型
        int plateform_type = 1;
        if (plateform_type == 0){
            out_rec.put("status", 0);
            out_rec.put("message", "平台类型不能为空");
        }
        //获取账户名称
        String number_name = "小团团";
        if (number_name.isEmpty()){
            out_rec.put("status", 0);
            out_rec.put("message", "账户名称不能为空");
        }
        //粉丝数量
        String fans_count = "45万";
        if (fans_count.isEmpty()){
            out_rec.put("status", 0);
            out_rec.put("message", "粉丝数量不能为空");
        }
        //账户的类型
        String number_titly = "吃鸡";
        if (number_titly.isEmpty()){
            out_rec.put("status", 0);
            out_rec.put("message", "账户的类型不能为空");
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

   /* //查询所有用户账户数据(管理页面)
    @WebMethod("expert/get_all_expertnumber")
    public Record get_all_expertnumber(HttpServletRequest req, QueryParams qp) {

        Record out_rec = new Record();

        int STATE = (int)qp.getInt("STATE",9);
        int page = (int)qp.getInt("PAGE",0);
        int count = (int)qp.getInt("COUNT",0);
        //数据回显
        RecordSet recordSet = GlobalLogics.getExpertLogic().get_all_expertnumber(NICK_NAME, START_TIME, END_TIME, NICK_NAME, STATE, page, count);


        out_rec.put("status", 1);
        out_rec.put("message", "回显成功");
        out_rec.put("record",recordSet);
        return out_rec;

    }*/

    //回显数据
    @WebMethod("expert/expertnumberByUserId")
    public Record expertnumberByUserId(HttpServletRequest req, QueryParams qp) {

        Record out_rec = new Record();
        //获取user_id
        String user_id = "3266032270399972252";
        //数据回显
        RecordSet recordSet = GlobalLogics.getExpertLogic().expertnumberByUserId(user_id);

        out_rec.put("status", 1);
        out_rec.put("message", "回显成功");
        out_rec.put("record",recordSet);
        return out_rec;

    }

    //账户管理(更新)
    @WebMethod("expert/update_expert_kol_number")
    public Record update_expert_kol_number(HttpServletRequest req, QueryParams qp) {
        //NUMBER_ID,EXPERT_ID,PLATEFORM_TYPE,NUMBER_NAME,FANS_COUNT,NUMBER_TITLY,NUMBER_STATUS,CREATE_TIME,UPDATE_TIME
        Record out_rec = new Record();

        //获取达人的number_id
        String number_id = "3266796503836601290";
        //获取平台类型
        int plateform_type = 1;
        if (plateform_type == 0){
            out_rec.put("status", 0);
            out_rec.put("message", "平台类型不能为空");
        }
        //获取账户名称
        String number_name = "软泥怪";
        if (number_name.isEmpty()){
            out_rec.put("status", 0);
            out_rec.put("message", "账户名称不能为空");
        }
        //粉丝数量
        String fans_count = "32万";
        if (fans_count.isEmpty()){
            out_rec.put("status", 0);
            out_rec.put("message", "粉丝数量不能为空");
        }
        //账户的内容类型
        String number_titly = "王者荣耀";
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

        boolean record = GlobalLogics.getExpertLogic().update_expert_number(number_id,plateform_type,number_name,fans_count,number_titly,number_status,update_time);

        out_rec.put("status", 1);
        out_rec.put("message", "账户更新成功");
        out_rec.put("record",record);
        return out_rec;

    }

    //达人账户审核
    @WebMethod("expert/update_expert_kol_numberByStatus")
    public Record expertnumberByStatus(HttpServletRequest req, QueryParams qp) {

        Record out_rec = new Record();
        //获取账户id
        String number_id = "3266610822541546726";
        //获取用户状态
        int user_status = 2;
        //当前时间
        Date update_time = new Date();
        //数据审核
        boolean status = GlobalLogics.getExpertLogic().expertnumberByStatus(number_id,user_status,update_time);

        if (user_status == 1) {
            out_rec.put("status", 0);
            out_rec.put("message", "待审核");
            out_rec.put("record", user_status);
            return out_rec;
        }
        if (user_status == 3){
            out_rec.put("status", 0);
            out_rec.put("message", "审核失败");
            out_rec.put("record",user_status);
            return out_rec;
        }
        out_rec.put("status", 1);
        out_rec.put("message", "审核成功");
        out_rec.put("record",user_status);
        return out_rec;

    }

}
