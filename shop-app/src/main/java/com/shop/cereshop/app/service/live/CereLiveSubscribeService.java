/*
* Copyright (C) 2017-2021
* All rights reserved, Designed By 深圳中科鑫智科技有限公司
* Copyright authorization contact 18814114118
*/
package com.shop.cereshop.app.service.live;

/**
 * <p>
 * 业务接口
 * 直播订阅表
 * </p>
 *
 * @author JustArgo
 * @date 2021-12-25
 */
public interface CereLiveSubscribeService {

    boolean subscribe(Long buyerUserId, Long liveId);

    void sendNotice(Long valueOf);
}
