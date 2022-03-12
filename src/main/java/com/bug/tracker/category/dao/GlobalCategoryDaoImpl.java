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
import java.util.ArrayList;
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
    CommonListTO<GlobalCategoryBO> commonListTO = new CommonListTO<>();
    CriteriaQuery<Long> criteriaQuery2 = criteriaBuilder.createQuery(Long.class);
    Root<GlobalCategoryBO> root2 = criteriaQuery2.from(GlobalCategoryBO.class);
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

    TypedQuery<GlobalCategoryBO> typedQuery = entityManager.createQuery(criteriaQuery);
    // Condition for paging.
    if (paginationCriteria.getPage() != 0 && paginationCriteria.getLimit() > 0) {
      typedQuery.setFirstResult((paginationCriteria.getPage() - 1) * paginationCriteria.getLimit());
      typedQuery.setMaxResults(paginationCriteria.getLimit());
    }
    commonListTO.setDataList(typedQuery.getResultList());
    return commonListTO;
  }

  private ArrayList<Predicate> searchPredicates(CriteriaBuilder criteriaBuilder, Root<GlobalCategoryBO> root, PaginationCriteria paginationCriteria) {
    ArrayList<Predicate> predicates = new ArrayList<>();
    predicates.add(criteriaBuilder.equal(root.get("deleteFlag"), false));
    if (paginationCriteria.getSearchFor() != null) {
      predicates.add(criteriaBuilder.like(root.get("name"), "%" + paginationCriteria.getSearchFor() + "%"));
    }
    return predicates;
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
