package com.shop.cereshop.app.page.distributor;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LevelDescDTO {

    @ApiModelProperty("等级id")
    private Long distributorLevelId;

    @ApiModelProperty("等级描述")
    private String levelDesc;

    @ApiModelProperty("等级名称")
    private String levelName;

    @ApiModelProperty("等级编号")
    private Integer levelNum;

    @ApiModelProperty("等级图标")
    private String levelLogo;

}
