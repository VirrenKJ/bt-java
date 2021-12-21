package com.bug.tracker.project.service;

import com.bug.tracker.common.object.CommonListTO;
import com.bug.tracker.common.object.PaginationCriteria;
import com.bug.tracker.common.object.SearchResponseTO;
import com.bug.tracker.common.service.ModelConvertorService;
import com.bug.tracker.project.dao.ProjectDao;
import com.bug.tracker.project.dto.ProjectTO;
import com.bug.tracker.project.entity.ProjectBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {

  @Autowired
  private ProjectDao projectDao;

  @Autowired
  private ModelConvertorService modelConvertorService;

  @Override
  public ProjectTO addProject(ProjectTO projectTO) {
    ProjectBO projectBO = modelConvertorService.map(projectTO, ProjectBO.class);
    return modelConvertorService.map(projectDao.addProject(projectBO), ProjectTO.class);
  }

  @Override
  public ProjectTO updateProject(ProjectTO projectTO) {
    ProjectBO projectBO = modelConvertorService.map(projectTO, ProjectBO.class);
    return modelConvertorService.map(projectDao.updateProject(projectBO), ProjectTO.class);
  }

  @Override
  @Transactional(Transactional.TxType.NOT_SUPPORTED)
  public SearchResponseTO getProjectList(PaginationCriteria paginationCriteria) {
    SearchResponseTO searchResponseTO = new SearchResponseTO();
    CommonListTO<ProjectBO> commonListTO = projectDao.getProjectList(paginationCriteria);
    
    List<ProjectBO> projectBOS = commonListTO.getDataList();
    List<ProjectTO> projectTOS = modelConvertorService.map(projectBOS, ProjectTO.class);

    searchResponseTO.setList(projectTOS);
    searchResponseTO.setPageCount(commonListTO.getPageCount());
    searchResponseTO.setTotalRowCount(commonListTO.getTotalRow().intValue());
    return searchResponseTO;
  }

  @Override
  @Transactional(Transactional.TxType.NOT_SUPPORTED)
  public ProjectTO getProjectId(Integer id) {
    ProjectTO projectTO = modelConvertorService.map(projectDao.getProjectId(id), ProjectTO.class);
    return projectTO;
  }

  @Override
  public void deleteProject(List<Integer> id) {
    projectDao.deleteGlobalCategory(id);
  }
}
