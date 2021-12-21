package com.bug.tracker.systemProfile.service;

import com.bug.tracker.common.object.CommonListTO;
import com.bug.tracker.common.object.PaginationCriteria;
import com.bug.tracker.common.object.SearchResponseTO;
import com.bug.tracker.common.service.ModelConvertorService;
import com.bug.tracker.systemProfile.dao.SystemProfileDao;
import com.bug.tracker.systemProfile.dto.SystemProfileTO;
import com.bug.tracker.systemProfile.entity.SystemProfileBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class SystemProfileServiceImpl implements SystemProfileService {

  @Autowired
  private SystemProfileDao systemProfileDao;

  @Autowired
  private ModelConvertorService modelConvertorService;

  @Override
  public SystemProfileTO addSystemProfile(SystemProfileTO systemProfileTO) {
    SystemProfileBO systemProfileBO = modelConvertorService.map(systemProfileTO, SystemProfileBO.class);
    return modelConvertorService.map(systemProfileDao.addSystemProfile(systemProfileBO), SystemProfileTO.class);
  }

  @Override
  public SystemProfileTO updateSystemProfile(SystemProfileTO systemProfileTO) {
    SystemProfileBO systemProfileBO = modelConvertorService.map(systemProfileTO, SystemProfileBO.class);
    return modelConvertorService.map(systemProfileDao.updateSystemProfile(systemProfileBO), SystemProfileTO.class);
  }

  @Override
  @Transactional(Transactional.TxType.NOT_SUPPORTED)
  public SearchResponseTO getSystemProfileList(PaginationCriteria paginationCriteria) {
    SearchResponseTO searchResponseTO = new SearchResponseTO();
    CommonListTO<SystemProfileBO> commonListTO = systemProfileDao.getSystemProfileList(paginationCriteria);

    List<SystemProfileBO> systemProfileBOS = commonListTO.getDataList();
    List<SystemProfileTO> systemProfileTOS = modelConvertorService.map(systemProfileBOS, SystemProfileTO.class);

    searchResponseTO.setList(systemProfileTOS);
    searchResponseTO.setPageCount(commonListTO.getPageCount());
    searchResponseTO.setTotalRowCount(commonListTO.getTotalRow().intValue());
    return searchResponseTO;
  }

  @Override
  @Transactional(Transactional.TxType.NOT_SUPPORTED)
  public SystemProfileTO getSystemProfileId(Integer id) {
    SystemProfileTO systemProfileTO = modelConvertorService.map(systemProfileDao.getSystemProfileId(id), SystemProfileTO.class);
    return systemProfileTO;
  }

  @Override
  public void deleteSystemProfile(List<Integer> id) {
    systemProfileDao.deleteGlobalCategory(id);
  }
}
