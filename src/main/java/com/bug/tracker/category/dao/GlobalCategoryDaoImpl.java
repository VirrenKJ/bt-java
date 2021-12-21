package com.bug.tracker.category.dao;

import com.bug.tracker.category.entity.GlobalCategoryBO;
import com.bug.tracker.common.object.CommonListTO;
import com.bug.tracker.common.object.PaginationCriteria;
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
public class GlobalCategoryDaoImpl implements GlobalCategoryDao {

  @PersistenceContext
  private EntityManager entityManager;

  private static final Logger logger = LoggerFactory.getLogger(GlobalCategoryDaoImpl.class);

  @Override
  public GlobalCategoryBO addGlobalCategory(GlobalCategoryBO globalCategoryBO) {
    entityManager.persist(globalCategoryBO);
    logger.info("Global Category has added successfully, Global Category details=" + globalCategoryBO);
    return globalCategoryBO;
  }

  @Override
  public GlobalCategoryBO updateGlobalCategory(GlobalCategoryBO globalCategoryBO) {
    entityManager.merge(globalCategoryBO);
    logger.info("Global Category has updated successfully, Global Category details=" + globalCategoryBO);
    return globalCategoryBO;
  }

  @Override
  public CommonListTO<GlobalCategoryBO> getGlobalCategoryList(PaginationCriteria paginationCriteria) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<GlobalCategoryBO> criteriaQuery = criteriaBuilder.createQuery(GlobalCategoryBO.class);
    Root<GlobalCategoryBO> root = criteriaQuery.from(GlobalCategoryBO.class);
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
    CommonListTO<GlobalCategoryBO> commonListTO = new CommonListTO<>();
    CriteriaQuery<Long> criteriaQuery2 = criteriaBuilder.createQuery(Long.class);
    Root<GlobalCategoryBO> root2 = criteriaQuery2.from(GlobalCategoryBO.class);
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

    TypedQuery<GlobalCategoryBO> typedQuery = entityManager.createQuery(criteriaQuery);
    // Condition for paging.
    if (paginationCriteria.getPage() != 0 && paginationCriteria.getLimit() > 0) {
      typedQuery.setFirstResult((paginationCriteria.getPage() - 1) * paginationCriteria.getLimit());
      typedQuery.setMaxResults(paginationCriteria.getLimit());
    }
    commonListTO.setDataList(typedQuery.getResultList());
    return commonListTO;
  }

  @Override
  public GlobalCategoryBO getGlobalCategoryById(Integer id) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<GlobalCategoryBO> criteriaQuery = criteriaBuilder.createQuery(GlobalCategoryBO.class);

    Root<GlobalCategoryBO> root = criteriaQuery.from(GlobalCategoryBO.class);
    Predicate predicateForId = criteriaBuilder.equal(root.get("id"), id);
    Predicate predicateForDeleteFlag = criteriaBuilder.equal(root.get("deleteFlag"), false);
    Predicate predicate = criteriaBuilder.and(predicateForId, predicateForDeleteFlag);
    criteriaQuery.where(predicate);
    try {
      GlobalCategoryBO globalCategoryBO = entityManager.createQuery(criteriaQuery).getSingleResult();
      return globalCategoryBO;
    } catch (EmptyResultDataAccessException | NoResultException e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public void deleteGlobalCategory(List<Integer> id) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaUpdate<GlobalCategoryBO> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(GlobalCategoryBO.class);
    Root<GlobalCategoryBO> root = criteriaUpdate.from(GlobalCategoryBO.class);
    criteriaUpdate.set("deleteFlag", true);
    criteriaUpdate.where(root.get("id").in(id));
    entityManager.createQuery(criteriaUpdate).executeUpdate();
  }
}
