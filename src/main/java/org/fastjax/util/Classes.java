/* Copyright (c) 2006 FastJAX
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * You should have received a copy of The MIT License (MIT) along with this
 * program. If not, see <http://opensource.org/licenses/MIT/>.
 */

package org.fastjax.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

public final class Classes {
  public static String getDeclaringClassName(final String className) {
    if (!JavaIdentifiers.isValid(className))
      throw new IllegalArgumentException("Not a valid java identifier: " + className);

    int index = className.length() - 1;
    while ((index = className.lastIndexOf('$', index - 1)) > 1) {
      char ch = className.charAt(index - 1);
      if (ch != '.' && ch != '$')
        break;
    }

    return index <= 0 ? className : className.substring(0, index);
  }

  public static String getRootDeclaringClassName(final String className) {
    if (!JavaIdentifiers.isValid(className))
      throw new IllegalArgumentException("Not a valid java identifier: " + className);

    final int limit = className.length() - 1;
    int index = 0;
    while ((index = className.indexOf('$', index + 1)) > 1) {
      char ch = className.charAt(index - 1);
      if (index == limit)
        return className;

      if (ch != '.' && ch != '$')
        break;
    }

    return index == -1 ? className : className.substring(0, index);
  }

  public static String toCanonicalClassName(final String className) {
    if (!JavaIdentifiers.isValid(className))
      throw new IllegalArgumentException("Not a valid java identifier: " + className);

    final StringBuilder builder = new StringBuilder();
    builder.append(className.charAt(0));
    builder.append(className.charAt(1));
    char last = '\0';
    for (int i = 2; i < className.length() - 1; i++) {
      final char ch = className.charAt(i);
      builder.append(last != '.' && last != '$' && ch == '$' ? '.' : ch);
      last = ch;
    }

    builder.append(className.charAt(className.length() - 1));
    return builder.toString();
  }

  /**
   * Returns the "Compound Name" of the class or interface represented by
   * {@code cls}.
   * <p>
   * The "Compound Name" is the fully qualified name of a class
   * ({@link Class#getName()} with its package name
   * ({@link Class#getPackage()}.getName()) removed.
   * <p>
   * For example:
   * <ol>
   * <li>The "Compound Name" of {@code java.lang.String} is {@code String}.</li>
   * <li>The "Compound Name" of {@code java.lang.Map.Entry} is
   * {@code Map$Entry}.</li>
   * </ol>
   *
   * @param cls The class or interface.
   * @return The "Compound Name" of the class or interface represented by
   *         {@code cls}.
   */
  public static String getCompoundName(final Class<?> cls) {
    final String pkg = cls.getPackageName();
    return pkg.length() == 0 ? cls.getName() : cls.getName().substring(pkg.length() + 1);
  }

  /**
   * Returns the canonical "Compound Name" of the class or interface represented
   * by {@code cls}.
   * <p>
   * The canonical "Compound Name" is the fully qualified name of a class
   * ({@link Class#getCanonicalName()} with its package name
   * ({@link Class#getPackage()}.getName()) removed.
   * <p>
   * For example:
   * <ol>
   * <li>The canonical "Compound Name" of {@code java.lang.String} is
   * {@code String}.</li>
   * <li>The canonical "Compound Name" of {@code java.lang.Map.Entry} is
   * {@code Map.Entry}.</li>
   * </ol>
   *
   * @param cls The class or interface.
   * @return The canonical "Compound Name" of the class or interface represented
   *         by {@code cls}.
   */
  public static String getCanonicalCompoundName(final Class<?> cls) {
    final String pkg = cls.getPackageName();
    return pkg.length() == 0 ? cls.getCanonicalName() : cls.getCanonicalName().substring(pkg.length() + 1);
  }

  public static Type[] getGenericSuperclasses(final Class<?> cls) {
    return cls.getGenericSuperclass() instanceof ParameterizedType ? ((ParameterizedType)cls.getGenericSuperclass()).getActualTypeArguments() : null;
  }

