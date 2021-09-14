package com.bug.tracker.user.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;

@Setter
@Getter
@Entity
@Table(name = "user_role")
public class UserRoleBO implements Serializable {

    private static final long serialVersionUID = -1613290288522924516L;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "user_id", unique = false)
    private Integer userId;

    @Column(name = "role_id", unique = false)
    private Integer roleId;

}
