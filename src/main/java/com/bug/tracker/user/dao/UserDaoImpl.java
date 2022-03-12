package com.bug.tracker.user.dao;

import com.bug.tracker.common.object.CommonListTO;
import com.bug.tracker.common.object.PaginationCriteria;
import com.bug.tracker.company.entity.CompanyBO;
import com.bug.tracker.user.entity.UserBO;
import com.bug.tracker.user.entity.UserBasicBO;
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
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

  @PersistenceContext
  private EntityManager entityManager;

  private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

  @Override
  public UserBO addUser(UserBO userBO) {
    entityManager.persist(userBO);
    logger.info("User has added successfully, User details=" + userBO);
    return userBO;
  }

  @Override
  public UserBasicBO copyUserToTenant(UserBasicBO userBasicBO) {
    entityManager.persist(userBasicBO);
    logger.info("User has copied successfully, User details=" + userBasicBO);
    return userBasicBO;
  }

  @Override
  public UserBasicBO updateUserToTenant(UserBasicBO userBasicBO) {
    entityManager.merge(userBasicBO);
    logger.info("User has updated successfully, User details=" + userBasicBO);
    return userBasicBO;
  }

  @Override
  public UserBO updateUser(UserBO userBO) {
    entityManager.merge(userBO);
    logger.info("User has updated successfully, User details=" + userBO);
    return userBO;
  }

  @Override
  public CommonListTO<UserBO> getUserList(PaginationCriteria paginationCriteria) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<UserBO> criteriaQuery = criteriaBuilder.createQuery(UserBO.class);
    Root<UserBO> root = criteriaQuery.from(UserBO.class);
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
    CommonListTO<UserBO> commonListTO = new CommonListTO<>();
    CriteriaQuery<Long> criteriaQuery2 = criteriaBuilder.createQuery(Long.class);
    Root<UserBO> root2 = criteriaQuery2.from(UserBO.class);
    criteriaQuery2.where(searchPredicates(criteriaBuilder,root2,paginationCriteria).toArray(new Predicate[0]));
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

    TypedQuery<UserBO> typedQuery = entityManager.createQuery(criteriaQuery);
    // Condition for paging.
    if (paginationCriteria.getPage() != 0 && paginationCriteria.getLimit() > 0) {
      typedQuery.setFirstResult((paginationCriteria.getPage() - 1) * paginationCriteria.getLimit());
      typedQuery.setMaxResults(paginationCriteria.getLimit());
    }
    commonListTO.setDataList(typedQuery.getResultList());
    return commonListTO;
  }

  private ArrayList<Predicate> searchPredicates(CriteriaBuilder criteriaBuilder, Root<UserBO> root, PaginationCriteria paginationCriteria) {
    ArrayList<Predicate> predicates = new ArrayList<>();
    predicates.add(criteriaBuilder.equal(root.get("deleteFlag"), false));
    if (paginationCriteria.getSearchFor() != null) {
      if (paginationCriteria.getSearchFor().contains("\\")) {
        paginationCriteria.setSearchFor(
                paginationCriteria.getSearchFor().replace("\\", "\\\\\\\\"));
      }
      Predicate predicateUsername = criteriaBuilder.like(root.get("username"), "%" + paginationCriteria.getSearchFor() + "%");
      Predicate predicateEmail = criteriaBuilder.like(root.get("email"), "%" + paginationCriteria.getSearchFor() + "%");
      Predicate predicateSearch = criteriaBuilder.or(predicateUsername, predicateEmail);
      predicates.add(predicateSearch);
      if (paginationCriteria.getId() != null) {
        predicates.add(criteriaBuilder.notEqual(root.get("id"), paginationCriteria.getId()));
      }
    }
    return predicates;
  }

  /*
  SELECT u.*, c.name
  FROM user as u JOIN company_employee as ce ON u.id = ce.user_id
  JOIN company AS c ON ce.company_id = c.id
  WHERE c.id IN (28, 29, 32, 34, 35, 36, 76, 87)
  */
  @Override
  public CommonListTO<UserDetailBO> getEmployeeList(PaginationCriteria paginationCriteria) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CompanyBO> criteriaQuery = criteriaBuilder.createQuery(CompanyBO.class);
    Root<CompanyBO> root = criteriaQuery.from(CompanyBO.class);
    criteriaQuery.select(root.get("userDetails")).distinct(true);

    Join<CompanyBO, UserDetailBO> lineJoin = root.join("userDetails");
    ArrayList<Predicate> predicates = new ArrayList<>();
    predicates.add(root.get("id").in(paginationCriteria.getIds()));
    predicates.add(criteriaBuilder.equal(root.get("deleteFlag"), false));
    criteriaQuery.where(predicates.toArray(new Predicate[0]));

    //condition for search
    if (paginationCriteria.getSearchFor() != null) {
      if (paginationCriteria.getSearchFor().contains("\\")) {
        paginationCriteria.setSearchFor(
                paginationCriteria.getSearchFor().replace("\\", "\\\\\\\\"));
      }
      Path<String> pathUsername = lineJoin.get("username");
      Predicate predicateUsername = criteriaBuilder.like(pathUsername, "%" + paginationCriteria.getSearchFor() + "%");
      Path<String> pathEmail = lineJoin.get("email");
      Predicate predicateEmail = criteriaBuilder.like(pathEmail, "%" + paginationCriteria.getSearchFor() + "%");
      predicates.add(criteriaBuilder.or(predicateUsername, predicateEmail));
      criteriaQuery.where(predicates.toArray(new Predicate[0]));
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

//     Adding Pagination total Count
    CommonListTO<UserDetailBO> commonListTO = new CommonListTO<>();
    CriteriaQuery<Long> criteriaQuery2 = criteriaBuilder.createQuery(Long.class);
    Root<CompanyBO> root2 = criteriaQuery2.from(CompanyBO.class);

    Join<CompanyBO, UserDetailBO> lineJoin2 = root2.join("userDetails");
    Predicate predicateIds2 = root2.get("id").in(paginationCriteria.getIds());
    Predicate predicateDelete2 = criteriaBuilder.equal(root2.get("deleteFlag"), false);
    criteriaQuery2.where(criteriaBuilder.and(predicateIds2, predicateDelete2));

    CriteriaQuery<Long> select = criteriaQuery2.select(criteriaBuilder.countDistinct(lineJoin2));
//    select.distinct(true);
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
    List<?> list = typedQuery.getResultList();
    commonListTO.setDataListUnknownType(list);
    return commonListTO;
  }

  @Override
  public CommonListTO<UserDetailBO> getEmployeeListByCompany(PaginationCriteria paginationCriteria) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<UserDetailBO> criteriaQuery = criteriaBuilder.createQuery(UserDetailBO.class);
    Root<UserDetailBO> root = criteriaQuery.from(UserDetailBO.class);

    Join<UserDetailBO, CompanyBO> lineJoin = root.join("companies");
    Predicate predicateCompany = criteriaBuilder.equal(lineJoin.get("id"), paginationCriteria.getId());
    Predicate predicateDelete = criteriaBuilder.equal(root.get("deleteFlag"), false);
    Predicate predicate = criteriaBuilder.and(predicateCompany, predicateDelete);
    criteriaQuery.where(predicate);

    //condition for search
    if (paginationCriteria.getSearchFor() != null) {
      if (paginationCriteria.getSearchFor().contains("\\")) {
        paginationCriteria.setSearchFor(
                paginationCriteria.getSearchFor().replace("\\", "\\\\\\\\"));
      }
      Path<String> pathUsername = root.get("username");
      Predicate predicateUsername = criteriaBuilder.like(pathUsername, "%" + paginationCriteria.getSearchFor() + "%");
      Path<String> pathEmail = root.get("email");
      Predicate predicateEmail = criteriaBuilder.like(pathEmail, "%" + paginationCriteria.getSearchFor() + "%");
      Predicate predicateSearch = criteriaBuilder.or(predicateUsername, predicateEmail);
      criteriaQuery.where(criteriaBuilder.and(predicateSearch, predicate));
      if (paginationCriteria.getId() != null) {
        Predicate predicateId = criteriaBuilder.notEqual(root.get("id"), paginationCriteria.getId());
        criteriaQuery.where(criteriaBuilder.and(predicateSearch, predicate, predicateId));
      }
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
    CommonListTO<UserDetailBO> commonListTO = new CommonListTO<>();
    CriteriaQuery<Long> criteriaQuery2 = criteriaBuilder.createQuery(Long.class);
    Root<CompanyBO> root2 = criteriaQuery2.from(CompanyBO.class);

    Join<CompanyBO, UserDetailBO> lineJoin2 = root2.join("userDetails");
    Predicate predicateCompany2 = criteriaBuilder.equal(root2.get("id"), paginationCriteria.getId());
    Predicate predicateDelete2 = criteriaBuilder.equal(root2.get("deleteFlag"), false);
    criteriaQuery2.where(criteriaBuilder.and(predicateCompany2, predicateDelete2));

    CriteriaQuery<Long> select = criteriaQuery2.select(criteriaBuilder.countDistinct(lineJoin2));
    Long count = entityManager.createQuery(select).getSingleResult();
    commonListTO.setTotalRow(count);
    int size = count.intValue();
    int limit = paginationCriteria.getLimit();
    if (limit != 0) {
      commonListTO.setPageCount((size + limit - 1) / limit);
    } else {
      commonListTO.setPageCount(1);
    }

    TypedQuery<UserDetailBO> typedQuery = entityManager.createQuery(criteriaQuery);
    // Condition for paging.
    if (paginationCriteria.getPage() != 0 && paginationCriteria.getLimit() > 0) {
      typedQuery.setFirstResult((paginationCriteria.getPage() - 1) * paginationCriteria.getLimit());
      typedQuery.setMaxResults(paginationCriteria.getLimit());
    }
    List<UserDetailBO> list = null;
    try {
      list = typedQuery.getResultList();
    } catch (Exception e) {
      e.printStackTrace();
    }
    commonListTO.setDataList(list);
    return commonListTO;
  }

  @Override
  public UserBO getUserById(Integer id) {
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
  public UserBO getUserByUsername(String username) {
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
  public void deleteUser(List<Integer> id) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaUpdate<UserBO> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(UserBO.class);
    Root<UserBO> root = criteriaUpdate.from(UserBO.class);
    criteriaUpdate.set("deleteFlag", true);
    criteriaUpdate.where(root.get("id").in(id));
    entityManager.createQuery(criteriaUpdate).executeUpdate();
  }
}
