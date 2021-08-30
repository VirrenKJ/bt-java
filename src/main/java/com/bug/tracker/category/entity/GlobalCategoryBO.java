package com.bug.tracker.category.entity;

import com.bug.tracker.common.object.Audit;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "global_category")
public class GlobalCategoryBO extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "assigned_id")
    private Integer assignedId;

    @Column(name = "delete_flag")
    private boolean deleteFlag;

}
