package com.fk.notification.domain.rules;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

public class BooleanField implements FieldTypeWrapper<Boolean> {
  public Difference evaluate(Boolean value, Boolean valToCompare)
      throws InvocationTargetException, IllegalAccessException {
    if (Boolean.valueOf(value) && !Boolean.valueOf(valToCompare)) {
      return new Difference((List<Evaluation>) Arrays.asList(Evaluation.GREATER, Evaluation.UNEQUAL),
          Boolean.valueOf(value),
          Boolean.valueOf(valToCompare));
    } else if (!Boolean.valueOf(value) && Boolean.valueOf(valToCompare)) {
      return new Difference((List<Evaluation>) Arrays.asList(Evaluation.LESS, Evaluation.UNEQUAL),
          Boolean.valueOf(value),
          Boolean.valueOf(valToCompare));
    }
    return null;
  }
}
