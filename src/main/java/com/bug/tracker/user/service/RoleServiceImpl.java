package com.bug.tracker.user.service;

import com.bug.tracker.common.object.CommonListTO;
import com.bug.tracker.common.object.SearchCriteriaObj;
import com.bug.tracker.common.service.ModelConvertorService;
import com.bug.tracker.user.dao.RoleDao;
import com.bug.tracker.user.dto.RoleTO;
import com.bug.tracker.user.entity.RoleBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements RoleService{

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private ModelConvertorService modelConvertorService;

    @Override
    public RoleTO add(RoleTO roleTO) {
        RoleBO roleBO = modelConvertorService.map(roleTO, RoleBO.class);
        return modelConvertorService.map(roleDao.add(roleBO), RoleTO.class);
    }

    @Override
    public RoleTO update(RoleTO roleTO) {
        RoleBO roleBO = modelConvertorService.map(roleTO, RoleBO.class);
        return modelConvertorService.map(roleDao.update(roleBO), RoleTO.class);
    }

    @Override
    public List<RoleTO> getList(SearchCriteriaObj searchCriteriaObj) {
        CommonListTO<RoleBO> commonListTO = roleDao.getList(searchCriteriaObj);
        List<RoleBO> roleBOS = commonListTO.getDataList();
        List<RoleTO> roleTOS = modelConvertorService.map(roleBOS, RoleTO.class);
        return roleTOS;
    }

    @Override
    public RoleTO getById(Integer id) {
        RoleTO roleTO = modelConvertorService.map(roleDao.getById(id), RoleTO.class);
        return roleTO;
    }

    @Override
    public void delete(List<Integer> id) {
        roleDao.delete(id);
    }
}
