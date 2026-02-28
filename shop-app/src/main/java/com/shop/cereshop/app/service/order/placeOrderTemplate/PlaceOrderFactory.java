package com.shop.cereshop.app.service.order.placeOrderTemplate;

import com.shop.cereshop.commons.constant.IntegerEnum;
import com.shop.cereshop.commons.utils.EmptyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PlaceOrderFactory {
    @Autowired
    private NormalPlaceOrder normalPlaceOrder;

    @Autowired
    private LaunchPlaceOrder launchPlaceOrder;

    @Autowired
    private JoinPlaceOrder joinPlaceOrder;

    /**
     * 获取指定 下单方法
     * @param type /
     * @return
     */
    public PlaceOrderTemplate getPlaceOrderTemplate(Integer type) {

        if (EmptyUtils.isEmpty(type)
                || IntegerEnum.SECKILL_SUBMIT.getCode().equals(type)
                || IntegerEnum.DISCOUNT_SUBMIT.getCode().equals(type)) {
            //正常下单
            return normalPlaceOrder;
        } else if (IntegerEnum.GROUP_WORK_LAUNCH.getCode().equals(type)) {
            //发起拼团
            return launchPlaceOrder;
        } else if (IntegerEnum.GROUP_WORK_JOIN.getCode().equals(type)) {
            //参与拼团
            return joinPlaceOrder;
        } /*else if(IntegerEnum.SECKILL_SUBMIT.getCode().equals(param.getType())){
            //秒杀活动下单
            seckillOrder(param,user,ip,payUrl,time);
        } else if(IntegerEnum.DISCOUNT_SUBMIT.getCode().equals(param.getType())){
            //限时折扣活动下单
            discountOrder(param,user,ip,payUrl,time);
        }*/
        return null;
    }
}
