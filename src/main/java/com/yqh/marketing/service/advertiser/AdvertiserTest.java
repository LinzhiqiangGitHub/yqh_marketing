package com.yqh.marketing.service.advertiser;

import com.yqh.marketing.base.LoginUserConstant;
import com.yqh.marketing.base.NumberConstant;
import com.yqh.marketing.basedevss.base.data.Record;
import com.yqh.marketing.basedevss.base.util.RandomUtils;
import com.yqh.marketing.basedevss.base.web.QueryParams;
import com.yqh.marketing.basedevss.base.web.webmethod.WebMethod;
import com.yqh.marketing.basedevss.base.web.webmethod.WebMethodServlet;
import com.yqh.marketing.common.GlobalLogics;
import com.yqh.marketing.service.expert.ExpertUserConstant;
import com.yqh.marketing.utils.RegexUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.logging.Logger;

public class AdvertiserTest extends WebMethodServlet {

    private static final Logger logger = Logger.getLogger("AdvertiserTest.class");

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @WebMethod("advertiser1/create_advertiser1")
    public Record create_advertiser(HttpServletRequest req, QueryParams qp) {

        Record out_rec = new Record();

        //用户ID
        String user_id = "3265914295129071699";

        //广告客户ID
        String advertiser_id = String.valueOf(RandomUtils.generateId());

        //昵称
        String nick_name = "一条小团团OVO";
        if (nick_name.isEmpty()){
            out_rec.put("status", 0);
            out_rec.put("message", "昵称不能为空（可以为公司名称）");
            return out_rec;
        }

        //公司名称
        String company_name = "斗鱼";

        //logo
        String advertiser_logo = "url://" + String.valueOf(RandomUtils.generateId());
        //联系人
        String advertiser_contacts = "团";
        if (advertiser_contacts.isEmpty()){
            out_rec.put("status", 0);
            out_rec.put("message", "联系人不能为空");
            return out_rec;
        }


        //联系电话
        String advertiser_phone = "15755408899";
        if (advertiser_phone.isEmpty()){
            out_rec.put("status", 0);
            out_rec.put("message", "联系电话不能为空");
            return out_rec;
        }
        if (!RegexUtils.checkMobile(advertiser_phone)){
            out_rec.put("status", 0);
            out_rec.put("message", "联系电话格式不正确");
            return out_rec;
        }


        //联系邮箱
        String advertiser_email = "linlinzq@163.com";
        if (advertiser_email.isEmpty()){
            out_rec.put("status", 1);
            out_rec.put("message", "联系邮箱不能为空");
            return out_rec;
        }
        if (!RegexUtils.checkMail(advertiser_email)){
            out_rec.put("status", 0);
            out_rec.put("message", "邮箱格式不正确");
            return out_rec;
        }

        //联系人类型（品类） 公司/个人
        String advertiser_type = AdvertiserUserConstant.UserType.COMPANY;
        if (advertiser_type.isEmpty()){
            out_rec.put("status", 1);
            out_rec.put("message", "联系人类型不能为空");
            return out_rec;
        }

        //负责人微信号
        String microsignal = "1234578";
        if (microsignal.isEmpty()){
            out_rec.put("status", 1);
            out_rec.put("message", "微信号不能为空");
            return out_rec;
        }

        //联系地址
        String advertiser_site = "北京市-昌平区-回龙观DDDDD";

        /*创建时间*/
        Date create_time = new Date();
        //图片ID
        String img_id = String.valueOf(RandomUtils.generateId());
        //图片地址
        String img_url = "urllll://3248759435397729355464369759743";
        //图片类型(达人)
        String img_type = LoginUserConstant.User_Image.ADVERTISER_PROFILE_PICTURE;

        boolean advertise = GlobalLogics.getAdvertiserLogic().create_advertise_information(
                advertiser_id,user_id,nick_name,company_name,advertiser_logo,
                advertiser_contacts,advertiser_phone,advertiser_email,
                advertiser_type,microsignal,advertiser_site,create_time);
        System.out.println(advertise);

        boolean image = GlobalLogics.getExpertLogic().create_Image(img_id, user_id, img_url, img_type, create_time);

        System.out.println(image);

        out_rec.put("status", 1);
        out_rec.put("message", "填写成功");
        out_rec.put("record",advertiser_id);
        return out_rec;
    }

