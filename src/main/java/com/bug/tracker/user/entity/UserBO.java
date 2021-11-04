package com.bug.tracker.user.entity;

import com.bug.tracker.company.entity.CompanyBO;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "user")
public class UserBO implements UserDetails {

  private static final long serialVersionUID = -2700877336857161147L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", unique = true, nullable = false)
  private Integer id;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "username")
  private String username;

  @Column(name = "password")
  private String password;

  @Column(name = "email")
  private String email;

  @Column(name = "enabled")
  private boolean enabled = true;

  @Column(name = "delete_flag")
  private boolean deleteFlag;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "user_role", joinColumns = {@JoinColumn(name = "user_id", updatable = false, insertable = false)},
          inverseJoinColumns = {@JoinColumn(name = "role_id", updatable = false, insertable = false)})
  private List<RoleBO> roles;

  @ManyToMany(mappedBy="users")
  @JsonManagedReference
  private List<CompanyBO> companies = new ArrayList<>();

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<UserAuthority> userAuthorities = new ArrayList<>();
    if (roles != null && !roles.isEmpty()) {
      roles.forEach(role -> {
        userAuthorities.add(new UserAuthority(role.getRoleName()));
      });
    }
    return userAuthorities;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }
}
