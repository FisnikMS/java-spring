
package com.fk.notification.domain.rules;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

import com.fk.notification.utils.Helper;

public class ListField implements FieldStructureWrapper<List<Object>> {
  @Override
  public HashMap<String, Difference> evaluate(List<Object> value, List<Object> valToCompare)
      throws InvocationTargetException, IllegalAccessException {

    HashMap<String, Difference> result = new HashMap<>();
    for (int index = 0; index < value.size(); index++) {
      Object oldValue = value.get(index);
      Object updatedValue = valToCompare.get(index);
      FieldTypeWrapper<Object> fw = (FieldTypeWrapper<Object>) new FieldTypeWrapperFactory()
          .getFieldWrapper(oldValue.getClass());
      try {
        if (fw == null) {
          // if value is of type data structure then compare the fields recursively.
          FieldStructureWrapper<Object> fsw = (FieldStructureWrapper<Object>) new FieldStructureWrapperFactory()
              .getFieldWrapper(oldValue.getClass());
          result.putAll(Helper.transformMap(String.valueOf(index), fsw.evaluate(oldValue, updatedValue)));
        } else if (!valToCompare.contains(oldValue)) {
          // if value is a primitive data type and is missing from the updated list.
          result.put(String.valueOf(index),
              new Difference(Evaluation.REMOVE, oldValue, null));
        } else {
          // if value is a primitive data type and is different.
          result.put(String.valueOf(index), fw.evaluate(value, valToCompare).addEvaluation(Evaluation.UPDATE));
        }
      } catch (Exception e) {
        // @TODO handle exception properly
        // @TODO log
      }
    }
    if (valToCompare.size() > value.size()) {
      for (int index = 0; index < valToCompare.size(); index++) {
        Object updatedValue = valToCompare.get(index);
        if (!value.contains(updatedValue)) {
          result.put(String.valueOf(index),
              new Difference(Evaluation.ADD, null, updatedValue));
        }
      }
    }
    return result;
  }
}
