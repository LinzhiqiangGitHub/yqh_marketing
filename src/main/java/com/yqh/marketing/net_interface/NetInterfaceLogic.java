package com.yqh.marketing.net_interface;

import com.yqh.marketing.basedevss.base.data.Record;
import com.yqh.marketing.basedevss.base.data.RecordSet;

public interface NetInterfaceLogic {
    Record getSingleThirdStoreInfo(String app_key);
    Record getSingleThirdStoreInfoById(String store_id);
    Record getLogisticsByCode(String LOGISTICS_CODE);
    RecordSet getStoreOrder(String store_id, String start_time, String end_time, int page, int count);

    void getPushOrder(String order_id);

    RecordSet getGoodsAll(String third_id, int page, int count);

}

