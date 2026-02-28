/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.express.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shop.cereshop.app.service.express.KuaiDiNiaoService;
import com.shop.cereshop.commons.domain.express.KuaiDiNiaoCode;
import com.shop.cereshop.commons.domain.express.ShippingTrace;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.utils.KdniaoTrackQueryAPI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KuaiDiNiaoServiceImpl implements KuaiDiNiaoService {

    //电商ID
    @Value("${kdNiao.eBusinessID}")
    private String EBUSINESSID;

    //电商加密私钥，快递鸟提供，注意保管，不要泄漏
    @Value("${kdNiao.appKey}")
    private String APPKEY;

    //获取物流轨迹url
    @Value("${kdNiao.queryUrl}")
    private String queryUrl;

    //获取快递公司编码url
    @Value("${kdNiao.getCodeUrl}")
    private String getCodeUrl;

    @Override
    public List<ShippingTrace> findTraces(String code, String formid) throws CoBusinessException,Exception {
        String requestData = "{'OrderCode':'','ShipperCode':'" + code + "','LogisticCode':'" + formid + "'}";

        Map<String, String> params = new HashMap<>();
        params.put("RequestData", KdniaoTrackQueryAPI.urlEncoder(requestData, "UTF-8"));
        params.put("EBusinessID", EBUSINESSID);
        params.put("RequestType", "1002");
        String dataSign = KdniaoTrackQueryAPI.encrypt(requestData, APPKEY, "UTF-8");
        params.put("DataSign", KdniaoTrackQueryAPI.urlEncoder(dataSign, "UTF-8"));
        params.put("DataType", "2");
        String result = KdniaoTrackQueryAPI.sendPost(queryUrl, params);
        JSONObject obj = JSON.parseObject(result);
        if (obj != null) {
            List<ShippingTrace> shippingTraceList = new ArrayList<>();
            JSONArray array = obj.getJSONArray("Traces");
            if (array != null && array.size() > 0) {
                for (int i = 0; i < array.size(); i++) {
                    ShippingTrace trace = new ShippingTrace();
                    trace.setAcceptTime(array.getJSONObject(i).getString("AcceptTime"));
                    trace.setAcceptStation(array.getJSONObject(i).getString("AcceptStation"));
                    shippingTraceList.add(trace);
                }
            }
            return shippingTraceList;
        }
        return null;
    }

    @Override
    public List<KuaiDiNiaoCode> findCode(String formid) throws CoBusinessException, Exception {
        String requestData= "{'OrderCode':'','LogisticCode':'" + formid + "'}";

        Map<String, String> params = new HashMap<String, String>();
        params.put("RequestData", KdniaoTrackQueryAPI.urlEncoder(requestData, "UTF-8"));
        params.put("EBusinessID", EBUSINESSID);
        params.put("RequestType", "2002");
        String dataSign=KdniaoTrackQueryAPI.encrypt(requestData, APPKEY, "UTF-8");
        params.put("DataSign", KdniaoTrackQueryAPI.urlEncoder(dataSign, "UTF-8"));
        params.put("DataType", "2");
        String result=KdniaoTrackQueryAPI.sendPost(queryUrl, params);
        //根据公司业务处理返回的信息......
        JSONObject jsonObject = JSON.parseObject(result);
        boolean success = (boolean) jsonObject.get("Success");
        if(success) {
            String shippers = jsonObject.getString("Shippers");
            List<KuaiDiNiaoCode> list = JSONArray.parseArray(shippers, KuaiDiNiaoCode.class);
            return list;
        }
        return null;
    }
}
