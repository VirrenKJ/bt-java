package com.bug.tracker.systemProfile.service;

import com.bug.tracker.common.object.PaginationCriteria;
import com.bug.tracker.common.object.SearchResponseTO;
import com.bug.tracker.systemProfile.dto.SystemProfileTO;

import java.util.List;

public interface SystemProfileService {

  SystemProfileTO addSystemProfile(SystemProfileTO systemProfileTO);

  SystemProfileTO updateSystemProfile(SystemProfileTO systemProfileTO);

  SearchResponseTO getSystemProfileList(PaginationCriteria paginationCriteria);

  SystemProfileTO getSystemProfileId(Integer id);

  void deleteSystemProfile(List<Integer> id);

}
