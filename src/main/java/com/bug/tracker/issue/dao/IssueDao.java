package com.bug.tracker.issue.dao;

import com.bug.tracker.common.object.CommonListTO;
import com.bug.tracker.common.object.PaginationCriteria;
import com.bug.tracker.issue.entity.IssueBO;

import java.util.List;

public interface IssueDao {

  IssueBO addIssue(IssueBO issueBO);

  IssueBO updateIssue(IssueBO issueBO);

  CommonListTO<IssueBO> getIssueList(PaginationCriteria paginationCriteria);

  IssueBO getIssueId(Integer id);

  void deleteGlobalCategory(List<Integer> id);

}
