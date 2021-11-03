package com.bug.tracker.company.dao;

import com.bug.tracker.common.object.CommonListTO;
import com.bug.tracker.common.object.SearchCriteriaObj;
import com.bug.tracker.company.entity.CompanyBO;
import com.bug.tracker.company.entity.CompanyDetailsNewTenantBO;
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
public class CompanyDaoImpl implements CompanyDao {

  @PersistenceContext
  private EntityManager entityManager;

  private static final Logger logger = LoggerFactory.getLogger(CompanyDaoImpl.class);

  @Override
  public CompanyBO add(CompanyBO companyBO) {
    entityManager.persist(companyBO);
    logger.info("Company has added successfully, Company details=" + companyBO);
    return companyBO;
  }

  @Override
  public CompanyDetailsNewTenantBO add(CompanyDetailsNewTenantBO companyDetailsNewTenantBO) {
    entityManager.persist(companyDetailsNewTenantBO);
    logger.info("Company has added successfully, Company details=" + companyDetailsNewTenantBO);
    return companyDetailsNewTenantBO;
  }

  @Override
  public CompanyBO update(CompanyBO companyBO) {
    entityManager.merge(companyBO);
    logger.info("Company has updated successfully, Company details=" + companyBO);
    return companyBO;
  }

  @Override
  public CommonListTO<CompanyBO> getList(SearchCriteriaObj searchCriteriaObj) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CompanyBO> criteriaQuery = criteriaBuilder.createQuery(CompanyBO.class);
    Root<CompanyBO> root = criteriaQuery.from(CompanyBO.class);
    criteriaQuery.where(criteriaBuilder.equal(root.get("deleteFlag"), false));

    //condition for search
    if (searchCriteriaObj.getSearchFieldsObj() != null) {
      if (searchCriteriaObj.getSearchFieldsObj().getSearchFor() != null) {
        Path<String> pathName = root.get("name");
        Predicate predicateForName = criteriaBuilder.like(pathName, "%" + searchCriteriaObj.getSearchFieldsObj().getSearchFor() + "%");
        criteriaQuery.where(predicateForName);
      }
    }

    // Condition for sorting.
    Order order;
    if (searchCriteriaObj.getSortField() != null && !searchCriteriaObj.getSortField().isEmpty()) {
      if (searchCriteriaObj.getSortType() == 2) {
        order = criteriaBuilder.desc(root.get(searchCriteriaObj.getSortField()));
      } else {
        order = criteriaBuilder.asc(root.get(searchCriteriaObj.getSortField()));
      }
    } else {
      order = criteriaBuilder.desc(root.get("id"));
    }
    criteriaQuery.orderBy(order);

    // Adding Pagination total Count
    CommonListTO<CompanyBO> commonListTO = new CommonListTO<>();
    CriteriaQuery<Long> criteriaQuery2 = criteriaBuilder.createQuery(Long.class);
    Root<CompanyBO> root2 = criteriaQuery2.from(CompanyBO.class);
    criteriaQuery2.where(criteriaBuilder.equal(root2.get("deleteFlag"), false));
    CriteriaQuery<Long> select = criteriaQuery2.select(criteriaBuilder.count(root2));
    Long count = entityManager.createQuery(select).getSingleResult();
    commonListTO.setTotalRow(count);
    int size = count.intValue();
    int limit = searchCriteriaObj.getLimit();
    if (limit != 0) {
      commonListTO.setPageCount((size + limit - 1) / limit);
    } else {
      commonListTO.setPageCount(1);
    }

