package com.fk.notification.domain.rules;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import com.fk.notification.utils.Helper;

public class ObjectField implements FieldStructureWrapper<Object> {

  @Override
  public HashMap<String, Difference> evaluate(Object value, Object valToCompare)
      throws InvocationTargetException, IllegalAccessException {
    HashMap<String, Difference> evals = new HashMap<String, Difference>();
    for (Field subField : value.getClass().getDeclaredFields()) {
      evals.putAll(compare(value, valToCompare, subField));
    }

    return evals;
  }

  // @TODO feature flag: detect circular dependencies.
  @Override
  public HashMap<String, Difference> compare(Object entity, Object entityToCompare, Field field)
      throws InvocationTargetException, IllegalAccessException {
    HashMap<String, Difference> evals = new HashMap<String, Difference>();
    FieldTypeWrapper<Object> fw = (FieldTypeWrapper<Object>) new FieldTypeWrapperFactory()
        .getFieldWrapper(field.getType());
    if (fw == null) {
      FieldStructureWrapper<Object> fsw = (FieldStructureWrapper<Object>) new FieldStructureWrapperFactory()
          .getFieldWrapper(field.getType());
      evals.putAll(Helper.transformMap(field.getName(),
          fsw.evaluate(accessField(field, entity),
              accessField(field, entityToCompare))));
    } else {
      evals.putAll(fw.compare(entity, entityToCompare, field));
    }
    return evals;
  }
}
