/*
* Copyright (C) 2017-2021
* All rights reserved, Designed By 深圳中科鑫智科技有限公司
* Copyright authorization contact 18814114118
*/
package com.shop.cereshop.app.service.advert;

import com.shop.cereshop.app.param.advert.PopupAdvertParam;
import com.shop.cereshop.commons.domain.advert.CereCloseAdvert;

import java.util.List;

/**
 * <p>
 * 业务接口
 * 弹窗广告表
 * </p>
 *
 * @author JustArgo
 * @date 2021-12-04
 */
public interface CerePopupAdvertService {

    List<PopupAdvertParam> selectByCondition(Long buyerUserId, Integer triggerCondition);

    Integer closeAdvert(CereCloseAdvert param);
}
