package com.bug.tracker.project.dao;

import com.bug.tracker.common.object.CommonListTO;
import com.bug.tracker.common.object.SearchCriteriaObj;
import com.bug.tracker.project.entity.ProjectBO;

import java.util.List;

public interface ProjectDao {

  ProjectBO addProject(ProjectBO projectBO);

  ProjectBO updateProject(ProjectBO projectBO);

  CommonListTO<ProjectBO> getProjectList(SearchCriteriaObj searchCriteriaObj);

  ProjectBO getProjectId(Integer id);

  void deleteGlobalCategory(List<Integer> id);

}
