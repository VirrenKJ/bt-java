package com.bug.tracker.user.entity;

import com.bug.tracker.common.object.Audit;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;

@Setter
@Getter
@Entity
@Table(name = "role")
public class RoleBO extends Audit implements Serializable {

    private static final long serialVersionUID = -4857844478191178785L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "role_id", unique = true, nullable = false)
    private Integer roleId;

    @Column(name = "role_name")
    private String roleName;

    @Column(name = "description")
    private String description;

    @Column(name = "delete_flag")
    private boolean deleteFlag;

}
