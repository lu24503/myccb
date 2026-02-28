/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.dao.groupwork;

import com.shop.cereshop.app.page.product.BroadCastDTO;
import com.shop.cereshop.commons.domain.collage.CereCollageOrderDetail;
import com.shop.cereshop.commons.domain.collage.CollageOrder;
import com.shop.cereshop.app.page.order.CollageDetail;
import com.shop.cereshop.app.page.order.CollagePerson;
import com.shop.cereshop.commons.domain.collage.CereCollageOrder;
import com.shop.cereshop.commons.domain.order.CereShopOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CereCollageOrderDAO extends BaseMapper<CereCollageOrder> {
    int deleteByPrimaryKey(Long collageId);

    int insertSelective(CereCollageOrder record);

    CereCollageOrder selectByPrimaryKey(Long collageId);

    int updateByPrimaryKeySelective(CereCollageOrder record);

    int updateByPrimaryKey(CereCollageOrder record);

    List<CollageOrder> findCollageOrders(@Param("shopGroupWorkId") Long shopGroupWorkId,
                                         @Param("buyerUserId") Long buyerUserId,@Param("productId") Long productId);

    int findSpelled(@Param("collageId") Long collageId);

    CereCollageOrder findByOrder(CereShopOrder order);

    int findSurplusNumber(@Param("collageId") Long collageId);

    List<Long> findNotPayCollageOrderIds(@Param("collageId") Long collageId,@Param("orderId") Long orderId);

    List<Long> findOrderIds(@Param("collageId") Long collageId);

    CollageDetail findDetail(@Param("shopGroupWorkId") Long shopGroupWorkId,@Param("orderId") Long orderId);

    List<CollagePerson> findPersons(@Param("collageId") Long collageId);

    CereCollageOrder findByUserId(@Param("buyerUserId") Long buyerUserId,@Param("orderId") Long orderId);

    List<CereShopOrder> findPayCollageOrderIds(@Param("collageId") Long collageId,@Param("orderId") Long orderId);

    void updateBuyerData(@Param("buyerUserId") Long buyerUserId,@Param("id") Long id);

    List<BroadCastDTO> findRecentCollageOrderGoing(@Param("oneHourAgo") String oneHourAgo,
                                                   @Param("shopGroupWorkId") Long shopGroupWorkId,
                                                   @Param("productId") Long productId);

    List<BroadCastDTO> findRecentCollageOrderDone(@Param("oneHourAgo") String oneHourAgo,
                                                  @Param("shopGroupWorkId") Long shopGroupWorkId,
                                                  @Param("productId") Long productId);
}
