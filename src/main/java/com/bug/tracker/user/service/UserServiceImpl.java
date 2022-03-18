package com.bug.tracker.user.service;

import com.bug.tracker.common.object.APP_CONST;
import com.bug.tracker.common.object.CommonListTO;
import com.bug.tracker.common.object.PaginationCriteria;
import com.bug.tracker.common.object.SearchResponseTO;
import com.bug.tracker.common.service.EmailService;
import com.bug.tracker.common.service.ModelConvertorService;
import com.bug.tracker.company.dto.CompanyTO;
import com.bug.tracker.company.service.CompanyService;
import com.bug.tracker.exception.UserNotFoundException;
import com.bug.tracker.user.dao.UserDao;
import com.bug.tracker.user.dto.*;
import com.bug.tracker.user.entity.PasswordResetTokenBO;
import com.bug.tracker.user.entity.UserBO;
import com.bug.tracker.user.entity.UserBasicBO;
import com.bug.tracker.user.entity.UserDetailBO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements UserService {

  private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

  @Autowired
  private UserDao userDao;

  @Autowired
  private CompanyService companyService;

  @Autowired
  private ModelConvertorService modelConvertorService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private EmailService emailService;

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
  public boolean changePassword(PasswordChangeTO passwordChangeTO) {
    UserTO userTO = getUserById(passwordChangeTO.getUserId());
    if (passwordEncoder.matches(passwordChangeTO.getCurrentPassword(), userTO.getPassword())) {
      userTO.setPassword(passwordEncoder.encode(passwordChangeTO.getNewPassword()));
      updateUser(userTO);
      return true;
    } else {
      return false;
    }
  }

  @Override
  public void sendToken(String userEmail) throws Exception {
    UserTO userTO = getUserByEmail(userEmail);
    if (userTO == null) {
      throw new UserNotFoundException();
    }
    String token = UUID.randomUUID().toString();
    createPasswordResetTokenForUser(userTO, token);
    String url = APP_CONST.BASE_URL_BACKEND + "/user/reset-password?token=" + token;
    String text = "This URL will only be valid for one hour.";
    emailService.sendSimpleMessage(userTO.getEmail(), "Reset Password", text + " \r\n" + url);
    logger.info("*************************  Email Sent   *************************");
  }

  private void createPasswordResetTokenForUser(UserTO userTO, String token) {
    UserBO userBO = modelConvertorService.map(userTO, UserBO.class);
    userBO.setRoles(null);
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.HOUR, 1);
    PasswordResetTokenBO passwordResetTokenBO = new PasswordResetTokenBO(token, userBO, cal.getTime());
    modelConvertorService.map(userDao.createPasswordResetTokenForUser(passwordResetTokenBO), PasswordResetTokenTO.class);
  }

  @Override
  public String validatePasswordResetToken(String token) {
    final PasswordResetTokenBO resetToken = userDao.getPasswordResetToken(token);

    return !isTokenFound(resetToken) ? "Invalid Token"
            : isTokenExpired(resetToken) ? "Token Expired"
            : null;
  }

  private boolean isTokenFound(PasswordResetTokenBO resetToken) {
    return resetToken != null;
  }

  private boolean isTokenExpired(PasswordResetTokenBO resetToken) {
    final Calendar cal = Calendar.getInstance();
    return resetToken.getExpiryDate().before(cal.getTime());
  }

  public UserTO resetPassword(ForgotPasswordTO forgotPasswordTO) {
    UserTO userTO = getUserByPasswordResetToken(forgotPasswordTO.getToken());
    UserTO userTO_return = null;
    if (userTO != null) {
      userTO.setPassword(passwordEncoder.encode(forgotPasswordTO.getNewPassword()));
      UserBO userBO = modelConvertorService.map(userTO, UserBO.class);
      userTO_return = modelConvertorService.map(userDao.addUser(userBO), UserTO.class);
    }
    return userTO_return;
  }

  private UserTO getUserByPasswordResetToken(String token) {
    PasswordResetTokenTO passwordResetTokenTO = modelConvertorService.map(userDao.getPasswordResetToken(token), PasswordResetTokenTO.class);
    return passwordResetTokenTO.getUser();
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
  @Transactional(Transactional.TxType.NOT_SUPPORTED)
  public UserTO getUserByEmail(String email) throws Exception {
    UserTO userTO = modelConvertorService.map(userDao.getUserByEmail(email), UserTO.class);
    return userTO;
  }

  @Override
  public void deleteUser(List<Integer> id) {
    userDao.deleteUser(id);
  }
}
