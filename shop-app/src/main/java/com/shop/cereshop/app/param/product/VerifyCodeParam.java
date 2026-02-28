package com.shop.cereshop.app.param.product;

import lombok.Data;

import java.io.Serializable;

@Data
public class VerifyCodeParam implements Serializable {

    /**
     * 刷新sku实时信息的校验码
     */
    private String verifyCode;

}
