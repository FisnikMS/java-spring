
package com.fk.notification.domain.rules;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;

public interface FieldWrapper<T> {

  public HashMap<String, Difference> compare(Object entity, Object entityToCompare, Field field)
      throws InvocationTargetException, IllegalAccessException;

  
  @SuppressWarnings("unchecked")
  default T accessField(Field field, Object entity) throws InvocationTargetException, IllegalAccessException {
    if (Modifier.isPublic(field.getModifiers())) {
      return (T) field.get(entity);
    }
    for (Method method : entity.getClass().getDeclaredMethods()) {
      if (method.getName()
          .equals("get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1))) {
        return (T) method.invoke(entity);
      }
    }
    throw new IllegalAccessException(
        "Field should be accessible.\nMake sure that all fields are either public or accessable via public getters and setters.");
  }


}
