package com.shop.cereshop.app.dao.product;

import com.shop.cereshop.commons.domain.product.CereProductStatsByDay;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CereProductStatsByDayDAO {

    int insertOrUpdate(CereProductStatsByDay stats);

}