  private static Field getField(final Class<?> cls, final String fieldName, final boolean declared) {
    final Field[] fields = declared ? cls.getDeclaredFields() : cls.getFields();
    for (final Field field : fields)
      if (fieldName.equals(field.getName()))
        return field;

    return null;
  }

  /**
   * Returns a {@code Field} object that reflects the specified public member
   * field of the class or interface represented by {@code cls} (including
   * inherited fields). The {@code name} parameter is a {@code String}
   * specifying the simple name of the desired field.
   * <p>
   * The field to be reflected is determined by the algorithm that follows. Let
   * C be the class or interface represented by this object:
   * <ol>
   * <li>If C declares a public field with the name specified, that is the field
   * to be reflected.</li>
   * <li>If no field was found in step 1 above, this algorithm is applied
   * recursively to each direct superinterface of C. The direct superinterfaces
   * are searched in the order they were declared.</li>
   * <li>If no field was found in steps 1 and 2 above, and C has a superclass S,
   * then this algorithm is invoked recursively upon S. If C has no superclass,
   * then this method returns {@code null}.</li>
   * </ol>
   * <p>
   * If this {@code Class} object represents an array type, then this method
   * does not find the {@code length} field of the array type.
   * <p>
   * This method differentiates itself from {@link Class#getField(String)} by
   * returning {@code null} when a field is not found, instead of throwing
   * {@link NoSuchFieldException}.
   *
   * @param cls The class in which to find the public field.
   * @param name The field name.
   * @return A {@code Field} object that reflects the specified public member
   *         field of the class or interface represented by {@code cls}
   *         (including inherited fields). The {@code name} parameter is a
   *         {@code String} specifying the simple name of the desired field.
   * @throws NullPointerException if {@code name} is {@code null}
   * @throws SecurityException If a security manager, <i>s</i>, is present and
   *           the caller's class loader is not the same as or an ancestor of
   *           the class loader for the current class and invocation of
   *           {@link SecurityManager#checkPackageAccess s.checkPackageAccess()}
   *           denies access to the package of this class.
   */
  public static Field getField(final Class<?> cls, final String name) {
    return Classes.getField(cls, name, false);
  }

  /**
   * Returns a {@code Field} object that reflects the specified declared member
   * field of the class or interface represented by {@code cls} (excluding
   * inherited fields). The {@code name} parameter is a {@code String}
   * specifying the simple name of the desired field.
   * <p>
   * Declared fields include public, protected, default (package) access,
   * and private visibility.
   * <p>
   * If this {@code Class} object represents an array type, then this method
   * does not find the {@code length} field of the array type.
   * <p>
   * This method differentiates itself from {@link Class#getDeclaredField(String)} by
   * returning {@code null} when a field is not found, instead of throwing
   * {@link NoSuchFieldException}.
   *
   * @param cls The class in which to find the declared field.
   * @param name The field name.
   * @return A {@code Field} object that reflects the specified public member
   *         field of the class or interface represented by {@code cls}
   *         (excluding inherited fields). The {@code name} parameter is a
   *         {@code String} specifying the simple name of the desired field.
   * @throws NullPointerException if {@code name} is {@code null}
   * @throws SecurityException If a security manager, <i>s</i>, is present and
   *           the caller's class loader is not the same as or an ancestor of
   *           the class loader for the current class and invocation of
   *           {@link SecurityManager#checkPackageAccess s.checkPackageAccess()}
   *           denies access to the package of this class.
   */
  public static Field getDeclaredField(final Class<?> cls, final String name) {
    return Classes.getField(cls, name, true);
  }

