
package com.fk.notification.domain.rules;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fk.notification.utils.Helper;

public class MapField<K, V> implements FieldStructureWrapper<Map<String, Object>> {

  public LinkedHashMap<String, Difference> evaluate(Map<String, Object> value, Map<String, Object> valToCompare)
      throws InvocationTargetException, IllegalAccessException {
    LinkedHashMap<String, Difference> result = new LinkedHashMap<>();
    value.keySet().stream().forEach(key -> {
      Object oldValue = value.get(key);
      Object newValue = valToCompare.get(key);
      if (!valToCompare.containsKey(key)) {
        result.put(key, new Difference(Evaluation.REMOVE,
            oldValue,
            newValue));
      } else if (!oldValue.equals(newValue)) {
        FieldTypeWrapper<Object> fw = (FieldTypeWrapper<Object>) new FieldTypeWrapperFactory()
            .getFieldWrapper(oldValue.getClass());
        try {
          if (fw == null) {
            FieldStructureWrapper<Object> fsw = (FieldStructureWrapper<Object>) new FieldStructureWrapperFactory()
                .getFieldWrapper(oldValue.getClass());
            result.putAll(Helper.transformMap(key, fsw.evaluate(oldValue, newValue)));
          } else {
            result.put(key, fw.evaluate(oldValue, newValue).addEvaluation(Evaluation.UPDATE));
          }
        } catch (Exception e) {
          // @TODO handle exception properly
          // @TODO log
        }
      }
    });

    return result;
  }
}
