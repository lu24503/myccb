/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.message.impl;

import com.shop.cereshop.app.dao.message.CereMessageLogDAO;
import com.shop.cereshop.app.service.message.CereMessageLogService;
import com.shop.cereshop.commons.constant.IntegerEnum;
import com.shop.cereshop.commons.constant.StringEnum;
import com.shop.cereshop.commons.domain.message.CereMessageLog;
import com.shop.cereshop.commons.domain.message.CerePlatfromMessage;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.utils.EmptyUtils;
import com.shop.cereshop.commons.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Map;

@Service
public class CereMessageLogServiceImpl implements CereMessageLogService {

    @Autowired
    private CereMessageLogDAO cereMessageLogDAO;

    @Override
    @Transactional(isolation= Isolation.DEFAULT,propagation= Propagation.REQUIRED,rollbackFor = {CoBusinessException.class, Exception.class})
    public void save(String phone, String template, Map<String,String> map, String subject, String resultCode) throws CoBusinessException {
        String time = TimeUtils.yyMMddHHmmss();
        CereMessageLog log=new CereMessageLog();
        log.setCreateTime(time);
        log.setPhone(phone);
        log.setTemplate(template);
        log.setSubject(subject);
        if(StringEnum.MESSAGE_RESULT_CODE.getCode().equals(resultCode)){
            //发送成功
            log.setState(IntegerEnum.MESSAGE_SUCCESS.getCode());
        }else {
            //发送失败
            log.setState(IntegerEnum.MESSAGE_ERROR.getCode());
        }
        //根据模板编码查询短信所属信息
        CerePlatfromMessage message=cereMessageLogDAO.findMessage(template);
        if(message!=null){
            log.setMessageId(message.getMessageId());
            log.setMessage(message.getTemplateDescribe());
            //设置内容,替换占位符
            if(!EmptyUtils.isEmpty(map)){
                if(message.getTemplateDescribe().contains("{")){
                    String format = MessageFormat.format(message.getTemplateDescribe(), map);
                    log.setMessage(format);
                }
            }
            cereMessageLogDAO.insert(log);
        }
    }
}