  /**
   * Returns a {@code Field} object that reflects the specified declared member
   * field of the class or interface represented by {@code cls} (including
   * inherited fields). The {@code name} parameter is a {@code String}
   * specifying the simple name of the desired field.
   * <p>
   * Declared fields include public, protected, default (package) access,
   * and private visibility.
   * <p>
   * If this {@code Class} object represents an array type, then this method
   * does not find the {@code length} field of the array type.
   * <p>
   * This method differentiates itself from {@link Class#getDeclaredField(String)} by
   * returning {@code null} when a field is not found, instead of throwing
   * {@link NoSuchFieldException}.
   *
   * @param cls The class in which to find the declared field.
   * @param name The field name.
   * @return A {@code Field} object that reflects the specified public member
   *         field of the class or interface represented by {@code cls}
   *         (including inherited fields). The {@code name} parameter is a
   *         {@code String} specifying the simple name of the desired field.
   * @throws NullPointerException if {@code name} is {@code null}
   * @throws SecurityException If a security manager, <i>s</i>, is present and
   *           the caller's class loader is not the same as or an ancestor of
   *           the class loader for the current class and invocation of
   *           {@link SecurityManager#checkPackageAccess s.checkPackageAccess()}
   *           denies access to the package of this class.
   */
  public static Field getDeclaredFieldDeep(Class<?> cls, final String name) {
    Field field;
    do
      field = getField(cls, name, true);
    while (field == null && (cls = cls.getSuperclass()) != null);
    return field;
  }

  /**
   * Returns a Constructor object that reflects the specified public constructor
   * of the class or interface represented by {@code cls} (including inherited
   * constructors), or {@code null} if the constructor is not found.
   * <p>
   * The {@code parameterTypes} parameter is an array of Class objects that
   * identify the constructor's formal parameter types, in declared order. If
   * {@code cls} represents an inner class declared in a non-static context, the
   * formal parameter types include the explicit enclosing instance as the first
   * parameter.
   * <p>
   * This method differentiates itself from
   * {@link Class#getConstructor(Class...)} by returning {@code null} when a
   * method is not found, instead of throwing {@link NoSuchMethodException}.
   *
   * @param cls The class in which to find the public constructor.
   * @param parameterTypes The parameter array.
   * @return A Constructor object that reflects the specified public constructor
   *         of the class or interface represented by {@code cls} (including
   *         inherited constructors), or {@code null} if the constructor is not
   *         found.
   */
  public static Constructor<?> getConstructor(final Class<?> cls, final Class<?> ... parameterTypes) {
    final Constructor<?>[] constructors = cls.getConstructors();
    for (final Constructor<?> constructor : constructors)
      if (java.util.Arrays.equals(constructor.getParameterTypes(), parameterTypes))
        return constructor;

    return null;
  }

  /**
   * Returns a Constructor object that reflects the specified declared
   * constructor of the class or interface represented by {@code cls} (excluding
   * inherited constructors), or {@code null} if the constructor is not found.
   * <p>
   * Declared constructors include public, protected, default (package) access,
   * and private visibility.
   * <p>
   * The {@code parameterTypes} parameter is an array of Class objects that
   * identify the constructor's formal parameter types, in declared order. If
   * {@code cls} represents an inner class declared in a non-static context, the
   * formal parameter types include the explicit enclosing instance as the first
   * parameter.
   * <p>
   * This method differentiates itself from
   * {@link Class#getDeclaredConstructor(Class...)} by returning {@code null}
   * when a method is not found, instead of throwing
   * {@link NoSuchMethodException}.
   *
   * @param cls The class in which to find the declared constructor.
   * @param parameterTypes The parameter array.
   * @return A Constructor object that reflects the specified declared
   *         constructor of the class or interface represented by {@code cls}
   *         (excluding inherited constructors), or {@code null} if the
   *         constructor is not found.
   */
  public static Constructor<?> getDeclaredConstructor(final Class<?> cls, final Class<?> ... parameterTypes) {
    final Constructor<?>[] constructors = cls.getDeclaredConstructors();
    for (final Constructor<?> constructor : constructors)
      if (java.util.Arrays.equals(constructor.getParameterTypes(), parameterTypes))
        return constructor;

    return null;
  }

