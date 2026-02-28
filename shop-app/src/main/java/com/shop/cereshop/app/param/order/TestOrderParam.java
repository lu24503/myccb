package com.shop.cereshop.app.param.order;

import lombok.Data;

@Data
public class TestOrderParam {

    private Long shopId;

    private Long productId;

    private Long skuId;

    private int cnt;

    private Long buyerUserId;

}
