package com.bug.tracker.issue.dao;

import com.bug.tracker.common.object.CommonListTO;
import com.bug.tracker.common.object.PaginationCriteria;
import com.bug.tracker.issue.entity.IssueBO;
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
public class IssueDaoImpl implements IssueDao {

  @PersistenceContext
  private EntityManager entityManager;

  private static final Logger logger = LoggerFactory.getLogger(IssueDaoImpl.class);

  @Override
  public IssueBO addIssue(IssueBO issueBO) {
    entityManager.persist(issueBO);
    logger.info("Issue has added successfully, Issue details=" + issueBO);
    return issueBO;
  }

  @Override
  public IssueBO updateIssue(IssueBO issueBO) {
    entityManager.merge(issueBO);
    logger.info("Issue has updated successfully, Issue details=" + issueBO);
    return issueBO;
  }

  @Override
  public CommonListTO<IssueBO> getIssueList(PaginationCriteria paginationCriteria) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<IssueBO> criteriaQuery = criteriaBuilder.createQuery(IssueBO.class);
    Root<IssueBO> root = criteriaQuery.from(IssueBO.class);
    ArrayList<Predicate> predicates = searchPredicates(criteriaBuilder, root, paginationCriteria);
    criteriaQuery.where(predicates.toArray(new Predicate[0]));

    // Condition for sorting.
    Order order;
    if (paginationCriteria.getSortField() != null && !paginationCriteria.getSortField().isEmpty()) {
      if (paginationCriteria.getSortType() == 2) {
        order = criteriaBuilder.desc(root.get(paginationCriteria.getSortField()));
      } else {
        order = criteriaBuilder.asc(root.get(paginationCriteria.getSortField()));
      }
    } else {
      order = criteriaBuilder.desc(root.get("id"));
    }
    criteriaQuery.orderBy(order);

    // Adding Pagination total Count
    CommonListTO<IssueBO> commonListTO = new CommonListTO<>();
    CriteriaQuery<Long> criteriaQuery2 = criteriaBuilder.createQuery(Long.class);
    Root<IssueBO> root2 = criteriaQuery2.from(IssueBO.class);
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

    TypedQuery<IssueBO> typedQuery = entityManager.createQuery(criteriaQuery);
    // Condition for paging.
    if (paginationCriteria.getPage() != 0 && paginationCriteria.getLimit() > 0) {
      typedQuery.setFirstResult((paginationCriteria.getPage() - 1) * paginationCriteria.getLimit());
      typedQuery.setMaxResults(paginationCriteria.getLimit());
    }
    commonListTO.setDataList(typedQuery.getResultList());
    return commonListTO;
  }

  private ArrayList<Predicate> searchPredicates(CriteriaBuilder criteriaBuilder, Root<IssueBO> root, PaginationCriteria paginationCriteria) {
    ArrayList<Predicate> predicates = new ArrayList<>();
    predicates.add(criteriaBuilder.equal(root.get("deleteFlag"), false));
    if (paginationCriteria.getAssignedId() != null) {
      predicates.add(criteriaBuilder.equal(root.get("assignedId"), paginationCriteria.getAssignedId()));
    }
    if (paginationCriteria.getReportedById() != null) {
      predicates.add(criteriaBuilder.equal(root.get("reportedById"), paginationCriteria.getReportedById()));
    }
    if (paginationCriteria.getSearchFor() != null) {
      Path<String> summary = root.get("summary");
      predicates.add(criteriaBuilder.like(summary, "%" + paginationCriteria.getSearchFor() + "%"));
    }
    return predicates;
  }

  @Override
  public IssueBO getIssueId(Integer id) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<IssueBO> criteriaQuery = criteriaBuilder.createQuery(IssueBO.class);

    Root<IssueBO> root = criteriaQuery.from(IssueBO.class);
    Predicate predicateForId = criteriaBuilder.equal(root.get("id"), id);
    Predicate predicateForDeleteFlag = criteriaBuilder.equal(root.get("deleteFlag"), false);
    Predicate predicate = criteriaBuilder.and(predicateForId, predicateForDeleteFlag);
    criteriaQuery.where(predicate);
    try {
      IssueBO issueBO = entityManager.createQuery(criteriaQuery).getSingleResult();
      return issueBO;
    } catch (EmptyResultDataAccessException | NoResultException e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public void deleteGlobalCategory(List<Integer> id) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaUpdate<IssueBO> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(IssueBO.class);
    Root<IssueBO> root = criteriaUpdate.from(IssueBO.class);
    criteriaUpdate.set("deleteFlag", true);
    criteriaUpdate.where(root.get("id").in(id));
    entityManager.createQuery(criteriaUpdate).executeUpdate();
  }
}
