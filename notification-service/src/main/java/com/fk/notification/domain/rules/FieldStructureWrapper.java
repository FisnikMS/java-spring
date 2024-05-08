
package com.fk.notification.domain.rules;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import com.fk.notification.utils.Helper;


public interface FieldStructureWrapper<T> extends FieldWrapper<T> {
  public HashMap<String, Difference> evaluate(T value, T valToCompare)
      throws InvocationTargetException, IllegalAccessException;

  default public HashMap<String, Difference> compare(Object entity, Object entityToCompare, Field field)
      throws InvocationTargetException, IllegalAccessException {
    HashMap<String, Difference> evals = Helper.transformMap(field.getName(),
        evaluate(accessField(field, entity), accessField(field, entityToCompare)));
    return evals;
  }
}