  /**
   * Changes the annotation value for {@code key} in {@code annotation} to
   * {@code newValue}, and returns the previous value.
   *
   * @param <T> Type parameter of the value.
   * @param annotation The annotation.
   * @param key The key.
   * @param newValue The new value.
   * @return The previous value assigned to {@code key}.
   * @throws IllegalArgumentException If {@code newValue} does not match the
   *           required type of the value for {@code key}.
   */
  @SuppressWarnings("unchecked")
  public static <T>T setAnnotationValue(final Annotation annotation, final String key, final T newValue) {
    final Object handler = Proxy.getInvocationHandler(annotation);
    final Field field;
    final Map<String,Object> memberValues;
    try {
      field = handler.getClass().getDeclaredField("memberValues");
      field.setAccessible(true);
      memberValues = (Map<String,Object>)field.get(handler);
    }
    catch (final IllegalArgumentException | IllegalAccessException | NoSuchFieldException e) {
      throw new IllegalStateException(e);
    }

    final T oldValue = (T)memberValues.get(key);
    if (oldValue == null)
      throw new IllegalArgumentException(key + " is not a valid key");

    if (newValue != null && oldValue.getClass() != newValue.getClass())
      throw new IllegalArgumentException(newValue.getClass().getName() + " does not match the required type " + oldValue.getClass().getName());

    memberValues.put(key, newValue);
    return oldValue;
  }

  private static final Repeat.Recurser<Field,Class<?>> declaredFieldRecurser = new Repeat.Recurser<Field,Class<?>>() {
    @Override
    public boolean accept(final Field member, final Object ... args) {
      return true;
    }

    @Override
    public Field[] members(final Class<?> container) {
      return container.getDeclaredFields();
    }

    @Override
    public Class<?> next(final Class<?> container) {
      return container.getSuperclass();
    }
  };

  private static final Repeat.Recurser<Method,Class<?>> declaredMethodRecurser = new Repeat.Recurser<Method,Class<?>>() {
    @Override
    public boolean accept(final Method member, final Object ... args) {
      return true;
    }

    @Override
    public Method[] members(final Class<?> container) {
      return container.getDeclaredMethods();
    }

    @Override
    public Class<?> next(final Class<?> container) {
      return container.getSuperclass();
    }
  };

  private static final Repeat.Recurser<Field,Class<?>> declaredFieldWithAnnotationRecurser = new Repeat.Recurser<Field,Class<?>>() {
    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public boolean accept(final Field member, final Object ... args) {
      return args[0] == null || member.getAnnotation((Class)args[0]) != null;
    }

    @Override
    public Field[] members(final Class<?> container) {
      return container.getDeclaredFields();
    }

    @Override
    public Class<?> next(final Class<?> container) {
      return container.getSuperclass();
    }
  };

  private static final Repeat.Recurser<Method,Class<?>> declaredMethodWithAnnotationRecurser = new Repeat.Recurser<Method,Class<?>>() {
    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public boolean accept(final Method member, final Object ... args) {
      return args[0] == null || member.getAnnotation((Class)args[0]) != null;
    }

    @Override
    public Method[] members(final Class<?> container) {
      return container.getDeclaredMethods();
    }

    @Override
    public Class<?> next(final Class<?> container) {
      return container.getSuperclass();
    }
  };

  private static final Repeat.Filter<Field> declaredFieldWithAnnotationFilter = new Repeat.Filter<Field>() {
    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public boolean accept(final Field member, final Object ... args) {
      return args[0] == null || member.getAnnotation((Class)args[0]) != null;
    }
  };

  private static final Repeat.Filter<Method> declaredMethodWithAnnotationFilter = new Repeat.Filter<Method>() {
    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public boolean accept(final Method member, final Object ... args) {
      return args[0] == null || member.getAnnotation((Class)args[0]) != null;
    }
  };

  private static final Repeat.Filter<Class<?>> classWithAnnotationFilter = new Repeat.Filter<Class<?>>() {
    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public boolean accept(final Class<?> member, final Object ... args) {
      return args[0] == null || member.getAnnotation((Class)args[0]) != null;
    }
  };

