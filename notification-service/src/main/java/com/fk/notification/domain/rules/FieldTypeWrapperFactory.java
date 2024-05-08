
package com.fk.notification.domain.rules;

public class FieldTypeWrapperFactory {
  public FieldWrapper<? extends Object> getFieldWrapper(Class<? extends Object> type) {

    if (String.class.isAssignableFrom(type)) {
      return new StringField();
    } else if (Number.class.isAssignableFrom(type)) {
      return new NumberField();
    } else if (Boolean.class.isAssignableFrom(type)) {
      return new BooleanField();
    }
    return null;
  }

}
