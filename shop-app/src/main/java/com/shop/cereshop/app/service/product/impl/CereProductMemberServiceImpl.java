/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.product.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.cereshop.app.dao.product.CereProductMemberDAO;
import com.shop.cereshop.app.page.MemberProduct;
import com.shop.cereshop.app.page.index.Product;
import com.shop.cereshop.app.page.product.ProductDetail;
import com.shop.cereshop.app.param.member.MemberProductPageParam;
import com.shop.cereshop.app.service.member.CerePlatformMemberLevelService;
import com.shop.cereshop.app.service.product.CereProductMemberService;
import com.shop.cereshop.commons.constant.IntegerEnum;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.member.CerePlatformMemberLevel;
import com.shop.cereshop.commons.domain.product.CereProductMember;
import com.shop.cereshop.commons.domain.product.Sku;
import com.shop.cereshop.commons.utils.EmptyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CereProductMemberServiceImpl implements CereProductMemberService {

    @Autowired
    private CereProductMemberDAO cereProductMemberDAO;

    @Autowired
    private CerePlatformMemberLevelService cerePlatformMemberLevelService;

    @Override
    public Page getMemberProducts(MemberProductPageParam param) {
        PageHelper.startPage(param.getPage(),param.getPageSize());
        List<MemberProduct> list=cereProductMemberDAO.getMemberProducts(param);
        if(!EmptyUtils.isEmpty(list) && param.getMemberLevelId() != null){
            list.forEach(product -> {
                //设置会员价格
                if(IntegerEnum.MEMBER_PRODUCT_MODE_DISCOUNT.getCode().equals(product.getMode())){
                    //如果是折扣，计算会员价
                    BigDecimal discount=product.getDiscount().divide(BigDecimal.TEN,2,BigDecimal.ROUND_HALF_UP);
                    product.setPrice(product.getPrice().multiply(discount).setScale(2,BigDecimal.ROUND_HALF_UP));
                }else {
                    product.setPrice(product.getDiscount());
                }
            });
        }
        PageInfo<MemberProduct> pageInfo=new PageInfo<>(list);
        Page page=new Page(pageInfo.getList(),pageInfo.getTotal());
        return page;
    }

    @Override
    public CereProductMember selectProductMember(Long memberLevelId, Long productId, Long skuId) {
        return cereProductMemberDAO.selectProductMember(memberLevelId, productId, skuId);
    }

    @Override
    public ProductDetail setActivityInfo(CereBuyerUser user, ProductDetail detail) {
        Long memberLevelId = null;
        if (user != null && user.getMemberLevelId() != null) {
            memberLevelId = user.getMemberLevelId();
        }
        if (memberLevelId == null) {
            CerePlatformMemberLevel firstLevel = cerePlatformMemberLevelService.selectFirstLevel();
            if (firstLevel != null) {
                memberLevelId = firstLevel.getMemberLevelId();
            }
        }
        // 如果还没有初始化会员等级信息 或者sku信息为空，直接返回商品详情
        if (memberLevelId == null || detail.getMap() == null) {
            return detail;
        }

        boolean anyMatched = false;
        for (Sku sku:detail.getMap().values()) {
            CereProductMember productMember = cereProductMemberDAO.selectProductMember(memberLevelId, detail.getProductId(), sku.getSkuId());
            if (productMember != null) {
                sku.setActivityType(IntegerEnum.ACTIVITY_TYPE_MEMBER.getCode());
                int mode = productMember.getMode();
                BigDecimal originalPrice = sku.getPrice();
                BigDecimal price = productMember.getPrice();
                sku.setOriginalPrice(originalPrice);
                if (IntegerEnum.MEMBER_PRODUCT_MODE_DISCOUNT.getCode().equals(mode)) {
                    sku.setPrice(sku.getPrice().multiply(price).divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_UP));
                } else {
                    sku.setPrice(price);
                }
                if (sku.getSkuId().equals(detail.getSkuId())) {
                    detail.setOriginalPrice(sku.getOriginalPrice());
                    detail.setPrice(sku.getPrice());
                }
                anyMatched = true;
            }
        }
        if (anyMatched) {
            detail.setActivityType(IntegerEnum.ACTIVITY_TYPE_MEMBER.getCode());
            detail.setActivityName(IntegerEnum.ACTIVITY_TYPE_MEMBER.getName());
        }
        return detail;
    }

    @Override
    public void batchSetActivityInfo(CereBuyerUser user, List<Product> detailList) {
        if (CollectionUtils.isEmpty(detailList)) {
            return;
        }
        Long memberLevelId = null;
        if (user != null && user.getMemberLevelId() != null) {
            memberLevelId = user.getMemberLevelId();
        }
        if (memberLevelId == null) {
            CerePlatformMemberLevel firstLevel = cerePlatformMemberLevelService.selectFirstLevel();
            if (firstLevel != null) {
                memberLevelId = firstLevel.getMemberLevelId();
            }
        }
        // 如果还没有初始化会员等级信息 或者sku信息为空，直接返回商品详情
        if (memberLevelId == null) {
            return;
        }

        List<Long> skuIdList = detailList.stream().map(Product::getSkuId).collect(Collectors.toList());
        List<CereProductMember> productMemberList = cereProductMemberDAO.selectBatchProductMember(memberLevelId, skuIdList);
        Map<Long, CereProductMember> memberMap = productMemberList.stream().collect(Collectors.toMap(CereProductMember::getSkuId, Function.identity()));
        for (Product product:detailList) {
            CereProductMember member = memberMap.get(product.getSkuId());
            if (member != null) {
                product.setActivityType(IntegerEnum.ACTIVITY_TYPE_MEMBER.getCode());
                int mode = member.getMode();
                BigDecimal price = member.getPrice();
                product.setOriginalPrice(product.getPrice());
                if (IntegerEnum.MEMBER_PRODUCT_MODE_DISCOUNT.getCode().equals(mode)) {
                    product.setPrice(product.getPrice().multiply(price).divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_UP));
                } else {
                    product.setPrice(price);
                }

            }
        }
    }
}
