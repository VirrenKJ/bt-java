package com.bug.tracker.common.object;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class SearchFieldsObj implements Serializable {

  private static final long serialVersionUID = 2909494450651767609L;

  private String searchFor;

  private Integer id;

  private List<Integer> ids;
}
