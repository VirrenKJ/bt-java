package com.bug.tracker.project.dao;

import com.bug.tracker.common.object.CommonListTO;
import com.bug.tracker.common.object.PaginationCriteria;
import com.bug.tracker.project.entity.ProjectBO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProjectDaoImpl implements ProjectDao {

  @PersistenceContext
  private EntityManager entityManager;

  private static final Logger logger = LoggerFactory.getLogger(ProjectDaoImpl.class);

  @Override
  public ProjectBO addProject(ProjectBO projectBO) {
    entityManager.persist(projectBO);
    logger.info("Project has added successfully, Project details=" + projectBO);
    return projectBO;
  }

  @Override
  public ProjectBO updateProject(ProjectBO projectBO) {
    entityManager.merge(projectBO);
    logger.info("Project has updated successfully, Project details=" + projectBO);
    return projectBO;
  }

  @Override
  public CommonListTO<ProjectBO> getProjectList(PaginationCriteria paginationCriteria) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<ProjectBO> criteriaQuery = criteriaBuilder.createQuery(ProjectBO.class);
    Root<ProjectBO> root = criteriaQuery.from(ProjectBO.class);
    criteriaQuery.where(searchPredicates(criteriaBuilder, root, paginationCriteria).toArray(new Predicate[0]));

    // Condition for sorting.
    if (paginationCriteria.getSortField() != null && !paginationCriteria.getSortField().isEmpty()) {
      Order order;
      if (paginationCriteria.getSortType() == 2) {
        order = criteriaBuilder.desc(root.get(paginationCriteria.getSortField()));
      } else {
        order = criteriaBuilder.asc(root.get(paginationCriteria.getSortField()));
      }
      criteriaQuery.orderBy(order);
    } else {
      Order order = criteriaBuilder.desc(root.get("id"));
      criteriaQuery.orderBy(order);
    }

    // Adding Pagination total Count
    CommonListTO<ProjectBO> commonListTO = new CommonListTO<>();
    CriteriaQuery<Long> criteriaQuery2 = criteriaBuilder.createQuery(Long.class);
    Root<ProjectBO> root2 = criteriaQuery2.from(ProjectBO.class);
    criteriaQuery2.where(searchPredicates(criteriaBuilder, root2, paginationCriteria).toArray(new Predicate[0]));
    CriteriaQuery<Long> select = criteriaQuery2.select(criteriaBuilder.count(root2));
    Long count = entityManager.createQuery(select).getSingleResult();
    commonListTO.setTotalRow(count);
    int size = count.intValue();
    int limit = paginationCriteria.getLimit();
    if (limit != 0) {
      commonListTO.setPageCount(((size + limit - 1) / limit));
    } else {
      commonListTO.setPageCount(1);
    }

    TypedQuery<ProjectBO> typedQuery = entityManager.createQuery(criteriaQuery);
    // Condition for paging.
    if (paginationCriteria.getPage() != 0 && paginationCriteria.getLimit() > 0) {
      typedQuery.setFirstResult((paginationCriteria.getPage() - 1) * paginationCriteria.getLimit());
      typedQuery.setMaxResults(paginationCriteria.getLimit());
    }
    List<ProjectBO> list = null;
    try {
      list = typedQuery.getResultList();
    } catch (Exception e) {
      e.printStackTrace();
    }
    commonListTO.setDataList(list);
    return commonListTO;
  }

  private ArrayList<Predicate> searchPredicates(CriteriaBuilder criteriaBuilder, Root<ProjectBO> root, PaginationCriteria paginationCriteria) {
    ArrayList<Predicate> predicates = new ArrayList<>();
    predicates.add(criteriaBuilder.equal(root.get("deleteFlag"), false));
    if (paginationCriteria.getSearchFor() != null) {
      Path<String> pathName = root.get("name");
      predicates.add(criteriaBuilder.like(pathName, "%" + paginationCriteria.getSearchFor() + "%"));
    }
    return predicates;
  }

  @Override
  public ProjectBO getProjectId(Integer id) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<ProjectBO> criteriaQuery = criteriaBuilder.createQuery(ProjectBO.class);

    Root<ProjectBO> root = criteriaQuery.from(ProjectBO.class);
    Predicate predicateForId = criteriaBuilder.equal(root.get("id"), id);
    Predicate predicateForDeleteFlag = criteriaBuilder.equal(root.get("deleteFlag"), false);
    Predicate predicate = criteriaBuilder.and(predicateForId, predicateForDeleteFlag);
    criteriaQuery.where(predicate);
    try {
      ProjectBO projectBO = entityManager.createQuery(criteriaQuery).getSingleResult();
      return projectBO;
    } catch (EmptyResultDataAccessException | NoResultException e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public void deleteGlobalCategory(List<Integer> id) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaUpdate<ProjectBO> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(ProjectBO.class);
    Root<ProjectBO> root = criteriaUpdate.from(ProjectBO.class);
    criteriaUpdate.set("deleteFlag", true);
    criteriaUpdate.where(root.get("id").in(id));
    entityManager.createQuery(criteriaUpdate).executeUpdate();
  }
}
