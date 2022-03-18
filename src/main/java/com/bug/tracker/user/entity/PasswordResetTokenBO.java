package com.bug.tracker.user.entity;

import com.bug.tracker.common.object.Audit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "password_reset_token")
public class PasswordResetTokenBO implements Serializable {

  private static final long serialVersionUID = -5978252003474563484L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @Column(name = "user_id", insertable = false, updatable = false)
  private Integer userId;

  @Column(name = "token")
  private String token;

  @OneToOne(targetEntity = UserBO.class, cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  @JoinColumn(nullable = false, name = "user_id")
  private UserBO user;

  @Column(name = "expiry_date")
  private Date expiryDate;

  public PasswordResetTokenBO(String token, UserBO user, Date expiryDate) {
    this.token = token;
    this.user = user;
    this.expiryDate = expiryDate;
  }
}
