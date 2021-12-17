package com.bug.tracker.user.service;

import com.bug.tracker.common.object.CommonListTO;
import com.bug.tracker.common.object.SearchCriteriaObj;
import com.bug.tracker.common.object.SearchFieldsObj;
import com.bug.tracker.common.object.SearchResponseTO;
import com.bug.tracker.common.service.ModelConvertorService;
import com.bug.tracker.company.dto.CompanyTO;
import com.bug.tracker.company.service.CompanyService;
import com.bug.tracker.user.dao.UserDao;
import com.bug.tracker.user.dto.UserDetailTO;
import com.bug.tracker.user.dto.UserTO;
import com.bug.tracker.user.entity.UserBO;
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
  public UserTO updateUser(UserTO userTO) {
    UserBO userBO = modelConvertorService.map(userTO, UserBO.class);
    return modelConvertorService.map(userDao.updateUser(userBO), UserTO.class);
  }

  @Override
  @Transactional(Transactional.TxType.NOT_SUPPORTED)
  public SearchResponseTO getUserList(SearchCriteriaObj searchCriteriaObj) {
    SearchResponseTO searchResponseTO = new SearchResponseTO();
    CommonListTO<UserBO> commonListTO = userDao.getUserList(searchCriteriaObj);
    List<UserTO> userTOS = modelConvertorService.map(commonListTO.getDataList(), UserTO.class);

    searchResponseTO.setList(userTOS);
    searchResponseTO.setPageCount(commonListTO.getPageCount());
    searchResponseTO.setTotalRowCount(commonListTO.getTotalRow().intValue());
    return searchResponseTO;
  }

  @Override
  @Transactional(Transactional.TxType.NOT_SUPPORTED)
  public SearchResponseTO getEmployeeList(SearchCriteriaObj searchCriteriaObj) {
    SearchCriteriaObj searchCriteriaObjCompany = new SearchCriteriaObj();
    searchCriteriaObjCompany.setSearchFieldsObj(new SearchFieldsObj());
    searchCriteriaObjCompany.getSearchFieldsObj().setId(searchCriteriaObj.getSearchFieldsObj().getId());
    SearchResponseTO searchResponseTOCompany = companyService.getList(searchCriteriaObjCompany);
    List<CompanyTO> companyTOS = (List<CompanyTO>) searchResponseTOCompany.getList();
    searchCriteriaObj.getSearchFieldsObj().setIds(new ArrayList<>());
    companyTOS.forEach(company -> searchCriteriaObj.getSearchFieldsObj().getIds().add(company.getId()));

    SearchResponseTO searchResponseTO = new SearchResponseTO();
    CommonListTO<UserDetailBO> commonListTO = userDao.getEmployeeList(searchCriteriaObj);
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
