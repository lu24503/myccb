/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.member;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.cereshop.commons.domain.member.CereMemberSigninRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CereMemberSigninRecordDAO extends BaseMapper<CereMemberSigninRecord> {

    List<CereMemberSigninRecord> selectSigninRecordList(@Param("buyerUserId")Long buyerUserId);

}
