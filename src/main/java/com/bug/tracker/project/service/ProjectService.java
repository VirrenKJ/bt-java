package com.bug.tracker.project.service;

import com.bug.tracker.common.object.SearchCriteriaObj;
import com.bug.tracker.common.object.SearchResponseTO;
import com.bug.tracker.project.dto.ProjectTO;

import java.util.List;

public interface ProjectService {

  ProjectTO addProject(ProjectTO projectTO);

  ProjectTO updateProject(ProjectTO projectTO);

  SearchResponseTO getProjectList(SearchCriteriaObj searchCriteriaObj);

  ProjectTO getProjectId(Integer id);

  void deleteProject(List<Integer> id);

}