  private static final Repeat.Recurser<Class<?>,Class<?>> classWithAnnotationRecurser = new Repeat.Recurser<Class<?>,Class<?>>() {
    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public boolean accept(final Class<?> member, final Object ... args) {
      return args[0] == null || member.getAnnotation((Class)args[0]) != null;
    }

    @Override
    public Class<?>[] members(final Class<?> container) {
      return container.getDeclaredClasses();
    }

    @Override
    public Class<?> next(final Class<?> container) {
      return container.getSuperclass();
    }
  };

  /**
   * Returns an array of Field objects declared in {@code cls} (including
   * inherited fields).
   * <p>
   * Declared fields include public, protected, default (package) access, and
   * private visibility.
   * <p>
   * If {@code cls} represents a class or interface with no declared fields,
   * then this method returns an array of length 0.
   * <p>
   * If {@code cls} represents an array type, a primitive type, or void, then
   * this method returns an array of length 0.
   * <p>
   * The elements in the returned array are sorted reflecting the inheritance
   * graph of {@code cls}, whereby inherited fields are first, and member fields
   * are last.
   *
   * @param cls The class in which to find declared fields.
   * @return An array of Field objects declared in {@code cls} (including
   *         inherited fields).
   */
  public static Field[] getDeclaredFieldsDeep(final Class<?> cls) {
    return Repeat.Recursive.<Field,Class<?>>inverted(cls, cls.getDeclaredFields(), Field.class, declaredFieldRecurser);
  }

  /**
   * Returns an array of Field objects declared in {@code cls} (excluding
   * inherited fields) that have an annotation of {@code annotationType}.
   * <p>
   * Declared fields include public, protected, default (package) access, and
   * private visibility.
   * <p>
   * If {@code cls} represents a class or interface with no declared fields,
   * then this method returns an array of length 0.
   * <p>
   * If {@code cls} represents an array type, a primitive type, or void, then
   * this method returns an array of length 0.
   * <p>
   * The elements in the returned array are not sorted and are not in any
   * particular order.
   *
   * @param cls The class in which to find declared fields.
   * @param annotationType The type of the annotation to match.
   * @return An array of Field objects declared in {@code cls} (excluding
   *         inherited fields) that have an annotation of
   *         {@code annotationType}.
   */
  public static Field[] getDeclaredFieldsWithAnnotation(final Class<?> cls, final Class<? extends Annotation> annotationType) {
    return Repeat.Recursive.<Field>ordered(cls.getDeclaredFields(), Field.class, declaredFieldWithAnnotationFilter, annotationType);
  }

  /**
   * Returns an array of Field objects declared in {@code cls} (including
   * inherited fields) that have an annotation of {@code annotationType}.
   * <p>
   * Declared fields include public, protected, default (package) access, and
   * private visibility.
   * <p>
   * If {@code cls} represents a class or interface with no declared fields,
   * then this method returns an array of length 0.
   * <p>
   * If {@code cls} represents an array type, a primitive type, or void, then
   * this method returns an array of length 0.
   * <p>
   * The elements in the returned array are sorted reflecting the inheritance
   * graph of {@code cls}, whereby inherited fields are first, and member fields
   * are last.
   *
   * @param cls The class in which to find declared fields.
   * @param annotationType The type of the annotation to match.
   * @return An array of Field objects declared in {@code cls} (including
   *         inherited fields) that have an annotation of
   *         {@code annotationType}.
   */
  public static Field[] getDeclaredFieldsWithAnnotationDeep(final Class<?> cls, final Class<? extends Annotation> annotationType) {
    return Repeat.Recursive.<Field,Class<?>>inverted(cls, cls.getDeclaredFields(), Field.class, declaredFieldWithAnnotationRecurser, annotationType);
  }

