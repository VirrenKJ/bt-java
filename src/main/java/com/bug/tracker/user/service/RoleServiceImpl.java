package com.bug.tracker.user.service;

import com.bug.tracker.common.object.CommonListTO;
import com.bug.tracker.common.object.PaginationCriteria;
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
public class RoleServiceImpl implements RoleService {

  @Autowired
  private RoleDao roleDao;

  @Autowired
  private ModelConvertorService modelConvertorService;

  @Override
  public RoleTO addRole(RoleTO roleTO) {
    RoleBO roleBO = modelConvertorService.map(roleTO, RoleBO.class);
    return modelConvertorService.map(roleDao.addRole(roleBO), RoleTO.class);
  }

  @Override
  public RoleTO updateRole(RoleTO roleTO) {
    RoleBO roleBO = modelConvertorService.map(roleTO, RoleBO.class);
    return modelConvertorService.map(roleDao.updateRole(roleBO), RoleTO.class);
  }

  @Override
  @Transactional(Transactional.TxType.NOT_SUPPORTED)
  public List<RoleTO> getRoleList(PaginationCriteria paginationCriteria) {
    CommonListTO<RoleBO> commonListTO = roleDao.getRoleList(paginationCriteria);
    List<RoleBO> roleBOS = commonListTO.getDataList();
    List<RoleTO> roleTOS = modelConvertorService.map(roleBOS, RoleTO.class);
    return roleTOS;
  }

  @Override
  @Transactional(Transactional.TxType.NOT_SUPPORTED)
  public RoleTO getRoleById(Integer id) {
    RoleTO roleTO = modelConvertorService.map(roleDao.getRoleById(id), RoleTO.class);
    return roleTO;
  }

  @Override
  public void deleteRole(List<Integer> id) {
    roleDao.deleteRole(id);
  }
}
