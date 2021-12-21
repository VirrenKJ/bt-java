package com.bug.tracker.issue.service;

import com.bug.tracker.common.object.CommonListTO;
import com.bug.tracker.common.object.PaginationCriteria;
import com.bug.tracker.common.object.SearchResponseTO;
import com.bug.tracker.common.service.ModelConvertorService;
import com.bug.tracker.issue.dao.IssueDao;
import com.bug.tracker.issue.dto.IssueTO;
import com.bug.tracker.issue.entity.IssueBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class IssueServiceImpl implements IssueService {

  @Autowired
  private IssueDao issueDao;

  @Autowired
  private ModelConvertorService modelConvertorService;

  @Override
  public IssueTO addIssue(IssueTO issueTO) {
    IssueBO issueBO = modelConvertorService.map(issueTO, IssueBO.class);
    return modelConvertorService.map(issueDao.addIssue(issueBO), IssueTO.class);
  }

  @Override
  public IssueTO updateIssue(IssueTO issueTO) {
    IssueBO issueBO = modelConvertorService.map(issueTO, IssueBO.class);
    return modelConvertorService.map(issueDao.updateIssue(issueBO), IssueTO.class);
  }

  @Override
  @Transactional(Transactional.TxType.NOT_SUPPORTED)
  public SearchResponseTO getIssueList(PaginationCriteria paginationCriteria) {
    SearchResponseTO searchResponseTO = new SearchResponseTO();
    CommonListTO<IssueBO> commonListTO = issueDao.getIssueList(paginationCriteria);

    List<IssueBO> issueBOS = commonListTO.getDataList();
    List<IssueTO> issueTOS = modelConvertorService.map(issueBOS, IssueTO.class);

    searchResponseTO.setList(issueTOS);
    searchResponseTO.setPageCount(commonListTO.getPageCount());
    searchResponseTO.setTotalRowCount(commonListTO.getTotalRow().intValue());
    return searchResponseTO;
  }

  @Override
  @Transactional(Transactional.TxType.NOT_SUPPORTED)
  public IssueTO getIssueId(Integer id) {
    IssueTO issueTO = modelConvertorService.map(issueDao.getIssueId(id), IssueTO.class);
    return issueTO;
  }

  @Override
  public void deleteIssue(List<Integer> id) {
    issueDao.deleteGlobalCategory(id);
  }
}
