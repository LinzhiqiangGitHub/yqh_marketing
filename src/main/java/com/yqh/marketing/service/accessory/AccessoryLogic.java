package com.yqh.marketing.service.accessory;

import com.yqh.marketing.basedevss.base.data.RecordSet;

import java.util.Date;

public interface AccessoryLogic {

    //boolean save_Accessory(TYqhEnterpriseAccessory tYqhEnterpriseAccessory);

    boolean save_Accessory1(long ACCESSORY_ID, String USER_ID, String ADVERTISER_ID, String JOB_ID, String COMPLETE_ID, int ACCESSORY_TYPE, String ACCESSORY_URL, int ACCESSORY_STATUS, Date CREATE_TIME,Date UPDATE_TIME);

    RecordSet getAllAccessory();

}
