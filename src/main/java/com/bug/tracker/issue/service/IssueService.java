package com.bug.tracker.issue.service;

import com.bug.tracker.common.object.PaginationCriteria;
import com.bug.tracker.common.object.SearchResponseTO;
import com.bug.tracker.issue.dto.IssueTO;

import java.util.List;

public interface IssueService {

  IssueTO addIssue(IssueTO issueTO);

  IssueTO updateIssue(IssueTO issueTO);

  SearchResponseTO getIssueList(PaginationCriteria paginationCriteria);

  IssueTO getIssueId(Integer id);

  void deleteIssue(List<Integer> id);

}