    TypedQuery<CompanyBO> typedQuery = entityManager.createQuery(criteriaQuery);
    // Condition for paging.
    if (searchCriteriaObj.getPage() != 0 && searchCriteriaObj.getLimit() > 0) {
      typedQuery.setFirstResult((searchCriteriaObj.getPage() - 1) * searchCriteriaObj.getLimit());
      typedQuery.setMaxResults(searchCriteriaObj.getLimit());
    }
    commonListTO.setDataList(typedQuery.getResultList());
    return commonListTO;
  }

  @Override
  public CommonListTO<CompanyBO> getBusinessList(SearchCriteriaObj searchCriteriaObj) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CompanyBO> criteriaQuery = criteriaBuilder.createQuery(CompanyBO.class);
    Root<CompanyBO> root = criteriaQuery.from(CompanyBO.class);
    Predicate predicateDelete = criteriaBuilder.equal(root.get("deleteFlag"), false);
    Predicate predicateId = criteriaBuilder.equal(root.get("userId"), searchCriteriaObj.getSearchFieldsObj().getId());
    Predicate predicate = criteriaBuilder.and(predicateDelete, predicateId);
    criteriaQuery.where(predicate);

    //condition for search
    if (searchCriteriaObj.getSearchFieldsObj() != null) {
      if (searchCriteriaObj.getSearchFieldsObj().getSearchFor() != null) {
        Path<String> pathName = root.get("name");
        Predicate predicateForName = criteriaBuilder.like(pathName, "%" + searchCriteriaObj.getSearchFieldsObj().getSearchFor() + "%");
        criteriaQuery.where(predicateForName);
      }
    }

    // Condition for sorting.
    Order order;
    if (searchCriteriaObj.getSortField() != null && !searchCriteriaObj.getSortField().isEmpty()) {
      if (searchCriteriaObj.getSortType() == 2) {
        order = criteriaBuilder.desc(root.get(searchCriteriaObj.getSortField()));
      } else {
        order = criteriaBuilder.asc(root.get(searchCriteriaObj.getSortField()));
      }
    } else {
      order = criteriaBuilder.desc(root.get("id"));
    }
    criteriaQuery.orderBy(order);

    // Adding Pagination total Count
    CommonListTO<CompanyBO> commonListTO = new CommonListTO<>();
    CriteriaQuery<Long> criteriaQuery2 = criteriaBuilder.createQuery(Long.class);
    Root<CompanyBO> root2 = criteriaQuery2.from(CompanyBO.class);
    Predicate predicateDeleteCount = criteriaBuilder.equal(root2.get("deleteFlag"), false);
    Predicate predicateIdCount = criteriaBuilder.equal(root2.get("userId"), searchCriteriaObj.getSearchFieldsObj().getId());
    Predicate predicateCount = criteriaBuilder.and(predicateDeleteCount, predicateIdCount);
    criteriaQuery2.where(predicateCount);
    CriteriaQuery<Long> select = criteriaQuery2.select(criteriaBuilder.count(root2));
    Long count = entityManager.createQuery(select).getSingleResult();
    commonListTO.setTotalRow(count);
    int size = count.intValue();
    int limit = searchCriteriaObj.getLimit();
    if (limit != 0) {
      commonListTO.setPageCount((size + limit - 1) / limit);
    } else {
      commonListTO.setPageCount(1);
    }

    TypedQuery<CompanyBO> typedQuery = entityManager.createQuery(criteriaQuery);
    // Condition for paging.
    if (searchCriteriaObj.getPage() != 0 && searchCriteriaObj.getLimit() > 0) {
      typedQuery.setFirstResult((searchCriteriaObj.getPage() - 1) * searchCriteriaObj.getLimit());
      typedQuery.setMaxResults(searchCriteriaObj.getLimit());
    }
    commonListTO.setDataList(typedQuery.getResultList());
    return commonListTO;
  }

  @Override
  public CompanyBO getById(Integer id) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CompanyBO> criteriaQuery = criteriaBuilder.createQuery(CompanyBO.class);

    Root<CompanyBO> root = criteriaQuery.from(CompanyBO.class);
    Predicate predicateForId = criteriaBuilder.equal(root.get("id"), id);
    Predicate predicateForDeleteFlag = criteriaBuilder.equal(root.get("deleteFlag"), false);
    Predicate predicate = criteriaBuilder.and(predicateForId, predicateForDeleteFlag);
    criteriaQuery.where(predicate);
    CompanyBO companyBO = null;
    try {
      companyBO = entityManager.createQuery(criteriaQuery).getSingleResult();
    } catch (EmptyResultDataAccessException | NoResultException e) {
      e.printStackTrace();
    }
    return companyBO;
  }

  @Override
  public void delete(List<Integer> id) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaUpdate<CompanyBO> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(CompanyBO.class);
    Root<CompanyBO> root = criteriaUpdate.from(CompanyBO.class);
    criteriaUpdate.set("deleteFlag", true);
    criteriaUpdate.where(root.get("id").in(id));
    entityManager.createQuery(criteriaUpdate).executeUpdate();
  }
}
