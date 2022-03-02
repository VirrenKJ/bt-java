package com.bug.tracker.issue.service;

import com.bug.tracker.category.service.GlobalCategoryService;
import com.bug.tracker.common.object.CommonListTO;
import com.bug.tracker.common.object.PaginationCriteria;
import com.bug.tracker.common.object.SearchResponseTO;
import com.bug.tracker.common.service.ModelConvertorService;
import com.bug.tracker.config.UserSessionContext;
import com.bug.tracker.issue.dao.IssueDao;
import com.bug.tracker.issue.dto.IssueTO;
import com.bug.tracker.issue.entity.IssueBO;
import com.bug.tracker.project.service.ProjectService;
import com.bug.tracker.systemProfile.service.SystemProfileService;
import com.bug.tracker.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class IssueServiceImpl implements IssueService {

  @Autowired
  private IssueDao issueDao;

  @Autowired
  private ProjectService projectService;

  @Autowired
  private GlobalCategoryService globalCategoryService;

  @Autowired
  private SystemProfileService systemProfileService;

  @Autowired
  private UserService userService;

  @Autowired
  private ModelConvertorService modelConvertorService;

  @Override
  public IssueTO addIssue(IssueTO issueTO) {
    issueTO.setReportedById(UserSessionContext.getCurrentTenant().getId());
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
    List<IssueTO> issueTOS = null;
    if (issueBOS != null && !issueBOS.isEmpty()) {
        issueTOS = new ArrayList<>();
      for (IssueBO issueBO : issueBOS) {
        IssueTO issue = modelConvertorService.map(issueBO, IssueTO.class);
        if (issue.getProjectId() != null) {
          issue.setProjectName(projectService.getProjectId(issue.getProjectId()).getName());
        }
        if (issue.getCategoryId() != null) {
          issue.setCategoryName(globalCategoryService.getGlobalCategoryById(issue.getCategoryId()).getName());
        }
        if (issue.getProfileId() != null) {
          issue.setProfileName(systemProfileService.getSystemProfileId(issue.getProfileId()).getOsName());
        }
        if (issue.getAssignedId() != null) {
          issue.setAssignedFirstName(userService.getUserById(issue.getAssignedId()).getFirstName());
          issue.setAssignedUsername(userService.getUserById(issue.getAssignedId()).getUsername());
        }
//        if (issue.getReportedById() != null) {
//          issue.setReportedByFirstName(userService.getUserById(issue.getReportedById()).getFirstName());
//          issue.setReportedByUsername(userService.getUserById(issue.getReportedById()).getUsername());
//        }
        issueTOS.add(issue);
      }
    }
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
