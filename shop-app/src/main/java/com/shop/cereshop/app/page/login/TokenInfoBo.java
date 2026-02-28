package com.shop.cereshop.app.page.login;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 用户token信息
 */
@Data
@AllArgsConstructor
public class TokenInfoBo {

    private Long buyerUserId;

    private String token;

    private String refreshToken;

}