  /**
   * Returns a Method object that reflects the specified declared method of the
   * class or interface represented by {@code cls} (excluding inherited
   * methods), or {@code null} if the method is not found.
   * <p>
   * Declared methods include public, protected, default (package) access, and
   * private visibility.
   * <p>
   * The {@code name} parameter is a String that specifies the simple name of
   * the desired method, and the {@code parameterTypes} parameter is an array of
   * Class objects that identify the method's formal parameter types, in
   * declared order. If more than one method with the same parameter types is
   * declared in a class, and one of these methods has a return type that is
   * more specific than any of the others, that method is returned; otherwise
   * one of the methods is chosen arbitrarily. If the name is "&lt;init&gt;"or
   * "&lt;clinit&gt;" this method returns {@code null}. If this Class object
   * represents an array type, then this method does not find the clone()
   * method.
   * <p>
   * This method differentiates itself from
   * {@link Class#getDeclaredMethod(String,Class...)} by returning {@code null}
   * when a method is not found, instead of throwing
   * {@link NoSuchMethodException}.
   *
   * @param cls The class in which to find the declared method.
   * @param name The simple name of the method.
   * @param parameterTypes The parameter array.
   * @return A Method object that reflects the specified declared method of the
   *         class or interface represented by {@code cls} (excluding inherited
   *         methods), or {@code null} if the method is not found.
   */
  public static Method getDeclaredMethod(final Class<?> cls, final String name, final Class<?> ... parameterTypes) {
    final Method[] methods = cls.getDeclaredMethods();
    for (final Method method : methods)
      if (method.getName().equals(name) && java.util.Arrays.equals(method.getParameterTypes(), parameterTypes))
        return method;

    return null;
  }

  /**
   * Returns a Method object that reflects the specified declared method of the
   * class or interface represented by {@code cls} (including inherited
   * methods), or {@code null} if the method is not found.
   * <p>
   * Declared methods include public, protected, default (package) access, and
   * private visibility.
   * <p>
   * The {@code name} parameter is a String that specifies the simple name of
   * the desired method, and the {@code parameterTypes} parameter is an array of
   * Class objects that identify the method's formal parameter types, in
   * declared order. If more than one method with the same parameter types is
   * declared in a class, and one of these methods has a return type that is
   * more specific than any of the others, that method is returned; otherwise
   * one of the methods is chosen arbitrarily. If the name is "&lt;init&gt;"or
   * "&lt;clinit&gt;" this method returns {@code null}. If this Class object
   * represents an array type, then this method does not find the clone()
   * method.
   * <p>
   * This method differentiates itself from
   * {@link Class#getDeclaredMethod(String,Class...)} by returning {@code null}
   * when a method is not found, instead of throwing
   * {@link NoSuchMethodException}.
   *
   * @param cls The class in which to find the declared method.
   * @param name The simple name of the method.
   * @param parameterTypes The parameter array.
   * @return A Method object that reflects the specified declared method of the
   *         class or interface represented by {@code cls} (including inherited
   *         methods), or {@code null} if the method is not found.
   */
  public static Method getDeclaredMethodDeep(Class<?> cls, final String name, final Class<?> ... parameterTypes) {
    Method method;
    do
      method = getDeclaredMethod(cls, name, parameterTypes);
    while (method == null && (cls = cls.getSuperclass()) != null);
    return method;
  }

  /**
   * Returns an array of Method objects declared in {@code cls} (including
   * inherited methods).
   * <p>
   * Declared methods include public, protected, default (package) access, and
   * private visibility.
   * <p>
   * If {@code cls} represents an array type, a primitive type, or void, then
   * this method returns an array of length 0.
   * <p>
   * The elements in the returned array are sorted reflecting the inheritance
   * graph of {@code cls}, whereby inherited methods are first, and member methods
   * are last.
   *
   * @param cls The class in which to find declared methods.
   * @return An array of Method objects declared in {@code cls} (including
   *         inherited methods).
   */
  public static Method[] getDeclaredMethodsDeep(final Class<?> cls) {
    return Repeat.Recursive.<Method,Class<?>>inverted(cls, cls.getDeclaredMethods(), Method.class, declaredMethodRecurser);
  }

