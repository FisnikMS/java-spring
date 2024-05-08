package com.fk.notification.utils;

import java.util.HashMap;

import com.fk.notification.domain.rules.Difference;

public class Helper {
  public static String transformString(Object key, Object value) {
    return key + "." + value;
  }

  public static HashMap<String, Difference> transformMap(String field,
      HashMap<String, Difference> evals) {
    HashMap<String, Difference> transformMap = new HashMap<String, Difference>();
    if (evals.size() > 0) {
      evals.keySet().stream().forEach(key -> {
        transformMap.put(Helper.transformString(field, key), evals.get(key));
      });
    }
    return transformMap;

  }

  public static HashMap<String, Difference> intoMap(String field,
      Difference eval) {
    HashMap<String, Difference> map = new HashMap<String, Difference>();
    if (eval != null) {
      map.put(field, eval);
    }
    return map;

  }

}
