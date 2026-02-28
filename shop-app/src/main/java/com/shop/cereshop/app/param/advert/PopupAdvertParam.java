package com.shop.cereshop.app.param.advert;

import com.shop.cereshop.app.param.coupon.CouponTakeState;
import com.shop.cereshop.commons.domain.advert.CerePopupAdvert;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("弹窗广告前端返回对象")
public class PopupAdvertParam extends CerePopupAdvert {

    List<CouponTakeState> couponTakeStateList;

}
