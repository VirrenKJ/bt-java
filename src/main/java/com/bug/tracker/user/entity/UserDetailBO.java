package com.bug.tracker.user.entity;

import com.bug.tracker.company.entity.CompanyBO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "user")
//Alternative to @JsonIgnoreProperties for bidirectional many to many mapping
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties("companies")
public class UserDetailBO implements Serializable {

  private static final long serialVersionUID = 1200297614127292483L;

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

  @ManyToMany(mappedBy="userDetails", fetch = FetchType.LAZY)
  private List<CompanyBO> companies = new ArrayList<>();

}
