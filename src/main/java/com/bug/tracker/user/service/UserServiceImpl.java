package com.bug.tracker.user.service;

import com.bug.tracker.common.object.CommonListTO;
import com.bug.tracker.common.object.SearchCriteriaObj;
import com.bug.tracker.common.object.SearchFieldsObj;
import com.bug.tracker.common.object.SearchResponseTO;
import com.bug.tracker.common.service.ModelConvertorService;
import com.bug.tracker.company.dto.CompanyTO;
import com.bug.tracker.company.service.CompanyService;
import com.bug.tracker.user.dao.UserDao;
import com.bug.tracker.user.dto.UserTO;
import com.bug.tracker.user.entity.UserBO;
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
  public UserTO add(UserTO userTO) {
    userTO.setPassword(passwordEncoder.encode(userTO.getPassword()));
    UserBO userBO = modelConvertorService.map(userTO, UserBO.class);
    return modelConvertorService.map(userDao.add(userBO), UserTO.class);
  }

  @Override
  public UserTO update(UserTO userTO) {
    UserBO userBO = modelConvertorService.map(userTO, UserBO.class);
    return modelConvertorService.map(userDao.update(userBO), UserTO.class);
  }

  @Override
  public SearchResponseTO getList(SearchCriteriaObj searchCriteriaObj) {
    SearchResponseTO searchResponseTO = new SearchResponseTO();
    CommonListTO<UserBO> commonListTO = userDao.getList(searchCriteriaObj);
    List<UserTO> userTOS = modelConvertorService.map(commonListTO.getDataList(), UserTO.class);

    searchResponseTO.setList(userTOS);
    searchResponseTO.setPageCount(commonListTO.getPageCount());
    searchResponseTO.setTotalRowCount(commonListTO.getTotalRow().intValue());
    return searchResponseTO;
  }

  @Override
  public List<?> getEmployeeList(SearchCriteriaObj searchCriteriaObj) {
    SearchCriteriaObj searchCriteriaObjCompany = new SearchCriteriaObj();
    searchCriteriaObjCompany.setSearchFieldsObj(new SearchFieldsObj());
    searchCriteriaObjCompany.getSearchFieldsObj().setId(searchCriteriaObj.getSearchFieldsObj().getId());
    SearchResponseTO searchResponseTOCompany = companyService.getList(searchCriteriaObjCompany);
    List<CompanyTO> companyTOS = (List<CompanyTO>) searchResponseTOCompany.getList();

    searchCriteriaObj.getSearchFieldsObj().setIds(new ArrayList<>());
    companyTOS.forEach(company -> searchCriteriaObj.getSearchFieldsObj().getIds().add(company.getId()));
//    SearchResponseTO searchResponseTO = new SearchResponseTO();
//    CommonListTO<UserBO> commonListTO = userDao.getEmployeeList(searchCriteriaObj);
//    List<UserTO> userTOS = modelConvertorService.map(commonListTO.getDataList(), UserTO.class);
//
//    searchResponseTO.setList(userTOS);
//    searchResponseTO.setPageCount(commonListTO.getPageCount());
//    searchResponseTO.setTotalRowCount(commonListTO.getTotalRow().intValue());
    return userDao.getEmployeeList(searchCriteriaObj);
  }

  @Override
  public UserTO getById(Integer id) {
    UserTO userTO = modelConvertorService.map(userDao.getById(id), UserTO.class);
    return userTO;
  }

  @Override
  public UserTO getByUsername(String username) throws Exception {
    UserTO userTO = modelConvertorService.map(userDao.getByUsername(username), UserTO.class);
    return userTO;
  }

  @Override
  public void delete(List<Integer> id) {
    userDao.delete(id);
  }
}
