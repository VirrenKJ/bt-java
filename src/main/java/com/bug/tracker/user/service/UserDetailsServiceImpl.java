package com.bug.tracker.user.service;

import com.bug.tracker.user.dao.UserDao;
import com.bug.tracker.user.entity.UserBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private UserDao userDao;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserBO userBO = null;
    try {
      userBO = userDao.getUserByUsername(username);
    } catch (Exception e) {
      e.printStackTrace();
      throw new UsernameNotFoundException(e.getMessage());
    }
    return userBO;
  }
}
