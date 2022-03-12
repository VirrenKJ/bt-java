package com.bug.tracker.systemProfile.dao;

import com.bug.tracker.common.object.CommonListTO;
import com.bug.tracker.common.object.PaginationCriteria;
import com.bug.tracker.systemProfile.entity.SystemProfileBO;
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
public class SystemProfileDaoImpl implements SystemProfileDao {

  @PersistenceContext
  private EntityManager entityManager;

  private static final Logger logger = LoggerFactory.getLogger(SystemProfileDaoImpl.class);

  @Override
  public SystemProfileBO addSystemProfile(SystemProfileBO systemProfileBO) {
    entityManager.persist(systemProfileBO);
    logger.info("System Profile has added successfully, System Profile details=" + systemProfileBO);
    return systemProfileBO;
  }

  @Override
  public SystemProfileBO updateSystemProfile(SystemProfileBO systemProfileBO) {
    entityManager.merge(systemProfileBO);
    logger.info("System Profile has updated successfully, System Profile details=" + systemProfileBO);
    return systemProfileBO;
  }

  @Override
  public CommonListTO<SystemProfileBO> getSystemProfileList(PaginationCriteria paginationCriteria) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<SystemProfileBO> criteriaQuery = criteriaBuilder.createQuery(SystemProfileBO.class);
    Root<SystemProfileBO> root = criteriaQuery.from(SystemProfileBO.class);
    criteriaQuery.where(searchPredicates(criteriaBuilder, root, paginationCriteria).toArray(new Predicate[0]));

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
    CommonListTO<SystemProfileBO> commonListTO = new CommonListTO<>();
    CriteriaQuery<Long> criteriaQuery2 = criteriaBuilder.createQuery(Long.class);
    Root<SystemProfileBO> root2 = criteriaQuery2.from(SystemProfileBO.class);
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

    TypedQuery<SystemProfileBO> typedQuery = entityManager.createQuery(criteriaQuery);
    // Condition for paging.
    if (paginationCriteria.getPage() != 0 && paginationCriteria.getLimit() > 0) {
      typedQuery.setFirstResult((paginationCriteria.getPage() - 1) * paginationCriteria.getLimit());
      typedQuery.setMaxResults(paginationCriteria.getLimit());
    }
    commonListTO.setDataList(typedQuery.getResultList());
    return commonListTO;
  }

  private ArrayList<Predicate> searchPredicates(CriteriaBuilder criteriaBuilder, Root<SystemProfileBO> root, PaginationCriteria paginationCriteria) {
    ArrayList<Predicate> predicates = new ArrayList<>();
    predicates.add(criteriaBuilder.equal(root.get("deleteFlag"), false));
    if (paginationCriteria.getSearchFor() != null) {
      predicates.add(criteriaBuilder.like(root.get("osName"), "%" + paginationCriteria.getSearchFor() + "%"));
    }
    return predicates;
  }

  @Override
  public SystemProfileBO getSystemProfileId(Integer id) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<SystemProfileBO> criteriaQuery = criteriaBuilder.createQuery(SystemProfileBO.class);

    Root<SystemProfileBO> root = criteriaQuery.from(SystemProfileBO.class);
    Predicate predicateForId = criteriaBuilder.equal(root.get("id"), id);
    Predicate predicateForDeleteFlag = criteriaBuilder.equal(root.get("deleteFlag"), false);
    Predicate predicate = criteriaBuilder.and(predicateForId, predicateForDeleteFlag);
    criteriaQuery.where(predicate);
    try {
      SystemProfileBO systemProfileBO = entityManager.createQuery(criteriaQuery).getSingleResult();
      return systemProfileBO;
    } catch (EmptyResultDataAccessException | NoResultException e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public void deleteGlobalCategory(List<Integer> id) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaUpdate<SystemProfileBO> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(SystemProfileBO.class);
    Root<SystemProfileBO> root = criteriaUpdate.from(SystemProfileBO.class);
    criteriaUpdate.set("deleteFlag", true);
    criteriaUpdate.where(root.get("id").in(id));
    entityManager.createQuery(criteriaUpdate).executeUpdate();
  }
}
