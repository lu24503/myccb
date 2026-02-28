/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.platformtool;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.cereshop.app.page.canvas.CanvasPlatformSeckill;
import com.shop.cereshop.app.page.seckill.PlatformSeckillProduct;
import com.shop.cereshop.app.page.tool.ToolProduct;
import com.shop.cereshop.app.param.canvas.RenovationParam;
import com.shop.cereshop.app.param.seckill.PlatformSeckillParam;
import com.shop.cereshop.commons.domain.buyer.CereBuyerPlatformSeckillVisit;
import com.shop.cereshop.commons.domain.permission.CerePlatformPermission;
import com.shop.cereshop.commons.domain.platformtool.CerePlatformSeckill;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CereBuyerPlatformSeckillVisitDAO extends BaseMapper<CereBuyerPlatformSeckillVisit> {

}
