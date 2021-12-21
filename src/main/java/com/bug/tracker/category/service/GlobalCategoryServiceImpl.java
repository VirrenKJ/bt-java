package com.bug.tracker.category.service;

import com.bug.tracker.category.dao.GlobalCategoryDao;
import com.bug.tracker.category.dto.GlobalCategoryTO;
import com.bug.tracker.category.entity.GlobalCategoryBO;
import com.bug.tracker.common.object.CommonListTO;
import com.bug.tracker.common.object.PaginationCriteria;
import com.bug.tracker.common.object.SearchResponseTO;
import com.bug.tracker.common.service.ModelConvertorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class GlobalCategoryServiceImpl implements GlobalCategoryService {

  @Autowired
  private GlobalCategoryDao globalCategoryDao;

  @Autowired
  private ModelConvertorService modelConvertorService;

  @Override
  public GlobalCategoryTO addGlobalCategory(GlobalCategoryTO globalCategoryTO) {
    GlobalCategoryBO globalCategoryBO = modelConvertorService.map(globalCategoryTO, GlobalCategoryBO.class);
    return modelConvertorService.map(globalCategoryDao.addGlobalCategory(globalCategoryBO), GlobalCategoryTO.class);
  }

  @Override
  public GlobalCategoryTO updateGlobalCategory(GlobalCategoryTO globalCategoryTO) {
    GlobalCategoryBO globalCategoryBO = modelConvertorService.map(globalCategoryTO, GlobalCategoryBO.class);
    return modelConvertorService.map(globalCategoryDao.updateGlobalCategory(globalCategoryBO), GlobalCategoryTO.class);
  }

  @Override
  @Transactional(Transactional.TxType.NOT_SUPPORTED)
  public SearchResponseTO getGlobalCategoryList(PaginationCriteria paginationCriteria) {
    SearchResponseTO searchResponseTO = new SearchResponseTO();
    CommonListTO<GlobalCategoryBO> commonListTO = globalCategoryDao.getGlobalCategoryList(paginationCriteria);

    List<GlobalCategoryBO> globalCategoryBOs = commonListTO.getDataList();
    List<GlobalCategoryTO> globalCategoryTOs = modelConvertorService.map(globalCategoryBOs, GlobalCategoryTO.class);

    searchResponseTO.setList(globalCategoryTOs);
    searchResponseTO.setPageCount(commonListTO.getPageCount());
    searchResponseTO.setTotalRowCount(commonListTO.getTotalRow().intValue());
    return searchResponseTO;
  }

  @Override
  @Transactional(Transactional.TxType.NOT_SUPPORTED)
  public GlobalCategoryTO getGlobalCategoryById(Integer id) {
    GlobalCategoryTO globalCategoryTO = modelConvertorService.map(globalCategoryDao.getGlobalCategoryById(id), GlobalCategoryTO.class);
    return globalCategoryTO;
  }

  @Override
  public void deleteGlobalCategory(List<Integer> id) {
    globalCategoryDao.deleteGlobalCategory(id);
  }
}
