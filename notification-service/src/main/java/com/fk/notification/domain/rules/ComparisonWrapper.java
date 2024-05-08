package com.fk.notification.domain.rules;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;

public class ComparisonWrapper {
  private LinkedHashMap<String, Object> model;

  public ComparisonWrapper(LinkedHashMap<String, Object> model) {
    this.model = model;
  }

  public LinkedHashMap<String,Difference> compare(LinkedHashMap<String, Object> compareTo)
      throws InvocationTargetException, IllegalAccessException {
      return new MapField<String,Object>().evaluate(model, compareTo);
  }

}
