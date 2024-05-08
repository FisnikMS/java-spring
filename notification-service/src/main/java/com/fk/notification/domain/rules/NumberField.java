package com.fk.notification.domain.rules;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

public class NumberField implements FieldTypeWrapper<Number> {
  public Difference evaluate(Number value, Number valToCompare)
      throws InvocationTargetException, IllegalAccessException {
    if (Double.compare(value.doubleValue(), valToCompare.doubleValue()) < 0) {
      return new Difference((List<Evaluation>) Arrays.asList(Evaluation.GREATER, Evaluation.UNEQUAL),
          value.doubleValue(),
          valToCompare.doubleValue());
    } else if (Double.compare(value.doubleValue(), valToCompare.doubleValue()) > 0) {
      return new Difference((List<Evaluation>) Arrays.asList(Evaluation.LESS, Evaluation.UNEQUAL),
          value.doubleValue(),
          valToCompare.doubleValue());
    }
    return null;
  }
}
