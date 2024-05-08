package com.fk.notification.domain.rules;

import java.lang.reflect.InvocationTargetException;

public class StringField implements FieldTypeWrapper<String> {
  public Difference evaluate(String value, String valToCompare)
      throws InvocationTargetException, IllegalAccessException {
    Difference res = null;
    if (!value.equals(valToCompare)) {
      res = new Difference(Evaluation.UNEQUAL, value, valToCompare);
    }
    return res;
  }
}
