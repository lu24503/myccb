/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.dict;

import com.shop.cereshop.commons.domain.dict.CerePlatformDict;
import com.shop.cereshop.commons.exception.CoBusinessException;

import java.util.List;
import java.util.Map;

public interface CerePlatformDictService {

    List<CerePlatformDict> getExpressSelect() throws CoBusinessException;

    List<String> getReasonSelect() throws CoBusinessException;

    String findByCompany(String express, Long dictPid);

    CerePlatformDict getByName(String name);

    Map<String, CerePlatformDict> getDictMap(List<String> nameList);

    List<CerePlatformDict> getSelect(String dictName);

    Map<Long, String> selectDictNameMap(List<Long> dictIdList);
}
