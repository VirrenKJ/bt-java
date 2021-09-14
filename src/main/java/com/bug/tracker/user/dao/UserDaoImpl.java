package com.bug.tracker.user.dao;

import com.bug.tracker.common.object.CommonListTO;
import com.bug.tracker.common.object.SearchCriteriaObj;
import com.bug.tracker.user.entity.RoleBO;
import com.bug.tracker.user.entity.UserBO;
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
        if (searchCriteriaObj.getSortField() != null && !searchCriteriaObj.getSortField().isEmpty()) {
            Order order = null;
            if (searchCriteriaObj.getSortType() == 2) {
                order = criteriaBuilder.desc(root.get(searchCriteriaObj.getSortField()));
            } else {
                order = criteriaBuilder.asc(root.get(searchCriteriaObj.getSortField()));
            }
            criteriaQuery.orderBy(order);
        } else {
            Order order = criteriaBuilder.desc(root.get("id"));
            criteriaQuery.orderBy(order);
        }

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
            commonListTO.setPageCount((int) ((size + limit - 1) / limit));
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

    @Override
    public UserBO getById(Integer id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserBO> criteriaQuery = criteriaBuilder.createQuery(UserBO.class);

        Root<UserBO> root = criteriaQuery.from(UserBO.class);
        Predicate predicateForId = criteriaBuilder.equal(root.get("id"), id);
        Predicate predicateForDeleteFlag = criteriaBuilder.equal(root.get("deleteFlag"), false);
        Predicate predicate = criteriaBuilder.and(predicateForId, predicateForDeleteFlag);
        criteriaQuery.where(predicate);
        try {
            UserBO userBO = entityManager.createQuery(criteriaQuery).getSingleResult();
            return userBO;
        } catch (EmptyResultDataAccessException | NoResultException e) {
            e.printStackTrace();
            return null;
        }
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
