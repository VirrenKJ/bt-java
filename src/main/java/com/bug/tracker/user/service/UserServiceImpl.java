package com.bug.tracker.user.service;

import com.bug.tracker.common.object.CommonListTO;
import com.bug.tracker.common.object.SearchCriteriaObj;
import com.bug.tracker.common.service.ModelConvertorService;
import com.bug.tracker.user.dao.UserDao;
import com.bug.tracker.user.dto.UserTO;
import com.bug.tracker.user.entity.UserBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService{

    @Autowired
    private UserDao userDao;

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
    public List<UserTO> getList(SearchCriteriaObj searchCriteriaObj) {
        CommonListTO<UserBO> commonListTO = userDao.getList(searchCriteriaObj);
        List<UserBO> userBOS = commonListTO.getDataList();
        List<UserTO> userTOS = modelConvertorService.map(userBOS, UserTO.class);
        return userTOS;
    }

    @Override
    public UserTO getById(Integer id) {
        UserTO userTO = modelConvertorService.map(userDao.getById(id), UserTO.class);
        return userTO;
    }

    @Override
    public UserTO getByUsername(String username) {
        UserTO userTO = modelConvertorService.map(userDao.getByUsername(username), UserTO.class);
        return userTO;
    }

    @Override
    public void delete(List<Integer> id) {
        userDao.delete(id);
    }
}
