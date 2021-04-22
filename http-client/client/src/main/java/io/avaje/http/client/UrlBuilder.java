package io.avaje.http.client;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Build a URL typically using a base url and adding path and query parameters.
 */
public class UrlBuilder {

  private final StringBuilder buffer = new StringBuilder(100);

  private boolean hasParams;

  /**
   * Create with a base url.
   */
  public UrlBuilder(String base) {
    buffer.append(base);
  }

  /**
   * Set the url. This effectively replaces a base url.
   */
  public UrlBuilder url(String url) {
    buffer.delete(0, buffer.length());
    buffer.append(url);
    return this;
  }

  /**
   * Add a path segment to the url.
   * <p>
   * This includes appending a "/" prefix with the path.
   */
  public UrlBuilder path(String path) {
    buffer.append("/").append(path);
    return this;
  }

  /**
   * Append a query parameter.
   * <p>
   * The name and value parameters are url encoded.
   */
  public UrlBuilder queryParam(String name, String value) {
    if (value != null) {
      buffer.append(hasParams ? '&' : '?');
      hasParams = true;
      buffer.append(enc(name)).append("=").append(enc(value));
    }
    return this;
  }

  /**
   * Append a matrix parameter.
   * <p>
   * The name and value parameters are url encoded.
   */
  public UrlBuilder matrixParam(String name, String value) {
    if (value != null) {
      buffer.append(';').append(enc(name)).append("=").append(enc(value));
    }
    return this;
  }

  /**
   * URL encode the value.
   */
  public static String enc(String val) {
    return URLEncoder.encode(val, StandardCharsets.UTF_8);
  }

  /**
   * Return the full URL.
   */
  public String build() {
    return buffer.toString();
  }

}