  /**
   * Returns an array of Method objects declared in {@code cls} (excluding
   * inherited methods) that have an annotation of {@code annotationType}.
   * <p>
   * Declared methods include public, protected, default (package) access, and
   * private visibility.
   * <p>
   * If {@code cls} represents a class or interface with no declared methods,
   * then this method returns an array of length 0.
   * <p>
   * If {@code cls} represents an array type, a primitive type, or void, then
   * this method returns an array of length 0.
   * <p>
   * The elements in the returned array are not sorted and are not in any
   * particular order.
   *
   * @param cls The class in which to find declared methods.
   * @param annotationType The type of the annotation to match.
   * @return An array of Method objects declared in {@code cls} (excluding
   *         inherited methods) that have an annotation of {@code annotationType}.
   */
  public static Method[] getDeclaredMethodsWithAnnotation(final Class<?> cls, final Class<? extends Annotation> annotationType) {
    return Repeat.Recursive.<Method>ordered(cls.getDeclaredMethods(), Method.class, declaredMethodWithAnnotationFilter, annotationType);
  }

  /**
   * Returns an array of Method objects declared in {@code cls} (including
   * inherited methods) that have an annotation of {@code annotationType}.
   * <p>
   * Declared methods include public, protected, default (package) access, and
   * private visibility.
   * <p>
   * If {@code cls} represents an array type, a primitive type, or void, then
   * this method returns an array of length 0.
   * <p>
   * The elements in the returned array are sorted reflecting the inheritance
   * graph of {@code cls}, whereby inherited methods are first, and member methods
   * are last.
   *
   * @param cls The class in which to find declared methods.
   * @param annotationType The type of the annotation to match.
   * @return An array of Method objects declared in {@code cls} (including
   *         inherited methods) that have an annotation of {@code annotationType}.
   */
  public static Method[] getDeclaredMethodsWithAnnotationDeep(final Class<?> cls, final Class<? extends Annotation> annotationType) {
    return Repeat.Recursive.<Method,Class<?>>inverted(cls, cls.getDeclaredMethods(), Method.class, declaredMethodWithAnnotationRecurser, annotationType);
  }

  /**
   * Returns an array of Class objects declared in {@code cls} (excluding
   * inherited classes) that have an annotation of {@code annotationType}.
   * <p>
   * Declared classes include public, protected, default (package) access, and
   * private visibility.
   * <p>
   * If {@code cls} represents a class or interface with no declared classes,
   * then this method returns an array of length 0.
   * <p>
   * If {@code cls} represents an array type, a primitive type, or void, then
   * this method returns an array of length 0.
   * <p>
   * The elements in the returned array are not sorted and are not in any
   * particular order.
   *
   * @param cls The class in which to find declared methods.
   * @param annotationType The type of the annotation to match.
   * @return An array of Class objects declared in {@code cls} (excluding
   *         inherited classes) that have an annotation of
   *         {@code annotationType}.
   */
  @SuppressWarnings("unchecked")
  public static Class<?>[] getDeclaredClassesWithAnnotation(final Class<?> cls, final Class<? extends Annotation> annotationType) {
    return Repeat.Recursive.<Class<?>>ordered(cls.getDeclaredClasses(), (Class<Class<?>>)Class.class.getClass(), classWithAnnotationFilter, annotationType);
  }

  /**
   * Returns an array of Class objects declared in {@code cls} (including
   * inherited classes) that have an annotation of {@code annotationType}.
   * <p>
   * Declared classes include public, protected, default (package) access, and
   * private visibility.
   * <p>
   * If {@code cls} represents an array type, a primitive type, or void, then
   * this method returns an array of length 0.
   * <p>
   * The elements in the returned array are sorted reflecting the inheritance
   * graph of {@code cls}, whereby inherited classes are first, and member classes
   * are last.
   *
   * @param cls The class in which to find declared methods.
   * @param annotationType The type of the annotation to match.
   * @return An array of Class objects declared in {@code cls} (including
   *         inherited classes) that have an annotation of {@code annotationType}.
   */
  @SuppressWarnings("unchecked")
  public static Class<?>[] getDeclaredClassesWithAnnotationDeep(final Class<?> cls, final Class<? extends Annotation> annotationType) {
    return Repeat.Recursive.<Class<?>,Class<?>>inverted(cls, cls.getDeclaredClasses(), (Class<Class<?>>)Class.class.getClass(), classWithAnnotationRecurser, annotationType);
  }

