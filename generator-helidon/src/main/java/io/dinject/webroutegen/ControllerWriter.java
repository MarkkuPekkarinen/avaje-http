package io.dinject.webroutegen;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Write Helidon specific web route adapter (a Helidon Service).
 */
class ControllerWriter extends BaseControllerWriter {

  private static final String AT_GENERATED = "@Generated(\"io.dinject.helidon-webgen\")";

  ControllerWriter(ControllerReader reader, ProcessingContext ctx) throws IOException {
    super(reader, ctx);
    reader.addImportType("io.helidon.common.http.FormParams");
    reader.addImportType("io.helidon.webserver.Handler");
    reader.addImportType("io.helidon.webserver.Routing");
    reader.addImportType("io.helidon.webserver.ServerRequest");
    reader.addImportType("io.helidon.webserver.ServerResponse");
    reader.addImportType("io.helidon.webserver.Service");
  }

  void write() {
    writePackage();
    writeImports();
    writeClassStart();
    writeAddRoutes();
    writeClassEnd();
  }

  private List<ControllerMethodWriter> getWriterMethods() {
    return reader.getMethods().stream()
      .filter(MethodReader::isWebMethod)
      .map(it -> new ControllerMethodWriter(it, writer))
      .collect(Collectors.toList());
  }

  private void writeAddRoutes() {
    final List<ControllerMethodWriter> methods = getWriterMethods();
    writeRoutes(methods);
    for (ControllerMethodWriter methodWriter : methods) {
      writeHandlerMethod(methodWriter);
    }
  }

  private void writeRoutes(List<ControllerMethodWriter> methods) {
    writer.append("  @Override").eol();
    writer.append("  public void update(Routing.Rules rules) {").eol().eol();
    for (ControllerMethodWriter methodWriter : methods) {
      methodWriter.writeRule();
      if (!reader.isDocHidden()) {
        methodWriter.buildApiDocumentation();
      }
    }
    writer.append("  }").eol().eol();
  }

  private void writeHandlerMethod(ControllerMethodWriter methodWriter) {
    methodWriter.writeHandler();
  }

  private void writeClassStart() {
    if (ctx.isGeneratedAvailable()) {
      writer.append(AT_GENERATED).eol();
    }
    writer.append("@Singleton").eol();
    writer.append("public class ").append(shortName).append("$route implements Service {").eol().eol();

    writer.append(" private final %s controller;", shortName).eol();
    if (reader.isIncludeValidator()) {
      writer.append(" private final Validator validator;").eol();
    }
    writer.eol();

    writer.append(" public %s$route(%s controller", shortName, shortName);
    if (reader.isIncludeValidator()) {
      writer.append(", Validator validator");
    }
    writer.append(") {").eol();
    writer.append("   this.controller = controller;").eol();
    if (reader.isIncludeValidator()) {
      writer.append("   this.validator = validator;").eol();
    }
    writer.append(" }").eol().eol();
  }

}