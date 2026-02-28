package com.shop.cereshop.app.param.member;

import com.shop.cereshop.commons.domain.member.CerePlatformMemberLevel;
import com.shop.cereshop.commons.domain.member.CerePlatformMembership;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("会员等级组合对象")
public class MemberLevelInfo extends CerePlatformMemberLevel {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("会员等级权益对象列表")
    List<CerePlatformMembership> membershipList;

}