  public static Class<?> getGreatestCommonSuperclass(final Class<?> ... classes) {
    if (classes == null || classes.length == 0)
      return null;

    if (classes.length == 1)
      return classes[0];

    Class<?> gcc = getGreatestCommonSuperclass(classes[0], classes[1]);
    for (int i = 2; i < classes.length && gcc != null; i++)
      gcc = getGreatestCommonSuperclass(gcc, classes[i]);

    return gcc;
  }

  private static final Function<Object,Class<?>> objectClassFunction = o -> o.getClass();

  @SafeVarargs
  private static <T>Class<?> getGreatestCommonSuperclass0(final Function<T,Class<?>> function, final T ... objects) {
    if (objects == null || objects.length == 0)
      return null;

    if (objects.length == 1)
      return function.apply(objects[0]);

    Class<?> gcc = getGreatestCommonSuperclass(function.apply(objects[0]), function.apply(objects[1]));
    for (int i = 2; i < objects.length && gcc != null; i++)
      gcc = getGreatestCommonSuperclass(gcc, function.apply(objects[i]));

    return gcc;
  }

  @SafeVarargs
  public static <T>Class<?> getGreatestCommonSuperclass(final T ... objects) {
    return getGreatestCommonSuperclass0(objectClassFunction, objects);
  }

  public static <T>Class<?> getGreatestCommonSuperclass(final Collection<T> objects) {
    return getGreatestCommonSuperclass0(objectClassFunction, objects.toArray());
  }

  @SafeVarargs
  public static <T>Class<?> getGreatestCommonSuperclass(final Function<T,Class<?>> function, final T ... objects) {
    return getGreatestCommonSuperclass0(function, objects);
  }

  @SuppressWarnings("unchecked")
  public static <T>Class<?> getGreatestCommonSuperclass(final Function<T,Class<?>> function, final Collection<T> objects) {
    return getGreatestCommonSuperclass0((Function<Object,Class<?>>)function, objects.toArray());
  }

  private static Class<?> getGreatestCommonSuperclass(Class<?> class1, Class<?> class2) {
    final Class<?> cls = class2;
    do {
      do
        if (class1.isAssignableFrom(class2))
          return class1;
      while ((class2 = class2.getSuperclass()) != null);
      class2 = cls;
    }
    while ((class1 = class1.getSuperclass()) != null);
    return null;
  }

  public static String getStrictName(final Class<?> cls) {
    if (cls.isArray())
      return getStrictName(cls.getComponentType()) + "[]";

    if (cls.isPrimitive()) {
      if (cls == int.class)
        return "int";

      if (cls == long.class)
        return "long";

      if (cls == double.class)
        return "double";

      if (cls == float.class)
        return "float";

      if (cls == boolean.class)
        return "boolean";

      if (cls == byte.class)
        return "byte";

      if (cls == short.class)
        return "short";

      if (cls == char.class)
        return "char";

      if (cls == void.class)
        return "void";

      throw new UnsupportedOperationException("Unknown primitive type: " + cls.getClass());
    }

    return recurseStrictName(cls).toString();
  }

  private static StringBuilder recurseStrictName(final Class<?> cls) {
    return cls.isMemberClass() ? recurseStrictName(cls.getEnclosingClass()).append('.').append(cls.getSimpleName()) : new StringBuilder(cls.getName());
  }

  private static class CallingClass extends SecurityManager {
    @Override
    public Class<?>[] getClassContext() {
      return super.getClassContext();
    }
  }

  public static Class<?>[] getCallingClasses() {
    return FastArrays.subArray(new CallingClass().getClassContext(), 3);
  }

  public static <T>T newInstance(final Class<? extends T> clazz) {
    try {
      return clazz.getDeclaredConstructor().newInstance();
    }
    catch (final IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  public static <T>T newInstance(final Constructor<T> consctuctor, final Object ... initargs) {
    try {
      return consctuctor.newInstance(initargs);
    }
    catch (final IllegalAccessException | InstantiationException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  private Classes() {
  }
}