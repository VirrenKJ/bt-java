package com.bug.tracker.user.dao;

import com.bug.tracker.common.object.CommonListTO;
import com.bug.tracker.common.object.SearchCriteriaObj;
import com.bug.tracker.company.entity.CompanyBO;
import com.bug.tracker.user.entity.UserBO;
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
public class UserDaoImpl implements UserDao {

  @PersistenceContext
  private EntityManager entityManager;

  private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

  @Override
  public UserBO add(UserBO userBO) {
    entityManager.persist(userBO);
    logger.info("User has added successfully, User details=" + userBO);
    return userBO;
  }

  @Override
  public UserBO update(UserBO userBO) {
    entityManager.merge(userBO);
    logger.info("User has updated successfully, User details=" + userBO);
    return userBO;
  }

  @Override
  public CommonListTO<UserBO> getList(SearchCriteriaObj searchCriteriaObj) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<UserBO> criteriaQuery = criteriaBuilder.createQuery(UserBO.class);
    Root<UserBO> root = criteriaQuery.from(UserBO.class);
    Predicate predicate = criteriaBuilder.equal(root.get("deleteFlag"), false);
    criteriaQuery.where(predicate);

    //condition for search
    if (searchCriteriaObj.getSearchFieldsObj() != null) {
      if (searchCriteriaObj.getSearchFieldsObj().getSearchFor() != null) {
        if (searchCriteriaObj.getSearchFieldsObj().getSearchFor().contains("\\")) {
          searchCriteriaObj.getSearchFieldsObj().setSearchFor(
                  searchCriteriaObj.getSearchFieldsObj().getSearchFor().replace("\\", "\\\\\\\\"));
        }
        Path<String> pathUsername = root.get("username");
        Predicate predicateUsername = criteriaBuilder.like(pathUsername, "%" + searchCriteriaObj.getSearchFieldsObj().getSearchFor() + "%");
        Path<String> pathEmail = root.get("email");
        Predicate predicateEmail = criteriaBuilder.like(pathEmail, "%" + searchCriteriaObj.getSearchFieldsObj().getSearchFor() + "%");
        Predicate predicateSearch = criteriaBuilder.or(predicateUsername, predicateEmail);
        criteriaQuery.where(criteriaBuilder.and(predicateSearch, predicate));
        if (searchCriteriaObj.getSearchFieldsObj().getId() != null) {
          Predicate predicateId = criteriaBuilder.notEqual(root.get("id"), searchCriteriaObj.getSearchFieldsObj().getId());
          criteriaQuery.where(criteriaBuilder.and(predicateSearch, predicate, predicateId));
        }
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
    CommonListTO<UserBO> commonListTO = new CommonListTO<>();
    CriteriaQuery<Long> criteriaQuery2 = criteriaBuilder.createQuery(Long.class);
    Root<UserBO> root2 = criteriaQuery2.from(UserBO.class);
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

    TypedQuery<UserBO> typedQuery = entityManager.createQuery(criteriaQuery);
    // Condition for paging.
    if (searchCriteriaObj.getPage() != 0 && searchCriteriaObj.getLimit() > 0) {
      typedQuery.setFirstResult((searchCriteriaObj.getPage() - 1) * searchCriteriaObj.getLimit());
      typedQuery.setMaxResults(searchCriteriaObj.getLimit());
    }
    commonListTO.setDataList(typedQuery.getResultList());
    return commonListTO;
  }

  /*
  SELECT u.*, c.name
  FROM user as u JOIN company_employee as ce ON u.id = ce.user_id
  JOIN company AS c ON ce.company_id = c.id
  WHERE c.id IN (28, 29, 32, 34, 35, 36, 76, 87)
  */
  @Override
  public CommonListTO<UserDetailBO> getEmployeeList(SearchCriteriaObj searchCriteriaObj) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CompanyBO> criteriaQuery = criteriaBuilder.createQuery(CompanyBO.class);
    Root<CompanyBO> root = criteriaQuery.from(CompanyBO.class);
    criteriaQuery.select(root.get("userDetails")).distinct(true);
    Predicate predicateIds = root.get("id").in(searchCriteriaObj.getSearchFieldsObj().getIds());
    Predicate predicateDelete = criteriaBuilder.equal(root.get("deleteFlag"), false);
    criteriaQuery.where(criteriaBuilder.and(predicateIds, predicateDelete));

    //condition for search
    if (searchCriteriaObj.getSearchFieldsObj() != null) {
      if (searchCriteriaObj.getSearchFieldsObj().getSearchFor() != null) {
        if (searchCriteriaObj.getSearchFieldsObj().getSearchFor().contains("\\")) {
          searchCriteriaObj.getSearchFieldsObj().setSearchFor(
                  searchCriteriaObj.getSearchFieldsObj().getSearchFor().replace("\\", "\\\\\\\\"));
        }
        Path<String> pathUsername = root.get("username");
        Predicate predicateUsername = criteriaBuilder.like(pathUsername, "%" + searchCriteriaObj.getSearchFieldsObj().getSearchFor() + "%");
        Path<String> pathEmail = root.get("email");
        Predicate predicateEmail = criteriaBuilder.like(pathEmail, "%" + searchCriteriaObj.getSearchFieldsObj().getSearchFor() + "%");
        Predicate predicateSearch = criteriaBuilder.or(predicateUsername, predicateEmail);
        criteriaQuery.where(predicateSearch);
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

//     Adding Pagination total Count
    CommonListTO<UserDetailBO> commonListTO = new CommonListTO<>();
    CriteriaQuery<Long> criteriaQuery2 = criteriaBuilder.createQuery(Long.class);
    Root<CompanyBO> root2 = criteriaQuery2.from(CompanyBO.class);
    Join<CompanyBO, UserDetailBO> lineJoin2 = root2.join("userDetails");
    Predicate predicateIds2 = root2.get("id").in(searchCriteriaObj.getSearchFieldsObj().getIds());
    Predicate predicateDelete2 = criteriaBuilder.equal(root2.get("deleteFlag"), false);
    criteriaQuery2.where(criteriaBuilder.and(predicateIds2, predicateDelete2));
    CriteriaQuery<Long> select = criteriaQuery2.select(criteriaBuilder.countDistinct(lineJoin2));
//    select.distinct(true);
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
    List<?> list = typedQuery.getResultList();
    commonListTO.setDataListUnknownType(list);
    return commonListTO;
  }

  @Override
  public UserBO getById(Integer id) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<UserBO> criteriaQuery = criteriaBuilder.createQuery(UserBO.class);

    Root<UserBO> root = criteriaQuery.from(UserBO.class);
    Predicate predicateForId = criteriaBuilder.equal(root.get("id"), id);
    Predicate predicateForDeleteFlag = criteriaBuilder.equal(root.get("deleteFlag"), false);
    Predicate predicate = criteriaBuilder.and(predicateForId, predicateForDeleteFlag);
    criteriaQuery.where(predicate);
    UserBO userBO = null;
    try {
      userBO = entityManager.createQuery(criteriaQuery).getSingleResult();
    } catch (EmptyResultDataAccessException | NoResultException e) {
      e.printStackTrace();
    }
    return userBO;
  }

  @Override
  public UserBO getByUsername(String username) throws Exception {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<UserBO> criteriaQuery = criteriaBuilder.createQuery(UserBO.class);

    Root<UserBO> root = criteriaQuery.from(UserBO.class);
    Predicate predicateForId = criteriaBuilder.equal(root.get("username"), username);
    Predicate predicateForDeleteFlag = criteriaBuilder.equal(root.get("deleteFlag"), false);
    Predicate predicate = criteriaBuilder.and(predicateForId, predicateForDeleteFlag);
    criteriaQuery.where(predicate);
    UserBO userBO = null;
    try {
      userBO = entityManager.createQuery(criteriaQuery).getSingleResult();
    } catch (EmptyResultDataAccessException | NoResultException e) {
      e.printStackTrace();
    }
    return userBO;
  }

  @Override
  public void delete(List<Integer> id) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaUpdate<UserBO> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(UserBO.class);
    Root<UserBO> root = criteriaUpdate.from(UserBO.class);
    criteriaUpdate.set("deleteFlag", true);
    criteriaUpdate.where(root.get("id").in(id));
    entityManager.createQuery(criteriaUpdate).executeUpdate();
  }
}
