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
    criteriaQuery.where(criteriaBuilder.equal(root.get("deleteFlag"), false));

    //condition for search
    if (paginationCriteria.getSearchFor() != null) {
      Path<String> pathName = root.get("name");
      Predicate predicateForName = criteriaBuilder.like(pathName, "%" + paginationCriteria.getSearchFor() + "%");
      criteriaQuery.where(predicateForName);
    }

    // Condition for sorting.
    if (paginationCriteria.getSortField() != null && !paginationCriteria.getSortField().isEmpty()) {
      Order order = null;
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
    CommonListTO<SystemProfileBO> commonListTO = new CommonListTO<>();
    CriteriaQuery<Long> criteriaQuery2 = criteriaBuilder.createQuery(Long.class);
    Root<SystemProfileBO> root2 = criteriaQuery2.from(SystemProfileBO.class);
    criteriaQuery2.where(criteriaBuilder.equal(root2.get("deleteFlag"), false));
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
