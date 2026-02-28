package com.shop.cereshop.app.timing;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.shop.cereshop.app.dao.buyer.CereBuyerUserDAO;
import com.shop.cereshop.app.dao.cart.CereShopCartDAO;
import com.shop.cereshop.app.dao.order.CereShopOrderDAO;
import com.shop.cereshop.commons.domain.buyer.CereBuyerUser;
import com.shop.cereshop.commons.domain.cart.CereShopCart;
import com.shop.cereshop.commons.domain.order.CereShopOrder;
import com.shop.cereshop.commons.utils.TimeUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 场景营销定时器
 */
@Slf4j(topic = "SmsMarketingNoticeRunnable")
@AllArgsConstructor
public class SmsMarketingNoticeRunnable implements Runnable {

    private final CereBuyerUserDAO cereBuyerUserDAO;

    private final CereShopCartDAO cereShopCartDAO;

    private final CereShopOrderDAO cereShopOrderDAO;

    @Override
    public void run() {
        registerAndNotAddCart();
    }

    private void registerAndNotAddCart() {
        //查询昨天注册的用户
        String startDate = TimeUtils.yesterday() + " 00:00:00";
        String endDate = TimeUtils.today() + " 00:00:00";
        List<CereBuyerUser> userList = cereBuyerUserDAO.selectList(Wrappers.<CereBuyerUser>lambdaQuery().between(CereBuyerUser::getCreateTime, startDate, endDate));
        //查询购物车
        if (CollectionUtils.isNotEmpty(userList)) {
            List<Long> buyerUserIdList = userList.stream().map(CereBuyerUser::getBuyerUserId).collect(Collectors.toList());
            List<Long> existsCartUserIdList = cereShopCartDAO.selectList(Wrappers.<CereShopCart>lambdaQuery().in(CereShopCart::getBuyerUserId, buyerUserIdList)).stream().map(CereShopCart::getBuyerUserId).distinct().collect(Collectors.toList());
            buyerUserIdList.removeAll(existsCartUserIdList);
            if (CollectionUtils.isNotEmpty(buyerUserIdList)) {
                List<Long> existsOrderUserIdList = cereShopOrderDAO.selectList(Wrappers.<CereShopOrder>lambdaQuery()
                        .in(CereShopOrder::getBuyerUserId, buyerUserIdList)).stream()
                        .map(CereShopOrder::getBuyerUserId).collect(Collectors.toList());
                buyerUserIdList.removeAll(existsCartUserIdList);
                for (Long buyerUserId:buyerUserIdList) {
                    //发送短信
                }
            }
        }
    }
}
