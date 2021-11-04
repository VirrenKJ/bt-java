package com.bug.tracker.user.dto;

import com.bug.tracker.company.dto.CompanyTO;
import com.bug.tracker.company.entity.CompanyBO;
import com.bug.tracker.user.entity.UserAuthority;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserTO {

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

    //UserDetails
    private List<UserAuthority> authorities;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
}
