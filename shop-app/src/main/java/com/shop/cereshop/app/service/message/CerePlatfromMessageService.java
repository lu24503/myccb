/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.message;

import com.shop.cereshop.app.param.message.MessageDeleteParam;
import com.shop.cereshop.app.param.message.MessageGetAllParam;
import com.shop.cereshop.app.param.message.MessageSaveParam;
import com.shop.cereshop.app.param.message.MessageUpdateParam;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.message.CerePlatfromMessage;
import com.shop.cereshop.commons.exception.CoBusinessException;

public interface CerePlatfromMessageService {
    void save(MessageSaveParam param, CereBuyerUser user) throws CoBusinessException;

    void update(MessageUpdateParam param, CereBuyerUser user) throws CoBusinessException;

    void delete(MessageDeleteParam param, CereBuyerUser user) throws CoBusinessException;

    CerePlatfromMessage getById(Long messageId) throws CoBusinessException;

    Page getAll(MessageGetAllParam param) throws CoBusinessException;
}
