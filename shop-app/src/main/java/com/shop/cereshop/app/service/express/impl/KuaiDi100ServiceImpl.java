/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.express.impl;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.shop.cereshop.app.service.express.KuaiDi100Service;
import com.shop.cereshop.commons.domain.express.*;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.utils.HttpUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 快递100
 * @author wangsong
 * @mail 1720696548@qq.com
 * @date 2020/9/16 0016 10:27
 * @version 1.0.0
 */
@Service
public class KuaiDi100ServiceImpl implements KuaiDi100Service {

    //授权码
    @Value("${kd100.key}")
    private String key;

    //公司编号
    @Value("${kd100.customer}")
    private String customer;

    //获取物流轨迹url
    @Value("${kd100.queryUrl}")
    private String queryUrl;

    @Override
    public List<ShippingTrace> findTraces(String code, String formid) throws CoBusinessException,Exception{
        QueryTrackReq queryTrackReq = new QueryTrackReq();
        QueryTrackParam queryTrackParam = new QueryTrackParam();
        queryTrackParam.setCom(code);
        queryTrackParam.setNum(formid);
        queryTrackReq.setParam(queryTrackParam);
        queryTrackReq.setCustomer(customer);
        queryTrackReq.setSign(DigestUtils.md5Hex(new Gson().toJson(queryTrackParam)+key+customer).toUpperCase());
        HttpResult httpResult = HttpUtils.doPost(queryUrl, queryTrackReq);
        if (httpResult.getStatus() == 200 && StringUtils.isNotBlank(httpResult.getBody())){
            QueryTrackResp queryTrackResp = JSON.parseObject(httpResult.getBody(), QueryTrackResp.class);
            ShippingDTO shippingDTO = new ShippingDTO();
            shippingDTO.setShipperCode(code);
            shippingDTO.setLogisticCode(formid);
            if (queryTrackResp != null && queryTrackResp.getData() != null) {
                List<ShippingTrace> shippingTraceList = new ArrayList();
                for(QueryTrackData queryTrackData:queryTrackResp.getData()){
                    ShippingTrace trace = new ShippingTrace();
                    trace.setAcceptTime(queryTrackData.getTime());
                    trace.setAcceptStation(queryTrackData.getContext());
                    shippingTraceList.add(trace);
                }
                shippingDTO.setTraces(shippingTraceList);
                return shippingDTO.getTraces();
            }
        }
        return new ShippingDTO().getTraces();
    }
}
