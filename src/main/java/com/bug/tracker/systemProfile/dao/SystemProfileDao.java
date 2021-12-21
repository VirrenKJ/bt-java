package com.bug.tracker.systemProfile.dao;

import com.bug.tracker.common.object.CommonListTO;
import com.bug.tracker.common.object.PaginationCriteria;
import com.bug.tracker.systemProfile.entity.SystemProfileBO;

import java.util.List;

public interface SystemProfileDao {

  SystemProfileBO addSystemProfile(SystemProfileBO systemProfileBO);

  SystemProfileBO updateSystemProfile(SystemProfileBO systemProfileBO);

  CommonListTO<SystemProfileBO> getSystemProfileList(PaginationCriteria paginationCriteria);

  SystemProfileBO getSystemProfileId(Integer id);

  void deleteGlobalCategory(List<Integer> id);

}
