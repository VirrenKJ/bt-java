package com.bug.tracker.category.service;

import com.bug.tracker.category.dao.GlobalCategoryDao;
import com.bug.tracker.category.dto.GlobalCategoryTO;
import com.bug.tracker.category.entity.GlobalCategoryBO;
import com.bug.tracker.common.object.CommonListTO;
import com.bug.tracker.common.object.SearchCriteriaObj;
import com.bug.tracker.common.service.ModelConvertorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class GlobalCategoryServiceImpl implements GlobalCategoryService{

    @Autowired
    private GlobalCategoryDao globalCategoryDao;

    @Autowired
    private ModelConvertorService modelConvertorService;

    @Override
    public GlobalCategoryTO add(GlobalCategoryTO globalCategoryTO) {
        GlobalCategoryBO globalCategoryBO = modelConvertorService.map(globalCategoryTO, GlobalCategoryBO.class);
        return modelConvertorService.map(globalCategoryDao.add(globalCategoryBO), GlobalCategoryTO.class);
    }

    @Override
    public GlobalCategoryTO update(GlobalCategoryTO globalCategoryTO) {
        GlobalCategoryBO globalCategoryBO = modelConvertorService.map(globalCategoryTO, GlobalCategoryBO.class);
        return modelConvertorService.map(globalCategoryDao.update(globalCategoryBO), GlobalCategoryTO.class);
    }

    @Override
    public List<GlobalCategoryTO> getList(SearchCriteriaObj searchCriteriaObj) {
        CommonListTO<GlobalCategoryBO> commonListTO = globalCategoryDao.getList(searchCriteriaObj);
        List<GlobalCategoryBO> globalCategoryBOs = commonListTO.getDataList();
        List<GlobalCategoryTO> globalCategoryTOs = modelConvertorService.map(globalCategoryBOs, GlobalCategoryTO.class);
        return globalCategoryTOs;
    }

    @Override
    public GlobalCategoryTO getById(Integer id) {
        GlobalCategoryTO globalCategoryTO = modelConvertorService.map(globalCategoryDao.getById(id), GlobalCategoryTO.class);
        return globalCategoryTO;
    }

    @Override
    public void delete(List<Integer> id) {
        globalCategoryDao.delete(id);
    }
}
