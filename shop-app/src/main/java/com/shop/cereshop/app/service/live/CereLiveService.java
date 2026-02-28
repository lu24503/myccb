package com.shop.cereshop.app.service.live;

import com.shop.cereshop.app.page.live.CereLivePage;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.common.PageParam;

public interface CereLiveService {

    Page<CereLivePage> selectLiveList(Long buyerUserId, PageParam param);

}
