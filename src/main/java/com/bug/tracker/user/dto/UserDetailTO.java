package com.bug.tracker.user.dto;

import com.bug.tracker.company.dto.CompanyTO;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserDetailTO {

  private Integer id;
  private String firstName;
  private String lastName;
  private String username;
  private String password;
  private String email;
  private boolean enabled = true;
  private boolean deleteFlag;
  private List<RoleTO> roles;
  private List<CompanyTO> companies = new ArrayList<>();

}
