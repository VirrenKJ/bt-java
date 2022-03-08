package com.bug.tracker.user.service;

import com.bug.tracker.common.object.CommonListTO;
import com.bug.tracker.common.object.PaginationCriteria;
import com.bug.tracker.common.object.SearchResponseTO;
import com.bug.tracker.common.service.ModelConvertorService;
import com.bug.tracker.company.dto.CompanyTO;
import com.bug.tracker.company.service.CompanyService;
import com.bug.tracker.user.dao.UserDao;
import com.bug.tracker.user.dto.PasswordResetTO;
import com.bug.tracker.user.dto.UserBasicTO;
import com.bug.tracker.user.dto.UserDetailTO;
import com.bug.tracker.user.dto.UserTO;
import com.bug.tracker.user.entity.UserBO;
import com.bug.tracker.user.entity.UserBasicBO;
import com.bug.tracker.user.entity.UserDetailBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

  @Autowired
  private UserDao userDao;

  @Autowired
  private CompanyService companyService;

  @Autowired
  private ModelConvertorService modelConvertorService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public UserTO addUser(UserTO userTO) {
    userTO.setPassword(passwordEncoder.encode(userTO.getPassword()));
    UserBO userBO = modelConvertorService.map(userTO, UserBO.class);
    return modelConvertorService.map(userDao.addUser(userBO), UserTO.class);
  }

  @Override
  public UserBasicTO copyUserToTenant(UserBasicTO userBasicTO) {
    UserBasicBO userBasicBO = modelConvertorService.map(userBasicTO, UserBasicBO.class);
    return modelConvertorService.map(userDao.copyUserToTenant(userBasicBO), UserBasicTO.class);
  }

  @Override
  public UserBasicTO updateUserToTenant(UserBasicTO userBasicTO) {
    UserBasicBO userBasicBO = modelConvertorService.map(userBasicTO, UserBasicBO.class);
    return modelConvertorService.map(userDao.updateUserToTenant(userBasicBO), UserBasicTO.class);
  }

  @Override
  public UserTO updateUser(UserTO userTO) {
    UserBO userBO = modelConvertorService.map(userTO, UserBO.class);
    return modelConvertorService.map(userDao.updateUser(userBO), UserTO.class);
  }

  @Override
  public boolean resetPassword(PasswordResetTO passwordResetTO) {
    UserTO userTO = getUserById(passwordResetTO.getUserId());
    if (passwordEncoder.matches(passwordResetTO.getCurrentPassword(), userTO.getPassword())) {
      userTO.setPassword(passwordEncoder.encode(passwordResetTO.getNewPassword()));
      updateUser(userTO);
      return true;
    } else {
      return false;
    }
  }

  @Override
  @Transactional(Transactional.TxType.NOT_SUPPORTED)
  public SearchResponseTO getUserList(PaginationCriteria paginationCriteria) {
    SearchResponseTO searchResponseTO = new SearchResponseTO();
    CommonListTO<UserBO> commonListTO = userDao.getUserList(paginationCriteria);
    List<UserTO> userTOS = modelConvertorService.map(commonListTO.getDataList(), UserTO.class);

    searchResponseTO.setList(userTOS);
    searchResponseTO.setPageCount(commonListTO.getPageCount());
    searchResponseTO.setTotalRowCount(commonListTO.getTotalRow().intValue());
    return searchResponseTO;
  }

  @Override
  @Transactional(Transactional.TxType.NOT_SUPPORTED)
  public SearchResponseTO getEmployeeList(PaginationCriteria paginationCriteria) {
    PaginationCriteria paginationCriteriaCompany = new PaginationCriteria();
    paginationCriteriaCompany.setId(paginationCriteria.getId());
    SearchResponseTO searchResponseTOCompany = companyService.getList(paginationCriteriaCompany);
    List<CompanyTO> companyTOS = (List<CompanyTO>) searchResponseTOCompany.getList();
    paginationCriteria.setIds(new ArrayList<>());
    companyTOS.forEach(company -> paginationCriteria.getIds().add(company.getId()));

    SearchResponseTO searchResponseTO = new SearchResponseTO();
    CommonListTO<UserDetailBO> commonListTO = userDao.getEmployeeList(paginationCriteria);
    List<UserDetailTO> userDetailTOS = modelConvertorService.map(commonListTO.getDataListUnknownType(), UserDetailTO.class);

    searchResponseTO.setList(userDetailTOS);
    searchResponseTO.setPageCount(commonListTO.getPageCount());
    searchResponseTO.setTotalRowCount(commonListTO.getTotalRow().intValue());
    return searchResponseTO;
  }

  @Override
  @Transactional(Transactional.TxType.NOT_SUPPORTED)
  public SearchResponseTO getEmployeeListByCompany(PaginationCriteria paginationCriteria) {
    SearchResponseTO searchResponseTO = new SearchResponseTO();
    CommonListTO<UserDetailBO> commonListTO = userDao.getEmployeeListByCompany(paginationCriteria);
    List<UserDetailTO> userDetailTOS = modelConvertorService.map(commonListTO.getDataListUnknownType(), UserDetailTO.class);

    searchResponseTO.setList(userDetailTOS);
    searchResponseTO.setPageCount(commonListTO.getPageCount());
    searchResponseTO.setTotalRowCount(commonListTO.getTotalRow().intValue());
    return searchResponseTO;
  }

  @Override
  @Transactional(Transactional.TxType.NOT_SUPPORTED)
  public UserTO getUserById(Integer id) {
    UserTO userTO = modelConvertorService.map(userDao.getUserById(id), UserTO.class);
    return userTO;
  }

  @Override
  @Transactional(Transactional.TxType.NOT_SUPPORTED)
  public UserTO getUserByUsername(String username) throws Exception {
    UserTO userTO = modelConvertorService.map(userDao.getUserByUsername(username), UserTO.class);
    return userTO;
  }

  @Override
  public void deleteUser(List<Integer> id) {
    userDao.deleteUser(id);
  }
}
