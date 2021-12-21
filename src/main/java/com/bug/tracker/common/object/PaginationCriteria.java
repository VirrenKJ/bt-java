package com.bug.tracker.common.object;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@ToString
public class PaginationCriteria implements Serializable {

  private static final long serialVersionUID = 9080407426651168569L;

  private int page;

  private int limit;

  private int firstLimit;

  private int endLimit;

  private int sortType;

  private String sortField;

  private String searchFor;

  private Integer id;

  private List<Integer> ids;

}
