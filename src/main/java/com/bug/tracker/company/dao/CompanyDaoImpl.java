package com.bug.tracker.company.dao;

import com.bug.tracker.common.object.CommonListTO;
import com.bug.tracker.common.object.PaginationCriteria;
import com.bug.tracker.company.entity.CompanyBO;
import com.bug.tracker.company.entity.CompanyCustomBO;
import com.bug.tracker.user.entity.UserDetailBO;
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
  public CompanyBO update(CompanyBO companyBO) {
    entityManager.merge(companyBO);
    logger.info("Company has updated successfully, Company details=" + companyBO);
    return companyBO;
  }

  @Override
  public CompanyCustomBO copyCompanyToTenant(CompanyCustomBO companyBO) {
    entityManager.persist(companyBO);
    logger.info("Company has copied successfully, Company details=" + companyBO);
    return companyBO;
  }

  @Override
  public CommonListTO<CompanyBO> getList(PaginationCriteria paginationCriteria) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CompanyBO> criteriaQuery = criteriaBuilder.createQuery(CompanyBO.class);
    Root<CompanyBO> root = criteriaQuery.from(CompanyBO.class);
    Predicate predicate = criteriaBuilder.equal(root.get("deleteFlag"), false);

    if (paginationCriteria.getId() != null) {
      Predicate predicateId = criteriaBuilder.equal(root.get("userId"), paginationCriteria.getId());
      predicate = criteriaBuilder.and(predicate, predicateId);
    }
    criteriaQuery.where(predicate);

    //condition for search
    if (paginationCriteria.getSearchFor() != null) {
      Path<String> pathName = root.get("name");
      Predicate predicateForName = criteriaBuilder.like(pathName, "%" + paginationCriteria.getSearchFor() + "%");
      criteriaQuery.where(predicateForName);
    }

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
    CommonListTO<CompanyBO> commonListTO = new CommonListTO<>();
    CriteriaQuery<Long> criteriaQuery2 = criteriaBuilder.createQuery(Long.class);
    Root<CompanyBO> root2 = criteriaQuery2.from(CompanyBO.class);
    Predicate predicate2 = criteriaBuilder.equal(root2.get("deleteFlag"), false);
    if (paginationCriteria.getId() != null) {
      Predicate predicateId = criteriaBuilder.equal(root2.get("userId"), paginationCriteria.getId());
      predicate2 = criteriaBuilder.and(predicate2, predicateId);
    }
    criteriaQuery2.where(predicate2);
    CriteriaQuery<Long> select = criteriaQuery2.select(criteriaBuilder.count(root2));
    Long count = entityManager.createQuery(select).getSingleResult();
    commonListTO.setTotalRow(count);
    int size = count.intValue();
    int limit = paginationCriteria.getLimit();
    if (limit != 0) {
      commonListTO.setPageCount((size + limit - 1) / limit);
    } else {
      commonListTO.setPageCount(1);
    }

    TypedQuery<CompanyBO> typedQuery = entityManager.createQuery(criteriaQuery);
    // Condition for paging.
    if (paginationCriteria.getPage() != 0 && paginationCriteria.getLimit() > 0) {
      typedQuery.setFirstResult((paginationCriteria.getPage() - 1) * paginationCriteria.getLimit());
      typedQuery.setMaxResults(paginationCriteria.getLimit());
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
  public List<?> getListByEmployeeId(Integer id) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<UserDetailBO> criteriaQuery = criteriaBuilder.createQuery(UserDetailBO.class);
    Root<UserDetailBO> root = criteriaQuery.from(UserDetailBO.class);

    criteriaQuery.select(root.get("companies"));
    Predicate predicateForId = criteriaBuilder.equal(root.get("id"), id);
    Predicate predicateForDeleteFlag = criteriaBuilder.equal(root.get("deleteFlag"), false);
    Predicate predicate = criteriaBuilder.and(predicateForId, predicateForDeleteFlag);
    criteriaQuery.where(predicate);
    return entityManager.createQuery(criteriaQuery).getResultList();
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
