package com.yqh.marketing.service.expert;

import com.yqh.marketing.basedevss.base.data.Record;
import com.yqh.marketing.basedevss.base.data.RecordSet;

import java.util.Date;

public interface ExpertLogic {

    //创建达人用户信息
    boolean create_expertLogic_information(String img_id, String img_type, String user_id, String expert_id, String logo,
                                           String nick_name, String gender, int expert_type,
                                           String admin_name, String v_id, String introduction,
                                           String email, String phone, String microsignal,
                                           String bank_card_number, Date create_time);

    Record getExpertByUserNameCount(String loginName);

    boolean create_Image(String img_id, String user_id, String img_url, String img_type, Date create_time);

    //回显图片
    Record ImageByuserId(String user_id);
    //回显数据
    Record expertByUserId(String user_id);

    //更新达人基础信息
    boolean update_expertLogic_information(String user_id, String logo, String nick_name, String gender, int expert_type, String admin_name, String v_id, String introduction, String email, String phone, String microsignal, String bank_card_number, Date update_time);
    //更新图片
    boolean update_Image(String user_id, String img_url, Date update_time);

    //创建达人账户信息
    boolean create_expert_number(String number_id, String user_id, int plateform_type, String number_name, String fans_count, String number_titly, int number_status, Date create_time);
    //更新达人账户信息
    boolean update_expert_number(String number_id, int plateform_type, String number_name, String fans_count, String number_titly, int number_status, Date update_time);
    //达人账户回显
    RecordSet expertnumberByUserId(String user_id);
    //审核状态
    boolean expertnumberByStatus(String number_id, int user_status, Date update_time);
    //查询所有账户信息
    Record get_all_expertnumber_page(String NICK_NAME, String START_TIME, String END_TIME, String nick_name, int STATE, int page, int count);
    //查询所有用户数据并且分页
    Record get_all_expert_page(String NICK_NAME, String start_time, String end_time, String nick_name, int state, int page, int count);
    //查询所有用户数据不分页
    RecordSet get_all_expertnumber();
}
