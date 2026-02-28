/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.compose.impl;

import com.shop.cereshop.app.dao.compose.CereComposeProductDAO;
import com.shop.cereshop.app.dao.compose.CereShopComposeDAO;
import com.shop.cereshop.app.dao.product.CereProductImageDAO;
import com.shop.cereshop.app.dao.product.CereProductSkuDAO;
import com.shop.cereshop.app.dao.product.CereShopProductDAO;
import com.shop.cereshop.app.param.compose.CereShopComposeDTO;
import com.shop.cereshop.app.service.compose.CereShopComposeService;
import com.shop.cereshop.commons.domain.product.CereProductImage;
import com.shop.cereshop.commons.domain.product.CereShopProduct;
import com.shop.cereshop.commons.domain.tool.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CereShopComposeServiceImpl implements CereShopComposeService {

    @Autowired
    private CereShopComposeDAO cereShopComposeDAO;

    @Autowired
    private CereComposeProductDAO cereComposeProductDAO;

    @Autowired
    private CereShopProductDAO cereShopProductDAO;

    @Autowired
    private CereProductImageDAO cereProductImageDAO;

    @Autowired
    private CereProductSkuDAO cereProductSkuDAO;

    @Override
    public CereShopCompose selectOnGoingByComposeId(Long composeId) {
        return cereShopComposeDAO.selectOnGoingByComposeId(composeId);
    }

    @Override
    public List<CereComposeDetail> selectComposeByProductId(Long productId) {
        List<Long> composeIdList =  cereShopComposeDAO.selectComposeIdListContainsProductId(productId);
        if (CollectionUtils.isEmpty(composeIdList)) {
            return Collections.emptyList();
        }
        List<CereComposeDetail> detailList = new ArrayList<>();
        List<CereShopCompose> composeList = cereShopComposeDAO.selectByComposeIdList(composeIdList);
        for (CereShopCompose compose:composeList) {
            CereComposeDetail detail = new CereComposeDetail();
            detail.setComposeId(compose.getComposeId());
            detail.setComposeName(compose.getComposeName());
            detail.setComposeType(compose.getComposeType());
            detail.setPromote(compose.getPromote());
            List<Long> productIdList = cereComposeProductDAO.selectByComposeId(compose.getComposeId());
            List<CereComposeProductInfo> composeProductInfoList = new ArrayList<>();
            for (Long pId:productIdList) {
                CereShopProduct cereShopProduct = cereShopProductDAO.selectByPrimaryKey(pId);
                List<CereProductImage> productImageList = cereProductImageDAO.selectByProductId(pId);
                CereComposeProductInfo productInfo = new CereComposeProductInfo();
                productInfo.setProductId(pId);
                productInfo.setProductName(cereShopProduct.getProductName());
                if (CollectionUtils.isNotEmpty(productImageList)) {
                    productInfo.setProductImage(productImageList.get(0).getProductImage());
                }
                //查询商品的sku列表信息
                List<CereComposeSkuInfo> skuInfoList = cereProductSkuDAO.selectSkuInfo(pId);
                productInfo.setComposeSkuInfoList(skuInfoList);

                composeProductInfoList.add(productInfo);
                detail.setComposeProductInfoList(composeProductInfoList);
            }
            //detail.setComposeSkuList();
            detailList.add(detail);
        }
        return detailList;
    }

    @Override
    public List<CereShopComposeDTO> selectByShopIdList(List<Long> shopIdList) {
        return cereShopComposeDAO.selectByShopIdList(shopIdList);
    }

    @Override
    public List<CereComposeMergeInfo> selectComposeByShopId(Long shopId) {
        List<CereComposeMergeInfo> result = new ArrayList<>();
        List<CereShopComposeDTO> composeList = selectByShopIdList(Collections.singletonList(shopId));
        if (composeList != null) {
            Map<Long, List<CereShopComposeDTO>> map = composeList.stream().collect(Collectors.groupingBy(CereShopComposeDTO::getComposeId));
            for(Map.Entry<Long, List<CereShopComposeDTO>> entry:map.entrySet()) {
                Long composeId = entry.getKey();
                List<Long> productIdList = entry.getValue().stream().map(CereShopComposeDTO::getProductId).collect(Collectors.toList());
                String composeName = entry.getValue().get(0).getComposeName();
                Integer composeType = entry.getValue().get(0).getComposeType();
                BigDecimal promote = entry.getValue().get(0).getPromote();
                CereComposeMergeInfo info = new CereComposeMergeInfo();
                info.setComposeId(composeId);
                info.setComposeName(composeName);
                info.setComposeType(composeType);
                info.setPromote(promote);
                info.setProductIdList(productIdList);
                result.add(info);
            }
        }
        return result;
    }
}