    //数据回显
    @WebMethod("advertiser1/advertiserByUserId")
    public Record advertiserByUserId(HttpServletRequest req, QueryParams qp) {

        Record out_rec = new Record();

        //获取user_id
        String user_id = "3265914295129071699";
        //数据回显
        Record record = GlobalLogics.getAdvertiserLogic().advertiseByuserId(user_id);
        //IMG数据回显
        Record recordIMG = GlobalLogics.getAdvertiserLogic().ImageByuserId(user_id);

        out_rec.put("status", 1);
        out_rec.put("message", "回显成功");
        out_rec.put("record",record);
        out_rec.put("record2",recordIMG);
        out_rec.put("record3",recordIMG.getString("IMG_ID"));
        out_rec.put("record4",record.getString("ADVERTISER_ID"));

        return out_rec;

    }

    @WebMethod("advertiser1/update_advertiser")
    public Record update_advertiser(HttpServletRequest req, QueryParams qp) {

        Record out_rec = new Record();

        //获取用户ID
        String user_id = "3265914295129071699";

        //昵称
        String nick_name = "猪猪侠";
        if (nick_name.isEmpty()){
            out_rec.put("status", 0);
            out_rec.put("message", "昵称不能为空（可以为公司名称）");
            return out_rec;
        }
        //公司名称
        String company_name = "众创";


        //图片地址
        String img_url = "uel://hell0000000o";
        //联系人
        String advertiser_contacts = "ADVERTISER_CONTACTS";
        if (advertiser_contacts.isEmpty()){
            out_rec.put("status", 1);
            out_rec.put("message", "联系人不能为空");
            return out_rec;
        }

        //联系电话
        String advertiser_phone = "15755430980";
        if (advertiser_phone.isEmpty()){
            out_rec.put("status", 1);
            out_rec.put("message", "联系电话不能为空");
            return out_rec;
        }
        if (!RegexUtils.checkMobile(advertiser_phone)){
            out_rec.put("status", 0);
            out_rec.put("message", "联系电话格式不正确");
            return out_rec;
        }


        //联系邮箱
        String advertiser_email = "1464345345@163.com";
        if (advertiser_email.isEmpty()){
            out_rec.put("status", 1);
            out_rec.put("message", "联系邮箱不能为空");
            return out_rec;
        }
        if (!RegexUtils.checkMail(advertiser_email)){
            out_rec.put("status", 0);
            out_rec.put("message", "邮箱格式不正确");
            return out_rec;
        }

        //联系人类型（品类） 公司/个人
        String advertiser_type = "1";
        if (advertiser_type.isEmpty()){
            out_rec.put("status", 1);
            out_rec.put("message", "联系人类型不能为空");
            return out_rec;
        }

        //负责人微信号
        String microsignal = "2131231";
        if (microsignal.isEmpty()){
            out_rec.put("status", 1);
            out_rec.put("message", "微信号不能为空");
            return out_rec;
        }

        //联系地址
        String advertiser_site = "beiji";

        /*更新时间*/
        Date update_time = new Date();


        boolean img_type =
                GlobalLogics.getAdvertiserLogic().update_Image(user_id, img_url,update_time);

        boolean advertise_type =
                GlobalLogics.getAdvertiserLogic().update_advertise_information(
                user_id,nick_name,company_name,img_url,
                advertiser_contacts,advertiser_phone,advertiser_email,
                advertiser_type,microsignal,advertiser_site,update_time);


        out_rec.put("status", 1);
        out_rec.put("message", "更新成功");
        out_rec.put("record",advertise_type);
        out_rec.put("record2",img_type);

        return out_rec;
    }

    //广告客户审核状态
    @WebMethod("advertiser1/update_advertiserByStatus1")
    public Record update_advertiserByStatus(HttpServletRequest req, QueryParams qp) {

        Record out_rec = new Record();
        //获取用户id
        String user_id = "3265914295129800784";
        //获取用户状态
        int user_status = 1;
        //当前时间
        Date update_time = new Date();
        //数据审核
        boolean status = GlobalLogics.getAdvertiserLogic().update_advertiserByStatus(user_id,user_status,update_time);

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

}
