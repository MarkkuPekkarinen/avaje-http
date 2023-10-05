package io.avaje.http.api;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * A parameter that is a cookie value.
 * <p>
 * We can put this on a method parameter or a <code>@Form</code> bean property.
 * </p>
 *
 */
@Target({PARAMETER, FIELD})
@Retention(RUNTIME)
public @interface Cookie {

  /**
   * The name of the cookie.
   */
  String value() default "";

}
