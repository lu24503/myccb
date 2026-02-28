package com.shop.cereshop.app.service.recommend.impl;

import com.shop.cereshop.app.dao.recommend.CereRecommendTypeDAO;
import com.shop.cereshop.app.page.recommend.CereRecommendTypeVO;
import com.shop.cereshop.app.service.recommend.CereRecommendTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CereRecommendTypeServiceImpl implements CereRecommendTypeService {

    @Autowired
    private CereRecommendTypeDAO cereRecommendTypeDAO;

    @Override
    public List<CereRecommendTypeVO> getAll() {
        return cereRecommendTypeDAO.getAll();
    }

}
