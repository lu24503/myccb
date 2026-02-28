package com.shop.cereshop.app.utils;

import com.shop.cereshop.app.page.cart.CartSku;
import com.shop.cereshop.app.page.settlement.Distribution;
import com.shop.cereshop.app.service.logistics.CereOrderLogisticsService;
import com.shop.cereshop.commons.constant.ParamEnum;
import com.shop.cereshop.commons.domain.buyer.CereBuyerReceive;
import com.shop.cereshop.commons.domain.logistics.CereLogisticsCharge;
import com.shop.cereshop.commons.domain.logistics.CereOrderLogistics;
import com.shop.cereshop.commons.utils.EmptyUtils;
import com.shop.cereshop.commons.utils.SpringUtil;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DistributionUtil {

    public static Distribution getDistribution(List<CereOrderLogistics> logistics, CereBuyerReceive receive,
                                               Map<Long, Integer> buyNumberMap, List<CartSku> cartSkus) {
        CereOrderLogisticsService cereOrderLogisticsService = SpringUtil.getBean(CereOrderLogisticsService.class);
        Distribution result = new Distribution();
        result.setDistributionPrice(BigDecimal.ZERO);
        boolean updated = false;
        for (CereOrderLogistics orderLogistic:logistics) {
            Distribution tmp = chargeInner(cereOrderLogisticsService, orderLogistic, receive, buyNumberMap, cartSkus);
            if (tmp != null && tmp.getDistributionPrice().compareTo(result.getDistributionPrice()) >= 0) {
                result = tmp;
                updated = true;
            }
        }
        if (updated) {
            return result;
        }
        return null;
    }

    /**
     * 根据不同类型进行计算
     * @param cereOrderLogisticsService
     * @param orderLogistic
     * @param receive
     * @param buyNumberMap
     * @param cartSkus
     * @return
     */
    private static Distribution chargeInner(CereOrderLogisticsService cereOrderLogisticsService,
                                            CereOrderLogistics orderLogistic, CereBuyerReceive receive,
                                            Map<Long, Integer> buyNumberMap, List<CartSku> cartSkus) {
        //如果是按件,查询所有计费明细
        List<CereLogisticsCharge> charges = cereOrderLogisticsService.findCharges(orderLogistic.getLogisticsId());
        BigDecimal finalPrice = new BigDecimal(-999999);
        BigDecimal totalFactor = BigDecimal.ZERO;
        if (ParamEnum.CHARGE_TYPE_NUMBER.getCode().equals(orderLogistic.getChargeType())) {
            for (CartSku cartSku : cartSkus) {
                totalFactor = totalFactor.add(new BigDecimal(buyNumberMap.get(cartSku.getSkuId())));
            }
        } else if (ParamEnum.CHARGE_TYPE_WEIGHT.getCode().equals(orderLogistic.getChargeType())) {
            for (CartSku cartSku : cartSkus) {
                BigDecimal buyNumber = new BigDecimal(buyNumberMap.get(cartSku.getSkuId()));
                totalFactor = totalFactor.add(cartSku.getWeight().multiply(buyNumber));
            }
        }
        boolean zeroFactor = false;
        if (totalFactor.compareTo(new BigDecimal("0.000001")) < 0) {
            zeroFactor = true;
            finalPrice = BigDecimal.ZERO;
        }
        if (!zeroFactor && !EmptyUtils.isEmpty(charges)) {
            //遍历计费明细计算物流费用
            for (CereLogisticsCharge charge:charges) {
                //校验城市匹配
                boolean flag = matching(charge, receive);
                if (flag) {
                    if (ParamEnum.CHARGE_TYPE.getCode().equals(orderLogistic.getChargeType())) {
                        if (BigDecimal.ZERO.compareTo(finalPrice) > 0) {
                            finalPrice = BigDecimal.ZERO;
                        }
                    } else if (!EmptyUtils.isEmpty(charge.getWeight())
                            && !EmptyUtils.isEmpty(charge.getPrice())
                            && !EmptyUtils.isEmpty(charge.getSecondWeight())
                            && !EmptyUtils.isEmpty(charge.getSecondPrice()) && !BigDecimal.ZERO.equals(charge.getSecondWeight())) {
                        //商品总件数-首件
                        BigDecimal remainFactor = totalFactor.subtract(charge.getWeight());
                        //加入首件价格
                        BigDecimal innerPrice = charge.getPrice();
                        if (remainFactor.compareTo(BigDecimal.ZERO) > 0) {
                            //如果还有剩余
                            if (!EmptyUtils.isEmpty(charge.getSecondWeight()) && !EmptyUtils.isEmpty(charge.getSecondPrice())) {
                                //剩余件数/续件数（向上取整）* 续件价格
                                innerPrice = innerPrice.
                                        add(remainFactor.divide(charge.getSecondWeight(), BigDecimal.ROUND_UP).multiply(charge.getSecondPrice()));
                            }
                        }
                        if (innerPrice.compareTo(finalPrice) > 0) {
                            finalPrice = innerPrice;
                        }
                    }
                }
            }
        }
        if (finalPrice.compareTo(BigDecimal.ZERO) >= 0) {
            Distribution distribution = new Distribution();
            distribution.setLogisticsId(orderLogistic.getLogisticsId());
            distribution.setDistributionPrice(finalPrice);
            distribution.setDistributionName(ParamEnum.getByCode(orderLogistic.getChargeType()));
            return distribution;
        }
        return null;
    }

    private static boolean matching(CereLogisticsCharge charge, CereBuyerReceive receive) {
        List<String> provinces = new ArrayList<>();
        List<String> cities = new ArrayList<>();
        //校验与当前收货地址城市是否匹配
        if (charge.getRegion().contains(";")) {
            //包含多个省,截取出所有的省
            String[] split = charge.getRegion().split(";");
            for (String str : split) {
                setProvinceCities(str, provinces, cities);
            }
        } else {
            //只有一个省的数据
            setProvinceCities(charge.getRegion(), provinces, cities);
        }
        //优先匹配城市级别的物流模板
        boolean flag = checkCities(receive, cities);
        if (!flag) {
            //城市级别的匹配不到，则匹配省份级别的
            flag = checkProvince(receive, provinces);
        }
        return flag;
    }

    private static boolean checkCities(CereBuyerReceive receive, List<String> cities) {
        String[] split = receive.getReceiveAdress().split("-");
        if (!EmptyUtils.isEmpty(split) && !EmptyUtils.isEmpty(cities) && split.length > 1) {
            String receiveProvinceAndCity = split[0] + "-" + split[1];
            if (cities.contains(receiveProvinceAndCity)) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkProvince(CereBuyerReceive receive, List<String> provinces) {
        if (receive.getReceiveAdress().contains("-")) {
            String[] split = receive.getReceiveAdress().split("-");
            if (!EmptyUtils.isEmpty(split) && !EmptyUtils.isEmpty(provinces)) {
                if (provinces.contains(split[0])) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param str 物流区域配送信息
     * @param provinces 只到省份层级的列表
     * @param cities 到市层级的列表
     */
    private static void setProvinceCities(String str, List<String> provinces, List<String> cities) {
        if (str.contains(":")) {
            //带市的数据
            String[] split1 = str.split(":");
            String provinceName = split1[0];
            if (StringUtils.isNotBlank(split1[0])) {
                if (split1[1].contains(",")) {
                    //包含多个市
                    String[] split2 = split1[1].split(",");
                    for (String cityName : split2) {
                        if (StringUtils.isNotBlank(cityName)) {
                            cities.add(provinceName + "-" + cityName);
                        }
                    }
                } else {
                    cities.add(provinceName + "-" + split1[1]);
                }
            }
        } else {
            //不带市
            provinces.add(str);
        }
    }
}
