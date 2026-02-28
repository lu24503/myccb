/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.message;

import com.shop.cereshop.app.param.message.MessageGetAllParam;
import com.shop.cereshop.commons.domain.message.CerePlatfromMessage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CerePlatfromMessageDAO extends BaseMapper<CerePlatfromMessage> {
    int deleteByPrimaryKey(Long messageId);

    int insertSelective(CerePlatfromMessage record);

    CerePlatfromMessage selectByPrimaryKey(Long messageId);

    int updateByPrimaryKeySelective(CerePlatfromMessage record);

    int updateByPrimaryKey(CerePlatfromMessage record);

    CerePlatfromMessage getById(@Param("messageId") Long messageId);

    List<CerePlatfromMessage> getAll(MessageGetAllParam param);
}
