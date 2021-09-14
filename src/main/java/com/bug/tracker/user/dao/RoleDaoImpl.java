package com.bug.tracker.user.dao;

import com.bug.tracker.common.object.CommonListTO;
import com.bug.tracker.common.object.SearchCriteriaObj;
import com.bug.tracker.user.entity.RoleBO;
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
public class RoleDaoImpl implements RoleDao {

    @PersistenceContext
    private EntityManager entityManager;

    private static final Logger logger = LoggerFactory.getLogger(RoleDaoImpl.class);

    @Override
    public RoleBO add(RoleBO roleBO) {
        entityManager.persist(roleBO);
        logger.info("Role has added successfully, Role details=" + roleBO);
        return roleBO;
    }

    @Override
    public RoleBO update(RoleBO roleBO) {
        entityManager.merge(roleBO);
        logger.info("Role has updated successfully, Role details=" + roleBO);
        return roleBO;
    }

    @Override
    public CommonListTO<RoleBO> getList(SearchCriteriaObj searchCriteriaObj) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<RoleBO> criteriaQuery = criteriaBuilder.createQuery(RoleBO.class);
        Root<RoleBO> root = criteriaQuery.from(RoleBO.class);
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
            Order order = criteriaBuilder.desc(root.get("roleId"));
            criteriaQuery.orderBy(order);
        }

        // Adding Pagination total Count
        CommonListTO<RoleBO> commonListTO = new CommonListTO<>();
        CriteriaQuery<Long> criteriaQuery2 = criteriaBuilder.createQuery(Long.class);
        Root<RoleBO> root2 = criteriaQuery2.from(RoleBO.class);
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

        TypedQuery<RoleBO> typedQuery = entityManager.createQuery(criteriaQuery);
        // Condition for paging.
        if (searchCriteriaObj.getPage() != 0 && searchCriteriaObj.getLimit() > 0) {
            typedQuery.setFirstResult((searchCriteriaObj.getPage() - 1) * searchCriteriaObj.getLimit());
            typedQuery.setMaxResults(searchCriteriaObj.getLimit());
        }
        commonListTO.setDataList(typedQuery.getResultList());
        return commonListTO;

    }

    @Override
    public RoleBO getById(Integer id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<RoleBO> criteriaQuery = criteriaBuilder.createQuery(RoleBO.class);

        Root<RoleBO> root = criteriaQuery.from(RoleBO.class);
        Predicate predicateForId = criteriaBuilder.equal(root.get("roleId"), id);
        Predicate predicateForDeleteFlag = criteriaBuilder.equal(root.get("deleteFlag"), false);
        Predicate predicate = criteriaBuilder.and(predicateForId, predicateForDeleteFlag);
        criteriaQuery.where(predicate);
        try {
            RoleBO roleBO = entityManager.createQuery(criteriaQuery).getSingleResult();
            return roleBO;
        } catch (EmptyResultDataAccessException | NoResultException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void delete(List<Integer> id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaUpdate<RoleBO> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(RoleBO.class);
        Root<RoleBO> root = criteriaUpdate.from(RoleBO.class);
        criteriaUpdate.set("deleteFlag", true);
        criteriaUpdate.where(root.get("roleId").in(id));
        entityManager.createQuery(criteriaUpdate).executeUpdate();
    }
}
