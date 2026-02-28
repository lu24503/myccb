package com.shop.cereshop.app.page.live;

import cn.binarywang.wx.miniapp.bean.live.WxMaLiveResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("直播间")
public class CereLivePage {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("主播头像")
    private String anchorHeadImg;

    @ApiModelProperty("主播昵称")
    private String anchorNickName;

    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("feedsImg")
    private String feedsImg;

    @ApiModelProperty("直播间id")
    private Integer roomId;

    @ApiModelProperty("直播间状态")
    private Integer liveStatus;

    @ApiModelProperty("临时订阅状态 1-已订阅 0-未订阅")
    private Integer subscribeStatus;

    @ApiModelProperty("直播间商品信息")
    private List<WxMaLiveResult.Goods> goods;
}
