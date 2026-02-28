package com.shop.cereshop.app.dao.live;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.cereshop.app.page.live.CereLivePage;
import com.shop.cereshop.commons.domain.live.CereLive;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CereLiveDAO extends BaseMapper<CereLive> {

    List<CereLivePage> selectLiveList(@Param("nowTime") String nowTime);

}
