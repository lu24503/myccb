/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.shop.cereshop.app.service.after.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.cereshop.app.dao.after.CereOrderAfterDAO;
import com.shop.cereshop.app.page.after.AfterDetail;
import com.shop.cereshop.app.page.after.AfterHistory;
import com.shop.cereshop.app.page.after.Afters;
import com.shop.cereshop.app.page.cart.CartSku;
import com.shop.cereshop.app.param.after.AfterGetAllParam;
import com.shop.cereshop.app.param.after.AfterGetByIdParam;
import com.shop.cereshop.app.param.after.AfterParam;
import com.shop.cereshop.app.param.after.PlatformParam;
import com.shop.cereshop.app.param.settlement.ProductSku;
import com.shop.cereshop.app.redis.service.api.StringRedisService;
import com.shop.cereshop.app.service.after.*;
import com.shop.cereshop.app.service.log.CerePlatformLogService;
import com.shop.cereshop.app.service.order.CereShopOrderService;
import com.shop.cereshop.app.service.redis.CereRedisKeyServcice;
import com.shop.cereshop.commons.constant.CoReturnFormat;
import com.shop.cereshop.commons.constant.IntegerEnum;
import com.shop.cereshop.commons.constant.StringEnum;
import com.shop.cereshop.commons.domain.after.CereAfterProduct;
import com.shop.cereshop.commons.domain.after.CereAfterProductAttribute;
import com.shop.cereshop.commons.domain.after.CereOrderAfter;
import com.shop.cereshop.commons.domain.after.CerePlatformAfter;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.common.Page;
import com.shop.cereshop.commons.domain.order.CereShopOrder;
import com.shop.cereshop.commons.exception.CoBusinessException;
import com.shop.cereshop.commons.utils.EmptyUtils;
import com.shop.cereshop.commons.utils.RandomStringUtil;
import com.shop.cereshop.commons.utils.TimeUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CereOrderAfterServiceImpl implements CereOrderAfterService
{

    @Autowired
    private CereOrderAfterDAO cereOrderAfterDAO;

    @Autowired
    private CereAfterProductService cereAfterProductService;

    @Autowired
    private CereAfterProductAttributeService cereAfterProductAttributeService;

    @Autowired
    private CerePlatformLogService cerePlatformLogService;

    @Autowired
    private StringRedisService stringRedisService;

    @Autowired
    private CereShopOrderService cereShopOrderService;

    @Autowired
    private CereRedisKeyServcice cereRedisKeyServcice;

    @Autowired
    private CerePlatformAfterService cerePlatformAfterService;

    @Autowired
    private CereAfterDileverService cereAfterDileverService;

    public static void main(String[] args) {
        BigDecimal a = new BigDecimal(3.00);
        BigDecimal b = new BigDecimal(1.00);
        System.out.println(b.divide(a,4, BigDecimal.ROUND_UP));
    }
    @Override
    @Transactional(isolation= Isolation.DEFAULT,propagation= Propagation.REQUIRED,rollbackFor = {CoBusinessException.class, Exception.class})
    public void submit(AfterParam param, CereBuyerUser user) throws CoBusinessException,Exception {
        String time = TimeUtils.yyMMddHHmmss();
        //查询订单信息
        CereShopOrder cereShopOrder=cereShopOrderService.findById(param.getOrderId());
        if(cereShopOrder!=null){
            //设置协商历史描述
            String describe="";
            if(IntegerEnum.AFTER_REFUND.getCode().equals(param.getAfterType())){
                describe="买家发起了仅退款申请,退款商品:";
            }else {
                describe="买家发起了退货退款申请,退款商品:";
            }
            //将商品数量放到map中
            Map<Long, Integer> numberMap = param.getSkus().stream()
                    .collect(Collectors.toMap(ProductSku::getSkuId, ProductSku::getNumber));
            //新增售后单数据
            CereOrderAfter cereOrderAfter=new CereOrderAfter();
            BeanUtils.copyProperties(param,cereOrderAfter);
            cereOrderAfter.setAfterFormid("SH"+RandomStringUtil.getRandomCode(15,0));
            cereOrderAfter.setAfterState(IntegerEnum.AFTER_STAY.getCode());
            cereOrderAfter.setCreateTime(time);
            cereOrderAfter.setUpdateTime(time);

            //查询该订单是否有售后中的商品
            List<CereAfterProduct> afterProductList = cereAfterProductService.findByOrderIdForCheck(param.getOrderId());
            List<Long> oldSkuIdList = afterProductList.stream().map(CereAfterProduct::getSkuId).collect(Collectors.toList());
            for (Long skuId:numberMap.keySet()) {
                if (oldSkuIdList.contains(skuId)) {
                    throw new CoBusinessException("商品售后中或售后结束，请勿重复申请");
                }
            }

            //查询规格信息数据
            List<CereAfterProduct> skus=cereAfterProductService.findSkuBySkus(param);
            //计算售后金额
            cereOrderAfter.setPrice(param.getPrice());
            cereOrderAfter.setReturnReason(param.getReturnReason());
            cereOrderAfterDAO.insert(cereOrderAfter);
            if(!EmptyUtils.isEmpty(numberMap)) {
                //插入售后商品明细数据
                if(!EmptyUtils.isEmpty(skus)){
                    List<CereAfterProductAttribute> attributes=new ArrayList<>();
                    for (CereAfterProduct afterProduct:skus) {
                        describe+=afterProduct.getProductName()+",";
                        afterProduct.setAfterId(cereOrderAfter.getAfterId());
                        afterProduct.setNumber(numberMap.get(afterProduct.getSkuId()));
                        cereAfterProductService.insert(afterProduct);
                        //查询规格值属性数据
                        List<CereAfterProductAttribute> list=cereAfterProductService.findValuesBySkuId(afterProduct.getSkuId());
                        if(!EmptyUtils.isEmpty(list)){
                            list.forEach(a -> {
                                a.setAfterProductId(afterProduct.getAfterProductId());
                                attributes.add(a);
                            });
                        }
                    }
                    //插入规格值属性数据
                    cereAfterProductAttributeService.insertBatch(attributes);
                }
            }
            if(EmptyUtils.isEmpty(param.getReturnReason())){
                param.setReturnReason("无");
            }
            if(IntegerEnum.YES.getCode().equals(param.getGoodsState())){
                describe+="货物状态：收到货,原因："+param.getReturnReason()+",金额："+param.getPrice()+"元";
            }else {
                describe+="货物状态：未收到货,原因："+param.getReturnReason()+",金额："+param.getPrice()+"元";
            }
            if(!EmptyUtils.isEmpty(stringRedisService.get(StringEnum.ORDER_CONFIRM_DILEVERY.getCode()+"-"+param.getOrderId()))){
                //暂停自动确认收货定时任务
                stringRedisService.delete(StringEnum.ORDER_CONFIRM_DILEVERY.getCode()+"-"+param.getOrderId());
            }

            //同步更新订单售后单状态为售后中
            /*cereShopOrder.setUpdateTime(time);
            cereShopOrder.setOrderId(param.getOrderId());
            cereShopOrder.setAfterState(IntegerEnum.AFTER_STAY.getCode());
            cereShopOrderService.update(cereShopOrder);*/

            //新增延时任务,如果3天后未处理自动关闭售后单
            stringRedisService.set(StringEnum.AFTER_CANCEL.getCode()+"-"+cereOrderAfter.getAfterId(),1,
                    TimeUtils.getCountDownByTime(time,TimeUtils.getMoreDayAfter(time,3)));
            //新增延时任务记录
            cereRedisKeyServcice.add(StringEnum.AFTER_CANCEL.getCode()+"-"+cereOrderAfter.getAfterId(),TimeUtils.getMoreDayAfter(time,3));
            //新增日志
            cerePlatformLogService.addLog(user,"售后管理","客户端操作",
                    describe,cereOrderAfter.getAfterId(),time);
        }
    }

    @Override
    public BigDecimal getReturnPrice(AfterParam param, CereBuyerUser user) throws Exception {
        String time = TimeUtils.yyMMddHHmmss();
        //查询订单信息
        CereShopOrder cereShopOrder=cereShopOrderService.findById(param.getOrderId());
        if(cereShopOrder==null){
            throw new CoBusinessException(CoReturnFormat.AFTER_NOT_RETURN);
        }
        //改价过的商品，这两个价格可能不一样，页面不允许拆开售后
        if (!cereShopOrder.getOrderPrice().equals(cereShopOrder.getPrice())) {
            return cereShopOrder.getPrice();
        }
        //查询规格信息数据
        List<CereAfterProduct> skus=cereAfterProductService.findSkuBySkus(param);
        //计算售后金额
        if(param.getIsAllSelect().equals(IntegerEnum.NO.getCode())){
            //拆单
            //退款价格
            BigDecimal total=BigDecimal.ZERO;
            //将商品数量放到map中
            Map<Long, Integer> numberMap = param.getSkus().stream()
                    .collect(Collectors.toMap(ProductSku::getSkuId, ProductSku::getNumber));
            //获取商品叠加价格
            for (CereAfterProduct sku : skus) {
                //计算商品占比
                BigDecimal skuTotal=sku.getProductPrice().multiply(new BigDecimal(numberMap.get(sku.getSkuId())));
                total=total.add(skuTotal);
            }
            //退款订单占比
            BigDecimal refundedOrdersDivide = total.divide(cereShopOrder.getOrderPrice(),4, BigDecimal.ROUND_DOWN);
            if(!EmptyUtils.isEmptyBigdecimal(cereShopOrder.getDiscountPrice())) {
                //有优惠情况
                BigDecimal refundedDiscountPrice = cereShopOrder.getDiscountPrice().multiply(refundedOrdersDivide).setScale(2, BigDecimal.ROUND_UP);
                total= total.subtract(refundedDiscountPrice);
            }
            //实际退款金额=订单总金额-优惠
//                if(!EmptyUtils.isEmptyBigdecimal(cereShopOrder.getLogisticsPrice())){
//                    //有运费情况
//                    if(param.getAfterType()==IntegerEnum.YES.getCode()) {
//                        BigDecimal refundedLogisticsPrice = cereShopOrder.getLogisticsPrice().multiply(refundedOrdersDivide).setScale(2, BigDecimal.ROUND_UP);
//                        total = total.add(refundedLogisticsPrice);
//                    }
//                }
           return total;
        }else{
            //不拆单
            //判断是否为待收货状态或
            if(param.getAfterType().equals(IntegerEnum.YES.getCode())){
                return cereShopOrder.getPrice();
            }else{
                if(cereShopOrder.getLogisticsPrice()!=null){
                    return cereShopOrder.getPrice().subtract(cereShopOrder.getLogisticsPrice());
                }else{
                    return cereShopOrder.getPrice();
                }
            }
        }


    }
    @Override
    public Page getAll(AfterGetAllParam param, CereBuyerUser user) throws CoBusinessException {
        PageHelper.startPage(param.getPage(),param.getPageSize());
        List<Afters> list=cereOrderAfterDAO.getAll(param.getState(),user.getBuyerUserId());
        if(!EmptyUtils.isEmpty(list)){
            list.forEach(afters -> {
                //查询售后商品明细数据
                List<CartSku> skus=cereAfterProductService.findSkusByAfterId(afters.getAfterId());
                if(!EmptyUtils.isEmpty(skus)){
                    skus.forEach(sku -> sku.setValues(EmptyUtils.getImages(sku.getValue())));
                }
                afters.setSkus(skus);
            });
        }
        PageInfo<Afters> pageInfo=new PageInfo<>(list);
        Page page=new Page(pageInfo.getList(),pageInfo.getTotal());
        return page;
    }

    @Override
    public AfterDetail getById(Long afterId) throws CoBusinessException,Exception {
        AfterDetail detail=cereOrderAfterDAO.getById(afterId);
        if(detail!=null){
            if(!EmptyUtils.isEmpty(detail.getImage())){
                detail.setImages(EmptyUtils.getImages(detail.getImage()));
            }
            //查询自动关闭结束时间
            String end=cereRedisKeyServcice.findByKey(StringEnum.AFTER_CANCEL.getCode()+"-"+detail.getAfterId());
            if(!EmptyUtils.isEmpty(end)){
                //计算自动关闭剩余时间
                detail.setTime(TimeUtils.getCountDownByTime(TimeUtils.yyMMddHHmmss(),end));
            }
            //查询售后商品明细数据
            List<CartSku> skus=cereAfterProductService.findSkusByAfterId(detail.getAfterId());
            if(!EmptyUtils.isEmpty(skus)){
                skus.forEach(sku -> sku.setValues(EmptyUtils.getImages(sku.getValue())));
            }
            detail.setSkus(skus);
            //查询协商历史
            List<AfterHistory> histories = cereOrderAfterDAO.findHistories(afterId);
            detail.setIsPlaformState(0);
            histories.forEach(s->{
                if("申请平台介入".equals(s.getTitle())){
                    detail.setIsPlaformState(1);
                }
            });
            detail.setAfterHistory(histories);
        }
        return detail;
    }

    @Override
    @Transactional(isolation= Isolation.DEFAULT,propagation= Propagation.REQUIRED,rollbackFor = {CoBusinessException.class, Exception.class})
    public void returnRefund(AfterGetByIdParam param, CereBuyerUser user) throws CoBusinessException,Exception {
        String time =TimeUtils.yyMMddHHmmss();
        //校验售后但状态是否为审核中或者审核不通过
        CereOrderAfter after=cereOrderAfterDAO.checkStateStayAndNo(param.getAfterId());
        if(after==null){
            throw new CoBusinessException(CoReturnFormat.AFTER_NOT_RETURN);
        }
        //修改售后单状态为已关闭
        CereOrderAfter cereOrderAfter=new CereOrderAfter();
        cereOrderAfter.setAfterId(param.getAfterId());
        cereOrderAfter.setAfterState(IntegerEnum.AFTER_CANCEL.getCode());
        cereOrderAfter.setUpdateTime(time);
        cereOrderAfterDAO.updateByPrimaryKeySelective(cereOrderAfter);

        //清空自动关闭售后延时任务和任务记录
        stringRedisService.delete(StringEnum.AFTER_CANCEL.getCode()+"-"+cereOrderAfter.getAfterId());
        //新增延时任务记录
        cereRedisKeyServcice.deleteByKey(StringEnum.AFTER_CANCEL.getCode()+"-"+cereOrderAfter.getAfterId());

        //更新订单状态
        /*CereShopOrder order = new CereShopOrder();
        order.setId(cereOrderAfter.getOrderId());
        order.setAfterState(IntegerEnum.AFTER_CANCEL.getCode());
        cereShopOrderService.update(order);*/

        //重新启动订单自动确认收货定时任务
        String end = cereRedisKeyServcice.findByKey(StringEnum.ORDER_CONFIRM_DILEVERY.getCode()+"-"+param.getOrderId());
        if(!EmptyUtils.isEmpty(end)){
            stringRedisService.set(StringEnum.ORDER_CONFIRM_DILEVERY.getCode()+"-"+param.getOrderId(), 1, TimeUtils.getCountDownByTime(time,end));
        }

        //新增日志
        cerePlatformLogService.addLog(user,"售后管理","客户端操作",
                "买家撤销了售后申请",cereOrderAfter.getAfterId(),time);
    }

    @Override
    @Transactional(isolation= Isolation.DEFAULT,propagation= Propagation.REQUIRED,rollbackFor = {CoBusinessException.class, Exception.class})
    public void returnGoods(AfterGetByIdParam param, CereBuyerUser user) throws CoBusinessException,Exception {
        String time =TimeUtils.yyMMddHHmmss();
        //校验售后但状态是否为审核通过且售后类型为退货退款
        CereOrderAfter after=cereOrderAfterDAO.checkStateStayAndNoAndType(param.getAfterId());
        if(after==null){
            throw new CoBusinessException(CoReturnFormat.AFTER_NOT_RETURN);
        }
        //修改售后单状态为已关闭
        CereOrderAfter cereOrderAfter=new CereOrderAfter();
        cereOrderAfter.setAfterId(param.getAfterId());
        cereOrderAfter.setAfterState(IntegerEnum.AFTER_CANCEL.getCode());
        cereOrderAfter.setUpdateTime(time);
        cereOrderAfterDAO.updateByPrimaryKeySelective(cereOrderAfter);

        //清空自动关闭售后延时任务和任务记录
        stringRedisService.delete(StringEnum.AFTER_CANCEL.getCode()+"-"+cereOrderAfter.getAfterId());
        //新增延时任务记录
        cereRedisKeyServcice.deleteByKey(StringEnum.AFTER_CANCEL.getCode()+"-"+cereOrderAfter.getAfterId());

        //更新订单状态
        /*CereShopOrder order = new CereShopOrder();
        order.setId(cereOrderAfter.getOrderId());
        order.setAfterState(IntegerEnum.AFTER_CANCEL.getCode());
        cereShopOrderService.update(order);*/

        //重新启动订单自动确认收货定时任务
        String end = cereRedisKeyServcice.findByKey(StringEnum.ORDER_CONFIRM_DILEVERY.getCode()+"-"+param.getOrderId());
        if(!EmptyUtils.isEmpty(end)){
            stringRedisService.set(StringEnum.ORDER_CONFIRM_DILEVERY.getCode()+"-"+param.getOrderId(), 1, TimeUtils.getCountDownByTime(time,end));
        }

        //新增日志
        cerePlatformLogService.addLog(user,"售后管理","客户端操作",
                "买家撤销退货",cereOrderAfter.getAfterId(),time);
    }

    @Override
    @Transactional(isolation= Isolation.DEFAULT,propagation= Propagation.REQUIRED,rollbackFor = {CoBusinessException.class, Exception.class})
    public void platform(PlatformParam param, CereBuyerUser user) throws CoBusinessException {
        String time =TimeUtils.yyMMddHHmmss();
        //校验售后但状态是否为审核不通过
        CereOrderAfter after=cereOrderAfterDAO.checkState(param.getAfterId(),IntegerEnum.AFTER_STAY_NO.getCode(),null);
        if(after==null){
             after=cereOrderAfterDAO.checkState(param.getAfterId(),IntegerEnum.AFTER_RETURN_SUCCESS_NOT_REFUND.getCode(),null);
            if(after==null){
                throw new CoBusinessException(CoReturnFormat.AFTER_NOT_RETURN);
            }
        }
        //修改售后单状态为评审中
        CereOrderAfter cereOrderAfter=new CereOrderAfter();
        cereOrderAfter.setAfterId(param.getAfterId());
        cereOrderAfter.setAfterState(IntegerEnum.AFTER_PLATFORM_STAY.getCode());
        cereOrderAfter.setUpdateTime(time);
        cereOrderAfterDAO.updateByPrimaryKeySelective(cereOrderAfter);
        CerePlatformAfter cerePlatformAfter=new CerePlatformAfter();
        cerePlatformAfter.setOrderId(param.getOrderId());
        cerePlatformAfter.setReason(param.getReason());
        cerePlatformAfter.setCreateTime(time);
        cerePlatformAfter.setImage(param.getImage());
        cerePlatformAfter.setState(IntegerEnum.PLATFORM_AFTER_STAY.getCode());
        cerePlatformAfterService.insert(cerePlatformAfter);
        //新增日志
        cerePlatformLogService.addLog(user,"售后管理","客户端操作","申请平台介入",param.getAfterId(),time);
    }

    @Override
    @Transactional(isolation= Isolation.DEFAULT,propagation= Propagation.REQUIRED,rollbackFor = {CoBusinessException.class, Exception.class})
    public void update(CereOrderAfter cereOrderAfter) throws CoBusinessException {
        cereOrderAfterDAO.updateByPrimaryKeySelective(cereOrderAfter);
    }

    @Override
    public Integer getPostSaleNumByMonth(Integer month, Long buyerUserId) {
        Date time = DateUtil.offsetMonth(new Date(), -month);
        return cereOrderAfterDAO.getPostSaleNumByMonth(time, buyerUserId);
    }

    @Override
    public void deleteAfterById(Long id) {
        cereOrderAfterDAO.deleteByPrimaryKey(id);
        cereAfterProductService.deleteByAfterId(id);
        cereAfterDileverService.deleteByAfterId(id);
    }

    @Override
    public List<Integer> selectAfterStateList(Long orderId) {
        LambdaQueryWrapper<CereOrderAfter> wrapper = Wrappers.<CereOrderAfter>lambdaQuery()
                .eq(CereOrderAfter::getOrderId, orderId);
        List<CereOrderAfter> orderAfterList = cereOrderAfterDAO.selectList(wrapper);
        return orderAfterList.stream().map(CereOrderAfter::getAfterState).collect(Collectors.toList());
    }

    @Override
    public int findAfterCount(Long buyerUserId) {
        return cereOrderAfterDAO.findAfterCount(buyerUserId);
    }

    @Override
    public List<CereOrderAfter> selectByOrderIdList(List<Long> orderIdList) {
        return cereOrderAfterDAO.selectList(Wrappers.<CereOrderAfter>lambdaQuery().in(CereOrderAfter::getOrderId, orderIdList));
    }
}
