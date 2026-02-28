/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.distributor.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.google.gson.Gson;
import com.shop.cereshop.app.alioss.service.UploadService;
import com.shop.cereshop.app.dao.distributor.CereDistributionOrderDAO;
import com.shop.cereshop.app.page.distributor.*;
import com.shop.cereshop.app.page.shop.Extension;
import com.shop.cereshop.app.param.distributor.DistributorOrderParam;
import com.shop.cereshop.app.param.distributor.DistributorParam;
import com.shop.cereshop.app.param.extension.ExtensionParam;
import com.shop.cereshop.app.service.distributor.CereDistributionOrderService;
import com.shop.cereshop.app.service.distributor.CereShopDistributorService;
import com.shop.cereshop.app.service.shop.CereShopExtensionService;
import com.shop.cereshop.commons.constant.IntegerEnum;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.common.PageParam;
import com.shop.cereshop.commons.domain.distributor.CereDistributionOrder;
import com.shop.cereshop.commons.domain.distributor.CereShopDistributor;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.utils.AppletTokenResult;
import com.shop.cereshop.commons.utils.EmptyUtils;
import com.shop.cereshop.commons.utils.QRCodeUtil;
import com.shop.cereshop.commons.utils.RandomStringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class CereDistributionOrderServiceImpl implements CereDistributionOrderService {

    @Autowired
    private CereDistributionOrderDAO cereDistributionOrderDAO;

    @Autowired
    private CereShopExtensionService cereShopExtensionService;

    @Autowired
    private CereShopDistributorService cereShopDistributorService;

    @Autowired
    private UploadService uploadService;

    @Override
    public void insertBatch(List<CereDistributionOrder> distributionOrders) throws CoBusinessException {
        cereDistributionOrderDAO.insertBatch(distributionOrders);
    }

    @Override
    public Distributor getDistributor(Long buyerUserId) throws CoBusinessException {
        Distributor distributor=new Distributor();
        //根据手机号查询分销员数据
        List<CereShopDistributor> list=cereDistributionOrderDAO.findDistributorByUserId(buyerUserId);
        if(!EmptyUtils.isEmpty(list)){
            BigDecimal decimal=BigDecimal.ZERO;
            //查询已提现或审核中金额
            BigDecimal price=cereDistributionOrderDAO.findWithdrawalMoney(buyerUserId);
            for (CereShopDistributor cereShopDistributor : list) {
                //查询总提现金额
                BigDecimal total=cereDistributionOrderDAO.findTotalByDistributorId(cereShopDistributor.getDistributorId());
                //计算总提现金额
                decimal=decimal.add(total);
            }
            //计算可提现金额=总提现金额-已提现或审核中的金额
            decimal=decimal.subtract(price);
            distributor.setPrice(decimal);
        }
        //查询历史记录
        distributor.setWithdrawals(cereDistributionOrderDAO.findHistory(buyerUserId));
        return distributor;
    }

    @Override
    public Page getDistributorAll(PageParam param,Long buyerUserId) throws CoBusinessException {
        PageHelper.startPage(param.getPage(),param.getPageSize());
        List<DistributorShop> list=cereDistributionOrderDAO.getDistributorAll(buyerUserId);
        if(!EmptyUtils.isEmpty(list)){
            list.forEach(a -> {
                //查询总收益
                a.setPrice(cereDistributionOrderDAO.findTotalByShopIdAndDistributor(a));
            });
        }
        PageInfo<DistributorShop> pageInfo=new PageInfo<>(list);
        Page page=new Page(pageInfo.getList(),pageInfo.getTotal());
        return page;
    }

    @Override
    public ShopDis getShopDistributor(DistributorParam param, CereBuyerUser user) throws CoBusinessException {
        ShopDis shopDis=new ShopDis();
        shopDis.setName(user.getWechatName());
        shopDis.setHeadImage(user.getHeadImage());
        //查询累计奖励
        shopDis.setCumulative(cereDistributionOrderDAO.findCumulativeByDistributorId(param.getShopId(),param.getDistributorId(),null));
        //查询未结算奖励
        shopDis.setUnsettled(cereDistributionOrderDAO.findCumulativeByDistributorId(param.getShopId(),param.getDistributorId(),0));
        //查询累计客户
        shopDis.setUsers(cereDistributionOrderDAO.findUsers(param.getShopId(),param.getDistributorId()));
        //查询累计分销员
        shopDis.setDistributors(cereDistributionOrderDAO.findDistributors(param.getShopId(),param.getDistributorId()));
        return shopDis;
    }

    @Override
    public Extension getShopExtension(ExtensionParam extensionParam, CereBuyerUser user) throws CoBusinessException,Exception {
        //查询店铺推广设置
        Extension extension=cereShopExtensionService.findByShopIdAndTitle(extensionParam.getShopId(),extensionParam.getTitle());
        if(extension!=null){
            if(IntegerEnum.YES.getCode().equals(extension.getIfHead())){
                //设置客户姓名头像
                extension.setName(user.getWechatName());
                extension.setHeadImage(user.getHeadImage());
            }
            //查询分销员邀请码
            extension.setInvitationCode(cereShopDistributorService.findInvitationCode(extensionParam.getDistributorId()));
            extension.setDistributorId(extensionParam.getDistributorId());
            //生成小程序码
//            RestTemplate rest = new RestTemplate();
//            InputStream inputStream = null;
//            AppletTokenResult appletTokenResult = QRCodeUtil.getAppletToken("");
//            if(appletTokenResult == null || StringUtil.isEmpty(appletTokenResult.getAccess_token())){
////                throw new CommonException("小程序token获取失败！");
//            }
//            String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token="
//                    +appletTokenResult.getAccess_token();
//            Map<String, Object> param= new HashMap<String, Object>();
//            String urlString = URLEncoder.encode("=", "GBK");
//            param.put("scene", extensionParam.getShopId()+"-"+extensionParam.getDistributorId()+"-"+extension.getInvitationCode());
//            param.put("page", "https://shopapi.zkthink.com/#/pages/store/index?shopId='+"+extensionParam.getShopId()
//                    +"'&salesId='"+extensionParam.getDistributorId()+"'");
//            param.put("width", 430);
//            param.put("auto_color", false);
//            Map<String,Object> line_color = new HashMap<>();
//            line_color.put("r", 0);
//            line_color.put("g", 0);
//            line_color.put("b", 0);
//            param.put("line_color", line_color);
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//            HttpEntity<String> requestEntity = new HttpEntity<String>(new Gson().toJson(param), headers);
//            ResponseEntity<byte[]> entity = rest.exchange(url, HttpMethod.POST,
//                    requestEntity, byte[].class, new Object[0]);
//            byte[] result = entity.getBody();
//            inputStream = new ByteArrayInputStream(result);
//            byte[] bytes = QRCodeUtil.toByteArray(inputStream);
//            String qrcode = uploadService.uploadByte("店铺推广", bytes);
//            extension.setPoster(qrcode);
        }
        return extension;
    }

    @Override
    public Page getDistributorOrderByShopId(DistributorOrderParam param) throws CoBusinessException {
        PageHelper.startPage(param.getPage(),param.getPageSize());
        List<DistributorOrder> list=cereDistributionOrderDAO.getDistributorOrderByShopId(param);
        PageInfo<DistributorOrder> pageInfo=new PageInfo<>(list);
        Page page=new Page(pageInfo.getList(),pageInfo.getTotal());
        return page;
    }

    @Override
    public Page getDistributors(DistributorOrderParam param, CereBuyerUser user) throws CoBusinessException {
        PageHelper.startPage(param.getPage(),param.getPageSize());
        List<Long> subDistributorList = cereDistributionOrderDAO.getSubDistributors(param);
        PageInfo pageInfo = new PageInfo(subDistributorList);
        Page page = new Page(Collections.emptyList(),0);
        if (subDistributorList.size() > 0) {
            //因为分销员id都是和店铺绑定的，所以这里参数不用再传shopId
            List<CumulativeDistributor> list = cereDistributionOrderDAO.getDistributors(param.getDistributorId(), subDistributorList);
            page=new Page(list, pageInfo.getTotal());
        }
        return page;
    }

    @Override
    public Reward getReward(DistributorOrderParam param, CereBuyerUser user) throws CoBusinessException {
        Reward reward=new Reward();
        PageHelper.startPage(param.getPage(),param.getPageSize());
        List<CumulativeReward> list=cereDistributionOrderDAO.getReward(param);
        PageInfo<CumulativeReward> pageInfo=new PageInfo<>(list);
        Page page=new Page(pageInfo.getList(),pageInfo.getTotal());
        reward.setPage(page);
        //查询累计奖励
        //TODO
        reward.setTotal(cereDistributionOrderDAO.findCumulativeByDistributorId(param.getShopId(),param.getDistributorId(),null));
        //查询直接奖励
        reward.setDirectPrice(cereDistributionOrderDAO.findPriceByShopIdAndType(param.getShopId(),param.getDistributorId(),IntegerEnum.DIRECT_TYPE.getCode()));
        //查询间接奖励
        reward.setIndirectPrice(cereDistributionOrderDAO.findPriceByShopIdAndType(param.getShopId(),param.getDistributorId(),IntegerEnum.IN_DIRECT_TYPE.getCode()));
        return reward;
    }

    @Override
    public Reward getNotReward(DistributorOrderParam param, CereBuyerUser user) throws CoBusinessException {
        Reward reward=new Reward();
        PageHelper.startPage(param.getPage(),param.getPageSize());
        List<CumulativeReward> list=cereDistributionOrderDAO.getNotReward(param);
        PageInfo<CumulativeReward> pageInfo=new PageInfo<>(list);
        Page page=new Page(pageInfo.getList(),pageInfo.getTotal());
        reward.setPage(page);
        //查询累计未结算奖励
        //TODO
        reward.setTotal(cereDistributionOrderDAO.findCumulativeByDistributorId(param.getShopId(),param.getDistributorId(),0));
        //查询未结算直接奖励
        reward.setDirectPrice(cereDistributionOrderDAO.findPriceByShopIdAndTypeAndState(
                param.getShopId(),param.getDistributorId(),IntegerEnum.DIRECT_TYPE.getCode()));
        //查询未结算间接奖励
        reward.setIndirectPrice(cereDistributionOrderDAO.findPriceByShopIdAndTypeAndState(
                param.getShopId(),param.getDistributorId(),IntegerEnum.IN_DIRECT_TYPE.getCode()));
        return reward;
    }

    @Override
    public Page getBuyers(DistributorParam param, CereBuyerUser user) throws CoBusinessException {
        PageHelper.startPage(param.getPage(),param.getPageSize());
        List<Long> buyerIdList = cereDistributionOrderDAO.getBuyersIdList(param);
        PageInfo pageInfo = new PageInfo(buyerIdList);
        Page page = new Page(Collections.emptyList(), 0);
        if (buyerIdList.size() > 0) {
            List<CumulativeBuyer> buyerList = cereDistributionOrderDAO.getBuyers(param.getShopId(), buyerIdList);
            page = new Page(buyerList, pageInfo.getTotal());
        }
        return page;
    }

    @Override
    public void settleOrder(long orderId) {
        cereDistributionOrderDAO.settleOrder(orderId);
    }
}
