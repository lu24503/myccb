/*
* Copyright (C) 2017-2021
* All rights reserved, Designed By 深圳中科鑫智科技有限公司
* Copyright authorization contact 18814114118
*/
package com.shop.cereshop.app.dao.advert;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.cereshop.commons.domain.advert.CerePopupAdvert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * 弹窗广告表
 * </p>
 *
 * @author JustArgo
 * @date 2021-12-04
 */
@Mapper
public interface CerePopupAdvertDAO extends BaseMapper<CerePopupAdvert> {

    List<CerePopupAdvert> selectByCondition(@Param("buyerUserId") Long buyerUserId,
                                            @Param("triggerCondition")Integer triggerCondition,
                                            @Param("startTime")String startTime,
                                            @Param("endTime") String endTime);

}
