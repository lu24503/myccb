package com.shop.cereshop.app.page.product;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BroadCastDTO {

    @ApiModelProperty("用户id")
    private Long buyerUserId;

    @ApiModelProperty("昵称")
    private String name;

    @ApiModelProperty("头像")
    private String headImage;

    @ApiModelProperty("类型 1-给好评 2-正在拼单 3-拼单成功 4-下单")
    private Integer type;

    /**
     * 操作时间
     */
    private String time;

    @ApiModelProperty("时间 几秒前 几分钟前")
    private String timeStr;

}
