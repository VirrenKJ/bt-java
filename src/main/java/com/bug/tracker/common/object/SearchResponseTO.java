package com.bug.tracker.common.object;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
public class SearchResponseTO implements Serializable {

  private static final long serialVersionUID = 6352502988408439822L;

  private int pageCount;

  private int totalRowCount;

  private List<?> list;
}
