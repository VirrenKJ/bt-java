package com.bug.tracker.user.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;

@Setter
@Getter
public class UserRoleBO {

    private Integer id;
    private Integer userId;
    private Integer roleId;
}
