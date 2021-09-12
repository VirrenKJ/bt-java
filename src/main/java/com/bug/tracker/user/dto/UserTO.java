package com.bug.tracker.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserTO {

    private Integer id;
    private String firstName;
    private String lastName;
    private String username;
    private Integer password;
    private Integer email;
    private boolean enabled = true;
}
