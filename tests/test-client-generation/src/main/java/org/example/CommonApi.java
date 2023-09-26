package org.example;

import io.avaje.http.api.Get;
import io.avaje.http.api.Path;
import io.avaje.http.api.Produces;
import io.avaje.http.api.QueryParam;

import java.time.LocalDate;

@Path("/common")
public interface CommonApi {

  @Produces("text/plain")
  @Get("plain")
  String hello();

  @Produces("text/plain")
  @Get("name/{name}")
  String name(String name);

  @Produces("text/plain")
  @Get("{id}/{name}")
  String p2(long id, String name, @QueryParam("after") LocalDate after, @QueryParam("more") Boolean more);

}
