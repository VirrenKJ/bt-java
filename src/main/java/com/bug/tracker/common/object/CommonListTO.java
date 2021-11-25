package com.bug.tracker.common.object;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CommonListTO<T> {

  private Long totalRow;

  private Integer pageCount;

  private List<T> dataList;

  private List<?> dataListUnknownType;

}
