/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.message.service.miaoxin;

import java.util.Map;

public interface MiaoxinMessageService {

    /**
     *
     * @param phone 手机号
     * @param map 变量参数
     * @return
     * @throws Exception
     */
    public String sendNotice(String phone, Map<String, String> map) throws Exception;

}
