package com.yqh.marketing.service.advertiser;

import com.yqh.marketing.basedevss.base.data.Record;

import java.util.Date;

public interface AdvertiserLogic {

    boolean create_advertise_information(String advertiser_id, String user_id, String nick_name, String company_name, String advertiser_logo, String advertiser_contacts, String advertiser_phone, String advertiser_email, String advertiser_type, String microsignal, String advertiser_site, Date create_time);

    Record getAdvertiserByAdvertiserNameCount(String company_name);

    boolean update_advertise_information(String user_id, String nick_name, String company_name, String advertiser_logo, String advertiser_contacts, String advertiser_phone, String advertiser_email, String advertiser_type, String microsignal, String advertiser_site, Date update_time);

    boolean gat_all_advertise_information(String user_id, String nick_name, String company_name, String img_url, String advertiser_contacts, String advertiser_phone, String advertiser_email, String advertiser_type, String microsignal, String advertiser_site, Date update_time);

    boolean update_Image(String user_id, String img_url, Date update_time);

    Record advertiseByuserId(String user_id);

    boolean create_Image(String img_id,String user_id, String img_url, String img_type, Date create_time);

    Record ImageByuserId(String user_id);

    boolean update_advertiserByStatus(String user_id, int user_status, Date update_time);

}
