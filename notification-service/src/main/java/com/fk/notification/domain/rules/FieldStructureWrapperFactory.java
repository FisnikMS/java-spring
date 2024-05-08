
package com.fk.notification.domain.rules;

import java.util.Collection;
import java.util.Map;

public class FieldStructureWrapperFactory {
  public FieldStructureWrapper<? extends Object> getFieldWrapper(Class<? extends Object> type) {
    if (Collection.class.isAssignableFrom(type)) {
      return new ListField();
    } else if (Map.class.isAssignableFrom(type)) {
      return new MapField<String, Object>();
    }

    return new ObjectField();
  }

}
